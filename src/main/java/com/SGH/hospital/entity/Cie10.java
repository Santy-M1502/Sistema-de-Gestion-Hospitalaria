package com.SGH.hospital.entity;

import jakarta.persistence.*;

@Entity
public class Cie10 {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String descripcion;

    private Boolean activo = true;

    //////////////////////////////////////////////////////////
    ////==================== Constructores ====================
    /// //////////////////////////////////////////////////////////
    
    public Cie10() {}

    public Cie10(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    //////////////////////////////////////////////////////////
    /// ==================== Getters y Setters ====================
    //////////////////////////////////////////////////////////////
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}