package com.example.lab09_10.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Estudiante implements Serializable {
    private int id;
    private String cedula;
    private String nombre;
    private String apellidos;
    private int edad;
    private int user;
    private List<Curso> cursosEstudiante;

    public Estudiante() {
        this.id = 0;
        this.cedula = "";
        this.nombre = "";
        this.apellidos = "";
        this.edad = 0;
        this.cursosEstudiante = new ArrayList<>();
        this.user = 0;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Estudiante(int id, String cedula, String nombre, String apellidos, int edad, List<Curso> cursosEstudiante, int user) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.cursosEstudiante = cursosEstudiante;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
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

    public List<Curso> getCursosEstudiante() {
        return cursosEstudiante;
    }

    public void setCursosEstudiante(List<Curso> cursosEstudiante) {
        this.cursosEstudiante = cursosEstudiante;
    }
}
