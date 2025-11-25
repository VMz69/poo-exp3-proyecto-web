package com.biblioteca.controllers;

import com.biblioteca.beans.*;
import com.biblioteca.model.EjemplarModel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

@WebServlet(name = "EjemplaresController", urlPatterns = {"/ejemplares.do"})
public class EjemplaresController extends HttpServlet {
    ArrayList<String> listaErrores = new ArrayList<>();
    EjemplarModel ejemplarModel = new EjemplarModel();

//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            if(request.getParameter("op") != null) {
//                listar(request, response);
//                return;
//            }
//            String operacion = request.getParameter("operacion");
//            switch(operacion) {
//                case "listar":
//                    listar(request, response);
//                    break;
//                case "nuevo":
//                    nuevo(request, response);
//                    break;
//                case "insertar":
//                    insertar(request, response);
//                    break;
//                case "obtener":
//                    obtener(request, response);
//                    break;
//                case "modificar":
//                    modificar(request, response);
//                    break;
//                case "eliminar":
//                    eliminar(request, response);
//                    break;
//                default:
//                    request.getRequestDispatcher("/error404.jsp").forward(request, response);
//                    break;
//            }
//        }
//    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String op = request.getParameter("op");

        if ("nuevo".equals(op)) {
            nuevo(request, response);
            return;
        }

        try {
            if (op == null || op.isEmpty() || "listar".equals(op)) {
                listar(request, response);
            } else if ("insertar".equals(op)) {
                insertar(request, response);
            } else if ("obtener".equals(op)) {
                obtener(request, response);
            } else if ("modificar".equals(op)) {
                modificar(request, response);
            } else if ("eliminar".equals(op)) {
                eliminar(request, response);
            } else {
                request.getRequestDispatcher("/error404.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // Si falla la base de datos, mostramos mensaje amigable
            request.setAttribute("error", "Error de conexión a la base de datos: " + e.getMessage());
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
        return "Controlador de Ejemplares - Biblioteca UDB";
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String criterio = request.getParameter("q");
            List<Ejemplar> lista;

            if (criterio != null && !criterio.trim().isEmpty()) {
                // Búsqueda con filtro
                lista = ejemplarModel.buscar(criterio.trim());
                request.setAttribute("criterio", criterio.trim());
            } else {
                // Listado completo
                lista = ejemplarModel.buscar("");
            }
            request.setAttribute("listaEjemplares", lista);
            request.setAttribute("totalEjemplares", lista.size());

            request.getRequestDispatcher("/ejemplares/listaEjemplares.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EjemplaresController.class.getName())
                    .log(Level.SEVERE, "Error al listar ejemplares", ex);
            request.setAttribute("error", "No se pudieron cargar los ejemplares. Inténtalo más tarde.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(EjemplaresController.class.getName())
                    .log(Level.SEVERE, "Error inesperado en listar()", ex);
            request.setAttribute("error", "Error del sistema.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void nuevo(HttpServletRequest request, HttpServletResponse response) {
        try{
            request.setAttribute("tiposDocumento", ejemplarModel.obtenerTiposDocumento());
            request.setAttribute("categorias", ejemplarModel.obtenerCategorias());
            request.setAttribute("ubicaciones", ejemplarModel.obtenerUbicaciones());
            request.getRequestDispatcher("/ejemplares/nuevoEjemplar.jsp").forward(request, response);
        } catch (Exception e){
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

//    private void nuevo(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            // === DATOS DE PRUEBA (solo mientras configuras la BD) ===
//            ArrayList<TipoDocumento> tipos = new ArrayList<>();
//            tipos.add(new TipoDocumento(1, "LIBRO"));
//            tipos.add(new TipoDocumento(2, "TESIS"));
//            tipos.add(new TipoDocumento(3, "REVISTA"));
//            tipos.add(new TipoDocumento(4, "CD"));
//            tipos.add(new TipoDocumento(5, "DVD"));
//            tipos.add(new TipoDocumento(6, "INFORME"));
//            tipos.add(new TipoDocumento(7, "MANUAL"));
//            request.setAttribute("tiposDocumento", tipos);
//
//            ArrayList<Categoria> cats = new ArrayList<>();
//            cats.add(new Categoria(1, "Ciencias"));
//            cats.add(new Categoria(2, "Literatura"));
//            request.setAttribute("categorias", cats);
//
//            ArrayList<Ubicacion> ubis = new ArrayList<>();
//            ubis.add(new Ubicacion(1, "A", "2", "B", "5"));
//            request.setAttribute("ubicaciones", ubis);
//            // === FIN DATOS DE PRUEBA ===
//
//            request.getRequestDispatcher("/ejemplares/nuevoEjemplar.jsp").forward(request, response);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    private void obtener(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            request.getSession().setAttribute("fracaso", "No se recibió el ID del ejemplar");
            response.sendRedirect("ejemplares.do?op=listar");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            Ejemplar ejemplar = ejemplarModel.obtenerEjemplar(id);

            // 2. Verificar que el ejemplar exista
            if (ejemplar == null) {
                request.getSession().setAttribute("fracaso", "El ejemplar con ID " + id + " no existe");
                response.sendRedirect("ejemplares.do?op=listar");
                return;
            }

            // 3. Cargar TODAS las listas necesarias para los <select>
            request.setAttribute("ejemplar", ejemplar);
            request.setAttribute("tiposDocumento", ejemplarModel.obtenerTiposDocumento());
            request.setAttribute("categorias", ejemplarModel.obtenerCategorias());
            request.setAttribute("ubicaciones", ejemplarModel.obtenerUbicaciones());

            // 4. Forward CORRECTO (¡RUTA COMPLETA!)
            request.getRequestDispatcher("/ejemplares/editarEjemplar.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("fracaso", "ID inválido: " + idParam);
            response.sendRedirect("ejemplares.do?op=listar");
        } catch (Exception e) {
            System.out.println("ERROR al cargar ejemplar para editar: " + e.getMessage());
            e.printStackTrace();

            request.getSession().setAttribute("fracaso", "Error del sistema al cargar el ejemplar");
            response.sendRedirect("ejemplares.do?op=listar");
        }
    }

    private void insertar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        listaErrores.clear();

        try {
            // Validaciones
            String idTipoStr = request.getParameter("idTipoDocumento");
            String titulo = request.getParameter("titulo");
            String cantidadStr = request.getParameter("cantidadTotal");
            String idUbicacionStr = request.getParameter("idUbicacion");

            if (idTipoStr == null || idTipoStr.trim().isEmpty() || "0".equals(idTipoStr)) {
                listaErrores.add("Debe seleccionar un Tipo de Documento");
            }
            if (titulo == null || titulo.trim().isEmpty()) {
                listaErrores.add("El Título es obligatorio");
            }
            if (cantidadStr == null || cantidadStr.trim().isEmpty() || Integer.parseInt(cantidadStr) <= 0) {
                listaErrores.add("La Cantidad Total debe ser mayor a 0");
            }
            if (idUbicacionStr == null || idUbicacionStr.trim().isEmpty() || "0".equals(idUbicacionStr)) {
                listaErrores.add("Debe seleccionar una Ubicación");
            }

            // Si hay errores → volver al formulario con todos los datos
            if (!listaErrores.isEmpty()) {
                request.setAttribute("listaErrores", listaErrores);
                nuevo(request, response);
                return;
            }

            int idTipoDocumento = Integer.parseInt(idTipoStr);
            int cantidadTotal = Integer.parseInt(cantidadStr);

            // Crear el ejemplar segun el tipo
            TipoDocumento tipoDoc = new TipoDocumento();
            tipoDoc.setIdTipoDoc(idTipoDocumento);

            switch (idTipoDocumento) {
                case 1: tipoDoc.setNombreTipo("Libro"); break;
                case 2: tipoDoc.setNombreTipo("Tesis"); break;
                case 3: tipoDoc.setNombreTipo("Revista"); break;
                case 4: tipoDoc.setNombreTipo("CD"); break;
                case 5: tipoDoc.setNombreTipo("DVD"); break;
                case 6: tipoDoc.setNombreTipo("Informe"); break;
                case 7: tipoDoc.setNombreTipo("Manual"); break;
                default:
                    listaErrores.add("Tipo de documento desconocido");
                    request.setAttribute("listaErrores", listaErrores);
                    nuevo(request, response);
                    return;
            }

            Ejemplar ejemplar = EjemplarFactory.crearEjemplar(tipoDoc);
            ejemplar.setTipoDocumento(tipoDoc);

            // Campos comunes
            ejemplar.setTitulo(titulo.trim());
            ejemplar.setAutor(request.getParameter("autor"));
            ejemplar.setEditorial(request.getParameter("editorial"));
            ejemplar.setIsbn(request.getParameter("isbn"));

            String anio = request.getParameter("anioPublicacion");
            ejemplar.setAnioPublicacion(anio != null && !anio.isEmpty() ? Integer.parseInt(anio) : 2025);

            ejemplar.setIdioma(request.getParameter("idioma") != null && !request.getParameter("idioma").trim().isEmpty()
                    ? request.getParameter("idioma") : "Espanol");

            ejemplar.setCantidadTotal(cantidadTotal);
            ejemplar.setCantidadDisponible(cantidadTotal);
            ejemplar.setFechaIngreso(LocalDate.now());

            String idCatStr = request.getParameter("idCategoria");
            if (idCatStr != null && !idCatStr.isEmpty() && !idCatStr.equals("0")) {
                Categoria cat = new Categoria();
                cat.setIdCategoria(Integer.parseInt(idCatStr));
                ejemplar.setCategoria(cat);
            }

            // Ubicación
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setIdUbicacion(Integer.parseInt(idUbicacionStr));
            ejemplar.setUbicacion(ubicacion);

            // ==================== CAMPOS ESPECÍFICOS POR TIPO ====================
            switch (idTipoDocumento) {
                case 1: // LIBRO
                    String pagLibro = request.getParameter("numPaginasLibro");
                    if (pagLibro != null && !pagLibro.isEmpty()) {
                        ((Libro) ejemplar).setNumPaginas(Integer.parseInt(pagLibro));
                    }
                    ((Libro) ejemplar).setColeccion(request.getParameter("coleccion"));
                    ((Libro) ejemplar).setNumeroSerie(request.getParameter("numeroSerie"));
                    break;

                case 2: // TESIS
                    String pagTesis = request.getParameter("numPaginasTesis");
                    if (pagTesis != null && !pagTesis.isEmpty()) {
                        ((Tesis) ejemplar).setNumPaginas(Integer.parseInt(pagTesis));
                    }
                    ((Tesis) ejemplar).setUniversidad(request.getParameter("universidad"));
                    ((Tesis) ejemplar).setFacultad(request.getParameter("facultad"));
                    ((Tesis) ejemplar).setCarrera(request.getParameter("carrera"));
                    ((Tesis) ejemplar).setAsesor(request.getParameter("asesor"));
                    ((Tesis) ejemplar).setGradoAcademico(request.getParameter("gradoAcademico"));
                    break;

                case 3: // REVISTA
                    String pagRevista = request.getParameter("numPaginasRevista");
                    if (pagRevista != null && !pagRevista.isEmpty()) {
                        ((Revista) ejemplar).setNumPaginas(Integer.parseInt(pagRevista));
                    }
                    ((Revista) ejemplar).setVolumen(request.getParameter("volumen"));
                    ((Revista) ejemplar).setNumero(request.getParameter("numero"));
                    ((Revista) ejemplar).setPeriodicidad(request.getParameter("periodicidad"));
                    break;

                case 4: // CD
                    String durCD = request.getParameter("duracionCD");
                    if (durCD != null && !durCD.isEmpty()) {
                        ((CD) ejemplar).setDuracion(Integer.parseInt(durCD));
                    }
                    ((CD) ejemplar).setFormato(request.getParameter("formatoCD"));
                    ((CD) ejemplar).setArtista(request.getParameter("artistaCD"));
                    break;

                case 5: // DVD
                    String durDVD = request.getParameter("duracionDVD");
                    if (durDVD != null && !durDVD.isEmpty()) {
                        ((DVD) ejemplar).setDuracion(Integer.parseInt(durDVD));
                    }
                    ((DVD) ejemplar).setFormato(request.getParameter("formatoDVD"));
                    ((DVD) ejemplar).setDirector(request.getParameter("directorDVD"));
                    break;

                case 6: // INFORME
                    String pagInforme = request.getParameter("numPaginasInforme");
                    if (pagInforme != null && !pagInforme.isEmpty()) {
                        ((Informe) ejemplar).setNumPaginas(Integer.parseInt(pagInforme));
                    }
                    ((Informe) ejemplar).setInstitucion(request.getParameter("institucion"));
                    ((Informe) ejemplar).setSupervisor(request.getParameter("supervisor"));
                    break;

                case 7: // MANUAL
                    String pagManual = request.getParameter("numPaginasManual");
                    if (pagManual != null && !pagManual.isEmpty()) {
                        ((Manual) ejemplar).setNumPaginas(Integer.parseInt(pagManual));
                    }
                    ((Manual) ejemplar).setArea(request.getParameter("areaManual"));
                    ((Manual) ejemplar).setNivelUsuario(request.getParameter("nivelUsuario"));
                    ((Manual) ejemplar).setVersion(request.getParameter("versionManual"));
                    break;

                default:
                    listaErrores.add("Tipo de documento no válido");
                    request.setAttribute("listaErrores", listaErrores);
                    nuevo(request, response);
                    return;
            }

            // ==================== GUARDAR EN BASE DE DATOS ====================
            int resultado = ejemplarModel.insertarEjemplar(ejemplar);

            if (resultado > 0) {
                request.getSession().setAttribute("exito",
                        "Ejemplar '" + ejemplar.getTitulo() + "' registrado exitosamente");
                response.sendRedirect("ejemplares.do?op=listar");
            } else {
                listaErrores.add("No se pudo registrar el ejemplar. Inténtalo más tarde.");
                request.setAttribute("listaErrores", listaErrores);
                nuevo(request, response);
            }

        } catch (NumberFormatException e) {
            listaErrores.add("Error en los números ingresados (páginas, año, etc.)");
            request.setAttribute("listaErrores", listaErrores);
            nuevo(request, response);
        } catch (Exception e) {
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, "Error crítico al insertar ejemplar", e);
            listaErrores.add("Error del sistema. Contacta al administrador.");
            request.setAttribute("listaErrores", listaErrores);
            nuevo(request, response);
        }
    }

    private void modificar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listaErrores.clear();

        String idStr = request.getParameter("idEjemplar");

        if (idStr == null || idStr.trim().isEmpty()) {
            request.getSession().setAttribute("fracaso", "ID de ejemplar no válido");
            response.sendRedirect("ejemplares.do?op=listar");
            return;
        }

        try {
            int idEjemplar = Integer.parseInt(idStr);
            Ejemplar existente = ejemplarModel.obtenerEjemplar(idEjemplar);

            if (existente == null) {
                request.getSession().setAttribute("fracaso", "El ejemplar no existe");
                response.sendRedirect("ejemplares.do?op=listar");
                return;
            }

            TipoDocumento tipoDocumento = existente.getTipoDocumento();
            Ejemplar ejemplar = EjemplarFactory.crearEjemplar(tipoDocumento);
            ejemplar.setIdEjemplar(idEjemplar);

            // === CAMPOS COMUNES ===
            ejemplar.setTitulo(request.getParameter("titulo").trim());
            ejemplar.setAutor(getStringOrEmpty(request.getParameter("autor")));
            ejemplar.setEditorial(getStringOrEmpty(request.getParameter("editorial")));
            ejemplar.setIsbn(getStringOrEmpty(request.getParameter("isbn")));
            ejemplar.setAnioPublicacion(parseInt(request.getParameter("anioPublicacion"), 2025));
            ejemplar.setIdioma(getStringOrEmpty(request.getParameter("idioma")));
            ejemplar.setCantidadTotal(parseInt(request.getParameter("cantidadTotal"), 1));
            ejemplar.setCantidadDisponible(parseInt(request.getParameter("cantidadDisponible"), 0));
            ejemplar.setActivo(existente.isActivo());
            ejemplar.setFechaIngreso(existente.getFechaIngreso());

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(parseInt(request.getParameter("idCategoria"), 0));
            ejemplar.setCategoria(categoria);

            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setIdUbicacion(parseInt(request.getParameter("idUbicacion"), 1));
            ejemplar.setUbicacion(ubicacion);

            ejemplar.setTipoDocumento(tipoDocumento);

            // === CAMPOS ESPECÍFICOS ===
            switch (tipoDocumento.getIdTipoDoc()) {
                case 1: // LIBRO
                    ((Libro) ejemplar).setNumPaginas(parseInt(request.getParameter("numPaginasLibro"), 0));
                    ((Libro) ejemplar).setColeccion(getStringOrEmpty(request.getParameter("coleccion")));
                    ((Libro) ejemplar).setNumeroSerie(getStringOrEmpty(request.getParameter("numeroSerie")));
                    break;

                case 2: // TESIS
                    ((Tesis) ejemplar).setNumPaginas(parseInt(request.getParameter("numPaginasTesis"), 0));
                    ((Tesis) ejemplar).setUniversidad(getStringOrEmpty(request.getParameter("universidad")));
                    ((Tesis) ejemplar).setFacultad(getStringOrEmpty(request.getParameter("facultad")));
                    ((Tesis) ejemplar).setCarrera(getStringOrEmpty(request.getParameter("carrera")));
                    ((Tesis) ejemplar).setAsesor(getStringOrEmpty(request.getParameter("asesor")));
                    ((Tesis) ejemplar).setGradoAcademico(getStringOrEmpty(request.getParameter("gradoAcademico")));
                    break;

                case 3: // REVISTA
                    ((Revista) ejemplar).setNumPaginas(parseInt(request.getParameter("numPaginasRevista"), 0));
                    ((Revista) ejemplar).setVolumen(getStringOrEmpty(request.getParameter("volumen")));
                    ((Revista) ejemplar).setNumero(getStringOrEmpty(request.getParameter("numero")));
                    ((Revista) ejemplar).setPeriodicidad(getStringOrEmpty(request.getParameter("periodicidad")));
                    break;

                case 4: // CD
                    ((CD) ejemplar).setDuracion(parseInt(request.getParameter("duracionCD"), 0));
                    ((CD) ejemplar).setFormato(getStringOrEmpty(request.getParameter("formatoCD")));
                    ((CD) ejemplar).setArtista(getStringOrEmpty(request.getParameter("artistaCD")));
                    break;

                case 5: // DVD
                    ((DVD) ejemplar).setDuracion(parseInt(request.getParameter("duracionDVD"), 0));
                    ((DVD) ejemplar).setFormato(getStringOrEmpty(request.getParameter("formatoDVD")));
                    ((DVD) ejemplar).setDirector(getStringOrEmpty(request.getParameter("directorDVD")));
                    break;

                case 6: // INFORME
                    ((Informe) ejemplar).setNumPaginas(parseInt(request.getParameter("numPaginasInforme"), 0));
                    ((Informe) ejemplar).setInstitucion(getStringOrEmpty(request.getParameter("institucion")));
                    ((Informe) ejemplar).setSupervisor(getStringOrEmpty(request.getParameter("supervisor")));
                    break;

                case 7: // MANUAL
                    ((Manual) ejemplar).setNumPaginas(parseInt(request.getParameter("numPaginasManual"), 0));
                    ((Manual) ejemplar).setArea(getStringOrEmpty(request.getParameter("areaManual")));
                    ((Manual) ejemplar).setNivelUsuario(getStringOrEmpty(request.getParameter("nivelUsuario")));
                    ((Manual) ejemplar).setVersion(getStringOrEmpty(request.getParameter("versionManual")));
                    break;
            }

            if (ejemplarModel.actualizarEjemplar(ejemplar) > 0) {
                request.getSession().setAttribute("exito", "Ejemplar actualizado correctamente");
            } else {
                request.getSession().setAttribute("fracaso", "No se pudo actualizar el ejemplar");
            }
            response.sendRedirect("ejemplares.do?op=listar");

        } catch (Exception ex) {
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, "Error al modificar ejemplar", ex);
            request.getSession().setAttribute("fracaso", "Error del sistema: " + ex.getMessage());
            response.sendRedirect("ejemplares.do?op=listar");
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listaErrores.clear();

        String idStr = request.getParameter("id");

        if (idStr == null) {
            request.getSession().setAttribute("fracaso", "ID de ejemplar inválido");
            response.sendRedirect(request.getContextPath() + "/ejemplares.do?op=listar");
            return;
        }

        int idEjemplar = Integer.parseInt(idStr);

        try {
            // Intentamos eliminar
            int resultado = ejemplarModel.eliminarEjemplar(idEjemplar);

            if (resultado > 0) {
                request.getSession().setAttribute("exito",
                        "Ejemplar eliminado correctamente junto con sus registros de préstamos");
            } else {
                // Si retorna 0, probablemente hay un error de FK o no existe
                Ejemplar e = ejemplarModel.obtenerEjemplar(idEjemplar);
                if (e != null) {
                    // Todavía existe → hay préstamos activos que no se pudieron borrar
                    request.getSession().setAttribute("fracaso",
                            "No se puede eliminar el ejemplar '" + e.getTitulo() +
                                    "' porque tiene préstamos activos o pendientes");
                } else {
                    request.getSession().setAttribute("fracaso",
                            "El ejemplar ya no existe o fue eliminado previamente");
                }
            }

            response.sendRedirect(request.getContextPath() + "/ejemplares.do?op=listar");

        } catch (SQLException ex) {
            Logger.getLogger(EjemplaresController.class.getName())
                    .log(Level.SEVERE, "Error al eliminar ejemplar ID: " + idEjemplar, ex);

            request.getSession().setAttribute("fracaso",
                    "Error del sistema al intentar eliminar el ejemplar.");
            response.sendRedirect(request.getContextPath() + "/ejemplares.do?op=listar");
        }
    }
    private void detalles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String idStr = request.getParameter("id");
            Ejemplar e = ejemplarModel.obtenerEjemplar(Integer.parseInt(idStr));
            if (e == null) {
                out.print("{\"error\": \"No encontrado\"}");
                return;
            }
            JSONObject json = new JSONObject();
            json.put("titulo", e.getTitulo());
            json.put("autor", e.getAutorOArtista());
            json.put("tipo", e.getTipoDocumentoString());
            json.put("disponibles", e.getCantidadDisponible());
            json.put("total", e.getCantidadTotal());
            json.put("info", e.getInformacionEspecifica());
            out.print(json);
        } catch (Exception ex) {
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Parsea un String a Integer de forma segura.
     * Si el valor es null, vacío o inválido, retorna el valor por defecto.
     */
    private int parseInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Retorna el String o una cadena vacía si es null.
     */
    private String getStringOrEmpty(String value) {
        return (value != null && !value.trim().isEmpty()) ? value.trim() : "";
    }

}
