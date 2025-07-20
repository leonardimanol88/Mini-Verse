package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entidades.Capitulo;
import entidades.Director;
import entidades.Serie;
import entidades.Temporada;
import servicio.ServicioSerie;
import util.JWTUtil;

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
		
		
		app.get("/detalleDirector", contexto -> { 
			contexto.req().setCharacterEncoding("UTF-8");
			
			
			
		    int idSerie = Integer.parseInt(contexto.queryParam("id"));
		    
           Director director = servicio.buscarDirectorPorIdSerie(idSerie);
		    

		    contexto.json(Map.of(
		        "director", director
		    ));
		});
		
		
		
		app.post("/agregarFavorita/{idSerie}", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");
		    
		    
		    String header = contexto.header("Authorization");

		    if (header == null || !header.startsWith("Bearer ")) {
		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
		        return;
		    }
		    
		    String token = header.replace("Bearer ", "");
		    Integer idUsuario = JWTUtil.verificarToken(token);

		    if (idUsuario == null) {
		        contexto.status(401).json(Map.of("error", "Token invalido o expirado"));
		        return;
		    }

		    
		    
		     int idSerie;
		    
		    try {
		        idSerie = Integer.parseInt(contexto.pathParam("idSerie"));
		    } catch (NumberFormatException e) {
		        contexto.status(400).json(Map.of("error", "id de serie invalido"));
		        return;
		    }

		    boolean agregada = servicio.agregarSerieFavorita(idUsuario, idSerie);

		    if (agregada) {
		        contexto.status(200).result("Serie agregada a favoritos");
		    } else {
		        contexto.status(400).result("No se puede agregar mas de 4 series favoritas");
		    }
		});
		
		
		
    }
}
