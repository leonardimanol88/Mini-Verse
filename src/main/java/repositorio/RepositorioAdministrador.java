package repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.sql.Time;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import dao.Conexion;
import entidades.Serie;
import entidades.Usuario;
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
	
	
    public boolean eliminarGenero(String nombre) {
		
		boolean eliminado = false;
		String sql = "DELETE from genero WHERE nombre = ?";
		
		 try(Connection con = Conexion.conectar();
		     PreparedStatement ps = con.prepareStatement(sql);
         ){
			 
			ps.setString(1, nombre);
	    	eliminado = ps.executeUpdate() > 0;
			 
		 } catch (Exception e) {
	            System.out.println("error al eliminar: " + e.getMessage());
		 }
		 
		 return eliminado;
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
    
    
    public int buscarIdSerieporNombre(String nombre) {
		
		int id = 0;
		String sql = "SELECT * from serie WHERE nombre = ?";
		
        try(Connection con = Conexion.conectar();
        	PreparedStatement ps = con.prepareStatement(sql);
        	
        	){ 
			ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
			
			if (rs.next()) { 
	            id = rs.getInt("id");
	        }
        	
        }catch (Exception e) {
            System.out.println("esa serie no existe: " + e.getMessage());
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
    	
        String sql = "SELECT 1 FROM serie WHERE nombre = ?";
        
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
    
    
    public boolean existeTemporada(int idTemporada) {
    	
        String sql = "SELECT 1 FROM temporada WHERE id = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setInt(1, idTemporada);
      
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar si la temporada existe: " + e.getMessage());
            return false;
        } 
    }
    
    
    
    public boolean eliminarTemporada(int numeroTemporada, int idSerie) {
    	
    	if (numeroTemporada <= 0 || idSerie <= 0) {
            System.out.println("Error: numero de temporada o ID de serie invalido");
            return false;
        }
		
		boolean eliminado = false;
		String sql = "DELETE from temporada WHERE numero = ? AND id_serie = ?";
		
		 try(Connection con = Conexion.conectar();
		     PreparedStatement ps = con.prepareStatement(sql);
         ){
			 
			ps.setInt(1, numeroTemporada);
			ps.setInt(2, idSerie);
			
			int filasAfectadas = ps.executeUpdate();
	        if (filasAfectadas == 0) {
	            System.out.println("No se elimino ninguna temporada");
	        }
	    	eliminado = ps.executeUpdate() > 0;
	    	
	    	
		 } catch (Exception e) {
	            System.out.println("error al eliminar: " + e.getMessage());
		 }
		 
		 return eliminado;
	}
    
    
    public boolean existeTemporadaporNumero(int numTemporada, int idSerie) {
    	
        String sql = "SELECT 1 FROM temporada WHERE numero = ? AND id_serie = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setInt(1, numTemporada);
            Consulta.setInt(2, idSerie);
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar si la temporada existe: " + e.getMessage());
            return false;
        } 
    }
    
    
    public boolean existeCapituloporNombre(String nombreCapitulo, int idTemporada) {
    	
        String sql = "SELECT 1 FROM capitulo WHERE titulo = ? AND id_temporada = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setString(1, nombreCapitulo);
            Consulta.setInt(2, idTemporada);
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar si el capitulo existe: " + e.getMessage());
            return false;
        } 
    }
    
    
    public boolean existeCapituloporNumero(int numero, int idTemporada) {
    	
        String sql = "SELECT 1 FROM capitulo WHERE numero = ? AND id_temporada = ?";
        
        try (Connection con = Conexion.conectar();
            PreparedStatement Consulta = con.prepareStatement(sql)) {
        	
            Consulta.setInt(1, numero);
            Consulta.setInt(2, idTemporada);
          
            ResultSet rs = Consulta.executeQuery();
            
            return rs.next();
            
        } catch (Exception e) {
            System.out.println("error al verificar si el capitulo existe: " + e.getMessage());
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
    
    
    public boolean agregarTemporada(int numero, int idSerie, String imagen_url, String nombre, String descripcion) {
		
		boolean insertado = false;
		
		String sql = "INSERT INTO temporada (numero, id_serie, imagen_url, nombre, descripcion) VALUES (?, ?, ?, ?, ?)";
		
		try(Connection con = Conexion.conectar();){
			
			try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setInt(1, numero);
			    ps.setInt(2, idSerie);
			    ps.setString(3, imagen_url);
			    ps.setString(4, nombre);
			    ps.setString(5, descripcion);
			    insertado = ps.executeUpdate() >0;
			}
			
		} catch (Exception e) {
            System.out.println("Error al guardar serie: " + e.getMessage());
        }
		return insertado;
	}
    
    
    public int buscarIdTemporadaporNumero(int numeroTemporada, int id_serie) {
		
		int id = 0;
		String sql = "SELECT id from temporada WHERE numero = ? AND id_serie = ?";
		
        try(Connection con = Conexion.conectar();
        	PreparedStatement ps = con.prepareStatement(sql);
        	
        	){ 
			ps.setInt(1, numeroTemporada);
			ps.setInt(2, id_serie);
            ResultSet rs = ps.executeQuery();
			
			if (rs.next()) { 
	            id = rs.getInt("id");
	        }
        	
        }catch (Exception e) {
            System.out.println("esa temporada no existe: " + e.getMessage());
        }
        
        return id;
	}
    
    
    public boolean agregarCapitulo(String titulo, int numero, String duracion, int idTemporada) {
		
		boolean insertado = false;
		
		String sql = "INSERT INTO capitulo (titulo, numero, duracion, id_temporada) VALUES (?, ?, ?, ?)";
		
		try(Connection con = Conexion.conectar();){
			
			try(PreparedStatement ps = con.prepareStatement(sql);){
			    ps.setString(1, titulo);
			    ps.setInt(2, numero);
			    LocalTime hora = LocalTime.parse(duracion); // debe ser "00;00;00"
		        ps.setTime(3, Time.valueOf(hora));
			    ps.setInt(4, idTemporada);
			    insertado = ps.executeUpdate() >0;
			}
			
		} catch (Exception e) {
            System.out.println("Error al guardar capitulo: " + e.getMessage());
        }
		return insertado;
	}
    
    
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
    
    
    public boolean eliminarCapitulos(int id) {
		
		boolean eliminados = false;
		String sql = "DELETE * from capitulo WHERE id_serie = ?";
		
		 try(Connection con = Conexion.conectar();
		     PreparedStatement ps = con.prepareStatement(sql);
         ){
			 
			ps.setInt(1, id);
	    	eliminados = ps.executeUpdate() > 0;
			 
		 } catch (Exception e) {
	            System.out.println("error al eliminar: " + e.getMessage());
		 }
		 
		 return eliminados;
	}
    
    
    public boolean eliminarCapitulo(String titulo, int idTemporada) {
    	
    	if (titulo == null || titulo.trim().isEmpty() || idTemporada <= 0) {
            System.out.println("Error: título vacío o ID de temporada inválido");
            return false;
        }
		
		boolean eliminados = false;
		String sql = "DELETE from capitulo WHERE titulo = ? AND id_temporada = ?";
		
		 try(Connection con = Conexion.conectar();
		     PreparedStatement ps = con.prepareStatement(sql);
         ){
			 
			ps.setString(1, titulo);
			ps.setInt(2, idTemporada);
	    	eliminados = ps.executeUpdate() > 0;
			 
		 } catch (Exception e) {
	            System.out.println("error al eliminar: " + e.getMessage());
		 }
		 
		 return eliminados;
	}
    
    
    public boolean eliminarTemporadas(int id) {
		
		boolean eliminados = false;
		String sql = "DELETE * from temporada WHERE id_serie = ?";
		
		 try(Connection con = Conexion.conectar();
		     PreparedStatement ps = con.prepareStatement(sql);
         ){
			 
			ps.setInt(1, id);
	    	eliminados = ps.executeUpdate() > 0;
			 
		 } catch (Exception e) {
	            System.out.println("error al eliminar: " + e.getMessage());
		 }
		 
		 return eliminados;
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
	
	
	
	

}
