package com.biblioteca.beans;

public class TipoDocumento {
    private int idTipoDoc;
    private String nombreTipo;
    private String descripcion;

    public TipoDocumento(int idTipoDoc, String nombreTipo) {
        this.idTipoDoc = idTipoDoc;
        this.nombreTipo = nombreTipo;
    }

    public TipoDocumento() {

    }

    public int getIdTipoDoc() { return idTipoDoc; }
    public void setIdTipoDoc(int idTipoDoc) { this.idTipoDoc = idTipoDoc; }

    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }

//    public String getDescripcion() { return descripcion; }
//    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreTipo;
    }
}