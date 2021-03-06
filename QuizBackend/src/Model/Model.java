/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Data.ServicioCurso;
import Data.ServicioEstudiante;
import Logic.Curso;
import Logic.Estudiante;
import java.util.List;

/**
 *
 * @author Josue
 */
public class Model implements InterfaceModel{
    private final ServicioEstudiante servicioEstudiante;
    private final ServicioCurso servicioCurso;

    private static Model uniqueInstance;

    public static Model instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Model() {
            };
        }
        return uniqueInstance;
    }

    private Model() {
        servicioEstudiante = new ServicioEstudiante();
        servicioCurso = new ServicioCurso();

    }
    @Override
    public void insertarEstudiante(Estudiante estudiante) throws Exception {
       ServicioEstudiante.instance().insertarProfesor(estudiante);
    }

    @Override
    public List<Estudiante> listarEstudiantes() throws Exception {
        return ServicioEstudiante.instance().listarEstudiantes();
    }

    @Override
    public void eliminarProfesor(int id) throws Exception {
       ServicioEstudiante.instance().eliminarEstudiante(id);
    }

    @Override
    public void modificarEstudiante(Estudiante estudiante) throws Exception {
        ServicioEstudiante.instance().modificarEstudiante(estudiante);
    }

    @Override
    public List<Curso> listarCursos() throws Exception {
        return ServicioCurso.instance().listarCursos();
    }
}
