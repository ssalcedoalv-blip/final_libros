package tiendaLibros.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Compra {
    private String usuario;
    private String detalle;
    private double total;
    private String fecha;

    public Compra(String usuario, String detalle, double total) {
        this.usuario = usuario;
        this.detalle = detalle;
        this.total = total;
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Compra(String usuario, String detalle, double total, String fecha) {
        this.usuario = usuario;
        this.detalle = detalle;
        this.total = total;
        this.fecha = fecha;
    }

    public String getUsuario() { return usuario; }
    public String getDetalle() { return detalle; }
    public double getTotal() { return total; }
    public String getFecha() { return fecha; }

    @Override
    public String toString() {
        return fecha + "|" + usuario + "|" + detalle + "|" + total;
    }
}
