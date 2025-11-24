package com.biblioteca.model;

import com.biblioteca.beans.Configuracion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfiguracionModel extends Conexion {
    public Configuracion obtenerConfiguracion() {
        try{
            String sql = "SELECT * FROM configuracion WHERE anio_aplicacion = YEAR(CURDATE())";
            Configuracion configuracion = new Configuracion();
            this.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                configuracion.setIdConfig(rs.getInt("id_configuracion"));
                configuracion.setMaxPrestamosAlumno(rs.getInt("max_prestamos_alumno"));
                configuracion.setMaxPrestamosProfesor(rs.getInt("max_prestamos_profesor"));
                configuracion.setDiasPrestamoAlumno(rs.getInt("dias_prestamo_alumno"));
                configuracion.setDiasPrestamoProfesor(rs.getInt("dias_prestamo_profesor"));
                configuracion.setMoraDiaria(rs.getDouble("mora_diaria"));
                configuracion.setAnioAplicacion(rs.getInt("anio_aplicacion"));
                this.desconectar();
                return configuracion;
            }
            this.desconectar();
            return configuracion;
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguracionModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return null;
        }
    }

    // Actualizar o guardar la configuracion del anio actual
    public int guardarConfiguracion(Configuracion configuracion) {
        try{
            String sql = "SELECT id_config FROM configuracion WHERE anio_aplicacion = ?";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, configuracion.getAnioAplicacion());
            rs =  ps.executeQuery();

            if(rs.next()){
                // Update - Ya existe la configuracion
                int idConfiguracion = rs.getInt("id_configuracion");
                String sqlUpdate = "UPDATE configuracion SET " +
                                   "max_prestamos_alumno = ?, " +
                                   "max_prestamos_profesor = ?, " +
                                   "dias_prestamo_alumno = ?, " +
                                   "dias_prestamo_profesor = ?, " +
                                   "mora_diaria = ? " +
                                   "WHERE id_config = ?";

                this.desconectar();
                this.conectar();
                ps = conexion.prepareStatement(sqlUpdate);
                ps.setInt(1, configuracion.getMaxPrestamosAlumno());
                ps.setInt(2, configuracion.getMaxPrestamosProfesor());
                ps.setInt(3, configuracion.getDiasPrestamoAlumno());
                ps.setInt(4, configuracion.getDiasPrestamoProfesor());
                ps.setDouble(5, configuracion.getMoraDiaria());
                ps.setInt(6, idConfiguracion);

                int filasAfectadas = ps.executeUpdate();
                this.desconectar();
                return filasAfectadas;
            } else {
                // Insert - No existe la configuracion
                String sqlInsert = "INSERT INTO configuracion " +
                                   "(max_prestamos_alumno, max_prestamos_profesor, dias_prestamo_alumno, " +
                                   "dias_prestamo_profesor, mora_diaria, anio_aplicacion) " +
                                   "VALUES (?, ?, ?, ?, ?, ?)";
                this.conectar();
                ps = conexion.prepareStatement(sqlInsert);
                ps.setInt(1, configuracion.getMaxPrestamosAlumno());
                ps.setInt(2, configuracion.getMaxPrestamosProfesor());
                ps.setInt(3, configuracion.getDiasPrestamoAlumno());
                ps.setInt(4, configuracion.getDiasPrestamoProfesor());
                ps.setDouble(5, configuracion.getMoraDiaria());
                ps.setInt(6, configuracion.getAnioAplicacion());

                int filasAfectadas = ps.executeUpdate();
                this.desconectar();
                return filasAfectadas;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguracionModel.class.getName()).log(Level.SEVERE, null, ex);
            this.desconectar();
            return 0;
        }
    }
}
