package pe.edu.upeu.sysventas.Service.impl;

import pe.edu.upeu.sysventas.Repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.Service.IProductoService;
import pe.edu.upeu.sysventas.model.Producto;
import pe.edu.upeu.sysventas.model.UnidMedida;

import java.util.List;

public class UnidadMedidaServiceImp extends CrudGenericoServiceImp<UnidMedida, Long>implements IProductoService {
    private final UnidMedida UnidadMedidaRepository;

    public UnidadMedidaServiceImp(UnidMedida unidadMedidaRepository) {
        UnidadMedidaRepository = unidadMedidaRepository;
    }

    @Override
    protected ICrudGenericoRepository<UnidMedida, Long> getRepo() {
        return UnidadMedidaRepository;
    }

    @Override
    public Producto save(Producto entity) {
        return null;
    }

    @Override
    public Producto update(Long aLong, Producto entity) {
        return null;
    }

    @Override
    public List<UnidMedida> finAll() {
        return List.of();
    }
}
