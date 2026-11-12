package pe.edu.upeu.sysventas.model;

public class ItemCarrito {
    private final Long idProducto;
    private final String nombre;
    private final Integer cantidad;
    private final Double precioUnitario;

    public ItemCarrito(Long idProducto, String nombre, Integer cantidad, Double precioUnitario) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public Double getSubtotal() {
        return Math.round(cantidad * precioUnitario * 100.0) / 100.0;
    }
}
