/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Logic.Curso;
import Logic.Usuario;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import oracle.jdbc.internal.OracleTypes;

/**
 *
 * @author Josue
 */
public class ServicioUsuario extends Servicio {

    private static final String GET_USUARIO = "{?=call PA_getUsuario(?,?)}";
    private static ServicioUsuario uniqueInstance;

    public static ServicioUsuario instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ServicioUsuario();
        }
        return uniqueInstance;
    }

    public ServicioUsuario() {
    }
     public Usuario getUsuario(String username, String password) throws GlobalException, NoDataException { // No vincula aun el arreglo de cursos correctamente
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace(); 
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Usuario usuario = null;
        CallableStatement pstmt = null;
        try {
            pstmt = conexion.prepareCall(GET_USUARIO);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
           
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while(rs.next()){
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
        return usuario;
    }
}
