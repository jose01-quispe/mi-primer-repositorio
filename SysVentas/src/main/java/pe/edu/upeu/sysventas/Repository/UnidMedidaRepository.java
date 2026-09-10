package pe.edu.upeu.sysventas.Repository;

import pe.edu.upeu.sysventas.model.UnidMedida;

public class UnidMedidaRepository extends AbstractJpaRepository<UnidMedida, Long> {
    private long sequense=1;
    @Override
    protected Long getId(UnidMedida entity) {
        return entity.getIdUnidad();
    }

    @Override
    protected void setId(UnidMedida entity, Long aLong) {
entity.setIdUnidad(aLong);
    }

    @Override
    protected Long generateId() {
        return sequense++;
    }
}
