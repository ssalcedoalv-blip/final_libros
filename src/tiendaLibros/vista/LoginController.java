package tiendaLibros.vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import tiendaLibros.MainApp;
import tiendaLibros.controlador.LoginControlador;
import tiendaLibros.model.Usuario;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblError;

    private final LoginControlador loginCtrl = new LoginControlador();

    @FXML
    private void handleLogin() {
        String nombre = txtUsuario.getText().trim();
        String pass   = txtContrasena.getText().trim();

        if (nombre.isEmpty() || pass.isEmpty()) {
            lblError.setText("Completa todos los campos."); return;
        }

        Usuario u = loginCtrl.validarLogin(nombre, pass);
        if (u != null) {
            try { abrirDashboard(u); }
            catch (Exception e) { lblError.setText("Error al abrir el sistema."); e.printStackTrace(); }
        } else {
            lblError.setText("Usuario o contraseña incorrectos.");
            txtContrasena.clear();
        }
    }

    private void abrirDashboard(Usuario usuario) throws Exception {
        String fxml = "admin".equals(usuario.getRol())
            ? "/tiendaLibros/fxml/DashboardAdmin.fxml"
            : "/tiendaLibros/fxml/DashboardUsuario.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();

        if ("admin".equals(usuario.getRol())) {
            AdminController ctrl = loader.getController();
            ctrl.setUsuario(usuario);
        } else {
            UsuarioController ctrl = loader.getController();
            ctrl.setUsuario(usuario);
        }

        MainApp.primaryStage.setScene(new Scene(root, 1050, 700));
        MainApp.primaryStage.setResizable(true);
    }
}
