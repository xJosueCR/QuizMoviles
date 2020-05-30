/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizbackend;

import Controllers.Control;
import Logic.Curso;
import Logic.Estudiante;
import Model.Model;
import java.util.List;

/**
 *
 * @author Josue
 */
public class QuizBackend {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Model dm1 = Model.instance();
        Control dm = new Control(dm1);
        //List<Estudiante> list = dm.listarProfesores();
        //List<Curso> cursos = dm.listarCursos();
        Estudiante e = new Estudiante(0,1,"josue","asas", 14, null);
        dm.insertarProfesor(e);
        String x;
        x = "";
    }

}
