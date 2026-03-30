package com.SGH.hospital.dto.especialidad;

import java.util.List;

public class EspecialidadResponse {

    private Long id;
    private String nombre;
    private Boolean activa;
    private List<MedicoResumen> medicos;

    public EspecialidadResponse() {}

    public EspecialidadResponse(Long id, String nombre, Boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.activa = activa;
    }

    public static class MedicoResumen {
        private Long id;
        private String nombre;
        private String apellido;
        private String matricula;

        public MedicoResumen() {}

        public MedicoResumen(Long id, String nombre, String apellido, String matricula) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.matricula = matricula;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public String getMatricula() { return matricula; }
        public void setMatricula(String matricula) { this.matricula = matricula; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public List<MedicoResumen> getMedicos() { return medicos; }
    public void setMedicos(List<MedicoResumen> medicos) { this.medicos = medicos; }
}