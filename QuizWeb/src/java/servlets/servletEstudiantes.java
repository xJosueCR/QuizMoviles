/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Controllers.Control;
import Logic.Estudiante;
import Model.Model;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Josue
 */
@WebServlet(name = "servletEstudiantes", urlPatterns = {"/servletEstudiantes"})
public class servletEstudiantes extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Model dm1 = Model.instance();
        Control dm = new Control(dm1);

        String json = new Gson().toJson(dm.listarProfesores());
        response.setContentType("aplication/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println(json);
        response.getWriter().println(json);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Model dm1 = Model.instance();
        Control dm = new Control(dm1);
        String estudiante = request.getParameter("estudiante");
        if (estudiante != null) {
            Estudiante p = new Gson().fromJson(estudiante, Estudiante.class);
            dm.insertarProfesor(p);
            String json = new Gson().toJson("insertado");
            response.getWriter().println(json);
            dm.insertarProfesor(p);
        } else {
            String json = new Gson().toJson("no insertado");
            response.getWriter().println(json);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Model dm1 = Model.instance();
        Control dm = new Control(dm1);
        String estudiante = request.getParameter("estudiante");
        if (estudiante != null) {
            Estudiante p = new Gson().fromJson(estudiante, Estudiante.class);
            dm.modificarProfesor(p);
            String json = new Gson().toJson("actualizado");
            response.getWriter().println(json);
        } else {
            String json = new Gson().toJson("no insertado");
            response.getWriter().println(json);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int x = Integer.parseInt(request.getParameter("x"));
        Model dm1 = Model.instance();
        Control dm = new Control(dm1);
            
        int result = dm.eliminarProfesor(x);
        String json = new Gson().toJson(result);
        response.getWriter().println(json);
    }
}
