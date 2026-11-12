# Smart_Library_App

Implementación conceptual del caso de uso **«Registrar préstamo»** y de la **consulta de préstamos** de SmartLibrary,
siguiendo el diagrama de clases del taller (Controller → Service → Repository, con las entidades como modelo del dominio).

- Java 21 · Maven · Spring Boot 3.5 · Spring Web · Spring Data JPA · Validation · H2 (en memoria)

## Ejecutar

```bash
mvn spring-boot:run      # arranca en http://localhost:8080
mvn test                 # pruebas unitarias y de extremo a extremo
```

Requiere JDK 21 y Maven 3.9+ (o abrir la carpeta en IntelliJ IDEA / VS Code / Eclipse y ejecutar `SmartLibraryApplication`).
Consola de H2: <http://localhost:8080/h2-console> con JDBC URL `jdbc:h2:mem:smartlibrary` y usuario `sa` (sin contraseña).

## Datos de ejemplo (`data.sql`)

| Estudiantes | Libros |
|---|---|
| 1 Ana Gómez · 2 Luis Pérez · 3 María Rojas | 1 Clean Code (disponible) · 2 Effective Java (disponible) · 3 Refactoring (**no disponible**) |

## API

| Método y ruta | Descripción | Respuestas |
|---|---|---|
| `POST /api/prestamos` | Registra un préstamo. Cuerpo: `{"estudianteId": 1, "libroId": 1}` | `201` (con `Location`), `400` solicitud inválida, `404` estudiante/libro inexistente, `409` libro no disponible |
| `GET /api/prestamos?estudianteId=1&soloActivos=true` | Consulta por estudiante y/o solo activos. Sin `estudianteId` exige `soloActivos=true` | `200`, `400` si no hay ningún criterio |

PowerShell (Windows):

```powershell
# Camino 1: éxito (201)
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/prestamos `
  -ContentType 'application/json' -Body '{"estudianteId":1,"libroId":1}'

# Camino 2: libro no disponible (409)
Invoke-WebRequest -Method Post -Uri http://localhost:8080/api/prestamos `
  -ContentType 'application/json' -Body '{"estudianteId":1,"libroId":3}' -SkipHttpErrorCheck

# Consulta
Invoke-RestMethod 'http://localhost:8080/api/prestamos?estudianteId=1'
```

## Del diagrama de clases al código

| Diagrama | Código (`com.smartlibrary…`) |
|---|---|
| `PrestamoController` | `controller.PrestamoController` — `registrar(PrestamoRequest)`, `consultar(estudianteId, soloActivos)` |
| `PrestamoRequest`, `PrestamoResponse` | `dto` (clases con getters y setters); `PrestamoResponse.desde(Prestamo)` |
| `PrestamoService` | `service.PrestamoService` — `registrarPrestamo`, `consultarPrestamos`; depende de `EstudianteService`, `LibroService` y `PrestamoRepository` |
| `EstudianteService`, `LibroService` | `service` — `buscarPorId(id)`; cada uno depende solo de su repositorio |
| `EstudianteRepository`, `LibroRepository`, `PrestamoRepository` | `repository` (interfaces) |
| `*RepositoryJpa` (realizaciones) | `repository.*RepositoryJpa` (JPA `EntityManager` sobre H2) |
| `Estudiante`, `Libro`, `Prestamo` | `model` (entidades JPA con getters y setters de cada atributo) |
| Agregaciones `Prestamo` → `Estudiante` / `Libro` | `@ManyToOne` en `Prestamo` |

Reglas del enunciado: **R1 y R3** en `Libro.marcarPrestado()` · **R2** en el constructor de `Prestamo` ·
**R4** validación de formato en `PrestamoRequest` y una sola transacción en `PrestamoService.registrarPrestamo` ·
**R5** el libro se marca no disponible al prestarse y solo vuelve a estarlo cuando termine el préstamo (la devolución queda fuera del alcance).

## Decisiones y límites

- Los accesores añadidos (`getId()`, `getFechaPrestamo()`, `getEstudiante()`, `getLibro()`) existen solo para mapear a `PrestamoResponse`; no se añadieron atributos al modelo.
- `LibroNoDisponibleException` (en `model`) es la excepción de dominio de R1. El controller traduce: `LibroNoDisponibleException` → 409 y `NoSuchElementException` → 404, con cuerpo vacío.
- No hay control de concurrencia: dos solicitudes simultáneas sobre el mismo libro podrían leerlo como disponible a la vez. Para producción convendría un bloqueo (`@Version` o bloqueo pesimista).
- Para un wrapper de Maven (`mvnw`), ejecute una vez `mvn wrapper:wrapper`.

## Pruebas con Postman (VS Code)

Importa `postman/SmartLibrary.postman_collection.json` y `postman/SmartLibrary.postman_environment.json`
(extensión Postman de VS Code → Import) y selecciona el entorno «SmartLibrary local».
Con la aplicación recién arrancada (los datos de `data.sql` se recargan en cada arranque), ejecuta la colección en orden:

| Escenario | Petición | Esperado |
|---|---|---|
| Libro disponible | `POST /api/prestamos` `{"estudianteId":1,"libroId":1}` | 201 y préstamo registrado |
| Libro ya prestado | `{"estudianteId":2,"libroId":3}` | 409, sin nuevo préstamo |
| Estudiante inexistente | `{"estudianteId":999,"libroId":2}` | 404 |
| Libro inexistente | `{"estudianteId":1,"libroId":999}` | 404 |
| Consulta | `GET /api/prestamos?estudianteId=1` | 200 con registros |

404 y 409 se traducen explícitamente en `PrestamoController` (`NoSuchElementException` → 404, `LibroNoDisponibleException` → 409).
`PrestamoService.registrarPrestamo` es `@Transactional`: el cambio de disponibilidad y la creación del préstamo se confirman juntos o no se confirma ninguno.
Concurrencia: dos solicitudes simultáneas sobre el mismo libro podrían leerlo como disponible a la vez; en un sistema real se resolvería con bloqueo optimista (`@Version`) o pesimista.
