package tiendaLibros.model;

/**
 * MODELO - Libro
 * Ahora incluye campo 'imagen' (ruta local o URL).
 * Formato en libros.txt: isbn,titulo,autor,genero,precio,stock,descripcion,imagen
 */
public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private String genero;
    private double precio;
    private int stock;
    private String descripcion;
    private String imagen; // ruta local o URL

    public Libro(String isbn, String titulo, String autor, String genero,
                 double precio, int stock, String descripcion, String imagen) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getGenero() { return genero; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public String getDescripcion() { return descripcion; }
    public String getImagen() { return imagen; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    @Override
    public String toString() {
        return isbn + "|" + titulo + "|" + autor + "|" + genero + "|"
             + precio + "|" + stock + "|" + descripcion + "|" + imagen;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Libro)) return false;
        return this.isbn.equals(((Libro) obj).isbn);
    }
}
