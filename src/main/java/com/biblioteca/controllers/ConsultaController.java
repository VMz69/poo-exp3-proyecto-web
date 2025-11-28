package com.biblioteca.controllers;

import com.biblioteca.beans.Usuario;
import com.biblioteca.model.EjemplarModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ConsultaController", urlPatterns = {"/consulta.do"})
public class ConsultaController extends HttpServlet {

    private final EjemplarModel ejemplarModel = new EjemplarModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        try {
            request.setAttribute("ejemplares", ejemplarModel.buscar(""));
            request.getRequestDispatcher("/public/consultaEjemplares.jsp").forward(request, response);

        } catch (Exception e) {
            Logger.getLogger(ConsultaController.class.getName()).log(Level.SEVERE, "", e);
            session.setAttribute("fracaso", "Error al cargar el catálogo de ejemplares");
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "Controlador de Consulta Pública de Ejemplares - Biblioteca UDB";
    }
}