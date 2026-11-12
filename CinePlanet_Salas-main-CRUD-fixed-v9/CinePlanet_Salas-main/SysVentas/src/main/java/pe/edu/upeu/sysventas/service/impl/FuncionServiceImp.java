package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.model.Funcion;
import pe.edu.upeu.sysventas.repository.FuncionRepository;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.service.IFuncionService;
import pe.edu.upeu.sysventas.service.IPeliculaService;
import pe.edu.upeu.sysventas.service.ISalaService;

import java.util.List;

@RequiredArgsConstructor
public class FuncionServiceImp extends CrudGenericoServiceImp<Funcion, Long> implements IFuncionService {
    private final FuncionRepository funcionRepository;
    private final ISalaService salaService;
    private final IPeliculaService peliculaService;
    private boolean sembrado;

    @Override
    protected ICrudGenericoRepository<Funcion, Long> getRepo() {
        return funcionRepository;
    }

    @Override
    public List<Funcion> findAll() {
        if (!sembrado) {
            sembrado = true;
            if (funcionRepository.findAll().isEmpty()) {
                funcionRepository.seedData(salaService.findAll(), peliculaService.findAll());
            }
        }
        return funcionRepository.findAll();
    }
}
