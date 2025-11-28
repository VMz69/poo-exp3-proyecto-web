package com.biblioteca.controllers;

import com.biblioteca.beans.Usuario;
import com.biblioteca.model.UsuarioModel;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "CambiarContrasenaController", urlPatterns = {"/cambiarContrasena.do"})
public class CambiarContrasenaController extends HttpServlet {

    private final UsuarioModel usuarioModel = new UsuarioModel();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String op = request.getParameter("op");

        // Mostrar formulario
        if (op == null || "form".equals(op)) {
            request.getRequestDispatcher("/usuarios/cambiarContrasena.jsp").forward(request, response);
            return;
        }

        // Cambiar contraseña
        if ("cambiar".equals(op)) {

            String nueva = request.getParameter("nueva");
            String confirmar = request.getParameter("confirmar");

            if (nueva == null || nueva.trim().isEmpty() ||
                    confirmar == null || confirmar.trim().isEmpty()) {
                request.getSession().setAttribute("fracaso", "Ambos campos son obligatorios");
                response.sendRedirect("cambiarContrasena.do");
                return;
            }

            if (!nueva.equals(confirmar)) {
                request.getSession().setAttribute("fracaso", "Las contraseñas no coinciden");
                response.sendRedirect("cambiarContrasena.do");
                return;
            }

            if (nueva.length() < 4) {
                request.getSession().setAttribute("fracaso", "La contraseña debe tener al menos 4 caracteres");
                response.sendRedirect("cambiarContrasena.do");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

            try {
                usuario.setContrasena(nueva);

                if (usuarioModel.actualizarContrasena(usuario) > 0) {
                    request.getSession().setAttribute("exito", "¡Contraseña cambiada con éxito!");
                    // Actualizamos el objeto en sesión
                    usuario.setContrasena(nueva);
                } else {
                    request.getSession().setAttribute("fracaso", "Error al cambiar la contraseña");
                }
            } catch (Exception e) {
                request.getSession().setAttribute("fracaso", "Error del sistema");
            }

            response.sendRedirect("cambiarContrasena.do");
        }
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}