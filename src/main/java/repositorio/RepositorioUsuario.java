package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt; //contrasenas


import dao.Conexion;
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
    
     
    
    
    
    

    
    //obtener una lista de objetos usuario 
    public ArrayList<Usuario> obtenerTodos() { //metodo que retorna un array de objetos usuario
    	
        ArrayList<Usuario> lista = new ArrayList<>();//creacion del array de objetos usuario
        String sql = "SELECT * FROM usuario"; // string de consulta select en la base
        
        try (
    	    Connection con = Conexion.conectar(); //hacer el metodo conectar de la clase conexion en el objeto con
            PreparedStatement ps = con.prepareStatement(sql); //ejecutar la sentencia sql con el string
            ResultSet rs = ps.executeQuery()) { //obtener el resultado al ejecutar la sentencia (select)
            while (rs.next()) { //con .next() avanza al siguiente elemento de la sentencia ejecutada 
                Usuario u = new Usuario( //creacion de un objeto usuario dentro del while, y en su constructor se guaradaran los elementos siguientes
                    rs.getInt("id"), //obtener el primer elemento "id" de lo obtenido de la ejecucion
                    rs.getString("nombre"), //en esta seccion obtiene lo de cada elemento de lo seleccionado en la base
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getInt("edad")
            );
                lista.add(u); // se agrega el usuario creado en el array creado al inicio , se agregan todos los usuarios hasta finalizar el while
            }
        } catch (Exception e) {
            System.out.println("Error al obtener usuarios: " + e.getMessage()); //mostrar el error
        }
        return lista;
    }
}
