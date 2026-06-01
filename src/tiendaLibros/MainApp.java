package tiendaLibros;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tiendaLibros.util.ArchivoUtil;

public class MainApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        ArchivoUtil.inicializarArchivos();
        mostrarLogin();
    }

    public static void mostrarLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(
            MainApp.class.getResource("/tiendaLibros/fxml/Login.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Tienda de Libros - Login");
        primaryStage.setScene(new Scene(root, 480, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
