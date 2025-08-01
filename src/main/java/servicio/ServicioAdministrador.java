package servicio;

import repositorio.RepositorioAdministrador;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import entidades.Serie;
import entidades.Comentario;
import entidades.Director;
import entidades.Genero;
import entidades.Resena;
import entidades.Usuario;
import objetosFront.CapituloEstadistica;
import objetosFront.CapituloEstadisticaEdad;

public class ServicioAdministrador {

	RepositorioAdministrador repo = new RepositorioAdministrador();
	
	
	public boolean agregarSerie(String nombre, int estreno, String sinopsis, int id_genero, int id_director, String imagen_url) {
		
		
		//System.out.println("buscando director con nombre : '" + id_director + "'");
//		int idDirector = repo.buscarIdDirectorporNombre(id_director); //estas funciones porque serie tiene fk
//		int idGenero = repo.buscarIdGeneroporNombre(id_genero);
		 
		if (id_director == 0) {
			    System.out.println("Error: Director o género no encontrados");
			    return false;
			}
		
		if (id_genero == 0) {
		    System.out.println("Error:  o género no encontrados");
		    return false;
		}
		
	    if (repo.existeSerie(nombre)) {System.out.println("La serie ya existe");return false;}
		
		Serie serieNueva = new Serie(nombre, estreno, sinopsis, id_genero, id_director, imagen_url);
		
		return repo.agregarSerie(serieNueva);
	}
	
	
	
	
    public boolean agregarTemporada(int numero, String nombreSerie, String imagen_url, String nombreTemporada, String descripcion) { //numero y nombre de la serie
		
    	int idSerie = repo.buscarIdSerieporNombre(nombreSerie);
    	
    	if (idSerie == 0 ) {
		    System.out.println("Error: Serie no encontrados");
		    return false;
		}
    	
    	if (repo.existeTemporadaporNumero(numero, idSerie)) {
    	        System.out.println("Ya existe una temporada con ese numero para esta serie");
    	        return false;
    	    }
    	
        if (!repo.existeSerie(nombreSerie)) {System.out.println("La serie no existe");;return false;}
      
		return repo.agregarTemporada(numero, idSerie, imagen_url, nombreTemporada, descripcion);
	}
    
    
    public boolean eliminarTemporada(int numero, String nombreSerie) { //numero y nombre de la serie
		
    	if (numero <= 0 || nombreSerie == null || nombreSerie.trim().isEmpty()) {
            System.out.println("Error: numero invalido o nombre de serie vacio");
            return false;
        }
    	
    	int idSerie = repo.buscarIdSerieporNombre(nombreSerie);
    	
    	if (idSerie == 0 ) {
		    System.out.println("Error: Serie no encontrados");
		    return false;
		}
    	
    	
    	
        if (!repo.existeSerie(nombreSerie)) {System.out.println("La serie no existe");;return false;}
      
		return repo.eliminarTemporada(numero, idSerie);
	}
    
    
    public boolean eliminarCapitulo(String titulo, String nombreSerie, int numTemporada) { //numero y nombre de la serie
		
    	if (numTemporada <= 0 || titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Error: numero invalido o nombre de serie vacio");
            return false;
        }
    	
    	int idSerie = repo.buscarIdSerieporNombre(nombreSerie);

    	if (idSerie == 0 ) {
		    System.out.println("Error: Serie no encontrada");
		    return false;
		}
    	int idTemporada = repo.buscarIdTemporadaporNumero(numTemporada, idSerie);
    	if (idTemporada == 0) {
            System.out.println("Error: la temporada no existe");
            return false;
        }
    	
    	
        if (!repo.existeSerie(nombreSerie)) {System.out.println("La serie no existe");;return false;}
      
		return repo.eliminarCapitulo(titulo, idTemporada);
	}
    
    
    public boolean agregarCapitulo(String titulo, int numero, String duracion, String nombreSerie, int numeroTemporada) { //numero y nombre de la serie
		
    	if (!repo.existeSerie(nombreSerie)) {System.out.println("La serie no existe");;return false;}
    	
    	int idSerie = repo.buscarIdSerieporNombre(nombreSerie); 
    	int idTemporada = repo.buscarIdTemporadaporNumero(numeroTemporada, idSerie);
    	
    	if (idSerie == 0) {
    	    System.out.println("Error: Serie no valida, faltan datos");
    	    return false;
    	}

    	if (idTemporada == 0) {
    	    System.out.println("Error: Temporada no valida, faltan datos");
    	    return false;
    	}
    	
        if (!repo.existeTemporada(idTemporada)) {System.out.println("La temporada no existe");;return false;}
        
        if (repo.existeCapituloporNombre(titulo, idTemporada)) {System.out.println("El capitulo ya existe");;return false;}
      
       if (repo.existeCapituloporNumero(numero, idTemporada)) {System.out.println("El capitulo ya existe");;return false;}//
        
		return repo.agregarCapitulo(titulo, numero, duracion, idTemporada);
	}
    
    
	
	
	public boolean agregarGenero(String nombre) {
		
		if (repo.existeGenero(nombre)) {System.out.println("El genero ya existe");return false;}
		return repo.agregarGenero(nombre);
	}
	
	
	public boolean agregarDirector(String nombre, String biografia) {
		
        if (repo.existeDirector(nombre)) {System.out.println("El director ya existe");return false;}
		return repo.agregarDirector(nombre, biografia);
	}
	
	
	
	public boolean eliminarSerie(String nombre) {
		
		if (nombre.isBlank()) {
	        System.out.println("campo vacio");
	        return false;
	    }
		
		return repo.eliminarSerie(nombre);
	}
	
	
    public boolean eliminarGenero(String nombre) {
		
		if (nombre.isBlank()) {
	        System.out.println("campo vacio");
	        return false;
	    }
		
		return repo.eliminarGenero(nombre);
	}
	
	
    public ArrayList<Serie> obtenerSeries() {
		
		return repo.obtenerSeries();
	}
    
    
     public ArrayList<Genero> obtenerGeneros() {
		
		return repo.obtenerGeneros();
	}
     
     
     public ArrayList<Director> obtenerDirectores() {
 		
 		return repo.obtenerDirectores();
 	}
 
    
    public ArrayList<Usuario> obtenerUsuarios() {
		
		return repo.obtenerTodos();
	} 
    
    
    public Usuario devolverUsuario(String correo, String contrasena) {
		
		
		return repo.buscarUsuarioPorCorreoyContrasena(correo, contrasena);
	}
	
    
    //est
    public List<CapituloEstadistica> obtenerCapitulosMasResenados() {
		
		return repo.obtenerCapitulosMasResenados();
	}
    
    
    public List<CapituloEstadisticaEdad> obtenerCapitulosMasResenadosEdad() {
		
		return repo.obtenerCapitulosMasResenadosEdad();
	}
    
    
    public List<Resena> obtenerResenasPorUsuario(int idUsuario){
    	
    	return repo.obtenerResenasPorUsuario(idUsuario);
    }
    
    
    public List<Comentario> obtenerComentariosPorUsuario(int idUsuario){
    	
    	return repo.obtenerComentariosPorUsuario(idUsuario);
    }

    
    public List<Usuario> buscarUsuariosPorNombre(String nombre){
    	
    	return repo.buscarUsuariosPorNombre(nombre);
    }
    
    
    public boolean eliminarUsuario(int id){
    	
    	return repo.eliminarUsuario(id);
    }
    
    
    public boolean eliminarResena(int id){
    	
    	return repo.eliminarResena(id);
    }
    
    
    public boolean eliminarComentario(int id){
    	
    	return repo.eliminarComentario(id);
    }
    
    public Usuario mostrarUsuario(int id) {
		
		
		return repo.buscarUsuario(id);
	}
    
}
