/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import Logic.Estudiante;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import oracle.jdbc.internal.OracleTypes;

/**
 *
 * @author Josue
 */
 public class ServicioEstudiante extends Servicio {

    private static final String INSERTAR_ESTUDIANTE = "{call PA_insertarProfesor(?,?,?,?)}";
    private static final String BUSCAR_ESTUDIANTE = "{?=call PA_buscarProfesor(?)}";
    private static final String LISTAR_ESTUDIANTE = "{?=call PA_listarProfesores()}";
    private static final String ELIMINAR_ESTUDIANTE = "{call PA_eliminarProfesor(?)}";
    private static final String MODIFICAR_ESTUDIANTE = "{call PA_modificarProfesor(?,?,?,?,?)}";
    private static ServicioEstudiante uniqueInstance;

    public static ServicioEstudiante instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ServicioEstudiante();
        }
        return uniqueInstance;
    }

    public ServicioEstudiante() {
    }

    public void insertarProfesor(Estudiante estudiante) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;

        try {
            pstmt = conexion.prepareCall(INSERTAR_ESTUDIANTE);
            pstmt.setInt(1, estudiante.getCedula());
            pstmt.setString(2, estudiante.getNombre());
            pstmt.setString(3, estudiante.getApellidos());
            pstmt.setInt(4, estudiante.getEdad());
            boolean resultado = pstmt.execute();
            if (resultado == true) {
                throw new NoDataException("No se realizo la insercion");
            }
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
    public List<Estudiante> listarEstudiantes() throws GlobalException, NoDataException { // No vincula aun el arreglo de cursos correctamente
        try {
            conectar();
        } catch (ClassNotFoundException ex) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        List<Estudiante> estudiantes = new LinkedList<>();
        Estudiante estudiante = null;
        CallableStatement pstmt = null;
        try {
            pstmt = conexion.prepareCall(LISTAR_ESTUDIANTE);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                estudiante = new Estudiante();
                estudiantes.add(estudiante);
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
        if (estudiantes.isEmpty()) {
            throw new NoDataException("No hay datos");
        }
        return estudiantes;
    }
     public void eliminarEstudiante(int id) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = conexion.prepareStatement(ELIMINAR_ESTUDIANTE);
            pstmt.setInt(1, id);

            int resultado = pstmt.executeUpdate();

            if (resultado != 1) {
                throw new NoDataException("No se realizo el borrado");
            } else {
                System.out.println("\nEliminación Satisfactoria!");
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

}
