/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Servicio {

    protected Connection conexion = null;
    
    public Servicio() {

        
    }
    
    
    protected void conectar() throws SQLException,ClassNotFoundException 
    {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try{
            conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","lab01_DBA","lab01_DBA_DB");
            }catch(SQLException e){
                conexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","lab01_DBA","lab01_DBA_DB");
            }
        
    }
    
    protected void desconectar() throws SQLException{
        if(!conexion.isClosed())
        {
            conexion.close();
        }
    }

}