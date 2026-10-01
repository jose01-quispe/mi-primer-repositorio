package pe.edu.upeu.sysventas.Service.impl;

import pe.edu.upeu.sysventas.Repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.Service.IMarcaService;
import pe.edu.upeu.sysventas.model.Marca;

import java.util.List;

public class MarcaServiceImp extends CrudGenericoServiceImp<Marca, Long> implements IMarcaService {
    private final MarcaServiceImp marcaRepository;

    public MarcaServiceImp(MarcaServiceImp marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    @Override
    protected ICrudGenericoRepository<Marca, Long> getRepo() {
        return marcaRepository;
    }

    @Override
    public List<Marca> finAll() {
        return List.of();
    }
}
