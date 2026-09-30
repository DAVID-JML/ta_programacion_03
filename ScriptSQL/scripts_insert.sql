USE prog3;

INSERT INTO USUARIO (dni, nombre, apellido, nombre_usuario, contrasena, activo) 
VALUES 
('12345678', 'Juan', 'Perez', 'jperez', '123456', 1),
('23456781', 'Maria', 'Gomez', 'mgomez', '123456',1),
('34567812', 'Carlos', 'Ramirez', 'cramirez', '123456',1),
('45678123', 'Ana', 'Torres', 'atorres', '123456', '1');

SELECT * FROM USUARIO;


