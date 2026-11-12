package pe.edu.upeu.sysventas.dto;

import lombok.Data;

@Data
public class SessionManager {
    private static SessionManager instance;

    private Long userId;
    private String userName;
    private String userPerfil;

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void limpiar() {
        userId = null;
        userName = null;
        userPerfil = null;
    }
}
