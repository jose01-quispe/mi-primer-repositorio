package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.enums.TipoProducto;
import pe.edu.upeu.sysventas.model.Producto;

public class ProductoRepository extends AbstractJpaRepository<Producto, Long>{
    private long sequence=1;
    @Override
    protected Long getId(Producto entity) {
        return entity.getIdProducto();
    }

    @Override
    protected void setId(Producto entity, Long id) {
        entity.setIdProducto(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(new Producto(generateId(), "Canchita grande", 12.0));
            save(new Producto(generateId(), "Canchita mediana", 9.0));
            save(new Producto(generateId(), "Gaseosa 500 ml", 6.0));
            save(new Producto(generateId(), "Nachos con queso", 10.0));
            save(new Producto(generateId(), "Hot dog", 8.0));
        }
    }

}
