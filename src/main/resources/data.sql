-- Datos de ejemplo: los estudiantes y libros ya están registrados (supuesto del taller).
INSERT INTO estudiante (nombre, correo) VALUES ('Ana Gómez', 'ana.gomez@smartlibrary.edu');
INSERT INTO estudiante (nombre, correo) VALUES ('Luis Pérez', 'luis.perez@smartlibrary.edu');
INSERT INTO estudiante (nombre, correo) VALUES ('María Rojas', 'maria.rojas@smartlibrary.edu');

INSERT INTO libro (titulo, disponible) VALUES ('Clean Code', TRUE);
INSERT INTO libro (titulo, disponible) VALUES ('Effective Java', TRUE);
-- Libro ya prestado: sirve para demostrar el camino «libro no disponible» (409)
INSERT INTO libro (titulo, disponible) VALUES ('Refactoring', FALSE);
