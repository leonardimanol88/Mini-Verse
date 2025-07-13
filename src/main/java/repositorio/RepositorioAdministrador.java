package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.Conexion;
import entidades.Serie;
import entidades.Genero;

public class RepositorioAdministrador {

	
	
	public boolean agregarSerie(Serie serie) {
		
		boolean insertado = false;
		
		String insertarSerie = "INSERT INTO serie (nombre, estreno, sinopsis, id_genero, id_director, imagen_url) VALUES (?, ?, ?, ?, ?, ?)";
		
		try(Connection con = Conexion.conectar();){ 
			
			try(PreparedStatement ps = con.prepareStatement(insertarSerie);){
			    ps.setString(1, serie.getNombre());
			    ps.setInt(2, serie.getEstreno());
			    ps.setString(3, serie.getSinopsis());
			    ps.setInt(4, serie.getIdGenero());
			    ps.setInt(5, serie.getIdDirector());
			    ps.setString(6, serie.getimagenUrl());

			    
			    insertado = ps.executeUpdate() >0;
			}
			
		} catch (SQLException ee) {
		    ee.printStackTrace(); 
		    return false;
		}
		return insertado;
		
	}
	
	
	public boolean eliminarSerie(String nombre) {
		
		boolean eliminada = false;
		String sql = "DELETE from serie WHERE nombre = ?";
		
		 try(Connection con = Conexion.conectar();
		     PreparedStatement ps = con.prepareStatement(sql);
         ){
			 
			ps.setString(1, nombre);
	    	eliminada = ps.executeUpdate() > 0;
			 
		 } catch (Exception e) {
	            System.out.println("error al eliminar: " + e.getMessage());
		 }
		 
		 return eliminada;
	}
	
	
	public int buscarIdDirectorporNombre(String nombre) {
		
		int id = 0;
		String sql = "SELECT * from director WHERE nombre = ?";
		
        try(Connection con = Conexion.conectar();
        	PreparedStatement ps = con.prepareStatement(sql);

        	){ 
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) { 
	            id = rs.getInt("id");
	        }
        	
        }catch (Exception e) {
            System.out.println("ese director no existe: " + e.getMessage());
        }
        
        return id;
	}
	
	
    public int buscarIdGeneroporNombre(String nombre) {
		
		int id = 0;
		String sql = "SELECT * from genero WHERE nombre = ?";
		
        try(Connection con = Conexion.conectar();
        	PreparedStatement ps = con.prepareStatement(sql);
        	
        	){ 
			ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
			
			if (rs.next()) { 
	            id = rs.getInt("id");
	        }
        	
        }catch (Exception e) {
            System.out.println("ese genero no existe: " + e.getMessage());
        }
        
        return id;
	}
    
    public boolean existeSerieId(int id) {
    	
        String sql = "SELECT * FROM serie WHERE id = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setInt(1, id);
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar si la serie existe: " + e.getMessage());
            return false;
        }
       
    }
    
    
    public boolean existeSerie(String nombre) {
    	
        String sql = "SELECT * FROM serie WHERE nombre = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setString(1, nombre);
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar si la serie existe: " + e.getMessage());
            return false;
        }
       
    }
    
	
    public boolean agregarGenero(String nombre) {
		
		boolean insertado = false;
		
		String sql = "INSERT INTO genero (nombre) VALUES (?)";
		
		try(Connection con = Conexion.conectar();){
			
			try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setString(1, nombre);
			    insertado = ps.executeUpdate() >0;
			}
			
		} catch (Exception e) {
            System.out.println("Error al guardar genero: " + e.getMessage());
        }
		return insertado;
		
	}
    
    
    public boolean existeGenero(String nombre) {
    	
        String sql = "SELECT * FROM genero WHERE nombre = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setString(1, nombre);
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar el genero existe: " + e.getMessage());
            return false;
        }
       
    }
    
    
    public boolean agregarDirector(String nombre, String biografia) {
		
		boolean insertado = false;
		
		String sql = "INSERT INTO director (nombre, biografia) VALUES (?, ?)";
		
		try(Connection con = Conexion.conectar();){
			
			try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setString(1, nombre);
			    ps.setString(2, biografia);
			    insertado = ps.executeUpdate() >0;
			}
			
		} catch (Exception e) {
            System.out.println("Error al guardar director: " + e.getMessage());
        }
		return insertado;
		
	}
    
    
    public boolean existeDirector(String nombre) {
    	
        String sql = "SELECT * FROM director WHERE nombre = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setString(1, nombre);
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar el director existe: " + e.getMessage());
            return false;
        }
       
    }
	

}
