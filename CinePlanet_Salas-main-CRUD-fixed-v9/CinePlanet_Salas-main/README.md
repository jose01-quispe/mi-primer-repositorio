# CinePlanet — Sistema de ventas (CRUD con JavaFX)

Proyecto final del curso. Aplicación de escritorio en **Java 21 + JavaFX 21 + Maven** con arquitectura en capas
(**Model → Repository → Service → Controller → View FXML**) e inyección de dependencias manual.

## Caso
CinePlanet administra salas, películas, funciones y la venta de entradas y productos de dulcería (confitería).
Un usuario del sistema inicia sesión, registra clientes y vende entradas para una función más productos del carrito.

## Lista de requerimientos
1. **Login**: ingreso con usuario y clave; la clave se guarda encriptada (PBKDF2-HMAC-SHA256 con salt).
2. **CRUD de Clientes** (DNI, CE, RUC, Pasaporte): listar, buscar, crear, editar y eliminar.
3. **CRUD de Películas**: título, género y duración.
4. **CRUD de Salas**: número, capacidad y tipo (2D, 3D, etc.).
5. **CRUD de Productos** de dulcería (categoría, marca, unidad de medida, precio).
6. **Funciones**: película + sala + fecha + hora + precio de entrada.
7. **Ventas**:
   - Seleccionar cliente y función, indicar cantidad de entradas.
   - Agregar productos al carrito, quitar o vaciar el carrito.
   - **Tipo de pago obligatorio: Efectivo, Yape o Tarjeta.**
   - Calcular subtotal de entradas, de productos y total.
   - Generar e imprimir el **ticket** de la venta, y volver a verlo desde la tabla de ventas.
8. **Validación de campos** en todos los formularios (Jakarta Validation) y **mensaje de la acción realizada**
   (guardado, actualizado, eliminado, venta registrada) o del error.
9. **Investigación adicional** (no vista en clases):
   - **Encriptación de claves** de usuario (`util/PasswordUtil`).
   - **Paginación de tablas** (`components/PaginadorTabla`) en Películas, Salas, Clientes y Productos:
     botones « ‹ › », selector de filas por página (5/10/20/50), texto "Mostrando 1–10 de N" y
     ordenamiento por columna que afecta a todas las páginas. Se combina con el buscador (vuelve a la página 1).

## Base de datos
Script en `db/cineplanet_script_unido.sql`. Modelo en SQL Power Architect.
Para ver la paginación con varias páginas, ejecutar después `db/datos_demo_paginacion.sql` (datos extra de prueba).

## Usuario de prueba
`admin` / `admin123`

## Ejecutar
```
cd SysVentas
./mvnw javafx:run
```
