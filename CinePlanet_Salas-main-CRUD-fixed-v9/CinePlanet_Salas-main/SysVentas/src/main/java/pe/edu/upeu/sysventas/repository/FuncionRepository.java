package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Funcion;
import pe.edu.upeu.sysventas.model.Pelicula;
import pe.edu.upeu.sysventas.model.Sala;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FuncionRepository extends AbstractJpaRepository<Funcion, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Funcion entity) {
        return entity.getIdFuncion();
    }

    @Override
    protected void setId(Funcion entity, Long id) {
        entity.setIdFuncion(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData(List<Sala> salas, List<Pelicula> peliculas) {
        if (findAll().isEmpty()) {
            crear(salas, peliculas, LocalDate.of(2026, 10, 20), LocalTime.of(15, 0), 18.0, 1L, 1L);
            crear(salas, peliculas, LocalDate.of(2026, 10, 20), LocalTime.of(18, 30), 25.0, 2L, 2L);
            crear(salas, peliculas, LocalDate.of(2026, 10, 20), LocalTime.of(21, 0), 35.0, 3L, 4L);
            crear(salas, peliculas, LocalDate.of(2026, 10, 21), LocalTime.of(16, 0), 18.0, 1L, 3L);
            crear(salas, peliculas, LocalDate.of(2026, 10, 21), LocalTime.of(20, 0), 25.0, 2L, 2L);
        }
    }

    private void crear(List<Sala> salas, List<Pelicula> peliculas, LocalDate fecha, LocalTime hora,
                       double precio, Long idSala, Long idPelicula) {
        Sala sala = salas.stream().filter(s -> idSala.equals(s.getIdSala())).findFirst().orElse(null);
        Pelicula pelicula = peliculas.stream().filter(p -> idPelicula.equals(p.getIdPelicula())).findFirst().orElse(null);
        if (sala == null || pelicula == null) {
            return;
        }
        save(Funcion.builder()
                .idFuncion(generateId())
                .fecha(fecha)
                .horaInicio(hora)
                .precioEntrada(precio)
                .sala(sala)
                .pelicula(pelicula)
                .build());
    }
}
