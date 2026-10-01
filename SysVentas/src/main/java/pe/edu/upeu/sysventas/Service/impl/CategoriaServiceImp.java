package pe.edu.upeu.sysventas.Service.impl;

import pe.edu.upeu.sysventas.Repository.CategoriaRepository;
import pe.edu.upeu.sysventas.Repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.Service.ICategoriaService;
import pe.edu.upeu.sysventas.model.Categoria;

import java.util.List;

public class CategoriaServiceImp extends CrudGenericoServiceImp<Categoria, Long> implements ICategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImp(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    protected ICrudGenericoRepository<Categoria, Long> getRepo() {
        return null;
    }

    @Override
    public List<Categoria> finAll() {
        return List.of();
    }


}
