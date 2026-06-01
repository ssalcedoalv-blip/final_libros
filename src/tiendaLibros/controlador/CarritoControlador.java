package tiendaLibros.controlador;

import tiendaLibros.model.ItemCarrito;
import tiendaLibros.model.Libro;
import tiendaLibros.model.estructuras.Pila;
import tiendaLibros.util.ArchivoUtil;

/**
 * CONTROLADOR - CarritoControlador
 * Usa Pila LIFO propia. Guarda en carrito.txt.
 */
public class CarritoControlador {
    private Pila<ItemCarrito> carrito;
    private double total;
    private String usuario;

    public CarritoControlador(String usuario) {
        this.usuario = usuario;
        carrito = new Pila<>();
        total = 0;
    }

    public boolean agregar(Libro libro, int cantidad) {
        if (libro.getStock() < cantidad) return false;
        ItemCarrito item = new ItemCarrito(libro, cantidad);
        carrito.apilar(item);
        total += item.getSubtotal();
        ArchivoUtil.guardarCarrito(usuario, carrito);
        return true;
    }

    public ItemCarrito quitarUltimo() {
        ItemCarrito item = carrito.desapilar();
        if (item != null) {
            total -= item.getSubtotal();
            ArchivoUtil.guardarCarrito(usuario, carrito);
        }
        return item;
    }

    public ItemCarrito[] getItemsArray() {
        Object[] arr = carrito.toArray();
        ItemCarrito[] items = new ItemCarrito[arr.length];
        for (int i = 0; i < arr.length; i++) items[i] = (ItemCarrito) arr[i];
        return items;
    }

    public void vaciar() { carrito.vaciar(); total = 0; ArchivoUtil.guardarCarrito(usuario, carrito); }
    public double getTotal() { return total; }
    public boolean estaVacio() { return carrito.estaVacia(); }
    public int getCantidadItems() { return carrito.getTamanio(); }
    public Pila<ItemCarrito> getCarrito() { return carrito; }
}
