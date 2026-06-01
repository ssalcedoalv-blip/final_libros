package tiendaLibros.vista;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import tiendaLibros.MainApp;
import tiendaLibros.controlador.*;
import tiendaLibros.model.*;

/**
 * CONTROLADOR DE VISTA - UsuarioController
 * Catálogo con tarjetas tipo tienda (imagen + título + precio + botones)
 * Carrito: Pila LIFO | Deseos: ListaSencilla | Historial: Cola FIFO
 */
public class UsuarioController {

    @FXML private Label      lblBienvenida, lblCarritoCount;
    @FXML private FlowPane   flowCatalogo;
    @FXML private TextField  txtBuscar;

    @FXML private TableView<ItemCarrito>  tablaCarrito;
    @FXML private TableColumn<ItemCarrito,String>  colCarritoTitulo;
    @FXML private TableColumn<ItemCarrito,Integer> colCarritoCantidad;
    @FXML private TableColumn<ItemCarrito,Double>  colCarritoSubtotal;
    @FXML private Label lblTotal, lblMsgCarrito;

    @FXML private TableView<Libro>  tablaDeseos;
    @FXML private TableColumn<Libro,String> colDeseosTitulo, colDeseosAutor;
    @FXML private TableColumn<Libro,Double> colDeseosPrecio;
    @FXML private Label lblMsgDeseos;

    @FXML private TableView<Compra>  tablaHistorial;
    @FXML private TableColumn<Compra,String> colHistFecha, colHistDetalle;
    @FXML private TableColumn<Compra,Double> colHistTotal;

    private CatalogoControlador  catalogoCtrl;
    private CarritoControlador   carritoCtrl;
    private DeseosControlador    deseosCtrl;
    private HistorialControlador historialCtrl;
    private Usuario              usuarioActual;

    private ObservableList<ItemCarrito> datosCarrito;
    private ObservableList<Libro>       datosDeseos;
    private ObservableList<Compra>      datosHistorial;
    private Libro[]                     todosLosLibros;

    @FXML
    public void initialize() {
        catalogoCtrl  = new CatalogoControlador();

        // Carrito: Pila LIFO
        colCarritoTitulo.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(d.getValue().getLibro().getTitulo()));
        colCarritoCantidad.setCellValueFactory(d ->
            new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getCantidad()));
        colCarritoSubtotal.setCellValueFactory(d ->
            new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getSubtotal()));
        datosCarrito = FXCollections.observableArrayList();
        tablaCarrito.setItems(datosCarrito);

        // Deseos: ListaSencilla
        colDeseosTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDeseosAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colDeseosPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        datosDeseos = FXCollections.observableArrayList();
        tablaDeseos.setItems(datosDeseos);

        // Historial: Cola FIFO
        colHistFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHistDetalle.setCellValueFactory(new PropertyValueFactory<>("detalle"));
        colHistTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        datosHistorial = FXCollections.observableArrayList();
        tablaHistorial.setItems(datosHistorial);
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " 👋");
        carritoCtrl  = new CarritoControlador(usuario.getNombre());
        deseosCtrl   = new DeseosControlador(usuario.getNombre());
        historialCtrl = new HistorialControlador();
        todosLosLibros = catalogoCtrl.getLibrosArray();
        cargarTarjetas(todosLosLibros);
        refrescarHistorial();
    }

    // ── CATÁLOGO CON TARJETAS ─────────────────────────────────

    /** Construye las tarjetas tipo tienda para cada libro */
    private void cargarTarjetas(Libro[] libros) {
        flowCatalogo.getChildren().clear();
        for (Libro libro : libros) {
            flowCatalogo.getChildren().add(crearTarjeta(libro));
        }
    }

    private VBox crearTarjeta(Libro libro) {
        VBox tarjeta = new VBox(8);
        tarjeta.setPrefWidth(200);
        tarjeta.setMaxWidth(200);
        tarjeta.setPadding(new Insets(12));
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setStyle(
            "-fx-background-color: #16213e; -fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3);"
        );

        // Imagen del libro
        ImageView imgView = new ImageView();
        imgView.setFitWidth(160);
        imgView.setFitHeight(200);
        imgView.setPreserveRatio(true);
        imgView.setStyle("-fx-background-radius: 8;");

        try {
            String src = libro.getImagen();
            if (src != null && !src.isEmpty()) {
                if (src.startsWith("http") || src.startsWith("file:")) {
                    imgView.setImage(new Image(src, 160, 200, true, true, true));
                } else {
                    imgView.setImage(new Image("file:///" + src.replace("\\", "/"), 160, 200, true, true, true));
                }
            }
        } catch (Exception e) {
            // Si no carga la imagen, mostrar placeholder
        }

        // Placeholder si no hay imagen
        if (imgView.getImage() == null || imgView.getImage().isError()) {
            Label placeholder = new Label("📚");
            placeholder.setStyle("-fx-font-size: 60px;");
            placeholder.setPrefSize(160, 200);
            placeholder.setAlignment(Pos.CENTER);
            tarjeta.getChildren().add(placeholder);
        } else {
            tarjeta.getChildren().add(imgView);
        }

        // Título
        Label lblTitulo = new Label(libro.getTitulo());
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #e2e2e2; -fx-font-size: 13px;");
        lblTitulo.setWrapText(true);
        lblTitulo.setMaxWidth(176);

        // Autor
        Label lblAutor = new Label(libro.getAutor());
        lblAutor.setStyle("-fx-text-fill: #a8a8b3; -fx-font-size: 11px;");
        lblAutor.setWrapText(true);
        lblAutor.setMaxWidth(176);

        // Género
        Label lblGenero = new Label(libro.getGenero());
        lblGenero.setStyle(
            "-fx-background-color: #0f3460; -fx-text-fill: #a8a8b3;" +
            "-fx-background-radius: 10; -fx-padding: 2 8 2 8; -fx-font-size: 10px;"
        );

        // Descripción
        Label lblDesc = new Label(libro.getDescripcion());
        lblDesc.setStyle("-fx-text-fill: #888; -fx-font-size: 10px;");
        lblDesc.setWrapText(true);
        lblDesc.setMaxWidth(176);
        lblDesc.setMaxHeight(40);

        // Precio
        Label lblPrecio = new Label("$" + String.format("%.0f", libro.getPrecio()));
        lblPrecio.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        // Stock
        Label lblStock = new Label("Stock: " + libro.getStock());
        lblStock.setStyle("-fx-text-fill: #a8a8b3; -fx-font-size: 10px;");

        // Botones
        Button btnCarrito = new Button("🛒 Agregar");
        btnCarrito.setPrefWidth(176);
        btnCarrito.setStyle(
            "-fx-background-color: #e94560; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;"
        );
        btnCarrito.setOnAction(e -> {
            if (carritoCtrl.agregar(libro, 1)) {
                refrescarCarrito();
                lblCarritoCount.setText("🛒 " + carritoCtrl.getCantidadItems() + " items");
            } else {
                new Alert(Alert.AlertType.WARNING, "Stock insuficiente.", ButtonType.OK).showAndWait();
            }
        });

        Button btnDeseos = new Button("❤️ Deseos");
        btnDeseos.setPrefWidth(176);
        btnDeseos.setStyle(
            "-fx-background-color: #0f3460; -fx-text-fill: #a8a8b3;" +
            "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;"
        );
        btnDeseos.setOnAction(e -> {
            if (deseosCtrl.agregar(libro)) {
                refrescarDeseos();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Ya está en tu lista de deseos.", ButtonType.OK).showAndWait();
            }
        });

        tarjeta.getChildren().addAll(lblTitulo, lblAutor, lblGenero, lblDesc, lblPrecio, lblStock, btnCarrito, btnDeseos);
        return tarjeta;
    }

    @FXML private void handleBuscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        if (texto.isEmpty()) { cargarTarjetas(todosLosLibros); return; }
        java.util.List<Libro> filtrados = new java.util.ArrayList<>();
        for (Libro l : todosLosLibros) {
            if (l.getTitulo().toLowerCase().contains(texto) ||
                l.getAutor().toLowerCase().contains(texto) ||
                l.getGenero().toLowerCase().contains(texto)) {
                filtrados.add(l);
            }
        }
        cargarTarjetas(filtrados.toArray(new Libro[0]));
    }

    @FXML private void handleMostrarTodos() {
        txtBuscar.clear();
        todosLosLibros = catalogoCtrl.getLibrosArray();
        cargarTarjetas(todosLosLibros);
    }

    // ── CARRITO (PILA) ────────────────────────────────────────

    @FXML private void handleQuitarUltimo() {
        ItemCarrito q = carritoCtrl.quitarUltimo();
        if (q != null) {
            refrescarCarrito();
            lblCarritoCount.setText("🛒 " + carritoCtrl.getCantidadItems() + " items");
            msg(lblMsgCarrito, "↩️ Quitado: " + q.getLibro().getTitulo(), true);
        } else msg(lblMsgCarrito, "El carrito está vacío.", false);
    }

    @FXML private void handleConfirmarCompra() {
        if (carritoCtrl.estaVacio()) { msg(lblMsgCarrito, "El carrito está vacío.", false); return; }
        double total = carritoCtrl.getTotal();
        historialCtrl.registrarCompra(usuarioActual.getNombre(), carritoCtrl.getItemsArray(), total);
        carritoCtrl.vaciar();
        refrescarCarrito();
        refrescarHistorial();
        lblCarritoCount.setText("🛒 0 items");
        msg(lblMsgCarrito, "✅ Compra realizada por $" + String.format("%.0f", total), true);
        new Alert(Alert.AlertType.INFORMATION, "¡Gracias por tu compra!\nTotal: $" + String.format("%.0f", total), ButtonType.OK).showAndWait();
    }

    // ── DESEOS (LISTA SENCILLA) ───────────────────────────────

    @FXML private void handleEliminarDeseo() {
        Libro sel = tablaDeseos.getSelectionModel().getSelectedItem();
        if (sel == null) { msg(lblMsgDeseos, "Selecciona un libro.", false); return; }
        if (deseosCtrl.eliminar(sel)) { refrescarDeseos(); msg(lblMsgDeseos, "✅ Eliminado de deseos.", true); }
    }

    @FXML private void handleMoverAlCarrito() {
        Libro sel = tablaDeseos.getSelectionModel().getSelectedItem();
        if (sel == null) { msg(lblMsgDeseos, "Selecciona un libro.", false); return; }
        if (carritoCtrl.agregar(sel, 1)) {
            deseosCtrl.eliminar(sel);
            refrescarDeseos(); refrescarCarrito();
            lblCarritoCount.setText("🛒 " + carritoCtrl.getCantidadItems() + " items");
            msg(lblMsgDeseos, "✅ Movido al carrito: " + sel.getTitulo(), true);
        } else msg(lblMsgDeseos, "Stock insuficiente.", false);
    }

    // ── HISTORIAL (COLA) ──────────────────────────────────────

    @FXML private void handleRefrescarHistorial() {
        historialCtrl = new HistorialControlador(); refrescarHistorial();
    }

    @FXML private void handleCerrarSesion() {
        try { MainApp.mostrarLogin(); } catch (Exception e) { e.printStackTrace(); }
    }

    // ── REFRESCAR ─────────────────────────────────────────────

    private void refrescarCarrito() {
        datosCarrito.clear();
        for (ItemCarrito i : carritoCtrl.getItemsArray()) datosCarrito.add(i);
        lblTotal.setText("Total: $" + String.format("%.0f", carritoCtrl.getTotal()));
    }

    private void refrescarDeseos() {
        datosDeseos.clear();
        for (Libro l : deseosCtrl.getDeseosArray()) datosDeseos.add(l);
    }

    private void refrescarHistorial() {
        datosHistorial.clear();
        for (Compra c : historialCtrl.getComprasArray()) datosHistorial.add(c);
    }

    private void msg(Label lbl, String texto, boolean ok) {
        lbl.setStyle(ok ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
        lbl.setText(texto);
    }
}
