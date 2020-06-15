/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Logic.Curso;
import Logic.Estudiante;
import Logic.Usuario;
import Model.InterfaceModel;
import java.util.List;

/**
 *
 * @author Josue
 */
public class Control {

    protected InterfaceModel model;

    public Control() {

    }

    public Control(InterfaceModel model) {
        this.model = model;
    }

    public int insertarProfesor(Estudiante estudiante) {
        try {
            this.model.insertarEstudiante(estudiante);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public List<Estudiante> listarProfesores() {
        try {
            return this.model.listarEstudiantes();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public Estudiante getEstudiante(int user) {
        try {
            return this.model.getEstudiante(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public int eliminarProfesor(int id) {
        try {
            this.model.eliminarProfesor(id);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int modificarProfesor(Estudiante estudiante) {
        try {
            this.model.modificarEstudiante(estudiante);
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public List<Curso> listarCursos() {
        try {
            return this.model.listarCursos();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Usuario getUsuario(String username, String password) {
        try {
            return this.model.getUsuario(username, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean eliminarCursos(int id) {
        try {
            return this.model.eliminarCursos(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void matricularCursos(int estudiante, int[] values) {
        try {
            this.model.matricularCursos(estudiante, values);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
        public List<Curso> cursosEstudiante(int id) {
        try {
            return this.model.cursosEstudiante(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
