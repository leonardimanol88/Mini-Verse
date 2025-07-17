package servicio;

import java.util.ArrayList;
import repositorio.RepositorioSerie;
import entidades.Serie;
import entidades.Temporada;
import entidades.Capitulo;

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
	
	

}
