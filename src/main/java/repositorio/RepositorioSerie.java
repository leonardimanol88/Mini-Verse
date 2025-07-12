package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.Conexion;
import entidades.Serie;


public class RepositorioSerie {
	
	
	public ArrayList<Serie> obtenerSeries() { 
    	
        ArrayList<Serie> lista = new ArrayList<>();
        String sql = "SELECT * FROM serie";
        
        try (
    	    Connection con = Conexion.conectar(); //hacer el metodo conectar de la clase conexion en el objeto con
            PreparedStatement ps = con.prepareStatement(sql); 
            ResultSet rs = ps.executeQuery()) { //obtener el resultado al ejecutar la sentencia (select)
            while (rs.next()) { //con .next() avanza al siguiente elemento de la sentencia ejecutada 
                Serie serieObtenida = new Serie( 
                   
                    rs.getString("nombre"), 
                    rs.getInt("estreno"),
                    rs.getString("sinopsis"),
                    rs.getInt("id_genero"),
                    rs.getInt("id_director"),
                    rs.getString("imagen_url")
            );
                lista.add(serieObtenida);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las series: " + e.getMessage()); 
        }
        return lista;
    }
	
	
	public ArrayList<Serie> obtenerSeriesOrdenadas(String criterio) {
		
		ArrayList<Serie> listaOrdenada = new ArrayList<>();
		
		 if (!criterio.equals("nombre") && !criterio.equals("estreno")) {
		      criterio = "nombre"; 
		    }
		
		String sql = "SELECT * FROM serie ORDER BY "+ criterio +" ASC";
		 
		try (
				Connection con = Conexion.conectar();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
			){
			while (rs.next()) { 
                Serie serieObtenida = new Serie( 
                   
                    rs.getString("nombre"), 
                    rs.getInt("estreno"),
                    rs.getString("sinopsis"),
                    rs.getInt("id_genero"),
                    rs.getInt("id_director"),
                    rs.getString("imagen_url")
            );
                listaOrdenada.add(serieObtenida);
            }
		} catch (Exception e) {
            System.out.println("Error al obtener las series: " + e.getMessage()); 
        }
		 return listaOrdenada;	
	}
	
	
	
	

	

}
