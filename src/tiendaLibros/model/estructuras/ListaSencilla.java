package tiendaLibros.model.estructuras;

import tiendaLibros.model.nodos.Nodo;
import java.util.function.Predicate;

/**
 * LISTA ENLAZADA SENCILLA propia.
 * Usada para: usuarios del Login y lista de deseos.
 */
public class ListaSencilla<T> {
    private Nodo<T> cabeza;
    private int tamanio;

    public ListaSencilla() { this.cabeza = null; this.tamanio = 0; }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) { cabeza = nuevo; }
        else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;
        if (cabeza.dato.equals(dato)) { cabeza = cabeza.siguiente; tamanio--; return true; }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--; return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public T buscar(Predicate<T> condicion) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (condicion.test(actual.dato)) return actual.dato;
            actual = actual.siguiente;
        }
        return null;
    }

    public Object[] toArray() {
        Object[] arr = new Object[tamanio];
        Nodo<T> actual = cabeza;
        int i = 0;
        while (actual != null) { arr[i++] = actual.dato; actual = actual.siguiente; }
        return arr;
    }

    public int getTamanio() { return tamanio; }
    public boolean estaVacia() { return cabeza == null; }
    public Nodo<T> getCabeza() { return cabeza; }
}
