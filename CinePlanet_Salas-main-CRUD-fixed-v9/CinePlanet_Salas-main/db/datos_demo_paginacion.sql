-- Datos extra SOLO para probar la paginación (ejecutar después de cineplanet_script_unido.sql).
-- Con más de 10 registros por tabla se ven varias páginas y los botones « ‹ › ».

INSERT INTO Sala (idSala, numero, capacidad, tipo) VALUES
(4, 4, 90, '2D'), (5, 5, 70, '3D'), (6, 6, 60, 'VIP'), (7, 7, 120, '2D'),
(8, 8, 85, '3D'), (9, 9, 55, 'VIP'), (10, 10, 100, '2D'), (11, 11, 75, '3D'),
(12, 12, 110, '2D');

INSERT INTO Pelicula (idPelicula, titulo, genero, duracion) VALUES
(6, 'Mad Max: Furia en el camino', 'Acción', 120),
(7, 'Spider-Man: Un nuevo universo', 'Animación', 117),
(8, 'Interestelar', 'Ciencia ficción', 169),
(9, 'El Rey León', 'Animación', 88),
(10, 'Gladiador', 'Acción', 155),
(11, 'Parásitos', 'Drama', 132),
(12, 'Titanic', 'Romance', 195),
(13, 'Toy Story', 'Animación', 81),
(14, 'Inception', 'Ciencia ficción', 148),
(15, 'Joker', 'Drama', 122);

INSERT INTO Cliente (dniruc, nombres, repLegal, tipoDocumento) VALUES
('72145896', 'Luis Mendoza Paredes', NULL, 'DNI'),
('74589632', 'Rosa Quispe Mamani', NULL, 'DNI'),
('70123456', 'Carlos Huamán Ccori', NULL, 'DNI'),
('75896321', 'Elena Vargas Soto', NULL, 'DNI'),
('71234567', 'Pedro Alanoca Ticona', NULL, 'DNI'),
('002345678', 'John Smith Parker', NULL, 'CE'),
('003456789', 'Laura Gómez Ríos', NULL, 'CE'),
('20512345678', 'Inversiones Titicaca S.A.', 'Marta Flores Apaza', 'RUC'),
('20598765432', 'Cinemarket E.I.R.L.', 'Raúl Choque Pari', 'RUC'),
('AB1234567', 'Sofía Martínez Ruiz', NULL, 'PASAPORTE'),
('76543210', 'Diego Salas Cutipa', NULL, 'DNI');

INSERT INTO Producto (idProducto, nombre, precio) VALUES
(6, 'Canchita pequeña', 6.00), (7, 'Gaseosa 1 litro', 9.00), (8, 'Agua mineral', 4.00),
(9, 'Chocolate', 5.50), (10, 'Combo pareja', 28.00), (11, 'Combo familiar', 45.00),
(12, 'Helado', 7.00), (13, 'Café', 6.50);
