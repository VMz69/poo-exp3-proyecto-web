package com.biblioteca.controllers;

import com.biblioteca.beans.Usuario;
import com.biblioteca.beans.TipoUsuario;
import com.biblioteca.model.UsuarioModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "UsuarioController", urlPatterns = {"/usuarios.do"})
public class UsuarioController extends HttpServlet {

    private final UsuarioModel usuarioModel = new UsuarioModel();
    private final ArrayList<String> listaErrores = new ArrayList<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario logueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (!"Administrador".equals(logueado.getTipoUsuario().getNombreTipo())) {
            request.getSession().setAttribute("fracaso", "Solo administradores pueden acceder a esta sección");
            response.sendRedirect("index.jsp");
            return;
        }

        String op = request.getParameter("op");

        try {
            if (op == null) op = "listar";

            switch (op) {
                case "listar":
                    listar(request, response);
                    break;
                case "nuevo":
                    nuevo(request, response);
                    break;
                case "insertar":
                    insertar(request, response);
                    break;
                case "editar":
                    editar(request, response);
                    break;
                case "actualizar":
                    actualizar(request, response);
                    break;
                case "eliminar":
                    eliminar(request, response);
                    break;
                case "pagarMora":
                    pagarMora(request, response);
                    break;
                default:
                    response.sendRedirect("usuarios.do");
                    break;
            }
        } catch (Exception e) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, "Error en UsuarioController", e);
            request.setAttribute("error", "Error del sistema: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse s)
            throws ServletException, IOException {
        processRequest(r, s);
    }
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse s)
            throws ServletException, IOException {
        processRequest(r, s);
    }


    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Usuario> lista = usuarioModel.listarUsuarios();

            // Agregar datos extra a cada usuario
            for (Usuario u : lista) {
                int activos = usuarioModel.contarPrestamosActivos(u.getIdUsuario());
                int vencidos = usuarioModel.contarPrestamosVencidos(u.getIdUsuario());

                u.setPrestamosActivos(activos);
                u.setPrestamosVencidos(vencidos);
            }

            request.setAttribute("notaMora", "La mora se calcula cuando el usuario devuelve el libro.");
            request.setAttribute("listaUsuarios", lista);

            request.getRequestDispatcher("/usuarios/listaUsuarios.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("tiposUsuario", usuarioModel.obtenerTipoUsuarios());
            request.getRequestDispatcher("/usuarios/nuevoUsuario.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void insertar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        listaErrores.clear();

        String nombreCompleto = request.getParameter("nombre_completo");
        String correo = request.getParameter("correo");
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        String tipoStr = request.getParameter("tipo_usuario");

        if (isEmpty(nombreCompleto)) listaErrores.add("Nombre completo obligatorio");
        if (isEmpty(correo)) listaErrores.add("Correo obligatorio");
        if (isEmpty(usuario)) listaErrores.add("Usuario obligatorio");
        if (isEmpty(contrasena)) listaErrores.add("Contraseña obligatoria");
        if (isEmpty(tipoStr)) listaErrores.add("Selecciona tipo de usuario");

        if (!listaErrores.isEmpty()) {
            request.setAttribute("listaErrores", listaErrores);
            request.setAttribute("tiposUsuario", usuarioModel.obtenerTipoUsuarios());
            nuevo(request, response);
            return;
        }

        Usuario u = new Usuario();
        u.setNombreCompleto(nombreCompleto.trim());
        u.setCorreo(correo.trim());
        u.setUsuario(usuario.trim());
        u.setContrasena(contrasena);

        TipoUsuario tipo = new TipoUsuario();
        tipo.setIdTipo(Integer.parseInt(tipoStr));
        u.setTipoUsuario(tipo);
        u.setActivo(true);

        try {
            if (usuarioModel.insertarUsuario(u) > 0) {
                request.getSession().setAttribute("exito", "Usuario creado exitosamente");
            } else {
                request.getSession().setAttribute("fracaso", "Error al crear usuario");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("fracaso", "Error: " + e.getMessage());
        }
        response.sendRedirect("usuarios.do");
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("usuario", usuarioModel.obtenerUsuario(id));
            request.setAttribute("tiposUsuario", usuarioModel.obtenerTipoUsuarios());
            request.getRequestDispatcher("/usuarios/editarUsuario.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void actualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Usuario u = new Usuario();
            u.setIdUsuario(Integer.parseInt(request.getParameter("id_usuario")));
            u.setNombreCompleto(request.getParameter("nombre_completo"));
            u.setCorreo(request.getParameter("correo"));
            u.setUsuario(request.getParameter("usuario"));

            TipoUsuario tipo = new TipoUsuario();
            tipo.setIdTipo(Integer.parseInt(request.getParameter("tipo_usuario")));
            u.setTipoUsuario(tipo);

            u.setActivo("on".equals(request.getParameter("activo")));

            if (usuarioModel.actualizarUsuario(u) > 0) {
                request.getSession().setAttribute("exito", "Usuario actualizado");
            } else {
                request.getSession().setAttribute("fracaso", "No se pudo actualizar");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("fracaso", "Error: " + e.getMessage());
        }
        response.sendRedirect("usuarios.do");
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (usuarioModel.eliminarUsuario(id) > 0) {
                request.getSession().setAttribute("exito", "Usuario eliminado");
            } else {
                request.getSession().setAttribute("fracaso", "No se pudo eliminar (quizás tiene préstamos)");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("fracaso", "Error al eliminar");
        }
        response.sendRedirect("usuarios.do");
    }

    private void pagarMora(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (usuarioModel.pagarMora(id) > 0) {
                request.getSession().setAttribute("exito", "Mora pagada exitosamente");
            } else {
                request.getSession().setAttribute("fracaso", "Error al procesar el pago");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("fracaso", "Error del sistema");
        }
        response.sendRedirect("usuarios.do");
    }

    // Metodo auxiliar
    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}