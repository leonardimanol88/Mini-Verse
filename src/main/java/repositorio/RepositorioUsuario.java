package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt; //contrasenas
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import dao.Conexion;
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
        String sql = "SELECT 1 FROM usuario WHERE correo = ?";
        
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String hashAlmacenado = rs.getString("contrasena");// contrasena modificada almacenada
                
                if (BCrypt.checkpw(contrasena, hashAlmacenado)) { 
                    usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        hashAlmacenado, 
                        rs.getInt("edad")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return usuario;
    }

    
    
    public boolean agregarResena(String titulo, String contenido, String duracion, int id_capitulo, int id_usuario) { 
    	
    	boolean insertado = false;
    	
        String sql = "INSERT INTO resena (titulo, contenido, fecha_creacion, id_capitulo, id_usuario) VALUES (?, ?, ?, ?, ?)"; 
        
        try (Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, contenido);
            LocalTime duracio = LocalTime.parse(duracion); // debe ser "00;00;00"
	        ps.setTime(3, Time.valueOf(duracio));
            ps.setInt(4, id_capitulo);
            ps.setInt(5, id_usuario);
            
            insertado = ps.executeUpdate() >0;
        } catch (Exception e) {
            System.out.println("Error al guardar resena: " + e.getMessage());
        }
        return insertado;
    }
    
    
     public ArrayList<Serie> obtenerSeriesporGenero(int idGenero) { 
    	
        ArrayList<Serie> lista = new ArrayList<>();
        String sql = "SELECT * FROM serie WHERE genero = ?";
        
        try (
    	    Connection con = Conexion.conectar(); 
            PreparedStatement ps = con.prepareStatement(sql);
            )
        	{ 
        	ps.setInt(1, idGenero);
        	ResultSet rs = ps.executeQuery();
                
            while (rs.next()) {
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
    
}
