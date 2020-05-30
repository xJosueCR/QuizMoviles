package com.example.quiz.LogicaDeNegocio;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private int id;
    private String nombre;
    private String apellidos;
    private int edad;
    private String cedula;
    private List<Curso> cursos;

    public Estudiante(){
        this.id = 0;
        this.nombre = "";
        this.apellidos = "";
        this.edad = 0;
        this.cursos = new ArrayList<>();
    }
    public Estudiante(int id,  String cedula, String nombre, String apellidos, int edad, List<Curso> cursos) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.cursos = cursos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
}
