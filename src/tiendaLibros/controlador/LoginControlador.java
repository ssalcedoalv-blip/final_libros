package tiendaLibros.controlador;

import tiendaLibros.model.Usuario;
import tiendaLibros.model.estructuras.ListaSencilla;
import tiendaLibros.util.ArchivoUtil;

/**
 * CONTROLADOR - LoginControlador
 * Valida credenciales usando ListaSencilla cargada desde usuarios.txt
 */
public class LoginControlador {
    private ListaSencilla<Usuario> listaUsuarios;

    public LoginControlador() {
        listaUsuarios = ArchivoUtil.cargarUsuarios();
    }

    public Usuario validarLogin(String nombre, String contrasena) {
        return listaUsuarios.buscar(u ->
            u.getNombre().equals(nombre) && u.getContrasena().equals(contrasena));
    }

    public boolean registrarUsuario(String nombre, String contrasena, String rol) {
        if (listaUsuarios.buscar(u -> u.getNombre().equals(nombre)) != null) return false;
        listaUsuarios.agregar(new Usuario(nombre, contrasena, rol));
        ArchivoUtil.guardarUsuarios(listaUsuarios);
        return true;
    }

    public boolean eliminarUsuario(Usuario usuario) {
        boolean ok = listaUsuarios.eliminar(usuario);
        if (ok) ArchivoUtil.guardarUsuarios(listaUsuarios);
        return ok;
    }

    public ListaSencilla<Usuario> getListaUsuarios() { return listaUsuarios; }
}
