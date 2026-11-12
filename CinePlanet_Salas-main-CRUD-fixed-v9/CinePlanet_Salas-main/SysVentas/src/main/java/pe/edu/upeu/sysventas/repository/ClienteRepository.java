package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.enums.TipoDocumento;
import pe.edu.upeu.sysventas.model.Cliente;
//HE
public class ClienteRepository extends AbstractJpaRepository<Cliente, String> {
    @Override
    protected String getId(Cliente entity) {
        return entity.getDniruc();
    }
//PO
    @Override
    protected void setId(Cliente entity, String id) {
        entity.setDniruc(id);
    }

    @Override
    protected String generateId() {
        return null;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            save(new Cliente("70000001", "Cliente Demo", "", TipoDocumento.DNI));
            save(new Cliente("73625148", "Ana Torres Huanca", "", TipoDocumento.DNI));
            save(new Cliente("71456238", "Jorge Ramos Flores", "", TipoDocumento.DNI));
            save(new Cliente("001234567", "Maria Condori Quispe", "", TipoDocumento.CE));
            save(new Cliente("20601234567", "Distribuidora Andina S.A.C.", "Carlos Mamani Lopez", TipoDocumento.RUC));
        }
    }
}
