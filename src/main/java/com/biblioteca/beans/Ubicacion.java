package com.biblioteca.beans;

public class Ubicacion {
    private int idUbicacion;
    private String edificio;
    private String piso;
    private String seccion;
    private String estante;
    private String descripcion;

    public Ubicacion(int idUbicacion, String edificio, String piso, String seccion, String estante) {
        this.idUbicacion = idUbicacion;
        this.edificio = edificio;
        this.piso = piso;
        this.seccion = seccion;
        this.estante = estante;
    }

    public Ubicacion() {

    }

    public int getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(int idUbicacion) { this.idUbicacion = idUbicacion; }

    public String getEdificio() { return edificio; }
    public void setEdificio(String edificio) { this.edificio = edificio; }

    public String getPiso() { return piso; }
    public void setPiso(String piso) { this.piso = piso; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }

    public String getEstante() { return estante; }
    public void setEstante(String estante) { this.estante = estante; }

//    public String getDescripcion() { return descripcion; }
//    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacionCompleta() {
        return edificio + " - " + piso + " - " + seccion + " - " + estante;
    }

    @Override
    public String toString() {
        return getUbicacionCompleta();
    }
}