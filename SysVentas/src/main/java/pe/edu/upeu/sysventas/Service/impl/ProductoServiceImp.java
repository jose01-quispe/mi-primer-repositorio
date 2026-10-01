package pe.edu.upeu.sysventas.Service.impl;

import pe.edu.upeu.sysventas.Repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.Repository.ProductoRepository;
import pe.edu.upeu.sysventas.Service.IProductoService;
import pe.edu.upeu.sysventas.model.Producto;

import java.util.List;

public class ProductoServiceImp extends CrudGenericoServiceImp<Producto, Long>implements IProductoService {
    private final ProductoRepository productoRepository;

    public ProductoServiceImp(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    protected ICrudGenericoRepository<Producto, Long> getRepo() {
        return productoRepository;
    }

    @Override
    public List<Producto> finAll() {
        return List.of();
    }
}
