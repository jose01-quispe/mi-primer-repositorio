package pe.edu.upeu.sysventas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaDetalle {
    private Long idDetalle;
    private Integer cantidad;
    private Double precioUnitario;
    private Producto producto;
}
