package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import excepciones.DataAccessException;


import dao.Conexion;
import entidades.Usuario;
import entidades.Resena;
import entidades.Comentario;




public class RepositorioResena {
	
	private static final Logger logger = LoggerFactory.getLogger(RepositorioResena.class);
	
	

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
    
    
    public boolean guardarPuntuacionSerie(int idUsuario, int idSerie, double puntuacion) {
		
		boolean guardada = false;
		
		String sql = """
	            INSERT INTO puntuacionSerie (id_usuario, id_serie, puntuacion)
	            VALUES (?, ?, ?)
	            ON DUPLICATE KEY UPDATE puntuacion = VALUES(puntuacion), fecha = CURRENT_TIMESTAMP
	        """;
		
		try(Connection con = Conexion.conectar();)
		    {try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setInt(1, idUsuario);
	
			    ps.setInt(2, idSerie);
			    ps.setDouble(3, puntuacion);  
		    
		    guardada = ps.executeUpdate() >0;
		}
		
	} catch (SQLException ee) {
	    ee.printStackTrace(); 
	    return false;
	}
	return guardada;
		
	}
    
    
    
    public boolean guardarPuntuacionCapitulo(int idUsuario, int idCapitulo, double puntuacion) {
		
		boolean guardada = false;
		
		String sql = """
	            INSERT INTO puntuacionCapitulo (id_usuario, id_capitulo, puntuacion)
	            VALUES (?, ?, ?)
	            ON DUPLICATE KEY UPDATE puntuacion = VALUES(puntuacion), fecha = CURRENT_TIMESTAMP
	        """;
		
		try(Connection con = Conexion.conectar();)
		    {try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setInt(1, idUsuario);
	
			    ps.setInt(2, idCapitulo);
			    ps.setDouble(3, puntuacion);  
		    
		    guardada = ps.executeUpdate() >0;
		}
		
	} catch (SQLException ee) {
	    ee.printStackTrace(); 
	    return false;
	}
	return guardada;
		
	}
    
    
//    public double obtenerPromedioCapitulo(int idCapitulo) throws SQLException {
//        String sql = "SELECT AVG(puntuacion) AS promedio FROM puntuacion WHERE id_capitulo = ?";
//        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
//            stmt.setInt(1, idCapitulo);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getDouble("promedio");
//                }
//                return 0.0;
//            }
//        }
//    }
    
    
    public Optional<Double> obtenerPuntuacionSerieUsuario(int idUsuario, int idSerie) {
    	
        String sql = "SELECT puntuacion FROM puntuacionSerie WHERE id_usuario = ? AND id_serie = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.setInt(2, idSerie);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getDouble("puntuacion"));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error al obtener puntuación: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    
    
    public Optional<Double> obtenerPuntuacionCapituloUsuario(int idUsuario, int idCapitulo) {
    	
        String sql = "SELECT puntuacion FROM puntuacionCapitulo WHERE id_usuario = ? AND id_capitulo = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idUsuario);
            ps.setInt(2, idCapitulo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(rs.getDouble("puntuacion"));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error al obtener puntuación: " + e.getMessage());
        }
        return Optional.empty();
    }
    
    
    public boolean eliminarComentario(int id) {
	    boolean eliminado = false;

	    String sql = "DELETE FROM comentario WHERE id = ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setInt(1, id);
	      

	        eliminado = ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        System.out.println("error al eliminar comentario: " + e.getMessage());
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
    				
    				String nombreUsuario = buscarNombreUsuario(r.getIdUsuario());
                    r.setNombreUsuario(nombreUsuario);
                    
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
   
   
   public boolean eliminarResena(int id) {
	    boolean eliminado = false;

	    String sql = "DELETE FROM resena WHERE id = ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setInt(1, id);
	       

	        eliminado = ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        System.out.println("error al eliminar resena: " + e.getMessage());
	    }

	    return eliminado;
	}
   
   
   public boolean editarResena(int id, String contenidoNuevo) {
	    boolean eliminado = false;

	    String sql = "UPDATE resena SET contenido = ? WHERE id= ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, contenidoNuevo);
	        ps.setInt(2, id);
	        

	        eliminado = ps.executeUpdate() > 0;
	        
	    } catch (Exception e) {
	        System.out.println("error al editar resena: " + e.getMessage());
	    }

	    return eliminado;
	}
   
   
   
   public boolean editarComentario(int id, String contenidoNuevo) {
	    boolean eliminado = false;

	    String sql = "UPDATE comentario SET contenido = ? WHERE id= ?";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, contenidoNuevo);
	        ps.setInt(2, id);
	        

	        eliminado = ps.executeUpdate() > 0;
	        
	    } catch (Exception e) {
	        System.out.println("error al editar comentario: " + e.getMessage());
	    }

	    return eliminado;
	}
   
   
   public int buscarIdUsuarioPorResena(int id_resena) {
	    int id = 0;

	    String sql = "SELECT id_usuario from resena WHERE id = ? ";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, id_resena);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) { 
 				id = rs.getInt("id_usuario");
	        }
	        
	    } catch (Exception e) {
	        System.out.println("error al buscar el id del usuario: " + e.getMessage());
	    }

	    return id;
	}
   
   
   public int buscarIdUsuarioPorComentario(int id_comentario) {
	    int id = 0;

	    String sql = "SELECT id_usuario from comentario WHERE id = ? ";

	    try (Connection con = Conexion.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, id_comentario);
	        ResultSet rs = ps.executeQuery();
	        
	        if (rs.next()) { 
				id = rs.getInt("id_usuario");
	        }
	        
	    } catch (Exception e) {
	        System.out.println("error al buscar el id del usuario: " + e.getMessage());
	    }

	    return id;
	}
   
   
   public int contarCapitulosFavoritos(int idUsuario) {
	   
	   String sql = "SELECT COUNT(*) FROM favoritO WHERE id_usuario = ?";
	   
	   int total = 0;
	   
	   try(Connection con = Conexion.conectar();
			   PreparedStatement ps = con.prepareStatement(sql)){
		   
           ps.setInt(1, idUsuario); 
           ResultSet rs = ps.executeQuery();
           
           if (rs.next()) {
	            total = rs.getInt(1);
	        }
           
	   } catch (Exception e) {
	        System.out.println("error al buscar favoritos: " + e.getMessage());
	    }
	   
	   return total;
   }
   
   
   public boolean agregarFavorito(int idUsuario, int idCapitulo) {
   	
   	
       String sqlVerificarUsuario = "SELECT 1 FROM usuario WHERE id = ?";
       String sqlVerificarCapitulo = "SELECT 1 FROM capitulo WHERE id = ?";
       String sqlInsertar = "INSERT INTO favorito (id_usuario, id_capitulo) VALUES (?, ?)";

       try (Connection con = Conexion.conectar()) {

           try (PreparedStatement psUsuario = con.prepareStatement(sqlVerificarUsuario)) {
               psUsuario.setInt(1, idUsuario);
               if (!psUsuario.executeQuery().next()) {
                   System.out.println("Usuario no encontrado");
                   return false;
               }
           }

           try (PreparedStatement psCapitulo = con.prepareStatement(sqlVerificarCapitulo)) {
               psCapitulo.setInt(1, idCapitulo);
               if (!psCapitulo.executeQuery().next()) {
                   System.out.println("Capitulo no encontrado");
                   return false;
               }
           }

           try (PreparedStatement psInsertar = con.prepareStatement(sqlInsertar)) {
               psInsertar.setInt(1, idUsuario);
               psInsertar.setInt(2, idCapitulo);
               psInsertar.executeUpdate();
               return true;
           }

       } catch (SQLException e) {
           e.printStackTrace();
           return false;
       }
   }
   
    
}
