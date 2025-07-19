package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.time.LocalTime;



import dao.Conexion;
import entidades.Usuario;
import entidades.Resena;
import entidades.Capitulo;



public class RepositorioResena {
	
	

	public boolean agregarResena(int idUsuario, int idCapitulo, String contenido) {
		
		boolean insertado = false;
		
		String sql = "INSERT INTO resena (contenido, id_capitulo, id_usuario) VALUES (?, ?, ?)";
		
		try(Connection con = Conexion.conectar();)
		    {try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setString(1, contenido);
	
			    ps.setInt(2, idCapitulo);
			    ps.setInt(3, idUsuario);
			   
		    
		    insertado = ps.executeUpdate() >0;
		}
		
	} catch (SQLException ee) {
	    ee.printStackTrace(); 
	    return false;
	}
	return insertado;
		
	}
	
	
	public boolean buscarResenaIgual(int idUsuario, String contenido) {
		
        boolean encontrado = false;
		
		String sql = "SELECT * FROM resena WHERE id_usuario = ? AND contenido = ?";
		
		try (Connection con = Conexion.conectar();){
			try( PreparedStatement ps = con.prepareStatement(sql);){
				ps.setInt(1, idUsuario);
				
			    ps.setString(2, contenido);
			    ResultSet rs = ps.executeQuery();
			    
			    if(rs.next()) {encontrado = true;}
	
			}
		}catch (SQLException ee) {
		    ee.printStackTrace(); 
		    return false;
		}
		return encontrado;
		
	}
	
	
    public boolean agregarComentario(int idUsuario, int idResena, String contenido) {
		
		boolean insertado = false;
		
		String sql = "INSERT INTO comentario (contenido, id_usuario, id_resena) VALUES (?, ?, ?)";
		
		try(Connection con = Conexion.conectar();)
		    {try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setString(1, contenido);
	
			    ps.setInt(2, idUsuario);
			    ps.setInt(3, idResena);
			   
		    
		    insertado = ps.executeUpdate() >0;
		}
		
	} catch (SQLException ee) {
	    ee.printStackTrace(); 
	    return false;
	}
	return insertado;
		
	}
    
    
    public boolean buscarComentarioIgual(int idUsuario, String contenido) {
		
        boolean encontrado = false;
		
		String sql = "SELECT * FROM comentario WHERE id_usuario = ? AND contenido = ?";
		
		try (Connection con = Conexion.conectar();){
			try( PreparedStatement ps = con.prepareStatement(sql);){
				ps.setInt(1, idUsuario);
				
			    ps.setString(2, contenido);
			    ResultSet rs = ps.executeQuery();
			    
			    if(rs.next()) {encontrado = true;}
	
			}
		}catch (SQLException ee) {
		    ee.printStackTrace(); 
		    return false;
		}
		return encontrado;
		
	}

    
    
    
    
    
}
