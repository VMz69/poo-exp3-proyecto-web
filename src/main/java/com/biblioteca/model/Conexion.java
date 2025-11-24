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
    protected static Connection conexion = null;
    protected PreparedStatement ps;
    protected CallableStatement cs;
    protected ResultSet rs;

    public Conexion(){
        this.ps = null;
        this.rs = null;
    }

    public void conectar(){
        try{
            if(conexion == null || conexion.isClosed()){
                Context init = new InitialContext();
                Context context = (Context) init.lookup("java:comp/env");
                DataSource dataSource = (DataSource) context.lookup("jdbc/mysql");
                conexion = dataSource.getConnection();

            }
        } catch (NamingException e) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void desconectar(){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
