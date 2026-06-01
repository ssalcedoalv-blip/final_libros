package tiendaLibros.controlador;

import tiendaLibros.model.Compra;
import tiendaLibros.model.ItemCarrito;
import tiendaLibros.model.estructuras.Cola;
import tiendaLibros.util.ArchivoUtil;

/**
 * CONTROLADOR - HistorialControlador
 * Cola FIFO propia. Lee y guarda en historial.txt.
 */
public class HistorialControlador {
    private Cola<Compra> historial;

    public HistorialControlador() {
        historial = ArchivoUtil.cargarHistorial();
    }

    public void registrarCompra(String usuario, ItemCarrito[] items, double total) {
        StringBuilder detalle = new StringBuilder();
        for (ItemCarrito item : items)
            detalle.append(item.getLibro().getTitulo()).append(" x").append(item.getCantidad()).append("; ");
        Compra compra = new Compra(usuario, detalle.toString(), total);
        historial.encolar(compra);
        ArchivoUtil.agregarHistorial(compra);
    }

    public Compra[] getComprasArray() {
        Object[] arr = historial.toArray();
        Compra[] compras = new Compra[arr.length];
        for (int i = 0; i < arr.length; i++) compras[i] = (Compra) arr[i];
        return compras;
    }

    public Cola<Compra> getHistorial() { return historial; }
}
