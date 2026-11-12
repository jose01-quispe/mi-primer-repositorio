CREATE TABLE Perfil (
 id_perfil BIGINT NOT NULL PRIMARY KEY,
 nombre VARCHAR(60) NOT NULL,
 codigo VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE Acceso (
 id_acceso VARCHAR(10) NOT NULL PRIMARY KEY,
 url_acceso VARCHAR(120) NOT NULL,
 menu_item_nombre VARCHAR(60) NOT NULL,
 menu_nombre VARCHAR(20) NOT NULL,
 nombre_tab VARCHAR(10) NOT NULL DEFAULT 'INTERNO'
);

CREATE TABLE Perfil_Acceso (
 id_perfil BIGINT NOT NULL,
 id_acceso VARCHAR(10) NOT NULL,
 PRIMARY KEY (id_perfil, id_acceso),
 FOREIGN KEY (id_perfil)
 REFERENCES Perfil(id_perfil),
 FOREIGN KEY (id_acceso)
 REFERENCES Acceso(id_acceso)
);

CREATE TABLE Usuario (
 id_usuario BIGINT NOT NULL PRIMARY KEY,
 usuario VARCHAR(40) NOT NULL UNIQUE,
 clave VARCHAR(255) NOT NULL,
 id_perfil BIGINT NOT NULL,
 estado VARCHAR(10) NOT NULL DEFAULT 'ACTIVO',
 FOREIGN KEY (id_perfil)
 REFERENCES Perfil(id_perfil)
);

CREATE TABLE Sala (
 idSala BIGINT NOT NULL PRIMARY KEY,
 numero INTEGER NOT NULL UNIQUE,
 capacidad INTEGER NOT NULL,
 tipo VARCHAR(20) NOT NULL,
 CHECK (capacidad > 0),
 CHECK (numero > 0)
);

CREATE TABLE Pelicula (
 idPelicula BIGINT NOT NULL PRIMARY KEY,
 titulo VARCHAR(150) NOT NULL,
 genero VARCHAR(50) NOT NULL,
 duracion INTEGER NOT NULL,
 CHECK (duracion > 0)
);

CREATE TABLE Cliente (
 dniruc VARCHAR(11) NOT NULL PRIMARY KEY,
 nombres VARCHAR(150) NOT NULL,
 repLegal VARCHAR(150),
 tipoDocumento VARCHAR(10) NOT NULL,
 CHECK (tipoDocumento IN ('DNI', 'CE', 'RUC', 'PASAPORTE'))
);

CREATE TABLE Producto (
 idProducto BIGINT NOT NULL PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL UNIQUE,
 precio NUMERIC(8, 2) NOT NULL,
 CHECK (precio >= 0)
);

CREATE TABLE Funcion (
 idFuncion BIGINT NOT NULL PRIMARY KEY,
 fecha DATE NOT NULL,
 horaInicio TIME NOT NULL,
 precioEntrada NUMERIC(8, 2) NOT NULL,
 idSala BIGINT NOT NULL,
 idPelicula BIGINT NOT NULL,
 FOREIGN KEY (idSala)
 REFERENCES Sala(idSala),
 FOREIGN KEY (idPelicula)
 REFERENCES Pelicula(idPelicula),
 CHECK (precioEntrada >= 0)
);

CREATE TABLE Venta (
 idVenta BIGINT NOT NULL PRIMARY KEY,
 fechaVenta TIMESTAMP NOT NULL,
 cantidadEntradas INTEGER NOT NULL,
 total NUMERIC(10, 2) NOT NULL,
 tipoPago VARCHAR(10) NOT NULL DEFAULT 'EFECTIVO',
 dniruc VARCHAR(11) NOT NULL,
 idFuncion BIGINT NOT NULL,
 id_usuario BIGINT NOT NULL,
 FOREIGN KEY (dniruc)
 REFERENCES Cliente(dniruc),
 FOREIGN KEY (idFuncion)
 REFERENCES Funcion(idFuncion),
 FOREIGN KEY (id_usuario)
 REFERENCES Usuario(id_usuario),
 CHECK (cantidadEntradas >= 0),
 CHECK (total >= 0),
 CHECK (tipoPago IN ('EFECTIVO', 'YAPE', 'TARJETA'))
);

CREATE TABLE DetalleVenta (
 idDetalle BIGINT NOT NULL PRIMARY KEY,
 cantidad INTEGER NOT NULL,
 precioUnitario NUMERIC(8, 2) NOT NULL,
 idVenta BIGINT NOT NULL,
 idProducto BIGINT NOT NULL,
 FOREIGN KEY (idVenta)
 REFERENCES Venta(idVenta),
 FOREIGN KEY (idProducto)
 REFERENCES Producto(idProducto),
 CHECK (cantidad > 0),
 CHECK (precioUnitario >= 0)
);

INSERT INTO Perfil
(id_perfil, nombre, codigo)
VALUES
(1, 'Administrador', 'ADMIN');

INSERT INTO Acceso
(id_acceso, url_acceso, menu_item_nombre, menu_nombre, nombre_tab)
VALUES
('A01', '/view/main_venta.fxml', 'Ventas', 'VENTAS', 'INTERNO'),
('A02', '/view/main_cliente.fxml', 'Clientes', 'CLIENTES', 'INTERNO'),
('A03', '/view/main_sala.fxml', 'Salas', 'SALAS', 'INTERNO'),
('A04', '/view/main_pelicula.fxml', 'Películas', 'PELICULAS', 'INTERNO'),
('A05', '/view/main_producto.fxml', 'Productos', 'PRODUCTOS', 'INTERNO');

INSERT INTO Perfil_Acceso
(id_perfil, id_acceso)
VALUES
(1, 'A01'),
(1, 'A02'),
(1, 'A03'),
(1, 'A04'),
(1, 'A05');

INSERT INTO Usuario
(id_usuario, usuario, clave, id_perfil, estado)
VALUES
(1, 'admin', 'admin123', 1, 'ACTIVO');

INSERT INTO Sala
(idSala, numero, capacidad, tipo)
VALUES
(1, 1, 100, '2D'),
(2, 2, 80, '3D'),
(3, 3, 50, 'VIP');

INSERT INTO Pelicula
(idPelicula, titulo, genero, duracion)
VALUES
(1, 'Intensamente 2', 'Animación', 96),
(2, 'Dune: Parte Dos', 'Ciencia ficción', 166),
(3, 'Coco', 'Animación', 105),
(4, 'Oppenheimer', 'Drama', 180),
(5, 'Película Demo', 'Acción', 120);

INSERT INTO Cliente
(dniruc, nombres, repLegal, tipoDocumento)
VALUES
('70000001', 'Cliente Demo', NULL, 'DNI'),
('73625148', 'Ana Torres Huanca', NULL, 'DNI'),
('71456238', 'Jorge Ramos Flores', NULL, 'DNI'),
('001234567', 'Maria Condori Quispe', NULL, 'CE'),
('20601234567', 'Distribuidora Andina S.A.C.', 'Carlos Mamani Lopez', 'RUC');

INSERT INTO Producto
(idProducto, nombre, precio)
VALUES
(1, 'Canchita grande', 12.00),
(2, 'Canchita mediana', 9.00),
(3, 'Gaseosa 500 ml', 6.00),
(4, 'Nachos con queso', 10.00),
(5, 'Hot dog', 8.00);

INSERT INTO Funcion
(idFuncion, fecha, horaInicio, precioEntrada, idSala, idPelicula)
VALUES
(1, '2026-10-20', '15:00', 18.00, 1, 1),
(2, '2026-10-20', '18:30', 25.00, 2, 2),
(3, '2026-10-20', '21:00', 35.00, 3, 4),
(4, '2026-10-21', '16:00', 18.00, 1, 3),
(5, '2026-10-21', '20:00', 25.00, 2, 2);

INSERT INTO Venta
(idVenta, fechaVenta, cantidadEntradas, total, tipoPago, dniruc, idFuncion, id_usuario)
VALUES
(1, '2026-10-20 14:40:00', 2, 60.00, 'EFECTIVO', '70000001', 1, 1),
(2, '2026-10-20 18:10:00', 3, 85.00, 'YAPE', '73625148', 2, 1),
(3, '2026-10-20 20:30:00', 2, 98.00, 'TARJETA', '20601234567', 3, 1),
(4, '2026-10-21 15:30:00', 4, 90.00, 'EFECTIVO', '71456238', 4, 1),
(5, '2026-10-21 19:15:00', 1, 25.00, 'YAPE', '70000001', 5, 1);

INSERT INTO DetalleVenta
(idDetalle, cantidad, precioUnitario, idVenta, idProducto)
VALUES
(1, 1, 12.00, 1, 1),
(2, 2, 6.00, 1, 3),
(3, 1, 10.00, 2, 4),
(4, 2, 8.00, 3, 5),
(5, 2, 6.00, 3, 3),
(6, 2, 9.00, 4, 2);
