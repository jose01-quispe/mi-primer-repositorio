package pe.edu.upeu.sysventas.service.impl;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.repository.ICrudGenericoRepository;
import pe.edu.upeu.sysventas.repository.UsuarioRepository;
import pe.edu.upeu.sysventas.service.IUsuarioService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UsuarioServiceImp extends CrudGenericoServiceImp<Usuario, Long> implements IUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private boolean sembrado;

    @Override
    protected ICrudGenericoRepository<Usuario, Long> getRepo() {
        return usuarioRepository;
    }

    @Override
    public List<Usuario> findAll() {
        sembrar();
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> loginUsuario(String usuario, String clave) {
        sembrar();
        return usuarioRepository.findByUsuarioAndClave(usuario, clave);
    }

    private void sembrar() {
        if (!sembrado) {
            sembrado = true;
            if (usuarioRepository.findAll().isEmpty()) {
                usuarioRepository.seedData();
            }
        }
    }
}
