package tiendaLibros.model.estructuras;

import tiendaLibros.model.nodos.Nodo;

/**
 * COLA (QUEUE) FIFO propia.
 * Usada para: historial de compras.
 */
public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> final_;
    private int tamanio;

    public Cola() { this.frente = null; this.final_ = null; this.tamanio = 0; }

    public void encolar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) { frente = nuevo; final_ = nuevo; }
        else { final_.siguiente = nuevo; final_ = nuevo; }
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) return null;
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) final_ = null;
        tamanio--;
        return dato;
    }

    public T verFrente() { return estaVacia() ? null : frente.dato; }

    public Object[] toArray() {
        Object[] arr = new Object[tamanio];
        Nodo<T> actual = frente;
        int i = 0;
        while (actual != null) { arr[i++] = actual.dato; actual = actual.siguiente; }
        return arr;
    }

    public boolean estaVacia() { return frente == null; }
    public int getTamanio() { return tamanio; }
}
