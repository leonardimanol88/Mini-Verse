package repositorio;

//import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt; //contrasenas
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import dao.Conexion;
import entidades.Comentario;
import entidades.Genero;
import entidades.Resena;
import entidades.Serie;
import entidades.Usuario;


public class RepositorioUsuario {

	
	//post //registrarusuario
	//guardar usuarios en base, (recibe un objeto usuario) ///registro
    public boolean agregarUsuario(Usuario usuario) { 
    	
    	boolean insertado = false;
    	
    	String hashContrasena = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());//convertir la contrasena
        String sql = "INSERT INTO usuario (nombre, correo, contrasena, edad) VALUES (?, ?, ?, ?)";//este sera el string a utilizar en la base
        
        try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {//aqui se pone el string
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
            return resultado.next(); // si cuando devuelva eso da algo , el correo ya existe
            
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
    
    
    public Usuario buscarUsuarioPorId(int id) {////
    	
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
                String hashAlmacenado = rs.getString("contrasena");// contrasena modificada almacenada
                
                if (BCrypt.checkpw(contrasena, hashAlmacenado)) { //verificar la contrasena original con la encriptada con .checkpw
                   
                	
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
    
     
//     public boolean agregarFavorita(int idUsuario, int idSerie) {
// 
//
//    	    String sql = "INSERT INTO favorita (id_usuario, id_serie) VALUES (?, ?)";
//
//    	    try (Connection con = Conexion.conectar();
//    	         PreparedStatement ps = con.prepareStatement(sql)) {
//
//    	        ps.setInt(1, idUsuario);
//    	        ps.setInt(2, idSerie);
//    	        ps.executeUpdate();
//    	        return true;
//
//    	    } catch (SQLException e) {
//    	        e.printStackTrace();
//    	        return false;
//    	    }
//    	}
//     
//     
//     public int contarFavoritas(int idUsuario) {
//    	    String sql = "SELECT COUNT(*) FROM favorito WHERE id_usuario = ?";
//    	    int total = 0;
//
//    	    try (Connection con = Conexion.conectar();
//    	         PreparedStatement ps = con.prepareStatement(sql)) {
//
//    	        ps.setInt(1, idUsuario);
//    	        ResultSet rs = ps.executeQuery();
//
//    	        if (rs.next()) {
//    	            total = rs.getInt(1);
//    	        }
//    	    } catch (SQLException e) {
//    	        e.printStackTrace();
//    	    }
//
//    	    return total;
//    	}
     
     
     
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
