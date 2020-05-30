package com.example.quiz.LogicaDeNegocio;

public class Curso {
    private int id;
    private String descripcion;
    private int creditos;

    public Curso(){

    }
    public Curso(int id, String descripcion, int creditos) {
        this.id = id;
        this.descripcion = descripcion;
        this.creditos = creditos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {
        return "Curso{" + "id=" + id + ", descripcion=" + descripcion + ", creditos=" + creditos + '}';
    }

}
