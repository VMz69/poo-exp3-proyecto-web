package com.biblioteca.beans;

public class EjemplarFactory {

    public static Ejemplar crearEjemplar(int idTipoDocumento) {
        switch (idTipoDocumento) {
            case 1:
                return new Libro();
            case 2:
                return new Tesis();
            case 3:
                return new Revista();
            case 4:
                return new CD();
            case 5:
                return new DVD();
            case 6:
                return new Informe();
            case 7:
                return new Manual();
            default:
                throw new IllegalArgumentException("Tipo de documento desconocido: " + idTipoDocumento);
        }
    }

    public static Ejemplar crearEjemplar(String tipoDocumento) {
        if (tipoDocumento == null || tipoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo o vacío");
        }

        String tipo = tipoDocumento.toUpperCase().trim();

        switch (tipo) {
            case "LIBRO":
                return new Libro();

            case "TESIS":
                return new Tesis();

            case "REVISTA":
                return new Revista();

            case "CD":
                return new CD();

            case "DVD":
                return new DVD();

            case "INFORME":
                return new Informe();

            case "MANUAL":
                return new Manual();

            default:
                // Para tipos no especificados, crear un libro genérico
                return new Libro();
        }
    }

    public static Ejemplar crearEjemplar(TipoDocumento tipoDocumento) {
        if (tipoDocumento == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        }
        return crearEjemplar(tipoDocumento.getNombreTipo());
    }
}
