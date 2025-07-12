package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dao.Conexion;
import entidades.Serie;

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
	
public boolean agregarGenero(Serie serie) {
		
		boolean insertado = false;
		
		String insertarSerie = "INSERT INTO genero (nombre) VALUES (?)";
		
		try(Connection con = Conexion.conectar();){
			
			try(PreparedStatement ps = con.prepareStatement(insertarSerie);){
			    ps.setString(1, serie.getNombre());
			    ps.setInt(2, serie.getEstreno());
			    ps.setString(3, serie.getSinopsis());
			    ps.setInt(4, serie.getIdGenero());
			    ps.setString(5, serie.getimagenUrl());
			    ps.setInt(6, serie.getIdDirector());
			    
			    insertado = ps.executeUpdate() >0;
			}
			
		} catch (Exception e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
		return insertado;
		
	}
	

}
