package pe.edu.upeu.sysventas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.sysventas.enums.TipoPago;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private Long idVenta;
    private LocalDateTime fechaVenta;
    private Integer cantidadEntradas;
    private Double total;
    private TipoPago tipoPago;
    private Cliente cliente;
    private Funcion funcion;
    private Usuario usuario;
    private List<VentaDetalle> detalles;

    public String getFechaTexto() {
        return fechaVenta == null ? "" : fechaVenta.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTipoPagoTexto() {
        return tipoPago == null ? "" : tipoPago.getEtiqueta();
    }

    public String getClienteNombre() {
        return cliente == null ? "" : cliente.getNombres();
    }

    public String getUsuarioNombre() {
        return usuario == null ? "" : usuario.getUsuario();
    }
}
