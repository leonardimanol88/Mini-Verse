package servicio;

import java.util.ArrayList;
import java.util.List;


import entidades.Resena;
import entidades.Comentario;
import objetosFront.ResenaConComentario;
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
	
	
    public boolean eliminarResena(int idUsuario, int idResena) {
		

		
		if (repo.buscarIdUsuarioPorResena(idResena) != idUsuario) {
			System.out.println("Esta resena no es aplicable a eliminar");
	        return false;
		}
		
//		if (repo.buscarResenaIgual(idUsuario, contenido)) { System.out.println("Comentario repetido"); return false;}
		
		return repo.eliminarResena(idResena);
		
	}
    
    
    public boolean editarResena(int idUsuario, String contenidoNuevo, int idResena) {
		
		
		if (contenidoNuevo.isBlank()) {
	        System.out.println("Campos vacios");
	        return false; 
	    }
		
		
		if (repo.buscarIdUsuarioPorResena(idResena) != idUsuario) {
			System.out.println("Esta resena no es aplicable a editar");
	        return false;
		}
		
		
		return repo.editarResena(idResena, contenidoNuevo);
		
	}
	
	
    public boolean agregarComentario(int idUsuario, int idResena, String contenido) {
		
		if (contenido.isBlank()) {
	        System.out.println("Campos vacios");
	        return false;
	    }
		
		if (repo.buscarComentarioIgual(idUsuario, contenido)) { System.out.println("Comentario repetido"); return false;}
		
		return repo.agregarComentario(idUsuario, idResena, contenido);
		
	}
    
    
    public boolean editarComentario(int idUsuario, String contenidoNuevo, int idComentario) {
		
		
		if (contenidoNuevo.isBlank()) {
	        System.out.println("Campos vacios");
	        return false; 
	    }
		
		
		if (repo.buscarIdUsuarioPorResena(idComentario) != idUsuario) {
			System.out.println("Este comentario no es aplicable a editar");
	        return false;
		}
		
		
		return repo.editarResena(idComentario, contenidoNuevo);
		
	}
    
    
    public boolean eliminarComentario(int idUsuario, int idComentario) {
		

		
		if (repo.buscarIdUsuarioPorResena(idComentario) != idUsuario) {
			System.out.println("Este comentario no es aplicable a eliminar");
	        return false;
		}
	
		
		return repo.eliminarComentario(idComentario);
		
	}
    
    
    public List<ResenaConComentario> obtenerResenasConComentarios(int idCapitulo) {
    	
    	
    	List<ResenaConComentario> resultado = new ArrayList<>(); ////
    	
        List<Resena> resenas = repo.obtenerResenasPorCapitulo(idCapitulo);

        
        for (Resena r : resenas) {
        	
            List<Comentario> comentarios = repo.obtenerComentariosPorResena(r.getId());
            
            ResenaConComentario combinado = new ResenaConComentario();
            
            combinado.setId(r.getId());
            combinado.setContenido(r.getContenido());
            combinado.setNombreUsuario(repo.buscarNombreUsuario(r.getIdUsuario()));
            combinado.setIdUsuario(r.getIdUsuario());
            
            combinado.setFechaCreacion(r.getFechaCreacion());
            
            combinado.setComentarios(comentarios);
            
            resultado.add(combinado);
        }

        return resultado;
    }
    
    
    
}
