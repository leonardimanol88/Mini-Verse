package servicio;


import repositorio.RepositorioUsuario;
import entidades.Usuario;
import entidades.Comentario;
import entidades.Genero;
import entidades.Resena;
import entidades.Serie;

import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class ServicioUsuario {

RepositorioUsuario repo = new RepositorioUsuario();
	
	
    //recibe un objeto usuario //post
	public boolean registrarUsuario(Usuario usuario) {
		
		if (usuario.getCorreo().isBlank() || usuario.getNombre().isBlank()) {
	        System.out.println("Campos vacios");
	        return false;
	    }
		
		if (usuario.getContrasena().isBlank()) { System.out.println("Agrega una contrasena"); return false;}
		
		if (usuario.getEdad() <= 0 || usuario.getEdad() >100) {System.out.println("Agrega una edad valida"); return false;}
        
		//aqui verifica si el correo qdel objeto usuario que recibio el metodo ya esta registrado, con el metodo de repositorio
	    if (repo.existeCorreo(usuario.getCorreo())) {
	        System.out.println("Correo ya registrado");
	        return false;
	    }

	    return repo.agregarUsuario(usuario);
	}
	
	//en la vista donde pide la contrasena dos veces //get
	public boolean eliminarUsuario(int id, String contrasena) {
		
		if (contrasena.isBlank()) {
	        System.out.println("contrasena vacia");
	        return false;
	    }

	    Usuario usuario = repo.buscarUsuarioPorId(id);
	    
	    if (usuario == null) {
	        System.out.println("Usuario no encontrado");
	        return false;
	    }

	    if (!BCrypt.checkpw(contrasena, usuario.getContrasena())) {
	        System.out.println("contrasena incorrecta");
	        return false;
	    }

	    return repo.eliminarUsuario(id);
	}

	
	public boolean actualizarContrasena(int id, String contrasena, String nuevaContrasena) {
		
		if (contrasena.isBlank()) {
	        System.out.println("contrasena vacia");
	        return false;
	    }
		
		Usuario usuario = repo.buscarUsuarioPorId(id);
		if (usuario == null) {
		       System.out.println("Usuario no encontrado");
		       return false;
		}
		
		if (!BCrypt.checkpw(contrasena, usuario.getContrasena())) {
	        System.out.println("contrasena incorrecta");
	        return false;
	    }
		
		if (contrasena.equals(nuevaContrasena)) {
		    System.out.println("La nueva contrasena no puede ser igual a la actual");
		    return false;
		}
		
		return repo.actualizarContrasena(usuario, nuevaContrasena);	
	}
	
	/* /loggg
	public boolean iniciarSesion(String correo, String contrasena) {
	    
		if (correo.isBlank() || contrasena.isBlank()) {
	        System.out.println("Correo o contrasena vacios");
	        return false;
	    }
		
		Usuario usuario = repo.buscarUsuarioPorCorreo(correo);
		
		if (usuario == null) {
	        System.out.println("Usuario no encontrado");
	        return false;
	    }
		
		if (!BCrypt.checkpw(contrasena, usuario.getContrasena())) {
	        System.out.println("contrasena incorrecta");
	        return false;
	    }
		
		return true;
	}
	
	*/
	
	public Usuario devolverUsuario(String correo, String contrasena) {
		
		
		return repo.buscarUsuarioPorCorreoyContrasena(correo, contrasena);
	}
	
	
	
    public ArrayList<Serie> obtenerSeriesporGenero(String genero) {
    	
    	if (genero == null) {
		    System.out.println("Error: GENERO no encontrado");
		    return new ArrayList<>();
		}
    	
    	int idGenero = repo.buscarIdGeneroporNombre(genero);
    	
    	return repo.obtenerSeriesporGenero(idGenero);
    	
	}
    
    
    public ArrayList<Genero> obtenerGeneros() {
		
		return repo.obtenerGeneros();
	}
	
    
    public ArrayList<Serie> obtenerTodasSeries() {
    	return repo.obtenerSeries();
	}
    
    
    public ArrayList<Serie> obtenerTodasFavoritas(int idUsuario) {
    	return repo.obtenerSeriesFavoritas(idUsuario);
	}
	
    
    public ArrayList<Resena> obtenerUltimasResenas(int idUsuario){
    	
    	return repo.obtenerUltimasResenas(idUsuario);
    }
    
    
    public ArrayList<Comentario> obtenerUltimosComentarios(int idUsuario){
    	
    	return repo.obtenerUltimosComentarios(idUsuario);
    }
    
    
    public Usuario obtenerMiUsuario(int idUsuario){
    	
    	return repo.buscarUsuarioPorId(idUsuario);
    }
    
    
    public List<Serie> buscarPorNombre(String nombre){
    	
    	return repo.buscarSeriesPorNombre(nombre);
    }
    
	
	
}
