package pe.edu.upeu.sysventas.Service;

import java.util.List;

public interface ICruGenericoService <T, ID> {
T save(T entity);
T update(ID id,T entity);
List<T>finAll();
T findById(ID id);
void delete(ID id);
}
