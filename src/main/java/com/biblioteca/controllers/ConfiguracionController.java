package com.biblioteca.controllers;

import com.biblioteca.beans.Configuracion;
import com.biblioteca.model.ConfiguracionModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ConfiguracionController", urlPatterns = {"/configuracion.do"})
public class ConfiguracionController extends HttpServlet {

    private final ConfiguracionModel configModel = new ConfiguracionModel();
    private final ArrayList<String> listaErrores = new ArrayList<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar que este logueado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String op = request.getParameter("op");

        try {
            switch (op == null ? "editar" : op) {
                case "editar":
                    mostrarFormulario(request, response);
                    break;
                case "guardar":
                    guardarConfiguracion(request, response);
                    break;
                default:
                    request.getRequestDispatcher("/error404.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            Logger.getLogger(ConfiguracionController.class.getName())
                    .log(Level.SEVERE, "Error en ConfiguracionController", e);
            request.setAttribute("error", "Error del sistema: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

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

    @Override
    public String getServletInfo() {
        return "Controlador de Configuración Global - Biblioteca UDB";
    }

    // 1. Mostrar formulario de configuracion
    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Configuracion config = configModel.obtenerConfiguracion();

            request.setAttribute("config", config);
            request.getRequestDispatcher("/configuracion/editarConfiguracion.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(ConfiguracionController.class.getName())
                    .log(Level.SEVERE, "Error al cargar configuración", ex);
            request.setAttribute("error", "No se pudo cargar la configuración actual");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // 2. Guardar Configuracion
    private void guardarConfiguracion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        listaErrores.clear();

        String maxAlumnoStr = request.getParameter("max_alumno");
        String maxProfesorStr = request.getParameter("max_profesor");
        String diasAlumnoStr = request.getParameter("dias_alumno");
        String diasProfesorStr = request.getParameter("dias_profesor");
        String moraDiariaStr = request.getParameter("mora_diaria");

        // Validaciones
        if (isEmptyOrInvalid(maxAlumnoStr, 1, 10)) listaErrores.add("Máximo préstamos alumno: 1 a 10");
        if (isEmptyOrInvalid(maxProfesorStr, 1, 20)) listaErrores.add("Máximo préstamos profesor: 1 a 20");
        if (isEmptyOrInvalid(diasAlumnoStr, 1, 30)) listaErrores.add("Días préstamo alumno: 1 a 30");
        if (isEmptyOrInvalid(diasProfesorStr, 1, 60)) listaErrores.add("Días préstamo profesor: 1 a 60");

        double mora = 0.0;
        try {
            mora = Double.parseDouble(moraDiariaStr);
            if (mora <= 0 || mora > 20.00) {
                listaErrores.add("Mora diaria debe estar entre $0.01 y $20.00");
            }
        } catch (NumberFormatException e) {
            listaErrores.add("Mora diaria inválida");
        }

        // Si hay errores → volver al formulario
        if (!listaErrores.isEmpty()) {
            request.setAttribute("listaErrores", listaErrores);
            request.setAttribute("config", crearConfigDesdeParams(request));
            mostrarFormulario(request, response);
            return;
        }

        // Guardar en base de datos
        try {
            Configuracion nuevaConfig = new Configuracion();
            nuevaConfig.setMaxPrestamosAlumno(Integer.parseInt(maxAlumnoStr));
            nuevaConfig.setMaxPrestamosProfesor(Integer.parseInt(maxProfesorStr));
            nuevaConfig.setDiasPrestamoAlumno(Integer.parseInt(diasAlumnoStr));
            nuevaConfig.setDiasPrestamoProfesor(Integer.parseInt(diasProfesorStr));
            nuevaConfig.setMoraDiaria(mora);

            if (configModel.guardarConfiguracion(nuevaConfig) > 0) {
                request.getSession().setAttribute("exito",
                        "¡Configuración actualizada con éxito! Los cambios se aplican inmediatamente.");
            } else {
                request.getSession().setAttribute("fracaso", "No se pudo guardar la configuración");
            }
        } catch (Exception ex) {
            Logger.getLogger(ConfiguracionController.class.getName())
                    .log(Level.SEVERE, "Error al guardar configuración", ex);
            request.getSession().setAttribute("fracaso", "Error en la base de datos");
        }

        response.sendRedirect("configuracion.do");
    }

    // Metodos auxiliares
    private boolean isEmptyOrInvalid(String valor, int min, int max) {
        if (valor == null || valor.trim().isEmpty()) return true;
        try {
            int num = Integer.parseInt(valor);
            return num < min || num > max;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private Configuracion crearConfigDesdeParams(HttpServletRequest request) {
        Configuracion c = new Configuracion();
        try {
            c.setMaxPrestamosAlumno(Integer.parseInt(request.getParameter("max_alumno")));
            c.setMaxPrestamosProfesor(Integer.parseInt(request.getParameter("max_profesor")));
            c.setDiasPrestamoAlumno(Integer.parseInt(request.getParameter("dias_alumno")));
            c.setDiasPrestamoProfesor(Integer.parseInt(request.getParameter("dias_profesor")));
            c.setMoraDiaria(Double.parseDouble(request.getParameter("mora_diaria")));
        } catch (Exception e) {
            Logger.getLogger(ConfiguracionController.class.getName()).log(Level.SEVERE, "", e);

        }
        return c;
    }
}