package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Perfil;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.util.PasswordUtil;

import java.util.Optional;

public class UsuarioRepository extends AbstractJpaRepository<Usuario, Long> {
    private long sequence = 1;

    @Override
    protected Long getId(Usuario entity) {
        return entity.getIdUsuario();
    }

    @Override
    protected void setId(Usuario entity, Long id) {
        entity.setIdUsuario(id);
    }

    @Override
    protected Long generateId() {
        return sequence++;
    }

    public void seedData() {
        if (findAll().isEmpty()) {
            Perfil perfil = new Perfil();
            perfil.setIdPerfil(1L);
            perfil.setNombre("Administrador");
            perfil.setCodigo("ADMIN");
            save(Usuario.builder()
                    .idUsuario(generateId())
                    .usuario("admin")
                    .clave(PasswordUtil.encriptar("admin123"))
                    .idPerfil(perfil)
                    .estado("ACTIVO")
                    .build());
        }
    }

    public Optional<Usuario> findByUsuarioAndClave(String usuario, String clave) {
        return data.stream()
                .filter(u -> u.getUsuario() != null && u.getUsuario().equals(usuario))
                .filter(u -> PasswordUtil.verificar(clave, u.getClave()))
                .filter(u -> "ACTIVO".equals(u.getEstado()))
                .findFirst();
    }
}
