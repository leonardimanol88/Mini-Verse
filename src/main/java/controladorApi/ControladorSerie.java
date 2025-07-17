package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entidades.Capitulo;
import entidades.Serie;
import entidades.Temporada;
import servicio.ServicioSerie;

public class ControladorSerie {

    public ControladorSerie(Javalin app) {
        ServicioSerie servicio = new ServicioSerie();


		app.get("/obtenerSeriesOrdenadas", contexto -> { 
		    contexto.req().setCharacterEncoding("UTF-8");
		
		    
		 
		
		    
		    
		    String criterio = contexto.queryParam("criterio"); // nombre o estreno
		    ArrayList<Serie> lista = servicio.obtenerSeriesOrdenadas(criterio);
		    contexto.json(lista);//enviar al front la lista

		});
		
		
		
		app.get("/detalleSerie", contexto -> { 
			contexto.req().setCharacterEncoding("UTF-8");
			
			
			
		    int idSerie = Integer.parseInt(contexto.queryParam("id")); //recibo el id de la serie a la que se le da clic
		    
		    

		    
		    Serie serie = servicio.obtenerSerieporId(idSerie);
		    ArrayList<Temporada> temporadas = servicio.obtenerTemporadasPorSerie(idSerie);
		    
		    
		    Map<Integer, List<Capitulo>> capitulosPorTemporada = new HashMap<>();
		    
		    for (Temporada t : temporadas) {
		        List<Capitulo> capitulos = servicio.obtenerCapitulosPorTemporada(t.getId());
		        capitulosPorTemporada.put(t.getId(), capitulos);
		    }

		    contexto.json(Map.of(
		        "serie", serie,
		        "temporadas", temporadas,
		        "capitulosPorTemporada", capitulosPorTemporada
		    ));
		});
		
		
		
		
    }
}
