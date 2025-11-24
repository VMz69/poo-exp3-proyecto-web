package com.biblioteca.model;
import com.biblioteca.beans.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EjemplarModel extends Conexion {
    public int insertarEjemplar(Ejemplar e) throws SQLException {
        try {
            int filasAfectadas = 0;
            String sql = "INSERT INTO ejemplar (titulo, autor, editorial, isbn, anio_publicacion, " +
                    "id_tipo_documento, id_categoria, id_ubicacion, numero_edicion, idioma, " +
                    "num_paginas, descripcion, cantidad_total, cantidad_disponible, fecha_ingreso, activo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getAutor());
            ps.setString(3, e.getEditorial());
            ps.setString(4, e.getIsbn());
            ps.setInt(5, e.getAnioPublicacion());
            ps.setInt(6, e.getTipoDocumento().getIdTipoDoc());
            ps.setInt(7, e.getCategoria().getIdCategoria());
            ps.setInt(8, e.getUbicacion().getIdUbicacion());
            ps.setString(9, e.getNumeroEdicion());
            ps.setString(10, e.getIdioma());

            int paginas = 0;
            if (e instanceof Libro) {
                paginas = ((Libro) e).getNumPaginas();
            } else if (e instanceof Tesis) {
                paginas = ((Tesis) e).getNumPaginas();
            } else if (e instanceof Revista) {
                paginas = ((Revista) e).getNumPaginas();
            } else if (e instanceof Informe) {
                paginas = ((Informe) e).getNumPaginas();
            } else if (e instanceof Manual) {
                paginas = ((Manual) e).getNumPaginas();
            }
            ps.setInt(11, paginas);

            String descripcion = (e.getDescripcion() != null ? e.getDescripcion() : "") +
                    " | " + e.getInformacionEspecifica();
            ps.setString(12, descripcion.trim());

            ps.setInt(13, e.getCantidadTotal());
            ps.setInt(14, e.getCantidadDisponible());
            ps.setObject(15, e.getFechaIngreso());
            ps.setBoolean(16, e.isActivo());

            filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        e.setIdEjemplar(id);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            this.desconectar();
            return filasAfectadas;

        } catch (SQLException ex) {
            Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }

    // Obtener ejemplar por id
    public Ejemplar obtenerEjemplar(int id) {
        try {
            String sql = "SELECT e.*, td.*, c.*, u.* " +
                    "FROM ejemplar e " +
                    "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                    "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                    "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                    "WHERE e.id_ejemplar = ? AND e.activo = TRUE";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                this.desconectar();
                return mapearEjemplar(rs);
            }
            this.desconectar();
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
            this.conectar();
            return null;
        }
    }

    public List<Ejemplar> buscar(String criterio) throws SQLException {
        try {
            List<Ejemplar> listaEjemplar = new ArrayList<>();
            String sql = "SELECT e.*, td.*, c.*, u.* " +
                    "FROM ejemplar e " +
                    "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                    "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                    "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                    "WHERE (e.titulo LIKE ? OR e.autor LIKE ? OR e.isbn LIKE ?) AND e.activo = TRUE";

            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + criterio + "%");
            ps.setString(2, "%" + criterio + "%");
            ps.setString(3, "%" + criterio + "%");

            rs = ps.executeQuery();
            while (rs.next()) {
                listaEjemplar.add(mapearEjemplar(rs));
            }
            this.desconectar();
            return listaEjemplar;
        } catch (SQLException ex) {
            Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return null;
        }
    }

    public int actualizarDisponibilidad(int id, int cambio) throws SQLException {
        try {
            int filasAfectadas = 0;
            String sql = "UPDATE ejemplar SET cantidad_disponible = cantidad_disponible + ? WHERE id_ejemplar = ? AND activo = TRUE";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, cambio);
            ps.setInt(2, id);
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException ex) {
            Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }

    public int eliminarEjemplar(int id) throws SQLException {
        try {
            int filasAfectadas = 0;
            String sqlPrestamos = "DELETE FROM prestamo WHERE  id_ejemplar = ?";
            String sqlEjemplar = "DELETE FROM ejemplar WHERE id_ejemplar = ?";
            this.conectar();
            ps = conexion.prepareStatement(sqlPrestamos);
            ps.setInt(1, id);
            ps.executeUpdate();
            this.desconectar();
            this.conectar();
            ps = conexion.prepareStatement(sqlEjemplar);
            ps.setInt(1, id);
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException ex) {
            Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }

    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException {
        // Obtener tipo de documento
        TipoDocumento td = new TipoDocumento();
        td.setIdTipoDoc(rs.getInt("id_tipo_doc"));
        td.setNombreTipo(rs.getString("nombre_tipo"));

        // Crear instancia específica según el tipo
        Ejemplar e = EjemplarFactory.crearEjemplar(td);

        // Mapear campos comunes
        e.setIdEjemplar(rs.getInt("id_ejemplar"));
        e.setTitulo(rs.getString("titulo"));
        e.setAutor(rs.getString("autor"));
        e.setEditorial(rs.getString("editorial"));
        e.setIsbn(rs.getString("isbn"));
        e.setAnioPublicacion(rs.getInt("anio_publicacion"));
        e.setNumeroEdicion(rs.getString("numero_edicion"));
        e.setIdioma(rs.getString("idioma"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCantidadTotal(rs.getInt("cantidad_total"));
        e.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        e.setFechaIngreso(rs.getDate("fecha_ingreso") != null ? rs.getDate("fecha_ingreso").toLocalDate() : null);
        e.setActivo(rs.getBoolean("activo"));
        e.setTipoDocumento(td);

        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNombreCategoria(rs.getString("nombre_categoria"));
        e.setCategoria(c);

        Ubicacion u = new Ubicacion();
        u.setIdUbicacion(rs.getInt("id_ubicacion"));
        u.setEdificio(rs.getString("edificio"));
        u.setPiso(rs.getString("piso"));
        u.setSeccion(rs.getString("seccion"));
        u.setEstante(rs.getString("estante"));
        e.setUbicacion(u);

        int paginas = rs.getInt("num_paginas");
        if (e instanceof Libro) {
            ((Libro) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                if (desc.contains("Colección:")) {
                    String coleccion = extraerValor(desc, "Colección:");
                    ((Libro) e).setColeccion(coleccion);
                }
                if (desc.contains("Serie:")) {
                    String serie = extraerValor(desc, "Serie:");
                    ((Libro) e).setNumeroSerie(serie);
                }
            }
        } else if (e instanceof Tesis) {
            ((Tesis) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Tesis) e).setUniversidad(extraerValor(desc, "Universidad:"));
                ((Tesis) e).setFacultad(extraerValor(desc, "Facultad:"));
                ((Tesis) e).setCarrera(extraerValor(desc, "Carrera:"));
                ((Tesis) e).setAsesor(extraerValor(desc, "Asesor:"));
                ((Tesis) e).setGradoAcademico(extraerValor(desc, "Grado:"));
            }
        } else if (e instanceof Revista) {
            ((Revista) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Revista) e).setVolumen(extraerValor(desc, "Vol."));
                ((Revista) e).setNumero(extraerValor(desc, "Núm."));
                ((Revista) e).setPeriodicidad(extraerValor(desc, "Periodicidad:"));
            }
        } else if (e instanceof CD) {
            String desc = rs.getString("descripcion");
            if (desc != null) {
                String durStr = extraerValor(desc, "Duración:");
                if (durStr != null && !durStr.isEmpty()) {
                    try {
                        ((CD) e).setDuracion(Integer.parseInt(durStr.replaceAll("\\D", "")));
                    } catch (NumberFormatException ex) {
                    }
                }
                ((CD) e).setFormato(extraerValor(desc, "Formato:"));
                ((CD) e).setArtista(extraerValor(desc, "Artista:"));
            }
        } else if (e instanceof DVD) {
            String desc = rs.getString("descripcion");
            if (desc != null) {
                String durStr = extraerValor(desc, "Duración:");
                if (durStr != null && !durStr.isEmpty()) {
                    try {
                        ((DVD) e).setDuracion(Integer.parseInt(durStr.replaceAll("\\D", "")));
                    } catch (NumberFormatException ex) {
                    }
                }
                ((DVD) e).setFormato(extraerValor(desc, "Formato:"));
                ((DVD) e).setDirector(extraerValor(desc, "Director:"));
            }
        } else if (e instanceof Informe) {
            ((Informe) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Informe) e).setInstitucion(extraerValor(desc, "Institución:"));
                ((Informe) e).setSupervisor(extraerValor(desc, "Supervisor:"));
            }
        } else if (e instanceof Manual) {
            ((Manual) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Manual) e).setArea(extraerValor(desc, "Área:"));
                ((Manual) e).setNivelUsuario(extraerValor(desc, "Nivel:"));
                ((Manual) e).setVersion(extraerValor(desc, "Versión:"));
            }
        }

        return e;
    }

    // Metodo auxiliar para extraer valores de la descripción
    private String extraerValor(String descripcion, String clave) {
        if (descripcion == null || !descripcion.contains(clave)) {
            return "";
        }
        int inicio = descripcion.indexOf(clave) + clave.length();
        int fin = descripcion.indexOf("|", inicio);
        if (fin == -1) fin = descripcion.length();
        return descripcion.substring(inicio, fin).trim();
    }

    public List<TipoDocumento> obtenerTiposDocumento() {
        return cargarCombo("tipo_documento", "id_tipo_doc", "nombre_tipo", TipoDocumento.class);
    }

    public List<Categoria> obtenerCategorias() {
        return cargarCombo("categoria", "id_categoria", "nombre_categoria", Categoria.class);
    }

    public List<Ubicacion> obtenerUbicaciones() {
        return cargarCombo("ubicacion", "id_ubicacion", "edificio", Ubicacion.class);
    }

    private <T> List<T> cargarCombo(String tabla, String idCol, String nombreCol, Class<T> clazz) {
        try {
            List<T> lista = new ArrayList<>();
            String sql = "SELECT * FROM " + tabla + " ORDER BY " + nombreCol;

            this.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (clazz == TipoDocumento.class) {
                    TipoDocumento td = new TipoDocumento();
                    td.setIdTipoDoc(rs.getInt(idCol));
                    td.setNombreTipo(rs.getString(nombreCol));
                    lista.add((T) td);
                } else if (clazz == Categoria.class) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt(idCol));
                    c.setNombreCategoria(rs.getString(nombreCol));
                    lista.add((T) c);
                } else if (clazz == Ubicacion.class) {
                    Ubicacion u = new Ubicacion();
                    u.setIdUbicacion(rs.getInt(idCol));
                    u.setEdificio("Edificio " + rs.getString("edificio"));
                    u.setPiso("Piso " + rs.getString("piso"));
                    u.setSeccion("Seccion " + rs.getString("seccion"));
                    u.setEstante("Estante " + rs.getString("estante"));
                    lista.add((T) u);
                }
            }
            this.desconectar();
            return lista;
        } catch (SQLException e) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }

    public int actualizarEjemplar(Ejemplar e) throws SQLException {
        try {
            String sql = "UPDATE ejemplar SET " +
                         "titulo = ?, autor = ?, editorial = ?, isbn = ?, anio_publicacion = ?, " +
                         "id_tipo_documento = ?, id_categoria = ?, id_ubicacion = ?, " +
                         "numero_edicion = ?, idioma = ?, num_paginas = ?, descripcion = ?, " +
                         "cantidad_total = ?, cantidad_disponible = ?, fecha_ingreso = ?, activo = ? " +
                         "WHERE id_ejemplar = ?";

            this.conectar();
            ps = conexion.prepareStatement(sql);

            ps.setString(1, e.getTitulo());
            ps.setString(2, e.getAutor());
            ps.setString(3, e.getEditorial());
            ps.setString(4, e.getIsbn());
            ps.setInt(5, e.getAnioPublicacion());
            ps.setInt(6, e.getTipoDocumento().getIdTipoDoc());
            ps.setInt(7, e.getCategoria().getIdCategoria());
            ps.setInt(8, e.getUbicacion().getIdUbicacion());
            ps.setString(9, e.getNumeroEdicion());
            ps.setString(10, e.getIdioma());

            int paginas = 0;
            if (e instanceof Libro) paginas = ((Libro) e).getNumPaginas();
            else if (e instanceof Tesis) paginas = ((Tesis) e).getNumPaginas();
            else if (e instanceof Revista) paginas = ((Revista) e).getNumPaginas();
            else if (e instanceof Informe) paginas = ((Informe) e).getNumPaginas();
            else if (e instanceof Manual) paginas = ((Manual) e).getNumPaginas();
            ps.setInt(11, paginas);

            // Descripción con información específica (igual que en insertar)
            String descripcion = (e.getDescripcion() != null ? e.getDescripcion().trim() : "") +
                    " | " + e.getInformacionEspecifica();
            ps.setString(12, descripcion.trim());

            ps.setInt(13, e.getCantidadTotal());
            ps.setInt(14, e.getCantidadDisponible());
            ps.setObject(15, e.getFechaIngreso());
            ps.setBoolean(16, e.isActivo());
            ps.setInt(17, e.getIdEjemplar());

            int filas = ps.executeUpdate();
            this.desconectar();
            return filas;

        } catch (SQLException ex) {
            Logger.getLogger(EjemplarModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }
}

