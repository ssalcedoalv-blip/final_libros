package tiendaLibros.util;

import tiendaLibros.model.*;
import tiendaLibros.model.estructuras.*;
import tiendaLibros.model.nodos.Nodo;

import java.io.*;
import java.nio.file.*;

/**
 * UTIL - ArchivoUtil
 * Maneja TODOS los archivos .txt del sistema:
 *   - usuarios.txt     → Lista de usuarios
 *   - libros.txt       → Catálogo de libros (con imagen y descripción)
 *   - historial.txt    → Historial de compras
 *   - carrito.txt      → Carrito guardado por usuario
 *   - deseos.txt       → Lista de deseos por usuario
 */
public class ArchivoUtil {

    private static final String CARPETA    = "datos/";
    private static final String USUARIOS   = CARPETA + "usuarios.txt";
    private static final String LIBROS     = CARPETA + "libros.txt";
    private static final String HISTORIAL  = CARPETA + "historial.txt";
    private static final String CARRITO    = CARPETA + "carrito.txt";
    private static final String DESEOS     = CARPETA + "deseos.txt";

    /** Crea carpeta y archivos con datos de ejemplo si no existen */
    public static void inicializarArchivos() {
        try {
            Files.createDirectories(Paths.get(CARPETA));

            // usuarios.txt
            File fu = new File(USUARIOS);
            if (!fu.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(fu))) {
                    pw.println("admin,admin123,admin");
                    pw.println("juan,juan123,usuario");
                    pw.println("maria,maria456,usuario");
                }
            }

            // libros.txt — formato: isbn|titulo|autor|genero|precio|stock|descripcion|imagen
            File fl = new File(LIBROS);
            if (!fl.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(fl))) {
                    pw.println("ISBN001|Cien anos de soledad|Gabriel Garcia Marquez|Novela|35000|10|Obra maestra del realismo magico latinoamericano.|https://covers.openlibrary.org/b/isbn/9780307474728-L.jpg");
                    pw.println("ISBN002|El principito|Antoine de Saint-Exupery|Infantil|22000|15|Un clasico cuento sobre la amistad y el amor.|https://covers.openlibrary.org/b/isbn/9780156012195-L.jpg");
                    pw.println("ISBN003|1984|George Orwell|Ciencia Ficcion|28000|8|Distopia sobre el totalitarismo y la vigilancia.|https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg");
                    pw.println("ISBN004|Don Quijote|Miguel de Cervantes|Clasico|45000|5|La primera novela moderna de la literatura occidental.|https://covers.openlibrary.org/b/isbn/9788420412146-L.jpg");
                    pw.println("ISBN005|Harry Potter|J.K. Rowling|Fantasia|38000|12|La historia del nino mago mas famoso del mundo.|https://covers.openlibrary.org/b/isbn/9780439708180-L.jpg");
                    pw.println("ISBN006|El alquimista|Paulo Coelho|Autoayuda|25000|20|Una historia sobre seguir tus suenos y el destino.|https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg");
                }
            }

            // historial.txt
            File fh = new File(HISTORIAL);
            if (!fh.exists()) fh.createNewFile();

            // carrito.txt
            File fc = new File(CARRITO);
            if (!fc.exists()) fc.createNewFile();

            // deseos.txt
            File fd = new File(DESEOS);
            if (!fd.exists()) fd.createNewFile();

        } catch (IOException e) {
            System.err.println("Error inicializando archivos: " + e.getMessage());
        }
    }

    // ── USUARIOS ─────────────────────────────────────────────────────────────

    public static ListaSencilla<Usuario> cargarUsuarios() {
        ListaSencilla<Usuario> lista = new ListaSencilla<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] p = linea.split(",");
                    if (p.length == 3)
                        lista.agregar(new Usuario(p[0].trim(), p[1].trim(), p[2].trim()));
                }
            }
        } catch (IOException e) { System.err.println("Error cargando usuarios: " + e.getMessage()); }
        return lista;
    }

    public static void guardarUsuarios(ListaSencilla<Usuario> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USUARIOS))) {
            Nodo<Usuario> actual = lista.getCabeza();
            while (actual != null) { pw.println(actual.dato.toString()); actual = actual.siguiente; }
        } catch (IOException e) { System.err.println("Error guardando usuarios: " + e.getMessage()); }
    }

    // ── LIBROS ───────────────────────────────────────────────────────────────

    public static ListaDobleCircular<Libro> cargarLibros() {
        ListaDobleCircular<Libro> lista = new ListaDobleCircular<>();
        try (BufferedReader br = new BufferedReader(new FileReader(LIBROS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] p = linea.split("\\|", -1);
                    if (p.length >= 8) {
                        lista.agregar(new Libro(
                            p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                            Double.parseDouble(p[4].trim()),
                            Integer.parseInt(p[5].trim()),
                            p[6].trim(), p[7].trim()
                        ));
                    }
                }
            }
        } catch (IOException e) { System.err.println("Error cargando libros: " + e.getMessage()); }
        return lista;
    }

    public static void guardarLibros(ListaDobleCircular<Libro> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LIBROS))) {
            if (!lista.estaVacia()) {
                Nodo<Libro> actual = lista.getCabeza();
                for (int i = 0; i < lista.getTamanio(); i++) {
                    pw.println(actual.dato.toString());
                    actual = actual.siguiente;
                }
            }
        } catch (IOException e) { System.err.println("Error guardando libros: " + e.getMessage()); }
    }

    // ── HISTORIAL ────────────────────────────────────────────────────────────

    public static void agregarHistorial(Compra compra) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(HISTORIAL, true))) {
            pw.println(compra.toString());
        } catch (IOException e) { System.err.println("Error guardando historial: " + e.getMessage()); }
    }

    public static Cola<Compra> cargarHistorial() {
        Cola<Compra> cola = new Cola<>();
        try (BufferedReader br = new BufferedReader(new FileReader(HISTORIAL))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    String[] p = linea.split("\\|", -1);
                    if (p.length >= 4) {
                        try {
                            double total = Double.parseDouble(p[3].trim());
                            cola.encolar(new Compra(p[1].trim(), p[2].trim(), total, p[0].trim()));
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        } catch (IOException e) { System.err.println("Error cargando historial: " + e.getMessage()); }
        return cola;
    }

    // ── CARRITO ──────────────────────────────────────────────────────────────

    public static void guardarCarrito(String nombreUsuario, Pila<ItemCarrito> carrito) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CARRITO, false))) {
            // Leer el archivo existente y guardar los de otros usuarios
            // Formato: usuario|isbn|cantidad
            Object[] items = carrito.toArray();
            for (Object obj : items) {
                ItemCarrito item = (ItemCarrito) obj;
                pw.println(nombreUsuario + "|" + item.getLibro().getIsbn() + "|" + item.getCantidad());
            }
        } catch (IOException e) { System.err.println("Error guardando carrito: " + e.getMessage()); }
    }

    // ── DESEOS ───────────────────────────────────────────────────────────────

    public static void guardarDeseos(String nombreUsuario, ListaSencilla<Libro> deseos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DESEOS, false))) {
            Nodo<Libro> actual = deseos.getCabeza();
            while (actual != null) {
                pw.println(nombreUsuario + "|" + actual.dato.getIsbn());
                actual = actual.siguiente;
            }
        } catch (IOException e) { System.err.println("Error guardando deseos: " + e.getMessage()); }
    }
}
