package servicio;

import java.util.ArrayList;
import repositorio.RepositorioSerie;
import entidades.Serie;
import entidades.Temporada;
import entidades.Capitulo;
import entidades.Director;

public class ServicioSerie {
	

	RepositorioSerie repo = new RepositorioSerie();
	
	
	public ArrayList<Serie> obtenerSeries(){
		return repo.obtenerSeries();
	}
	
	public ArrayList<Serie> obtenerSeriesOrdenadas(String string){
		
		return repo.obtenerSeriesOrdenadas(string);
	}
	
	
	
    public Serie obtenerSerieporId(int id){
		
		return repo.obtenerSerieporId(id);
	}
    
    
    
    public ArrayList<Temporada> obtenerTemporadasPorSerie(int idSerie){
		return repo.obtenerTemporadasPorSerie(idSerie);
	}
    
    
    public ArrayList<Capitulo> obtenerCapitulosPorTemporada(int idTemporada){
    	
    	return repo.obtenerCapitulosPorTemporada(idTemporada);
    }
    
    
     public boolean agregarSerieFavorita(int idUsuario, int idSerie ){
    	
//    	if (repo.contarFavoritas(idUsuario) >= 4) {
//	        return false; 
//	    }
    	
    	return repo.agregarFavorita(idUsuario, idSerie);
    }
     
    
     public Director buscarDirectorPorIdSerie(int idSerie) {
    	 
    	 
    	 Serie serie = repo.obtenerSerieporId(idSerie);
    	 
    	 return repo.buscarDirectorPorId(serie.getIdDirector());
    	 
     }
	
	

}
