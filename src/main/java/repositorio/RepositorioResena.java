package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;



import dao.Conexion;
import entidades.Usuario;
import objetosFront.ResenaConComentario;
import entidades.Resena;
import entidades.Capitulo;
import entidades.Comentario;



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
    
    
    
    public boolean eliminarComentario(int id_usuario, int id_resena, String contenido) {
	    boolean eliminado = false;

	    String sql = "DELETE FROM comentario WHERE id_usuario = ? AND id_resena = ? AND contenido = ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setInt(1, id_usuario);
	        ps.setInt(2, id_resena);
	        ps.setString(3, contenido);

	        eliminado = ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        System.out.println("erroe al eliminar comentario: " + e.getMessage());
	    }

	    return eliminado;
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

    
    
    
    public List<Resena> obtenerResenasPorCapitulo(int idCapitulo){
    	
    	List<Resena> listaResenas = new ArrayList<>();
    	
    	String sql = "SELECT * from resena WHERE id_capitulo = ?";
    	
    	try (Connection con = Conexion.conectar();
    		PreparedStatement ps = con.prepareStatement(sql)){
    		
    		ps.setInt(1, idCapitulo); 
    		
    		try(ResultSet rs = ps.executeQuery();){
    			
    			while(rs.next()) {
    				Resena r = new Resena(
    						 rs.getInt("id"),
    	                     rs.getString("contenido"), 
    	                     rs.getTimestamp("fecha_creacion").toLocalDateTime(),
    	                     rs.getInt("id_capitulo"),
    	                     rs.getInt("id_usuario")
    						);
    				listaResenas.add(r);
    			}
    		}
    	}
    	catch (Exception e) {
            System.out.println("Error al obtener las resenas: " + e.getMessage()); 
        }
        return listaResenas;
    }
    
    
    
   public List<Comentario> obtenerComentariosPorResena(int idResena){
    	
    	List<Comentario> listaComentarios = new ArrayList<>();
    	
    	String sql = "SELECT * from comentario WHERE id_resena = ?";
    	
    	try (Connection con = Conexion.conectar();
    		PreparedStatement ps = con.prepareStatement(sql)){
    		
    		ps.setInt(1, idResena); 
    		
    		try(ResultSet rs = ps.executeQuery();){
    			
    			while(rs.next()) {
    				Comentario r = new Comentario(
    						 rs.getInt("id"),
    	                     rs.getString("contenido"), 
    	                     rs.getTimestamp("fecha_creacion").toLocalDateTime(),
    	                     rs.getInt("id_usuario"),
    	                     rs.getInt("id_resena")
    						);
    				listaComentarios.add(r);
    			}
    		}
    	}
    	catch (Exception e) {
            System.out.println("Error al obtener los comentarios: " + e.getMessage()); 
        }
        return listaComentarios;
    }
   
   
   public String buscarNombreUsuario(int id) {
	   
	   Usuario usuario = null;
   	
       String sql = "SELECT * FROM usuario WHERE id = ?";
       
       try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
           
           ps.setInt(1, id);
           ResultSet rs = ps.executeQuery();
           
           if (rs.next()) {
               usuario = new Usuario(
                   rs.getInt("id"),
                   rs.getString("nombre"),
                   rs.getString("correo"),
                   rs.getString("contrasena"), 
                   rs.getInt("edad")
               );
           }
       } catch (Exception e) {
           System.out.println("Error al buscar usuario: " + e.getMessage());
       }
       return (usuario != null) ? usuario.getNombre() : "Usuario desconocido";
   }
   
   
   public boolean eliminarResena(int id_usuario, int id_capitulo, String contenido) {
	    boolean eliminado = false;

	    String sql = "DELETE FROM resena WHERE id_usuario = ? AND id_capitulo = ? AND contenido = ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setInt(1, id_usuario);
	        ps.setInt(2, id_capitulo);
	        ps.setString(3, contenido);

	        eliminado = ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        System.out.println("erroe al eliminar resena: " + e.getMessage());
	    }

	    return eliminado;
	}
   
   
   public boolean editarResena(int id_usuario, int id_capitulo, String contenidoAntiguo, String contenidoNuevo) {
	    boolean eliminado = false;

	    String sql = "UPDATE resena SET contenido = ? WHERE id_usuario = ? AND id_capitulo = ? AND contenido = ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, contenidoNuevo);
	        ps.setInt(2, id_usuario);
	        ps.setInt(3, id_capitulo);
	        ps.setString(4, contenidoAntiguo);

	        eliminado = ps.executeUpdate() > 0;
	        
	    } catch (Exception e) {
	        System.out.println("error al editar resena: " + e.getMessage());
	    }

	    return eliminado;
	}
   
   
   
   public boolean editarComentario(int id_usuario, int id_resena, String contenidoAntiguo, String contenidoNuevo) {
	    boolean eliminado = false;

	    String sql = "UPDATE comentario SET contenido = ? WHERE id_usuario = ? AND id_resena = ? AND contenido = ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, contenidoNuevo);
	        ps.setInt(2, id_usuario);
	        ps.setInt(3, id_resena);
	        ps.setString(4, contenidoAntiguo);

	        eliminado = ps.executeUpdate() > 0;
	        
	    } catch (Exception e) {
	        System.out.println("error al editar comentario: " + e.getMessage());
	    }

	    return eliminado;
	}
    
}
