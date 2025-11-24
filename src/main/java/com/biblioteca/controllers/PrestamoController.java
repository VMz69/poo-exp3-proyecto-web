package com.biblioteca.controllers;

import com.biblioteca.beans.Usuario;
import com.biblioteca.model.PrestamoModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PrestamoController", urlPatterns = {"prestamos.do"})
public class PrestamoController extends HttpServlet {
    PrestamoModel prestamo = new PrestamoModel();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if(request.getParameter("op") == null) {
                listar(request, response);
                return;
            }
            String operacion = request.getParameter("op");

            switch (operacion) {
                case "listar":
                    listar(request, response);
                    break;
                case "nuevo":
                    break;
                case "actualizar":
                    break;
                default:
                    request.setAttribute("error", "Operacion invalida");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, null, e);
        }


    }

    private void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("listaPrestamos", prestamo.obtenerPrestamosAtivos());
        request.getRequestDispatcher("admin/prestamos.jsp").forward(request, response);
    }

    private void nuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");

        if(usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

    }
}
