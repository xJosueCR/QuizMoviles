/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Controllers.Control;
import Logic.Curso;
import Logic.Estudiante;
import Model.Model;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Josue
 */
//@WebServlet(name = "servletCursos", urlPatterns = {"/servletCursos"})
public class servletCursos extends HttpServlet {


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            Model dm1 = Model.instance();
            Control dm = new Control(dm1);
            List<Curso> list = dm.listarCursos();
            String json = new Gson().toJson(dm.listarCursos());
            response.setContentType("aplication/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(json);
        
    }   
    
}
