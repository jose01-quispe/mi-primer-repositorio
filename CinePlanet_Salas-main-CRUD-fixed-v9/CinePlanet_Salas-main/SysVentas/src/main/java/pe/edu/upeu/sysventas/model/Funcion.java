package pe.edu.upeu.sysventas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Funcion {
    private Long idFuncion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private Double precioEntrada;
    private Sala sala;
    private Pelicula pelicula;

    public String getEtiqueta() {
        String titulo = pelicula == null ? "" : pelicula.getTitulo();
        String datosSala = sala == null ? "" : "Sala " + sala.getNumero() + " (" + sala.getTipo() + ")";
        return titulo + " - " + datosSala + " - " + fecha + " " + horaInicio
                + " - S/ " + String.format(Locale.US, "%.2f", precioEntrada);
    }
}
