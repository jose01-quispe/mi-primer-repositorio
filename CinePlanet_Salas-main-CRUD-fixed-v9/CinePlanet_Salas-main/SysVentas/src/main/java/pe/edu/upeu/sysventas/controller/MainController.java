package pe.edu.upeu.sysventas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pe.edu.upeu.sysventas.SysVentas;
import pe.edu.upeu.sysventas.dto.SessionManager;

import java.io.IOException;

public class MainController {

    @FXML
    private Label lblUsuario;

    @FXML
    public void initialize() {
        String nombre = SessionManager.getInstance().getUserName();
        lblUsuario.setText("Usuario: " + (nombre == null ? "" : nombre));
    }

    @FXML
    public void cerrarSesion() {
        SessionManager.getInstance().limpiar();
        Stage stage = (Stage) lblUsuario.getScene().getWindow();
        try {
            SysVentas.mostrarLogin(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
