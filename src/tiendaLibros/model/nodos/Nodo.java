package tiendaLibros.model.nodos;

public class Nodo<T> {
    public T dato;
    public Nodo<T> siguiente;
    public Nodo<T> anterior;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }
}
