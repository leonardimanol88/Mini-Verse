package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import com.google.gson.Gson;
import java.util.ArrayList;

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
		
		
		
		
		
		
    }
}
