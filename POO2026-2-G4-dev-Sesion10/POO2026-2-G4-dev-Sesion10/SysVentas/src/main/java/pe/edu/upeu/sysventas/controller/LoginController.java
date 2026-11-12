package pe.edu.upeu.sysventas.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.sysventas.SysVentas;
import pe.edu.upeu.sysventas.components.StageManager;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.config.AppContext;
import pe.edu.upeu.sysventas.dto.SessionManager;
import pe.edu.upeu.sysventas.model.Usuario;
import pe.edu.upeu.sysventas.service.IUsuarioService;

@RequiredArgsConstructor
public class LoginController {

    private final IUsuarioService us;

    @FXML
    TextField txtUsuario;

    @FXML
    PasswordField txtClave;

    @FXML
    public void initialize(){
        txtClave.setOnKeyPressed((KeyEvent e)->{
            if(e.getCode()== KeyCode.ENTER){
                us.loginUsuario(txtUsuario.getText(), txtClave.getText())
                        .ifPresentOrElse(usu->{
                                    abrirPantalla(e, usu);
                                },
                                ()->{
                                    Stage stage=(Stage) ((Node)e.getSource()).getScene().getWindow();
                                    mensaje(stage, "Error de autenticacion!!", "#FFB7C5");
                                });
            }
        });
    }

    @FXML
    public void login(ActionEvent e){
       us.loginUsuario(txtUsuario.getText(), txtClave.getText())
               .ifPresentOrElse(usu->{ abrirPantalla(e, usu); },
                       ()->{
                           Stage stage=(Stage) ((Node)e.getSource()).getScene().getWindow();
                           mensaje(stage, "Error de autenticacion!!", "#FFB7C5");
                       });
    }

    public void mensaje(Stage stage, String msg, String color){
        double w = stage.getWidth() / 1.5, h = stage.getHeight() / 2;
        Toast.showToast(stage, msg, color,2000, w, h);
    }

    private void abrirPantalla(KeyEvent e, Usuario u){
        Stage stage=(Stage) ((Node)e.getSource()).getScene().getWindow();
        abrirPantalla(stage, u);
    }

    private void abrirPantalla(ActionEvent e, Usuario u){
        Stage stage=(Stage) ((Node)e.getSource()).getScene().getWindow();
        abrirPantalla(stage, u);
    }

    private void abrirPantalla(Stage stage, Usuario u){
        SessionManager.getInstance().setUserPerfil(u.getIdPerfil().getNombre());
        SessionManager.getInstance().setUserId(u.getIdUsuario());
        SessionManager.getInstance().setUserName(u.getUsuario());

        StageManager.setPrimaryStage(stage);

        try {
            AppContext appContext=AppContext.getInstance();
            Screen screen=Screen.getPrimary();
            FXMLLoader fxmlLoader = new FXMLLoader(SysVentas.class.getResource("/view/maingui.fxml"));
            fxmlLoader.setControllerFactory(appContext::getBean);
            Rectangle2D dimension=screen.getBounds();

            Scene scene = new Scene(fxmlLoader.load(), dimension.getWidth(), dimension.getHeight());
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setTitle("SysVentas Login!");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setX(dimension.getMinX());
            stage.setY(dimension.getMinY());
            stage.show();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    public void logout(){
        Platform.exit();
        System.exit(0);
    }



}
