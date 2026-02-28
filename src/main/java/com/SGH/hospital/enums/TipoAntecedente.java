package com.SGH.hospital.enums;

public enum TipoAntecedente {
    
    FAMILIAR("algun familiar tuvo esta enfermedad"),
    PERSONAL("el paciente tuvo esta enfermedad "),
    QUIRURGICO("el paciente se opero por esta enfermedad"),
    OTRO("el paciente tiene esta enfermedad por otras razones");

    private final String descripcion;

    TipoAntecedente(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescription(){
        return descripcion;
    }
}
