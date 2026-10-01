package pe.edu.upeu.sysventas.Service.impl;

import pe.edu.upeu.sysventas.Exception.ModelNotFoundException;
import pe.edu.upeu.sysventas.Repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.Service.ICruGenericoService;

import java.util.List;

public  abstract class CrudGenericoServiceImp <T, ID> implements ICruGenericoService<T, ID> {
protected abstract ICrudGenericoRepository<T, ID >getRepo();

    @Override
    public T save(T entity) {
        return getRepo().save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        if (!getRepo().existsById(id)) {
            throw new ModelNotFoundException("ID no existe: " + id);
        }
        return getRepo().save(entity);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();

        }

    @Override
    public T findById(ID id) {
        return getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID no existe: " + id));
    }

    @Override
    public void delete(ID id) {
        if (!getRepo().existsById(id)) {
            throw new ModelNotFoundException("ID no existe: " + id);
        }
        getRepo().deleteById(id);
    }
}

