/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Logic.Curso;
import Logic.Estudiante;
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
public class ServicioCurso extends Servicio {

    private static final String LISTAR_CURSOS= "{?=call PA_listarProfesores()}";
    private static ServicioCurso uniqueInstance;
    public static ServicioCurso instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ServicioCurso();
        }
        return uniqueInstance;
    }

    public ServicioCurso() {
    }
    public List<Curso> listarEstudiantes() throws GlobalException, NoDataException { // No vincula aun el arreglo de cursos correctamente
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        List<Curso> cursos = new LinkedList<>();
        Curso curso = null;
        CallableStatement pstmt = null;
        try {
            pstmt = conexion.prepareCall(LISTAR_CURSOS);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                curso = new Curso();
                cursos.add(curso);
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
        if (cursos.isEmpty()) {
            throw new NoDataException("No hay datos");
        }
        return cursos;
    }
}
