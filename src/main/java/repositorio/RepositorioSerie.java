package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.time.LocalTime;

import dao.Conexion;
import entidades.Serie;
import entidades.Temporada;
import entidades.Capitulo;
import entidades.Director;


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
	
	
	public Serie obtenerSerieporId(int id) {
	   
		
	    Serie serieMostrar = null;
	    String sql = "SELECT * FROM serie WHERE id = ?";
	    
	    try (
	        Connection con = Conexion.conectar();
	        PreparedStatement ps = con.prepareStatement(sql);
	    ) {
	        ps.setInt(1, id);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if(rs.next()) {
	                serieMostrar = new Serie(
	                    rs.getString("nombre"),
	                    rs.getInt("estreno"),
	                    rs.getString("sinopsis"),
	                    rs.getInt("id_genero"),
	                    rs.getInt("id_director"),
	                    rs.getString("imagen_url")
	                );
	            }
	        }
	        
	    } catch (Exception e) {
	        System.out.println("Error al obtener la serie: " + e.getMessage()); 
	    }
	    
	    return serieMostrar;    
	}
	
	
	public ArrayList<Temporada> obtenerTemporadasPorSerie(int idSerie){
		
		ArrayList<Temporada> lista = new ArrayList<>();
        String sql = "SELECT * FROM temporada WHERE id_serie = ?";
        
        try (
    	    Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);){
        	ps.setInt(1, idSerie);
        	
        	try(ResultSet rs = ps.executeQuery()){
        		while (rs.next()) {
                    Temporada temporadaObtenida = new Temporada( 
                    		
                    	rs.getInt("id"),
                        rs.getInt("numero"),
                        rs.getString("imagen_url"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
                    lista.add(temporadaObtenida);
        	    }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las series: " + e.getMessage()); 
        }
        return lista;
	}
        	
	
	
    public ArrayList<Capitulo> obtenerCapitulosPorTemporada(int idTemporada){
		
		ArrayList<Capitulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM capitulo WHERE id_temporada = ?";
        
        try (
    	    Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql);){
        	ps.setInt(1, idTemporada);
        	
        	try(ResultSet rs = ps.executeQuery()){
        		while (rs.next()) {
        			
        			Time tiempoSQL = rs.getTime("duracion");
        			LocalTime duracion;

        			if (tiempoSQL != null) {  
        			    duracion = tiempoSQL.toLocalTime();  
        			} else {  //
        			    duracion = LocalTime.of(0, 0, 0);  
        			} 
        			
                    Capitulo capituloObtenido = new Capitulo( 
                    		
                    	rs.getInt("id"),
                    	rs.getString("titulo"),
                        rs.getInt("numero"),
                        duracion,
                        rs.getInt("id_temporada")
                );
                    lista.add(capituloObtenido);
        	    }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las series: " + e.getMessage()); 
        }
        return lista;
	}
    
    
    public boolean agregarFavorita(int idUsuario, int idSerie) {
    	 

	    String sql = "INSERT INTO favorita (id_usuario, id_serie) VALUES (?, ?)";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idUsuario);
	        ps.setInt(2, idSerie);
	        ps.executeUpdate();
	        return true;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
 
 
     public int contarFavoritas(int idUsuario) {
    	 
    	 
	    String sql = "SELECT COUNT(*) FROM favorito WHERE id_usuario = ?";
	    int total = 0;

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idUsuario);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            total = rs.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return total;
	}
     
     
     public Director buscarDirectorPorId(int idDirector) {
 		
 	    Director director = null;
 		String sql = "SELECT * from director WHERE id = ?";
 		
         try(Connection con = Conexion.conectar();
         	PreparedStatement ps = con.prepareStatement(sql);

         	){ 
 			ps.setInt(1, idDirector);
 			ResultSet rs = ps.executeQuery();
 			
 			if (rs.next()) { 
 				
 				 director = new Director(
 						rs.getInt("id"),
 						rs.getString("nombre"),
 						rs.getString("biografia")
 						 
 						 );	
 	        }
         	
         }catch (Exception e) {
             System.out.println("ese director no existe: " + e.getMessage());
         }
         
		 return director;
 	}
	
	
}
