package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Beneficiary implements Serializable {

    @SerializedName("CURP")
    private String curp;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("apellido_paterno")
    private String apellidoPaterno;
    @SerializedName("apellido_materno")
    private String apellidoMaterno;
    @SerializedName("sexo")
    private String sexo;
    @SerializedName("calle")
    private String calle;
    @SerializedName("numero_int")
    private String numeroInt;
    @SerializedName("numero_ext")
    private String numeroExt;
    @SerializedName("cp")
    private String codigoPostal;
    @SerializedName("colonia")
    private String Colonia;
    @SerializedName("municipio")
    private String municipio;
    @SerializedName("estado")
    private String estado;
    @SerializedName("telefono")
    private String telefono;
    @SerializedName("celular")
    private String celular;

    public Beneficiary() {
    }

    public Beneficiary(String curp, String nombre) {
        this.curp = curp;
        this.nombre = nombre;
    }

    public Beneficiary(String curp, String nombre, String apellidoPaterno, String apellidoMaterno, String sexo, String calle, String numeroInt, String numeroExt, String codigoPostal, String colonia, String municipio, String estado, String telefono, String celular) {
        this.curp = curp;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.sexo = sexo;
        this.calle = calle;
        this.numeroInt = numeroInt;
        this.numeroExt = numeroExt;
        this.codigoPostal = codigoPostal;
        Colonia = colonia;
        this.municipio = municipio;
        this.estado = estado;
        this.telefono = telefono;
        this.celular = celular;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroInt() {
        return numeroInt;
    }

    public void setNumeroInt(String numeroInt) {
        this.numeroInt = numeroInt;
    }

    public String getNumeroExt() {
        return numeroExt;
    }

    public void setNumeroExt(String numeroExt) {
        this.numeroExt = numeroExt;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
                "curp='" + curp + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", sexo='" + sexo + '\'' +
                ", calle='" + calle + '\'' +
                ", numeroInt='" + numeroInt + '\'' +
                ", numeroExt='" + numeroExt + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", Colonia='" + Colonia + '\'' +
                ", municipio='" + municipio + '\'' +
                ", estado='" + estado + '\'' +
                ", telefono='" + telefono + '\'' +
                ", celular='" + celular + '\'' +
                '}';
    }
}
