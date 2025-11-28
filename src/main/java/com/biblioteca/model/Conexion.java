package com.biblioteca.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Conexion {

    // IMPORTANTE: conexión protegida y NO estática
    protected Connection conexion = null;
    protected PreparedStatement ps = null;
    protected CallableStatement cs = null;
    protected ResultSet rs = null;

    public Conexion() {
        // No inicializamos aquí, lo hacemos en conectar()
    }

    public void conectar() throws SQLException {
        try {
            if (conexion == null || conexion.isClosed()) {
                Context initContext = new InitialContext();
                Context envContext = (Context) initContext.lookup("java:/comp/env");
                DataSource dataSource = (DataSource) envContext.lookup("jdbc/mysql");
                conexion = dataSource.getConnection();
            }
        } catch (NamingException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, "Error JNDI", ex);
            throw new SQLException("No se pudo obtener el DataSource JNDI", ex);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, "Error SQL al conectar", ex);
            throw ex;
        }
    }

    // Cierre seguro de recursos
    public void desconectar() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.WARNING, "", e);
        }
        try {
            if (ps != null) {
                ps.close();
                ps = null;
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.WARNING, "", e);
        }
        try {
            if (cs != null) {
                cs.close();
                cs = null;
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.WARNING, "", e);
        }
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                conexion = null;
            }
        } catch (SQLException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.WARNING, "", e);
        }
    }
}