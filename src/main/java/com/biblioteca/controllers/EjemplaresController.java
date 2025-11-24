package com.biblioteca.controllers;

import com.biblioteca.beans.*;
import com.biblioteca.model.EjemplarModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

@WebServlet(name = "EjemplaresController", urlPatterns = {"/ejemplares.do"})
public class EjemplaresController extends HttpServlet {
    ArrayList<String> listaErrores = new ArrayList<>();
    EjemplarModel ejemplarModel = new EjemplarModel();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if(request.getParameter("op") != null) {
                listar(request, response);
                return;
            }
            String operacion = request.getParameter("operacion");
            switch(operacion) {
                case "listar":
                    listar(request, response);
                    break;
                case "nuevo":
                    nuevo(request, response);
                    break;
                case "insertar":
                    insertar(request, response);
                    break;
                case "obtener":
                    obtener(request, response);
                    break;
                case "modificar":
                    modificar(request, response);
                    break;
                case "eliminar":
                    eliminar(request, response);
                    break;
                default:
                    request.getRequestDispatcher("/error404.jsp").forward(request, response);
                    break;
            }
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
            if (criterio != null && !criterio.trim().isEmpty()) {
                request.setAttribute("listaEjemplares", ejemplarModel.buscar(criterio));
                request.setAttribute("criterio", criterio);
            } else {
                request.setAttribute("listaEjemplares", ejemplarModel.buscar(""));
            }
            request.getRequestDispatcher("/ejemplares/listaEjemplares.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void obtener(HttpServletRequest request, HttpServletResponse response) {
        try{
            String idString = request.getParameter("id");
            int id = Integer.parseInt(idString);
            // AGREGAR VALIDACION AQUI -> REDIRECCIONAR A ERROR404.JSP

            Ejemplar ejemplar = ejemplarModel.obtenerEjemplar(id);
            if(ejemplar != null) {
                request.setAttribute("ejemplar", ejemplar);
                request.setAttribute("tiposDocumento", ejemplarModel.obtenerTiposDocumento());
                request.setAttribute("categorias", ejemplarModel.obtenerCategorias());
                request.setAttribute("ubicaciones", ejemplarModel.obtenerUbicaciones());
                request.getRequestDispatcher("/ejemplares/editarEjemplar.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error404.jsp");
            }
        } catch (Exception e) {
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void insertar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            listaErrores.clear();
            // AGREGAR VALIDACIONES AQUI

            // AGREGAR VALIDACIONES AQUI

            if(!listaErrores.isEmpty()) {
                request.setAttribute("listaErrores", listaErrores);
                nuevo(request, response);
                return;
            }

            int idTipoDocumento = Integer.parseInt(request.getParameter("idTipoDocumento"));
            TipoDocumento tipoDocumento = new TipoDocumento();
            tipoDocumento.setIdTipoDoc(idTipoDocumento);

            Ejemplar ejemplar = EjemplarFactory.crearEjemplar(tipoDocumento);

            ejemplar.setTitulo(request.getParameter("titulo"));
            ejemplar.setAutor(request.getParameter("autor"));
            ejemplar.setEditorial(request.getParameter("editorial"));
            ejemplar.setIsbn(request.getParameter("isbn"));
            ejemplar.setAnioPublicacion(Integer.parseInt(request.getParameter("anioPublicacion")));
            ejemplar.setIdioma(request.getParameter("idioma"));
            ejemplar.setCantidadTotal(Integer.parseInt(request.getParameter("cantidadTotal")));
            ejemplar.setCantidadDisponible(ejemplar.getCantidadTotal());
            ejemplar.setFechaIngreso(LocalDate.now());

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
            categoria.setNombreCategoria(request.getParameter("nombreCategoria"));
            ejemplar.setCategoria(categoria);

            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setIdUbicacion(Integer.parseInt(request.getParameter("idUbicacion")));
            ubicacion.setEdificio(request.getParameter("edificio"));
            ubicacion.setPiso(request.getParameter("piso"));
            ubicacion.setSeccion(request.getParameter("seccion"));
            ubicacion.setEstante(request.getParameter("estante"));
            ejemplar.setUbicacion(ubicacion);

            switch (idTipoDocumento) {
                case 1: // LIBRO
                    Libro libro = (Libro) ejemplar;
                    libro.setNumPaginas(Integer.parseInt(request.getParameter("numPaginasLibro")));
                    libro.setColeccion(request.getParameter("coleccion"));
                    libro.setNumeroSerie(request.getParameter("numeroSerie"));
                    break;

                case 2: // TESIS
                    Tesis tesis = (Tesis) ejemplar;
                    tesis.setNumPaginas(Integer.parseInt(request.getParameter("numPaginasTesis")));
                    tesis.setUniversidad(request.getParameter("universidad"));
                    tesis.setFacultad(request.getParameter("facultad"));
                    tesis.setCarrera(request.getParameter("carrera"));
                    tesis.setAsesor(request.getParameter("asesor"));
                    tesis.setGradoAcademico(request.getParameter("gradoAcademico"));
                    break;

                case 3: // REVISTA
                    Revista revista = (Revista) ejemplar;
                    revista.setNumPaginas(Integer.parseInt(request.getParameter("numPaginasRevista")));
                    revista.setVolumen(request.getParameter("volumen"));
                    revista.setNumero(request.getParameter("numero"));
                    revista.setPeriodicidad(request.getParameter("periodicidad"));
                    break;

                case 4: // CD
                    CD cd = (CD) ejemplar;
                    cd.setDuracion(Integer.parseInt(request.getParameter("duracion")));
                    cd.setFormato(request.getParameter("formatoCD"));
                    cd.setArtista(request.getParameter("artistaCD"));
                    break;

                case 5: // DVD
                    DVD dvd = (DVD) ejemplar;
                    dvd.setDuracion(Integer.parseInt(request.getParameter("duracionDVD")));
                    dvd.setFormato(request.getParameter("formatoDVD"));
                    dvd.setDirector(request.getParameter("directorDVD"));
                    break;

                case 6: // INFORME
                    Informe informe = (Informe) ejemplar;
                    informe.setNumPaginas(Integer.parseInt(request.getParameter("numPaginasInforme")));
                    informe.setInstitucion(request.getParameter("institucion"));
                    informe.setSupervisor(request.getParameter("supervisor"));
                    break;

                case 7: // MANUAL
                    Manual manual = (Manual) ejemplar;
                    manual.setNumPaginas(Integer.parseInt(request.getParameter("numPaginasManual")));
                    manual.setArea(request.getParameter("areaManual"));
                    manual.setNivelUsuario(request.getParameter("nivelUsuario"));
                    manual.setVersion(request.getParameter("versionManual"));
                    break;

                default:
                    listaErrores.add("Tipo de documento no soportado");
                    request.setAttribute("listaErrores", listaErrores);
                    nuevo(request, response);
                    return;
            }

            // Insertar en base de datos
            if (ejemplarModel.insertarEjemplar(ejemplar) > 0) {
                request.getSession().setAttribute("exito",
                        "Ejemplar '" + ejemplar.getTitulo() + "' registrado exitosamente como " + ejemplar.getTipoDocumentoString());
                response.sendRedirect(request.getContextPath() + "/ejemplares.do?op=listar");
            } else {
                request.getSession().setAttribute("fracaso", "No se pudo registrar el ejemplar. Verifique los datos.");
                response.sendRedirect(request.getContextPath() + "/ejemplares.do?op=nuevo");
            }
        } catch (Exception e) {
            Logger.getLogger(EjemplaresController.class.getName()).log(Level.SEVERE, null, e);
            request.getSession().setAttribute("fracaso", "Error al registrar el ejemplar");
            response.sendRedirect(request.getContextPath() + "/ejemplares.do?op=nuevo");
        }
    }

    private void modificar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listaErrores.clear();

        String idStr = request.getParameter("idEjemplar");

        //AGREGAR VALIDACIONES AQUI ->

        if (!listaErrores.isEmpty()) {
            request.setAttribute("listaErrores", listaErrores);
            obtener(request, response);
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

            // Recreamos el objeto con el mismo tipo
            TipoDocumento tipoDocumento = existente.getTipoDocumento();
            Ejemplar ejemplar = EjemplarFactory.crearEjemplar(tipoDocumento);
            ejemplar.setIdEjemplar(idEjemplar);

            // === CAMPOS COMUNES ===
            ejemplar.setTitulo(request.getParameter("titulo").trim());
            ejemplar.setAutor(request.getParameter("autor"));
            ejemplar.setEditorial(request.getParameter("editorial"));
            ejemplar.setIsbn(request.getParameter("isbn"));
            ejemplar.setAnioPublicacion(Integer.parseInt(request.getParameter("anioPublicacion")));
            ejemplar.setIdioma(request.getParameter("idioma"));
            ejemplar.setCantidadTotal(Integer.parseInt(request.getParameter("cantidadTotal")));
            ejemplar.setCantidadDisponible(Integer.parseInt(request.getParameter("cantidadDisponible")));
            ejemplar.setActivo(existente.isActivo());

            // Relaciones
            Categoria categoria = new Categoria();
            categoria.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
            ejemplar.setCategoria(categoria);

            Ubicacion ubicacion = new Ubicacion();
            ubicacion.setIdUbicacion(Integer.parseInt(request.getParameter("idUbicacion")));
            ejemplar.setUbicacion(ubicacion);

            // === CAMPOS ESPECÍFICOS (según tipo real del ejemplar) ===
            switch (tipoDocumento.getIdTipoDoc()) {
                case 1: // LIBRO
                    ((Libro) ejemplar).setNumPaginas(Integer.parseInt(request.getParameter("numPaginasLibro")));
                    ((Libro) ejemplar).setColeccion(request.getParameter("coleccion"));
                    ((Libro) ejemplar).setNumeroSerie(request.getParameter("numeroSerie"));
                    break;

                case 2: // TESIS
                    ((Tesis) ejemplar).setNumPaginas(Integer.parseInt(request.getParameter("numPaginasTesis")));
                    ((Tesis) ejemplar).setUniversidad(request.getParameter("universidad"));
                    ((Tesis) ejemplar).setFacultad(request.getParameter("facultad"));
                    ((Tesis) ejemplar).setCarrera(request.getParameter("carrera"));
                    ((Tesis) ejemplar).setAsesor(request.getParameter("asesor"));
                    ((Tesis) ejemplar).setGradoAcademico(request.getParameter("gradoAcademico"));
                    break;

                case 3: // REVISTA
                    ((Revista) ejemplar).setNumPaginas(Integer.parseInt(request.getParameter("numPaginasRevista")));
                    ((Revista) ejemplar).setVolumen(request.getParameter("volumen"));
                    ((Revista) ejemplar).setNumero(request.getParameter("numero"));
                    ((Revista) ejemplar).setPeriodicidad(request.getParameter("periodicidad"));
                    break;

                case 4: // CD
                    ((CD) ejemplar).setDuracion(Integer.parseInt(request.getParameter("duracionCD")));
                    ((CD) ejemplar).setFormato(request.getParameter("formatoCD"));
                    ((CD) ejemplar).setArtista(request.getParameter("artistaCD"));
                    break;

                case 5: // DVD
                    ((DVD) ejemplar).setDuracion(Integer.parseInt(request.getParameter("duracionDVD")));
                    ((DVD) ejemplar).setFormato(request.getParameter("formatoDVD"));
                    ((DVD) ejemplar).setDirector(request.getParameter("directorDVD"));
                    break;

                case 6: // INFORME
                    ((Informe) ejemplar).setNumPaginas(Integer.parseInt(request.getParameter("numPaginasInforme")));
                    ((Informe) ejemplar).setInstitucion(request.getParameter("institucion"));
                    ((Informe) ejemplar).setSupervisor(request.getParameter("supervisor"));
                    break;

                case 7: // MANUAL
                    ((Manual) ejemplar).setNumPaginas(Integer.parseInt(request.getParameter("numPaginasManual")));
                    ((Manual) ejemplar).setArea(request.getParameter("areaManual"));
                    ((Manual) ejemplar).setNivelUsuario(request.getParameter("nivelUsuario"));
                    ((Manual) ejemplar).setVersion(request.getParameter("versionManual"));
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
            request.getSession().setAttribute("fracaso", "Error del sistema al modificar");
            response.sendRedirect("ejemplares.do?op=listar");
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listaErrores.clear();

        String idStr = request.getParameter("id");
        // AGREGAR VALIDACIONES AQUI ->
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

}
