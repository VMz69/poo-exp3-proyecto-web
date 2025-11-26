package com.biblioteca.model;

import com.biblioteca.beans.TipoUsuario;
import com.biblioteca.beans.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioModel extends Conexion {
    public int insertarUsuario(Usuario usuario) throws SQLException {
        try {
            int filasAfectadas = 0;
            String sql = "INSERT INTO usuarios (nombre_completo, correo, usuario, contrasena, id_tipo) " +
                         "VALUES (?, ?, ?, ?, ?)";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getUsuario());
            ps.setString(4, usuario.getContrasena());
            ps.setInt(5, usuario.getTipoUsuario().getIdTipo());

            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;

        } catch (SQLException e) {
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return 0;
        }
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        try{
            List<Usuario> listaUsuarios = new ArrayList<>();
            String sql ="SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                        "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo ORDER BY u.id_usuario";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTieneMora(rs.getBoolean("tiene_mora"));
                usuario.setMontoMora(rs.getDouble("monto_mora"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setActivo(rs.getBoolean("activo"));

                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                usuario.setTipoUsuario(tipoUsuario);

                listaUsuarios.add(usuario);
            }
            this.desconectar();
            return listaUsuarios;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }

    // Obtener usuario por id
    public Usuario obtenerUsuario(int id) throws SQLException {
        try{
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                         "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                         "WHERE u.id_usuario = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTieneMora(rs.getBoolean("tiene_mora"));
                usuario.setMontoMora(rs.getDouble("monto_mora"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));  // ← CORREGIDO
                usuario.setActivo(rs.getBoolean("activo"));

                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                usuario.setTipoUsuario(tipoUsuario);

                this.desconectar();
                return usuario;
            }
            this.desconectar();
            return null;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }

    // Actualizar usuario
    public int actualizarUsuario(Usuario usuario) throws SQLException {
        try{
            int filasAfectadas = 0;
            String sql = "UPDATE usuarios SET nombre_completo = ?, correo = ?, usuario = ?, id_tipo = ?, activo = ? WHERE id_usuario = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getUsuario());
            ps.setInt(4, usuario.getTipoUsuario().getIdTipo());
            ps.setBoolean(5,  usuario.isActivo());
            ps.setInt(6, usuario.getIdUsuario());

            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return 0;
        }
    }

    public int eliminarUsuario(int id) throws SQLException {
        try{
            int filasAfectadas = 0;
            String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return 0;
        }
    }

    // Buscar usuarios por nombre, usuario o correo
    public List<Usuario> buscarUsuarios(String criterio) throws SQLException {
        try{
            List<Usuario> listaUsuarios = new ArrayList<>();
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                         "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                         "WHERE u.nombre_completo LIKE ? OR u.usuario LIKE ? OR u.correo LIKE ?";
            String like = "%" + criterio + "%";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTieneMora(rs.getBoolean("tiene_mora"));
                usuario.setMontoMora(rs.getDouble("monto_mora"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setActivo(rs.getBoolean("activo"));

                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                usuario.setTipoUsuario(tipoUsuario);
                listaUsuarios.add(usuario);
            }
            this.desconectar();
            return listaUsuarios;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }

    public Usuario autenticar(String usuario, String contrasena) throws SQLException {
        try{
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                         "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                         "WHERE u.usuario = ? AND u.contrasena = ? AND u.activo = TRUE";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario1 = new Usuario();
                usuario1.setIdUsuario(rs.getInt("id_usuario"));
                usuario1.setNombreCompleto(rs.getString("nombre_completo"));
                usuario1.setCorreo(rs.getString("correo"));
                usuario1.setUsuario(rs.getString("usuario"));
                usuario1.setContrasena(rs.getString("contrasena"));
                usuario1.setTieneMora(rs.getBoolean("tiene_mora"));
                usuario1.setMontoMora(rs.getDouble("monto_mora"));
                usuario1.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario1.setActivo(rs.getBoolean("activo"));
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                usuario1.setTipoUsuario(tipoUsuario);
                return usuario1;
            }
            this.desconectar();
            return null;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }

    public int actualizarMora(int idUsuario, boolean tieneMora, double monto) throws SQLException {
        try{
            int filasAfectadas = 0;
            String sql = "UPDATE usuarios SET tiene_mora = ?, monto_mora = ? WHERE id_usuario = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setBoolean(1, tieneMora);
            ps.setDouble(2, monto);
            ps.setInt(3, idUsuario);
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return 0;
        }
    }

    public int actualizarContrasena (Usuario usuario) throws SQLException {
        try{
            int filasAfectadas = 0;
            String sql = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getContrasena());
            ps.setInt(2, usuario.getIdUsuario());
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return 0;
        }
    }

    // Pagar Mora
    public int pagarMora(int id) throws SQLException{
        try{
            int filasAfectadas = 0;
            String sql = "UPDATE usuarios SET tiene_mora = FALSE, monto_mora = 0.00 WHERE id_usuario = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            filasAfectadas = ps.executeUpdate();
            this.desconectar();
            return filasAfectadas;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return 0;
        }
    }

    // Obtener los tipos de usuario
    public List<TipoUsuario> obtenerTipoUsuarios() throws SQLException {
        try{
            List<TipoUsuario> listaTipoUsuarios = new ArrayList<>();
            String sql = "SELECT * FROM tipo_usuario ORDER BY id_tipo";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                listaTipoUsuarios.add(tipoUsuario);
            }
            this.desconectar();
            return listaTipoUsuarios;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }

    public List<Usuario> obtenerTodosUsuarios() throws SQLException {
        try{
            List<Usuario> listaUsuarios = new ArrayList<>();
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                         "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo ORDER BY u.id_usuario";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setTieneMora(rs.getBoolean("tiene_mora"));
                usuario.setMontoMora(rs.getDouble("monto_mora"));
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                usuario.setActivo(rs.getBoolean("activo"));
                TipoUsuario tipoUsuario = new TipoUsuario();
                tipoUsuario.setIdTipo(rs.getInt("id_tipo"));
                tipoUsuario.setNombreTipo(rs.getString("nombre_tipo"));
                listaUsuarios.add(usuario);
            }
            this.desconectar();
            return listaUsuarios;
        } catch (SQLException e){
            Logger.getLogger(UsuarioModel.class.getName()).log(Level.SEVERE, null, e);
            this.desconectar();
            return null;
        }
    }
}