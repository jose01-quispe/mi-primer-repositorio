package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Venta;

public class VentaRepository extends AbstractJpaRepository<Venta, Long> {
    private long sequence = 1;
    private long sequenceDetalle = 1;

    @Override
    protected Long getId(Venta entity) {
        return entity.getIdVenta();
    }

    @Override
    protected void setId(Venta entity, Long id) {
        entity.setIdVenta(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public long siguienteIdDetalle() {
        return sequenceDetalle++;
    }
}
