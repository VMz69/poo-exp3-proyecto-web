package com.biblioteca.controllers;

import com.biblioteca.beans.Usuario;
import com.biblioteca.model.UsuarioModel;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {

    private final UsuarioModel usuarioModel = new UsuarioModel();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        try {
            Usuario user = usuarioModel.autenticar(usuario, password);

            if (user != null && user.isActivo()) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", user);
                session.setAttribute("nombreUsuario", user.getNombreCompleto());
                session.setAttribute("rol", user.getTipoUsuario());
                response.sendRedirect("index.jsp");
            } else {
                response.sendRedirect("login.jsp?error=1");
            }
        } catch (Exception e) {
            response.sendRedirect("login.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // SI VIENE ?logout → cierra sesión
//        if ("logout".equals(request.getParameter("logout"))) {
//            HttpSession session = request.getSession(false);
//            if (session != null) {
//                session.invalidate();
//            }
//            response.sendRedirect("login.jsp?logout=1");
//            return;
//        }

        // CUALQUIER OTRA PETICIÓN GET A login.do → va al login LIMPIO
        response.sendRedirect("login.jsp");
    }
}