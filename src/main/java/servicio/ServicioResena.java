package servicio;

import repositorio.RepositorioResena;

public class ServicioResena {
	
	RepositorioResena repo = new RepositorioResena();

	public boolean agregarResena(int idUsuario, int idCapitulo, String contenido) {
		
		if (contenido.isBlank()) {
	        System.out.println("Campos vacios");
	        return false;
	    }
		
		if (repo.buscarResenaIgual(idUsuario, contenido)) { System.out.println("Comentario repetido"); return false;}
		
		return repo.agregarResena(idUsuario, idCapitulo, contenido);
		
	}
	
	
    public boolean agregarComentario(int idUsuario, int idResena, String contenido) {
		
		if (contenido.isBlank()) {
	        System.out.println("Campos vacios");
	        return false;
	    }
		
		if (repo.buscarComentarioIgual(idUsuario, contenido)) { System.out.println("Comentario repetido"); return false;}
		
		return repo.agregarComentario(idUsuario, idResena, contenido);
		
	}
    
}
