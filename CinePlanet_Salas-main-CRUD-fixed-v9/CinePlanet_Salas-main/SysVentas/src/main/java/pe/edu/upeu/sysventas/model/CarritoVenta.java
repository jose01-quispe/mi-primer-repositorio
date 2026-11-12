package pe.edu.upeu.sysventas.model;

import java.util.ArrayList;
import java.util.List;

public class CarritoVenta {
    private final List<ItemCarrito> items = new ArrayList<>();

    public boolean agregar(Long idProducto, String nombre, double precioUnitario, int cantidad) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        for (int i = 0; i < items.size(); i++) {
            ItemCarrito actual = items.get(i);
            if (actual.getIdProducto().equals(idProducto)) {
                items.set(i, new ItemCarrito(idProducto, actual.getNombre(), actual.getCantidad() + cantidad, actual.getPrecioUnitario()));
                return true;
            }
        }
        items.add(new ItemCarrito(idProducto, nombre, cantidad, precioUnitario));
        return false;
    }

    public boolean quitar(Long idProducto) {
        return items.removeIf(item -> item.getIdProducto().equals(idProducto));
    }

    public int cantidadDe(Long idProducto) {
        return items.stream()
                .filter(item -> item.getIdProducto().equals(idProducto))
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }

    public List<ItemCarrito> items() {
        return new ArrayList<>(items);
    }

    public double total() {
        double suma = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        return Math.round(suma * 100.0) / 100.0;
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public void vaciar() {
        items.clear();
    }
}
