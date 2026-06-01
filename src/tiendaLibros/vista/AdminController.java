package tiendaLibros.vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import tiendaLibros.MainApp;
import tiendaLibros.controlador.CatalogoControlador;
import tiendaLibros.controlador.HistorialControlador;
import tiendaLibros.controlador.LoginControlador;
import tiendaLibros.model.*;
import tiendaLibros.model.nodos.Nodo;
import tiendaLibros.util.ArchivoUtil;
import java.io.File;

public class AdminController {

    @FXML private TableView<Libro>   tablaCatalogo;
    @FXML private TableColumn<Libro,String>  colIsbn, colTitulo, colAutor, colGenero;
    @FXML private TableColumn<Libro,Double>  colPrecio;
    @FXML private TableColumn<Libro,Integer> colStock;
    @FXML private TextField  txtIsbn, txtTitulo, txtAutor, txtGenero, txtPrecio, txtStock, txtImagen;
    @FXML private TextArea   txtDescripcion;
    @FXML private Label      lblMsgCatalogo, lblAdminNombre;

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario,String> colUserNombre, colUserRol;
    @FXML private TextField  txtUserNombre;
    @FXML private PasswordField txtUserPass;
    @FXML private ComboBox<String> cmbRol;
    @FXML private Label lblMsgUsuarios;

    @FXML private TableView<Compra>  tablaHistorial;
    @FXML private TableColumn<Compra,String> colFecha, colUsuarioCompra, colDetalle;
    @FXML private TableColumn<Compra,Double> colTotal;

    private CatalogoControlador  catalogoCtrl;
    private LoginControlador     loginCtrl;
    private HistorialControlador historialCtrl;
    private Usuario              usuarioActual;

    private ObservableList<Libro>   datosCatalogo;
    private ObservableList<Usuario> datosUsuarios;
    private ObservableList<Compra>  datosHistorial;

    @FXML
    public void initialize() {
        catalogoCtrl  = new CatalogoControlador();
        loginCtrl     = new LoginControlador();
        historialCtrl = new HistorialControlador();

        // Catálogo
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        datosCatalogo = FXCollections.observableArrayList();
        tablaCatalogo.setItems(datosCatalogo);
        refrescarCatalogo();
        tablaCatalogo.getSelectionModel().selectedItemProperty().addListener((o, v, sel) -> {
            if (sel != null) {
                txtIsbn.setText(sel.getIsbn()); txtTitulo.setText(sel.getTitulo());
                txtAutor.setText(sel.getAutor()); txtGenero.setText(sel.getGenero());
                txtPrecio.setText(String.valueOf(sel.getPrecio()));
                txtStock.setText(String.valueOf(sel.getStock()));
                txtDescripcion.setText(sel.getDescripcion());
                txtImagen.setText(sel.getImagen());
            }
        });

        // Usuarios
        colUserNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUserRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        datosUsuarios = FXCollections.observableArrayList();
        tablaUsuarios.setItems(datosUsuarios);
        refrescarUsuarios();
        cmbRol.setItems(FXCollections.observableArrayList("usuario", "admin"));
        cmbRol.setValue("usuario");
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((o, v, sel) -> {
            if (sel != null) { txtUserNombre.setText(sel.getNombre()); cmbRol.setValue(sel.getRol()); }
        });

        // Historial
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colUsuarioCompra.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        datosHistorial = FXCollections.observableArrayList();
        tablaHistorial.setItems(datosHistorial);
        refrescarHistorial();
    }

    public void setUsuario(Usuario u) {
        usuarioActual = u;
        lblAdminNombre.setText("  |  " + u.getNombre() + " (Admin)");
    }

    // ── CATÁLOGO ─────────────────────────────────────────────

    @FXML private void handleBuscarImagen() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar imagen del libro");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.jpg","*.jpeg","*.png","*.gif"));
        File f = fc.showOpenDialog(MainApp.primaryStage);
        if (f != null) txtImagen.setText(f.toURI().toString());
    }

    @FXML private void handleAgregarLibro() {
        try {
            Libro nuevo = new Libro(
                txtIsbn.getText().trim(), txtTitulo.getText().trim(),
                txtAutor.getText().trim(), txtGenero.getText().trim(),
                Double.parseDouble(txtPrecio.getText().trim()),
                Integer.parseInt(txtStock.getText().trim()),
                txtDescripcion.getText().trim(), txtImagen.getText().trim()
            );
            if (catalogoCtrl.agregarLibro(nuevo)) {
                refrescarCatalogo(); limpiarFormLibro();
                msg(lblMsgCatalogo, "✅ Libro agregado.", true);
            } else msg(lblMsgCatalogo, "❌ ISBN ya existe.", false);
        } catch (NumberFormatException e) { msg(lblMsgCatalogo, "❌ Precio y stock deben ser números.", false); }
    }

    @FXML private void handleActualizarLibro() {
        try {
            boolean ok = catalogoCtrl.actualizarLibro(
                txtIsbn.getText().trim(), txtTitulo.getText().trim(),
                txtAutor.getText().trim(), txtGenero.getText().trim(),
                Double.parseDouble(txtPrecio.getText().trim()),
                Integer.parseInt(txtStock.getText().trim()),
                txtDescripcion.getText().trim(), txtImagen.getText().trim()
            );
            if (ok) { refrescarCatalogo(); msg(lblMsgCatalogo, "✅ Libro actualizado.", true); }
            else msg(lblMsgCatalogo, "❌ ISBN no encontrado.", false);
        } catch (NumberFormatException e) { msg(lblMsgCatalogo, "❌ Precio y stock deben ser números.", false); }
    }

    @FXML private void handleEliminarLibro() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) { msg(lblMsgCatalogo, "❌ Selecciona un libro.", false); return; }
        if (catalogoCtrl.eliminarLibro(isbn)) {
            refrescarCatalogo(); limpiarFormLibro();
            msg(lblMsgCatalogo, "✅ Libro eliminado.", true);
        } else msg(lblMsgCatalogo, "❌ No encontrado.", false);
    }

    // ── USUARIOS ─────────────────────────────────────────────

    @FXML private void handleRegistrarUsuario() {
        String nombre = txtUserNombre.getText().trim();
        String pass   = txtUserPass.getText().trim();
        if (nombre.isEmpty() || pass.isEmpty()) { msg(lblMsgUsuarios, "❌ Completa todos los campos.", false); return; }
        if (loginCtrl.registrarUsuario(nombre, pass, cmbRol.getValue())) {
            refrescarUsuarios(); txtUserNombre.clear(); txtUserPass.clear();
            msg(lblMsgUsuarios, "✅ Usuario registrado.", true);
        } else msg(lblMsgUsuarios, "❌ Ese usuario ya existe.", false);
    }

    @FXML private void handleEliminarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { msg(lblMsgUsuarios, "❌ Selecciona un usuario.", false); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar usuario: " + sel.getNombre() + "?", ButtonType.OK, ButtonType.CANCEL);
        a.setHeaderText("Confirmar eliminación");
        a.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                loginCtrl.eliminarUsuario(sel);
                refrescarUsuarios(); txtUserNombre.clear(); txtUserPass.clear();
                msg(lblMsgUsuarios, "✅ Usuario eliminado.", true);
            }
        });
    }

    // ── HISTORIAL ─────────────────────────────────────────────

    @FXML private void handleRefrescarHistorial() {
        historialCtrl = new HistorialControlador(); refrescarHistorial();
    }

    @FXML private void handleCerrarSesion() {
        try { MainApp.mostrarLogin(); } catch (Exception e) { e.printStackTrace(); }
    }

    // ── REFRESCAR ─────────────────────────────────────────────

    private void refrescarCatalogo() {
        datosCatalogo.clear();
        for (Libro l : catalogoCtrl.getLibrosArray()) datosCatalogo.add(l);
    }

    private void refrescarUsuarios() {
        datosUsuarios.clear();
        Nodo<tiendaLibros.model.Usuario> n = loginCtrl.getListaUsuarios().getCabeza();
        while (n != null) { datosUsuarios.add(n.dato); n = n.siguiente; }
    }

    private void refrescarHistorial() {
        datosHistorial.clear();
        for (Compra c : historialCtrl.getComprasArray()) datosHistorial.add(c);
    }

    private void limpiarFormLibro() {
        txtIsbn.clear(); txtTitulo.clear(); txtAutor.clear(); txtGenero.clear();
        txtPrecio.clear(); txtStock.clear(); txtDescripcion.clear(); txtImagen.clear();
    }

    private void msg(Label lbl, String texto, boolean ok) {
        lbl.setStyle(ok ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
        lbl.setText(texto);
    }
}
