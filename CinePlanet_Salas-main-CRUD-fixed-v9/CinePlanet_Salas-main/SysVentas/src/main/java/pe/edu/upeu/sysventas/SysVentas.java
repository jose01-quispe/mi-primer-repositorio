package pe.edu.upeu.sysventas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pe.edu.upeu.sysventas.config.AppContext;

import java.io.IOException;

public class SysVentas extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("CinePlanet");
        mostrarLogin(stage);
        stage.show();
    }

    public static void mostrarLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SysVentas.class.getResource("/view/login.fxml"));
        loader.setControllerFactory(AppContext.getInstance()::getBean);
        Scene scene = new Scene(loader.load(), 420, 470);
        scene.getStylesheets().add(SysVentas.class.getResource("/css/style.css").toExternalForm());
        stage.setMaximized(false);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.centerOnScreen();
    }

    public static void mostrarPrincipal(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SysVentas.class.getResource("/view/main.fxml"));
        loader.setControllerFactory(AppContext.getInstance()::getBean);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(loader.load(), bounds.getWidth(), bounds.getHeight() - 50);
        scene.getStylesheets().add(SysVentas.class.getResource("/css/style.css").toExternalForm());
        stage.setResizable(true);
        stage.setScene(scene);
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
