package servicio;

import java.util.ArrayList;
import repositorio.RepositorioSerie;
import entidades.Serie;


public class ServicioSerie {
	

	RepositorioSerie repo = new RepositorioSerie();
	
	
	public ArrayList<Serie> obtenerSeries(){
		return repo.obtenerSeries();
	}
	
	public ArrayList<Serie> obtenerSeriesOrdenadas(String string){
		
		return repo.obtenerSeriesOrdenadas(string);
	}


}
