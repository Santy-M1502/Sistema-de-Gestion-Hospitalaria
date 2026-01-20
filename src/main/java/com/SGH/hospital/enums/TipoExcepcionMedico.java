package com.SGH.hospital.enums;

public enum TipoExcepcionMedico {

    DIA_NO_LABORABLE("El medico hoy no trabaja"),
    VACACIONES("El medico tomo vacaciones"),
    ENFERMEDAD("El medico esta enfermo"),
    OTRO("Por otras razones el medico no esta disponible");

    private final String descripcion;

    TipoExcepcionMedico(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescription(){
        return descripcion;
    }
}
