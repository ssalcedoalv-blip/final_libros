package tiendaLibros.controlador;

import tiendaLibros.model.Libro;
import tiendaLibros.model.estructuras.ListaSencilla;
import tiendaLibros.util.ArchivoUtil;

/**
 * CONTROLADOR - DeseosControlador
 * Lista de deseos usando ListaSencilla propia. Guarda en deseos.txt.
 */
public class DeseosControlador {
    private ListaSencilla<Libro> listaDeseos;
    private String usuario;

    public DeseosControlador(String usuario) {
        this.usuario = usuario;
        listaDeseos = new ListaSencilla<>();
    }

    public boolean agregar(Libro libro) {
        if (listaDeseos.buscar(l -> l.getIsbn().equals(libro.getIsbn())) != null) return false;
        listaDeseos.agregar(libro);
        ArchivoUtil.guardarDeseos(usuario, listaDeseos);
        return true;
    }

    public boolean eliminar(Libro libro) {
        boolean ok = listaDeseos.eliminar(libro);
        if (ok) ArchivoUtil.guardarDeseos(usuario, listaDeseos);
        return ok;
    }

    public Libro[] getDeseosArray() {
        Object[] arr = listaDeseos.toArray();
        Libro[] libros = new Libro[arr.length];
        for (int i = 0; i < arr.length; i++) libros[i] = (Libro) arr[i];
        return libros;
    }

    public ListaSencilla<Libro> getLista() { return listaDeseos; }
    public boolean estaVacia() { return listaDeseos.estaVacia(); }
}
