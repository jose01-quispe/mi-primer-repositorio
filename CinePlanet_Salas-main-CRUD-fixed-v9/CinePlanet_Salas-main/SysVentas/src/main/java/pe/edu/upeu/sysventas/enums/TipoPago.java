package pe.edu.upeu.sysventas.enums;

public enum TipoPago {
    EFECTIVO("Efectivo"),
    YAPE("Yape"),
    TARJETA("Tarjeta");

    private final String etiqueta;

    TipoPago(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
