package servicio;

import repositorio.RepositorioAdministrador;
import entidades.Serie;

public class ServicioAdministrador {

	RepositorioAdministrador repo = new RepositorioAdministrador();
	
	
	public boolean agregarSerie(String nombre, int estreno, String sinopsis, String id_genero, String id_director, String imagen_url) {
		
		int idDirector = repo.buscarIdDirectorporNombre(id_director); //estas funciones porque serie tiene fk
		int idGenero = repo.buscarIdGeneroporNombre(id_genero);
		 
		if (idDirector == 0 || idGenero == 0) {
			    System.out.println("Error: Director o género no encontrados");
			    return false;
			}
		
	    if (repo.existeSerie(nombre)) {System.out.println("La serie ya existe");return false;}
		
		Serie serieNueva = new Serie(nombre, estreno, sinopsis, idGenero, idDirector, imagen_url);
		return repo.agregarSerie(serieNueva);
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
	        System.out.println("contrasena vacia");
	        return false;
	    }
		
		return repo.eliminarSerie(nombre);
	}

}
