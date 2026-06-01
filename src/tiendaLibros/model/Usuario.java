package tiendaLibros.model;

public class Usuario {
    private String nombre;
    private String contrasena;
    private String rol;

    public Usuario(String nombre, String contrasena, String rol) {
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }

    @Override
    public String toString() { return nombre + "," + contrasena + "," + rol; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Usuario)) return false;
        return this.nombre.equals(((Usuario) obj).nombre);
    }
}
