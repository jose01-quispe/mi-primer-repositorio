package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.SysVentas;
import pe.edu.upeu.sysventas.dto.SessionManager;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.service.IUsuarioService;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginController {

    private final IUsuarioService usuarioService;

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtClave;
    @FXML
    private Label lblError;

    @FXML
    public void initialize() {
        txtUsuario.setOnAction(e -> txtClave.requestFocus());
        txtClave.setOnAction(e -> ingresar());
        Platform.runLater(txtUsuario::requestFocus);
    }

    @FXML
    public void ingresar() {
        String usuario = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
        String clave = txtClave.getText() == null ? "" : txtClave.getText();
        if (usuario.isEmpty() || clave.isEmpty()) {
            lblError.setText("Ingresa tu usuario y tu clave");
            return;
        }
        usuarioService.loginUsuario(usuario, clave).ifPresentOrElse(
                this::abrirPrincipal,
                () -> {
                    lblError.setText("Usuario o clave incorrectos");
                    txtClave.clear();
                    txtClave.requestFocus();
                });
    }

    @FXML
    public void cerrar() {
        Platform.exit();
    }

    private void abrirPrincipal(Usuario usuario) {
        SessionManager sesion = SessionManager.getInstance();
        sesion.setUserId(usuario.getIdUsuario());
        sesion.setUserName(usuario.getUsuario());
        sesion.setUserPerfil(usuario.getIdPerfil() == null ? "" : usuario.getIdPerfil().getNombre());
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        try {
            SysVentas.mostrarPrincipal(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
