package dao;

import java.sql.Connection;
import java.sql.DriverManager;


public class Conexion {
	
    private static final String URL = "jdbc:mariadb://18.214.244.96:3306/miniverse"; 
    private static final String USUARIO = "leoima";
    private static final String CLAVE = "mar55"; 

    public static Connection conectar() {
    	
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } 
        catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
        
    }
}
