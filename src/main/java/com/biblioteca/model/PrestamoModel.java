package com.biblioteca.model;
import com.biblioteca.beans.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrestamoModel extends Conexion {
    public int insertarPrestamo(Prestamo prestamo) throws SQLException{
        try{
            int filasAfectadas = 0;
            String sql = "INSERT INTO prestamo (id_usuario, id_ejemplar, fecha_vencimiento, estado) " +
                    "VALUES (?, ?, ?, 'Activo')";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, prestamo.getIdUsuario());
            ps.setInt(2, prestamo.getIdEjemplar());
            ps.setDate(3, prestamo.getFechaVencimiento());

            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }

    public Prestamo obtenerPrestamo(int id) throws SQLException{
        try{
            String sql = "SELECT p.*, u.*, e.*, tu.nombre_tipo, td.id_tipo_doc, td.nombre_tipo as tipo_doc_nombre " +
                         "FROM prestamo p " +
                         "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                         "JOIN ejemplar e ON p.id_ejemplar = e.id_ejemplar " +
                         "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                         "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                         "WHERE p.id_prestamo = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setIdUsuario(rs.getInt("id_usuario"));
                prestamo.setIdEjemplar(rs.getInt("id_ejemplar"));
                prestamo.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
                prestamo.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                prestamo.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
                prestamo.setMoraCalculada(rs.getDouble("mora_calculada"));
                prestamo.setEstado(rs.getString("estado"));

                // Usuario
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setTieneMora(rs.getBoolean("tiene_mora"));
                usuario.setMontoMora(rs.getDouble("monto_mora"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setActivo(rs.getBoolean("activo"));

                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                usuario.setTipoUsuario(tipoUsuario);
                prestamo.setUsuario(usuario);

                // Ejemplar - USAR FACTORY
                TipoDocumento tipoDocumento = new TipoDocumento();
                tipoDocumento.setIdTipoDoc(rs.getInt("id_tipo_doc"));
                tipoDocumento.setNombreTipo(rs.getString("tipo_doc_nombre"));

                Ejemplar ejemplar = EjemplarFactory.crearEjemplar(tipoDocumento);
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setTitulo(rs.getString("titulo"));
                ejemplar.setAutor(rs.getString("autor"));
                ejemplar.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                ejemplar.setCantidadTotal(rs.getInt("cantidad_total"));
                ejemplar.setTipoDocumento(tipoDocumento);

                prestamo.setEjemplar(ejemplar);
                this.desconectar();
                return prestamo;
            }
            this.desconectar();
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return null;
        }
    }

    public List<Prestamo> obtenerPrestamosAtivos(){
        try{
            List<Prestamo> listaPrestamos = new ArrayList<>();
            String sql = "SELECT p.*, u.nombre_completo, e.titulo, td.id_tipo_doc, td.nombre_tipo as tipo_doc_nombre " +
                         "FROM prestamo p " +
                         "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                         "JOIN ejemplar e ON p.id_ejemplar = e.id_ejemplar " +
                         "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                         "WHERE p.estado = 'Activo' ORDER BY p.fecha_prestamo DESC";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setIdUsuario(rs.getInt("id_usuario"));
                prestamo.setIdEjemplar(rs.getInt("id_ejemplar"));
                prestamo.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
                prestamo.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                prestamo.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
                prestamo.setMoraCalculada(rs.getDouble("mora_calculada"));
                prestamo.setEstado(rs.getString("estado"));

                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                prestamo.setUsuario(usuario);

                // Ejemplar - USAR FACTORY
                TipoDocumento tipoDocumento = new TipoDocumento();
                tipoDocumento.setIdTipoDoc(rs.getInt("id_tipo_doc"));
                tipoDocumento.setNombreTipo(rs.getString("tipo_doc_nombre"));

                Ejemplar ejemplar = EjemplarFactory.crearEjemplar(tipoDocumento);
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setTitulo(rs.getString("titulo"));
                ejemplar.setTipoDocumento(tipoDocumento);
                prestamo.setEjemplar(ejemplar);

                listaPrestamos.add(prestamo);
            }
            this.desconectar();
            return listaPrestamos;
        } catch (SQLException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return null;
        }
    }

    // Devolucion de prestamo
    public int devolverPrestamo(Prestamo prestamo){
        try {
            int filasAfectadas = 0;
            String sql = "UPDATE prestamo SET fecha_devolucion = ?, mora_calculada = ?, estado = ? " +
                         "WHERE id_prestamo = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setTimestamp(1, prestamo.getFechaDevolucion());
            ps.setDouble(2, prestamo.getMoraCalculada());
            ps.setString(3, prestamo.getEstado());
            ps.setInt(4, prestamo.getIdPrestamo());
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }

    // Contar prestamos activos por usuario
    public int contarPrestamosActivos(int id){
        try {
            int count = 0;
            String sql = "SELECT COUNT(*) as total FROM prestamo " +
                         "WHERE id_usuario = ? AND estado = 'Activo'";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                count = rs.getInt("total");
            }
            this.desconectar();
            return count;
        } catch (SQLException ex) {
            Logger.getLogger(Categoria.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }

    // ---------- Milton: Logica de prestamos en mora para evitar nuevos prestamos ----------

    /**
     * Devuelve true si el usuario tiene al menos un préstamo en mora.
     */

    public boolean usuarioTienePrestamosEnMora(int idUsuario) throws SQLException {
        boolean tiene = false;
        String sql = "SELECT COUNT(*) AS total FROM prestamo " +
                "WHERE id_usuario = ? " +
                "AND estado = 'Activo' " +
                "AND fecha_vencimiento < NOW() " +
                "AND fecha_devolucion IS NULL";
        try {
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                tiene = rs.getInt("total") > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrestamoModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            this.desconectar();
        }
        return tiene;
    }

    /**
     * Calcula y devuelve la suma de las moras (mora_calculada) para todos los préstamos en mora del usuario.
     * Retorna 0 si no hay ninguna mora. Esto es importante por que, si hizo varios prestamos y solo 1 esta en mora por el momento,
     * no debe de prestarse nada. A pesar que los demás estén a tiempo.
     */

    public double calcularMoraTotal(int idUsuario) throws SQLException {
        double total = 0.0;
        String sql = "SELECT COALESCE(SUM(mora_calculada),0) AS total FROM prestamo " +
                "WHERE id_usuario = ? " +
                "AND estado = 'Activo' " +
                "AND fecha_vencimiento < NOW() " +
                "AND fecha_devolucion IS NULL";
        try {
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrestamoModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            this.desconectar();
        }
        return total;
    }

    public int contarPrestamosVencidosActivos(int idUsuario) throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM prestamo " +
                "WHERE id_usuario = ? AND estado = 'Activo' AND fecha_vencimiento < ?";
        try {
            this.conectar();
            ps = conexion.prepareStatement(sql);
            // fecha actual como java.sql.Date (solo fecha)
            java.sql.Date hoy = new java.sql.Date(System.currentTimeMillis());
            ps.setInt(1, idUsuario);
            ps.setDate(2, hoy);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PrestamoModel.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            this.desconectar();
        }
        return count;
    }
}
