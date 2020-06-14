/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Logic.Curso;
import Logic.Estudiante;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 *
 * @author Josue
 */
public class ServicioCurso extends Servicio {

    private static final String LISTAR_CURSOS = "{?=call PA_cursosList()}";
    private static final String DELETE_CURSOS = "{call PA_eliminarCursosEstudiante(?)}";
    private static final String MATRICULAR_CURSOS = "{call PA_matricularCursos(?,?)}";
    private static ServicioCurso uniqueInstance;

    public static ServicioCurso instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ServicioCurso();
        }
        return uniqueInstance;
    }

    public ServicioCurso() {
    }

    public List<Curso> listarCursos() throws GlobalException, NoDataException { // No vincula aun el arreglo de cursos correctamente
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
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
                curso = new Curso(rs.getInt("id"), rs.getString("nombre"), rs.getInt("creditos"));
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

    public boolean eliminarCursos(int id) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = conexion.prepareStatement(DELETE_CURSOS);
            pstmt.setInt(1, id);

            int resultado = pstmt.executeUpdate();

            if (resultado != 1) {
                throw new NoDataException("No se realizo el borrado");
            } else {
                System.out.println("\nEliminación Satisfactoria!");
                return true;
            }
        } catch (SQLException e) {
            throw new GlobalException("Sentencia no valida");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
    public void matricularCursos(int estudiante, int[]values) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(MATRICULAR_CURSOS);
            pstmt.setInt(1, estudiante);
            ArrayDescriptor des = ArrayDescriptor.createDescriptor("PRACTICACLASE.ARRAY_TABLE", conexion);
            ARRAY array_to_pass = new ARRAY(des, conexion, values);
            pstmt.setArray(2, array_to_pass);
            boolean resultado = pstmt.execute();
            if (resultado == true) {
                throw new NoDataException("No se realizo la insercion");
            }
            int x = 122;
        } catch (SQLException e) {
            throw new GlobalException("Llave duplicada");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos invalidos o nulos");
            }
        }
    }
}
