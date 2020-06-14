/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Controllers.Control;
import Logic.Usuario;
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
@WebServlet(name = "servletUsuario", urlPatterns = {"/servletUsuario"})
public class servletUsuario extends HttpServlet {

    void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Model dm1 = Model.instance();
        Control dm = new Control(dm1);
         Gson gson = new Gson();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(!username.isEmpty()&&!password.isEmpty()){
                Usuario u =  dm.getUsuario(username, password);
                PrintWriter out = response.getWriter();
                out.write(gson.toJson(u));
        }

    }

}
