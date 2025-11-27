package com.biblioteca.controllers;

import com.biblioteca.beans.*;
import com.biblioteca.model.ConfiguracionModel;
import com.biblioteca.model.EjemplarModel;
import com.biblioteca.model.PrestamoModel;
import com.biblioteca.model.UsuarioModel;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "PrestamoController", urlPatterns = {"/prestamos.do"})
public class PrestamoController extends HttpServlet {

    private final PrestamoModel prestamoModel = new PrestamoModel();
    private final EjemplarModel ejemplarModel = new EjemplarModel();
    private final ConfiguracionModel configuracionModel = new ConfiguracionModel();
    private final UsuarioModel usuarioModel = new UsuarioModel();
    private final ArrayList<String> listaErrores = new ArrayList<>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String op = request.getParameter("op");

        if ("nuevo".equals(op)) {
            nuevo(request, response);
            return;
        }

        try {
            switch (op) {
                case "listar":
                    listar(request, response);
                    break;
                case "insertar":
                    insertar(request, response);
                    break;
                case "devolver":
                    devolver(request, response);
                    break;
                default:
                    request.getRequestDispatcher("/error404.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, "Error en PrestamoController", e);
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
        return "Controlador de Préstamos - Biblioteca UDB";
    }

    // 1. Listar Préstamos Activos
    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Prestamo> lista = prestamoModel.obtenerPrestamosAtivos();
            Configuracion config = configuracionModel.obtenerConfiguracion();

            java.util.Date hoy = new java.util.Date();
            int atrasados = 0;

            for (Prestamo p : lista) {
                java.sql.Date vencimiento = p.getFechaVencimiento();
                double mora = 0.0;
                int diasRestantes = 0;

                if (vencimiento != null) {
                    diasRestantes = calcularDiasRestantes(vencimiento);

                    if (diasRestantes < 0) {
                        // Atrasado
                        long diasAtraso = Math.abs(diasRestantes);
                        mora = diasAtraso * config.getMoraDiaria();
                        p.setEstado("Atrasado");
                        atrasados++;
                    } else if (diasRestantes == 0) {
                        p.setEstado("Vence hoy");
                    } else {
                        p.setEstado("Activo");
                    }
                }

                p.setMoraCalculada(mora);

                request.setAttribute("diasRestantes_" + p.getIdPrestamo(), diasRestantes);
            }

            request.setAttribute("listaPrestamos", lista);
            request.setAttribute("totalPrestamos", lista.size());
            request.setAttribute("totalAtrasados", atrasados);
            request.setAttribute("moraDiaria", config.getMoraDiaria());

            request.getRequestDispatcher("/prestamos/listaPrestamos.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, "Error al listar préstamos", ex);
            request.getSession().setAttribute("fracaso", "Error al cargar los préstamos");
            response.sendRedirect("index.jsp");
        }
    }

    // 2. Formulario Nuevo Préstamo
    private void nuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Configuracion config = configuracionModel.obtenerConfiguracion();

            List<Ejemplar> ejemplares = ejemplarModel.buscar("");
            List<Usuario> usuarios = usuarioModel.listarUsuarios();

            request.setAttribute("ejemplaresDisponibles", ejemplares != null ? ejemplares : new ArrayList<>());
            request.setAttribute("usuarios", usuarios != null ? usuarios : new ArrayList<>());
            request.setAttribute("config", config);

            request.getRequestDispatcher("/prestamos/nuevoPrestamo.jsp").forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, "Error cargando nuevo préstamo", ex);
            request.setAttribute("error", "Error al cargar los datos. Intenta más tarde.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // 3. Registrar Préstamo
    private void insertar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        listaErrores.clear();
        Configuracion config = configuracionModel.obtenerConfiguracion();

        String idUsuarioStr = request.getParameter("id_usuario");
        String idEjemplarStr = request.getParameter("id_ejemplar");
        String diasStr = request.getParameter("dias");

        // Validaciones básicas
        if (idUsuarioStr == null || idUsuarioStr.isEmpty() || "0".equals(idUsuarioStr)) {
            listaErrores.add("Selecciona un usuario");
        }
        if (idEjemplarStr == null || idEjemplarStr.isEmpty() || "0".equals(idEjemplarStr)) {
            listaErrores.add("Selecciona un ejemplar");
        }
        if (diasStr == null || diasStr.trim().isEmpty()) {
            listaErrores.add("Ingresa cantidad de días");
        }

        int idUsuario = 0, idEjemplar = 0, dias = 0;
        Usuario usuario = null;

        if (listaErrores.isEmpty()) {
            try {
                idUsuario = Integer.parseInt(idUsuarioStr);
                idEjemplar = Integer.parseInt(idEjemplarStr);
                dias = Integer.parseInt(diasStr);

                usuario = usuarioModel.obtenerUsuario(idUsuario);
                if (usuario == null) {
                    listaErrores.add("Usuario no encontrado");
                }

                // Validar días según tipo de usuario
                int maxDias = usuario != null && usuario.getTipoUsuario().getNombreTipo().toLowerCase().contains("profesor")
                        ? config.getDiasPrestamoProfesor()
                        : config.getDiasPrestamoAlumno();

                if (dias < 1 || dias > maxDias) {
                    listaErrores.add("Días permitidos: 1 a " + maxDias + " para " +
                            (usuario != null && usuario.getTipoUsuario().getNombreTipo().toLowerCase().contains("profesor") ? "profesores" : "alumnos"));
                }

            } catch (NumberFormatException e) {
                listaErrores.add("Datos numéricos inválidos");
            }
        }

        // Validar límite de préstamos activos
        if (listaErrores.isEmpty()) {
            int prestamosActuales = prestamoModel.contarPrestamosActivos(idUsuario);
            int maxPermitidos = usuario != null && usuario.getTipoUsuario().getNombreTipo().toLowerCase().contains("profesor")
                    ? config.getMaxPrestamosProfesor()
                    : config.getMaxPrestamosAlumno();

            if (prestamosActuales >= maxPermitidos) {
                listaErrores.add("El usuario ya alcanzó el límite de " + maxPermitidos + " préstamos activos");
            }
        }

        // Validar disponibilidad del ejemplar
        if (listaErrores.isEmpty()) {
            Ejemplar ej = ejemplarModel.obtenerEjemplar(idEjemplar);
            if (ej == null || ej.getCantidadDisponible() <= 0) {
                listaErrores.add("El ejemplar seleccionado no está disponible");
            }
        }

        if (!listaErrores.isEmpty()) {
            request.setAttribute("listaErrores", listaErrores);
            request.setAttribute("id_usuario", idUsuarioStr);
            request.setAttribute("id_ejemplar", idEjemplarStr);
            request.setAttribute("dias", diasStr);
            nuevo(request, response);
            return;
        }

        // Registar prestamo
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, dias);
            Date fechaVencimiento = new Date(cal.getTimeInMillis());

            Prestamo p = new Prestamo();
            p.setIdUsuario(idUsuario);
            p.setIdEjemplar(idEjemplar);
            p.setFechaVencimiento(fechaVencimiento);

            if (prestamoModel.insertarPrestamo(p) > 0) {
                ejemplarModel.actualizarDisponibilidad(idEjemplar, -1);

                request.getSession().setAttribute("exito",
                        "Préstamo registrado - Vence: " + fechaVencimiento +
                                " (Mora diaria: $" + String.format("%.2f", config.getMoraDiaria()) + ")");
                response.sendRedirect("prestamos.do?op=listar");
            } else {
                throw new Exception("Error al insertar en base de datos");
            }
        } catch (Exception e) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, "Error al registrar préstamo", e);
            listaErrores.add("Error del sistema al registrar el préstamo");
            request.setAttribute("listaErrores", listaErrores);
            nuevo(request, response);
        }
    }

    // 4. DEVOLVER PRÉSTAMO
    private void devolver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            request.getSession().setAttribute("fracaso", "ID inválido");
            response.sendRedirect("prestamos.do?op=listar");
            return;
        }

        Configuracion config = configuracionModel.obtenerConfiguracion();

        try {
            int idPrestamo = Integer.parseInt(idStr);
            Prestamo p = prestamoModel.obtenerPrestamo(idPrestamo);

            if (p == null || !"Activo".equals(p.getEstado())) {
                request.getSession().setAttribute("fracaso", "Préstamo no encontrado o ya devuelto");
                response.sendRedirect("prestamos.do?op=listar");
                return;
            }

            java.util.Date hoy = new java.util.Date();
            double mora = 0.0;
            if (p.getFechaVencimiento() != null && p.getFechaVencimiento().before(hoy)) {
                long diasAtraso = (hoy.getTime() - p.getFechaVencimiento().getTime()) / (1000 * 60 * 60 * 24) + 1;
                mora = diasAtraso * config.getMoraDiaria();
            }

            p.setFechaDevolucion(new java.sql.Timestamp(hoy.getTime()));
            p.setMoraCalculada(mora);
            p.setEstado("Devuelto");

            if (prestamoModel.devolverPrestamo(p) > 0) {
                // Sumar disponibilidad
                ejemplarModel.actualizarDisponibilidad(p.getIdEjemplar(), +1);

                // Actualizar mora del usuario si hay
                if (mora > 0) {
                    usuarioModel.actualizarMora(p.getIdUsuario(), true, mora);
                }

                String msg = "Devolución exitosa";
                if (mora > 0) msg += " | Mora generada: $" + String.format("%.2f", mora);
                request.getSession().setAttribute("exito", msg);
            } else {
                request.getSession().setAttribute("fracaso", "Error al procesar devolución");
            }

            response.sendRedirect("prestamos.do?op=listar");

        } catch (Exception e) {
            Logger.getLogger(PrestamoController.class.getName()).log(Level.SEVERE, "Error al devolver", e);
            request.getSession().setAttribute("fracaso", "Error del sistema");
            response.sendRedirect("prestamos.do?op=listar");
        }
    }

    private int calcularDiasRestantes(java.sql.Date fechaVencimiento) {
        if (fechaVencimiento == null) {
            return 0;
        }

        // Obtener la fecha actual (hoy a medianoche)
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        // Fecha de vencimiento a medianoche
        Calendar calVenc = Calendar.getInstance();
        calVenc.setTime(fechaVencimiento);
        calVenc.set(Calendar.HOUR_OF_DAY, 0);
        calVenc.set(Calendar.MINUTE, 0);
        calVenc.set(Calendar.SECOND, 0);
        calVenc.set(Calendar.MILLISECOND, 0);

        // Diferencia en milisegundos
        long diff = calVenc.getTimeInMillis() - calHoy.getTimeInMillis();

        // Convertir a días (redondea hacia abajo)
        return (int) (diff / (1000L * 60 * 60 * 24));
    }
}