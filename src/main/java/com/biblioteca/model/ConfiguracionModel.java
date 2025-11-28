package com.biblioteca.model;

import com.biblioteca.beans.Configuracion;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfiguracionModel extends Conexion {
    public Configuracion obtenerConfiguracion() {
        Configuracion configuracion = new Configuracion();
        try{
            String sql = "SELECT * FROM configuracion WHERE anio_aplicacion = YEAR(CURDATE())";
            this.conectar();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                configuracion.setIdConfig(rs.getInt("id_config"));
                configuracion.setMaxPrestamosAlumno(rs.getInt("max_prestamos_alumno"));
                configuracion.setMaxPrestamosProfesor(rs.getInt("max_prestamos_profesor"));
                configuracion.setDiasPrestamoAlumno(rs.getInt("dias_prestamo_alumno"));
                configuracion.setDiasPrestamoProfesor(rs.getInt("dias_prestamo_profesor"));
                configuracion.setMoraDiaria(rs.getDouble("mora_diaria"));
                configuracion.setAnioAplicacion(rs.getInt("anio_aplicacion"));
                this.desconectar();
                return configuracion;
            } else {
                configuracion.setMaxPrestamosAlumno(3);
                configuracion.setMaxPrestamosProfesor(5);
                configuracion.setDiasPrestamoAlumno(7);
                configuracion.setDiasPrestamoProfesor(14);
                configuracion.setMoraDiaria(1.50);
                configuracion.setAnioAplicacion(java.time.Year.now().getValue());
            }
            this.desconectar();
            return configuracion;
        } catch (SQLException ex) {
            Logger.getLogger(ConfiguracionModel.class.getName()).log(Level.SEVERE, "Error cargando la configuracion", ex);
            configuracion.setMaxPrestamosAlumno(3);
            configuracion.setMaxPrestamosProfesor(5);
            configuracion.setDiasPrestamoAlumno(7);
            configuracion.setDiasPrestamoProfesor(14);
            configuracion.setMoraDiaria(1.50);
            configuracion.setAnioAplicacion(java.time.Year.now().getValue());
            this.desconectar();
            return null;
        }
    }

    // Actualizar o guardar la configuracion del anio actual
    public int guardarConfiguracion(Configuracion config) {
        String sql = "UPDATE configuracion SET " +
                "max_prestamos_alumno = ?, " +
                "max_prestamos_profesor = ?, " +
                "dias_prestamo_alumno = ?, " +
                "dias_prestamo_profesor = ?, " +
                "mora_diaria = ? " +
                "WHERE anio_aplicacion = YEAR(CURDATE())";

        try {
            this.conectar();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, config.getMaxPrestamosAlumno());
            ps.setInt(2, config.getMaxPrestamosProfesor());
            ps.setInt(3, config.getDiasPrestamoAlumno());
            ps.setInt(4, config.getDiasPrestamoProfesor());
            ps.setDouble(5, config.getMoraDiaria());

            int filas = ps.executeUpdate();

            // Si no existe registro para este año, lo insertamos
            if (filas == 0) {
                String insert = "INSERT INTO configuracion " +
                        "(max_prestamos_alumno, max_prestamos_profesor, " +
                        "dias_prestamo_alumno, dias_prestamo_profesor, " +
                        "mora_diaria, anio_aplicacion) " +
                        "VALUES (?, ?, ?, ?, ?, YEAR(CURDATE()))";
                ps = conexion.prepareStatement(insert);
                ps.setInt(1, config.getMaxPrestamosAlumno());
                ps.setInt(2, config.getMaxPrestamosProfesor());
                ps.setInt(3, config.getDiasPrestamoAlumno());
                ps.setInt(4, config.getDiasPrestamoProfesor());
                ps.setDouble(5, config.getMoraDiaria());
                return ps.executeUpdate();
            }

            return filas;

        } catch (SQLException ex) {
            Logger.getLogger(ConfiguracionModel.class.getName())
                    .log(Level.SEVERE, "Error al actualizar configuración", ex);
            return 0;
        } finally {
            this.desconectar();
        }
    }
}
