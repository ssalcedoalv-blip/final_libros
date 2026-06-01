package tiendaLibros.model.estructuras;

import tiendaLibros.model.nodos.Nodo;
import java.util.function.Predicate;

/**
 * LISTA DOBLEMENTE ENLAZADA CIRCULAR propia.
 * Usada para: catálogo de libros.
 * El último nodo apunta al primero (circular).
 */
public class ListaDobleCircular<T> {
    private Nodo<T> cabeza;
    private int tamanio;

    public ListaDobleCircular() { this.cabeza = null; this.tamanio = 0; }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
            cabeza.anterior = cabeza;
        } else {
            Nodo<T> ultimo = cabeza.anterior;
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
        }
        tamanio++;
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (actual.dato.equals(dato)) {
                if (tamanio == 1) { cabeza = null; }
                else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) cabeza = actual.siguiente;
                }
                tamanio--; return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public T buscar(Predicate<T> condicion) {
        if (cabeza == null) return null;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (condicion.test(actual.dato)) return actual.dato;
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean actualizar(Predicate<T> condicion, T nuevoDato) {
        if (cabeza == null) return false;
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) {
            if (condicion.test(actual.dato)) { actual.dato = nuevoDato; return true; }
            actual = actual.siguiente;
        }
        return false;
    }

    public Object[] toArray() {
        if (cabeza == null) return new Object[0];
        Object[] arr = new Object[tamanio];
        Nodo<T> actual = cabeza;
        for (int i = 0; i < tamanio; i++) { arr[i] = actual.dato; actual = actual.siguiente; }
        return arr;
    }

    public int getTamanio() { return tamanio; }
    public boolean estaVacia() { return cabeza == null; }
    public Nodo<T> getCabeza() { return cabeza; }
}
