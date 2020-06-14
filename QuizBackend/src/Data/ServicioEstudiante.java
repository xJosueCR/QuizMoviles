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
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 *
 * @author Josue
 */
public class ServicioEstudiante extends Servicio {

    private static final String INSERTAR_ESTUDIANTE = "{call PA_insertarEstudiante(?,?,?,?,?)}";
    private static final String LISTAR_ESTUDIANTES = "{?=call PA_listarEstudiantes()}";
    private static final String ELIMINAR_ESTUDIANTE = "{call PA_eliminarEstudiante(?)}";
    private static final String MODIFICAR_ESTUDIANTE = "{call PA_actualizarEst(?,?,?,?,?,?)}";
    private static ServicioEstudiante uniqueInstance;

    public static ServicioEstudiante instance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ServicioEstudiante();
        }
        return uniqueInstance;
    }

    public ServicioEstudiante() {
    }

    public int[] getCursosID(Estudiante estudiante) {
        int a[] = {};
        for (int i = 0; i < estudiante.getCursos().size(); i++) {
            a[i] = estudiante.getCursos().get(i).getId();
        }
        return a;
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
            pstmt.setString(1, estudiante.getCedula());
            pstmt.setString(2, estudiante.getNombre());
            pstmt.setString(3, estudiante.getApellidos());
            pstmt.setInt(4, estudiante.getEdad());
            //int array[] = getCursosID(estudiante);
            int array[] ={1,2,3};
            ArrayDescriptor des = ArrayDescriptor.createDescriptor("PRACTICACLASE.ARRAY_TABLE", conexion);
            ARRAY array_to_pass = new ARRAY(des, conexion, array);
            pstmt.setArray(5, array_to_pass);
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
            pstmt = conexion.prepareCall(LISTAR_ESTUDIANTES);
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                estudiante = new Estudiante(rs.getInt("id"),
                        rs.getString("cedula"), rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("edad"), null);
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

    public void modificarEstudiante(Estudiante estudiante) throws GlobalException, NoDataException {
        try {
            conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = conexion.prepareStatement(MODIFICAR_ESTUDIANTE);
            pstmt.setInt(1, estudiante.getId());
            pstmt.setString(2, estudiante.getCedula());
            pstmt.setString(3, estudiante.getNombre());
            pstmt.setString(4, estudiante.getApellidos());
            pstmt.setInt(5, estudiante.getEdad());
            int array[] = getCursosID(estudiante);
            ArrayDescriptor des = ArrayDescriptor.createDescriptor("PRACTICACLASE.ARRAY_TABLE", conexion);
            ARRAY array_to_pass = new ARRAY(des, conexion, array);
            pstmt.setArray(6, array_to_pass);
            int resultado = pstmt.executeUpdate();

            if (resultado != 1) {
                throw new NoDataException("No se realizo la actualización");
            } else {
                System.out.println("\nModificación Satisfactoria!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
