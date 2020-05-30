/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Logic.Curso;
import Logic.Estudiante;
import java.util.List;

/**
 *
 * @author Josue
 */
public interface InterfaceModel {
    //-------------------Estudiante-------------------------- 

    public void insertarEstudiante(Estudiante estudiante) throws Exception;

    public List<Estudiante> listarEstudiantes() throws Exception;

    public void eliminarProfesor(int id) throws Exception;

    public void modificarEstudiante(Estudiante estudiante) throws Exception;
    
      //-------------------Cursos-------------------------- 
    public List<Curso> listarCursos() throws Exception;
}
