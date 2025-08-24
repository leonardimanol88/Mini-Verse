package repositorio;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt; 
import java.util.List;
import java.sql.Timestamp;


import dao.Conexion;
import entidades.Capitulo;
import entidades.Comentario;
import entidades.Genero;
import entidades.Resena;
import entidades.Serie;
import entidades.Usuario;
import objetosFront.CapituloNombreSerie;


public class RepositorioUsuario {

	

    public boolean agregarUsuario(Usuario usuario) { 
    	
    	boolean insertado = false;
    	
    	String hashContrasena = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        String sql = "INSERT INTO usuario (nombre, correo, contrasena, edad) VALUES (?, ?, ?, ?)";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, hashContrasena);
            ps.setInt(4, usuario.getEdad());
            
            insertado = ps.executeUpdate() >0;
        } catch (Exception e) {
            System.out.println("Error al guardar usuario: " + e.getMessage());
        }
        return insertado;
    }
    
    
    public boolean existeCorreo(String correo) {
        String stringConsulta = "SELECT id FROM usuario WHERE correo = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement Consulta = con.prepareStatement(stringConsulta)) {
            Consulta.setString(1, correo);
            ResultSet resultado = Consulta.executeQuery();
            return resultado.next(); 
            
        } catch (Exception e) {
            System.out.println("El correo ya esta registrado: " + e.getMessage());
        }
        return false;
    }
   
    
    public boolean eliminarUsuario(int id) {
    	
    	boolean eliminado = false;
    	
    	String sql = "DELETE FROM usuario WHERE id = ?";
    	
    	try(
    		Connection con = Conexion.conectar();
    		PreparedStatement ps = con.prepareStatement(sql);
    		){
    		
    		ps.setInt(1, id);
    		eliminado = ps.executeUpdate() > 0;
    		
        	}catch (Exception e) {
                System.out.println("Error al eliminar: " + e.getMessage());
            }
    	
    	return eliminado;
    	
    }
    
    
    public boolean actualizarContrasena(Usuario usuario, String nuevaContrasena) {////
    
    	
    	
    	String hashContrasena = BCrypt.hashpw(nuevaContrasena, BCrypt.gensalt());
    	boolean actualizada = false;
    	int id = usuario.getId();
    	
    	String sql = "UPDATE usuario SET contrasena = ? WHERE id = ? ";
    	
    	try(
    		Connection con = Conexion.conectar();
    		PreparedStatement ps = con.prepareStatement(sql);
            ){
    		ps.setString(1, hashContrasena);
    		ps.setInt(2, id);
    		actualizada = ps.executeUpdate() >0;
    		
    	}catch (Exception e) {
            System.out.println("Error, no se puede actualizar: " + e.getMessage());
        }
    	return actualizada;
    }
    
    
    public Usuario buscarUsuarioPorId(int id) {
    	
    	Usuario usuario = null;
    	
    	String sql = "SELECT * FROM usuario WHERE id = ?";
    	
    	try(
    		Connection con = Conexion.conectar();
    		PreparedStatement ps = con.prepareStatement(sql);
    		 		
    		){
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
    		
    	}catch (Exception e) {
            System.out.println("Error, usuario no encontrado: " + e.getMessage());
        }
    	
    	return usuario;
    }
    
    
    public Usuario buscarUsuarioPorCorreo(String correo) {
    	
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
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
        return usuario;
    }
    
    
    public Usuario buscarUsuarioPorCorreoyContrasena(String correo, String contrasena) {
    	
        Usuario usuario = null;
        String sql = "SELECT id, nombre, correo, contrasena, edad, rol FROM usuario WHERE correo = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            
            ResultSet rs = ps.executeQuery();
           
            
            if (rs.next()) {
                String hashAlmacenado = rs.getString("contrasena");
                
                if (BCrypt.checkpw(contrasena, hashAlmacenado)) { 
                   
                	
                	usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        hashAlmacenado, 
                        rs.getInt("edad")
                    );
                	usuario.setRol(rs.getString("rol"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return usuario;
    }
    
    
    public boolean existeUsuarioPorCorreo(String correo) {
    	
    	
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (Exception e) {
            System.out.println("error al verificar correo: " + e.getMessage());
        }
        
        
        
        return false;
    }

    
    
    public boolean agregarResena(int id_usuario, String contenido, int id_capitulo) { 
    	
    	boolean insertado = false;
    	
        String sql = "INSERT INTO resena (contenido, fecha_creacion, id_capitulo, id_usuario) VALUES (?, ?, ?, ?)"; 
        
        try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, contenido);
            LocalDateTime fechaCreacion = LocalDateTime.now();
            ps.setTimestamp(2, Timestamp.valueOf(fechaCreacion));
           
            ps.setInt(3, id_capitulo);
            ps.setInt(4, id_usuario);
            
            insertado = ps.executeUpdate() >0;
        } catch (Exception e) {
            System.out.println("Error al guardar resena: " + e.getMessage());
        }
        return insertado;
    }
    
    
     public ArrayList<Serie> obtenerSeriesporGenero(int idGenero) { 
    	
        ArrayList<Serie> lista = new ArrayList<>();
        String sql = "SELECT * FROM serie WHERE id_genero = ?";
        
        try (
    	    Connection con = Conexion.conectar(); 
            PreparedStatement ps = con.prepareStatement(sql);
            )
        	{ 
        	ps.setInt(1, idGenero);
        	ResultSet rs = ps.executeQuery();
                
            while (rs.next()) {
                Serie serieObtenida = new Serie( 
                		
                    rs.getInt("id"),
                    rs.getString("nombre"), 
                    rs.getInt("estreno"),
                    rs.getString("sinopsis"),
                    rs.getInt("id_genero"),
       
                    rs.getString("imagen_url"),
                    rs.getInt("id_director")
            );
                lista.add(serieObtenida);
            }
        } catch (Exception e) {
            System.out.println("Error al obtener las series: " + e.getMessage()); 
        }
        return lista;
    }
     
     
     
     public ArrayList<Genero> obtenerGeneros() { 
     	
         ArrayList<Genero> lista = new ArrayList<>();
         String sql = "SELECT * FROM genero";
         
         try (
     	    Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) { 
             while (rs.next()) { 
                 Genero generoObtenido = new Genero( 
                    
                     rs.getString("nombre")
             );
                 lista.add(generoObtenido);
             }
         } catch (Exception e) {
             System.out.println("Error al obtener los generos: " + e.getMessage()); 
         }
         return lista;
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
    
    
     
     public ArrayList<Serie> obtenerSeries() { 
     	
         ArrayList<Serie> lista = new ArrayList<>();
         String sql = "SELECT * FROM serie";
         
         try (
     	    Connection con = Conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
             while (rs.next()) { 
                 Serie serieObtenida = new Serie( 
                    
                     rs.getInt("id"),
                     rs.getString("nombre"), 
                     rs.getInt("estreno"),
                     rs.getString("sinopsis"),
                     rs.getInt("id_genero"),
                  
                     rs.getString("imagen_url"),
                     rs.getInt("id_director")
             );
                 lista.add(serieObtenida);
             }
         } catch (Exception e) {
             System.out.println("Error al obtener las series: " + e.getMessage()); 
         }
         return lista;
     }
     
     
     
     public ArrayList<Serie> obtenerSeriesFavoritas(int idUsuario) {
    	 
    	 
    	    ArrayList<Serie> favoritas = new ArrayList<>();
    	    
    	    String sql = "SELECT s.* FROM favorita f JOIN serie s ON f.id_serie = s.id WHERE f.id_usuario = ?";

    	    try (Connection con = Conexion.conectar();
    	         PreparedStatement ps = con.prepareStatement(sql)) {

    	        ps.setInt(1, idUsuario);
    	        ResultSet rs = ps.executeQuery();

    	        while (rs.next()) {
    	            Serie serie = new Serie(
    	            		rs.getInt("id"),
    	    	            rs.getString("nombre"),
    	    	            rs.getInt("estreno"),
    	    	            rs.getString("sinopsis"),
    	    	            rs.getInt("id_genero"),
    	    	            rs.getString("imagen_url"),
    	    	            rs.getInt("id_director")
    	            		);
    	            
    	            favoritas.add(serie);
    	        }

    	    } catch (Exception e) {
                System.out.println("Error al obtener las series favoritas: " + e.getMessage()); 
            }
            return favoritas;
    	}
     
     
     public ArrayList<Capitulo> obtenerCapitulosFavoritos(int idUsuario) {
    	 
    	 
 	    ArrayList<Capitulo> favoritos = new ArrayList<>();
 	    
 	    String sql = "SELECT c.* FROM favorito f JOIN capitulo c ON f.id_capitulo = c.id WHERE f.id_usuario = ?";

 	    try (Connection con = Conexion.conectar();
 	         PreparedStatement ps = con.prepareStatement(sql)) {

 	        ps.setInt(1, idUsuario);
 	        ResultSet rs = ps.executeQuery();

 	        while (rs.next()) {
 	        	
 	        	Time tiempoSQL = rs.getTime("duracion");
    			LocalTime duracion;

    			if (tiempoSQL != null) {  
    			    duracion = tiempoSQL.toLocalTime();  
    			} else {  //
    			    duracion = LocalTime.of(0, 0, 0);  
    			} 
 	        	
 	            Capitulo capi = new Capitulo(
 	            		rs.getInt("id"),
 	    	            rs.getString("titulo"),
 	    	            rs.getInt("numero"),
 	    	            duracion,
 	    	            rs.getInt("id_temporada"),
 	    	            rs.getString("sinopsis"),
 	    	            rs.getString("imagen_url")
 	    	            
 	            		);
 	            
 	            favoritos.add(capi);
 	        }

 	    } catch (Exception e) {
             System.out.println("Error al obtener los capitulos favoritos: " + e.getMessage()); 
         }
         return favoritos;
 	}
     
     
     
     public ArrayList<CapituloNombreSerie> obtenerCapitulosFavoritosconNombreSerie(int idUsuario) {
    	 
    	 
  	    ArrayList<CapituloNombreSerie> favoritos = new ArrayList<>();
  	    
  	    String sql = "SELECT "+
  	              "c.*, s.nombre as nombre_serie "
  	    		+ "FROM capitulo c "
  	    		+ "JOIN temporada t ON c.id_temporada = t.id "
  	    		+ "JOIN serie s ON t.id_serie = s.id "
  	    		+ "JOIN favorito f ON c.id = f.id_capitulo "
  	    		+ "WHERE f.id_usuario = ?";

  	    try (Connection con = Conexion.conectar();
  	         PreparedStatement ps = con.prepareStatement(sql)) {

  	        ps.setInt(1, idUsuario);
  	        ResultSet rs = ps.executeQuery();

  	        while (rs.next()) {
  	        	
  	        	Time tiempoSQL = rs.getTime("duracion");
     			LocalTime duracion;

     			if (tiempoSQL != null) {  
     			    duracion = tiempoSQL.toLocalTime();  
     			} else {  //
     			    duracion = LocalTime.of(0, 0, 0);  
     			} 
  	        	
  	            CapituloNombreSerie capi = new CapituloNombreSerie(
  	            		rs.getInt("id"),
  	    	            rs.getString("titulo"),
  	    	            rs.getInt("numero"),
  	    	            duracion,
  	    	            rs.getInt("id_temporada"),
  	    	            rs.getString("sinopsis"),
  	    	            rs.getString("imagen_url"),
  	    	            rs.getString("nombre_serie")
  	            		);
  	            
  	            favoritos.add(capi);
  	        }

  	    } catch (Exception e) {
              System.out.println("Error al obtener los capitulos favoritos: " + e.getMessage()); 
          }
          return favoritos;
  	}
     
     
     
     public ArrayList<Comentario> obtenerUltimosComentarios(int idUsuario) {
    	 
    	 
    	    ArrayList<Comentario> lista = new ArrayList<>();
    	    
    	    String sql = "SELECT * FROM comentario WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT 3";

    	    try (Connection con = Conexion.conectar();
    	         PreparedStatement ps = con.prepareStatement(sql)) {

    	        ps.setInt(1, idUsuario);
    	        ResultSet rs = ps.executeQuery();

    	        while (rs.next()) {
    	            Comentario c = new Comentario(
    	            		
    	                rs.getInt("id"), 
    	                rs.getString("contenido"),
    	                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
    	                rs.getInt("id_usuario"),
    	                rs.getInt("id_resena")
    	            		);
    	          
    	          
    	            lista.add(c);
    	        }

    	    } catch (Exception e) {
                System.out.println("Error al obtener los ultimos comentarios: " + e.getMessage()); 
            }
            return lista;
    	}
     
     
     public ArrayList<Resena> obtenerUltimasResenas(int idUsuario) {
    	 
    	 
 	    ArrayList<Resena> lista = new ArrayList<>();
 	    
 	    String sql = "SELECT * FROM resena WHERE id_usuario = ? ORDER BY fecha_creacion DESC LIMIT 3";

 	    try (Connection con = Conexion.conectar();
 	         PreparedStatement ps = con.prepareStatement(sql)) {

 	        ps.setInt(1, idUsuario);
 	        ResultSet rs = ps.executeQuery();

 	        while (rs.next()) {
 	            Resena r = new Resena(
 	            		
 	                rs.getInt("id"), 
 	                rs.getString("contenido"),
 	                rs.getTimestamp("fecha_creacion").toLocalDateTime(),
 	                rs.getInt("id_capitulo"),
 	                rs.getInt("id_usuario")
 	            		);
 	          
 	          
 	            lista.add(r);
 	        }

 	    } catch (Exception e) {
             System.out.println("Error al obtener las ultimas resenas: " + e.getMessage()); 
         }
         return lista;
 	}
    
        
     public List<Serie> buscarSeriesPorNombre(String nombre) {
    	 
    	 
    	    List<Serie> series = new ArrayList<>();
    	    String sql = "SELECT * FROM serie WHERE nombre LIKE ?";

    	    try (Connection con = Conexion.conectar();
    	         PreparedStatement ps = con.prepareStatement(sql)) {

    	        ps.setString(1, "%" + nombre + "%");

    	        try (ResultSet rs = ps.executeQuery()) {
    	            while (rs.next()) {
    	                Serie s = new Serie(
    	                		rs.getInt("id"),
        	    	            rs.getString("nombre"),
        	    	            rs.getInt("estreno"),
        	    	            rs.getString("sinopsis"),
        	    	            rs.getInt("id_genero"),
        	    	            rs.getString("imagen_url"),
        	    	            rs.getInt("id_director")
        	            		
    	                );
    	                series.add(s);
    	            }
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }

    	    return series;
    	}
     
}
