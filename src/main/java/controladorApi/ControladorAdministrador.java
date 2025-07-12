package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import com.google.gson.Gson;

import entidades.Serie;
import servicio.ServicioAdministrador;

public class ControladorAdministrador {

    public ControladorAdministrador(Javalin app) {
        ServicioAdministrador servicio = new ServicioAdministrador();


		app.post("/series", ctx -> {
		    ctx.req().setCharacterEncoding("UTF-8");
		
		    
		    
		    // lee JSON del cuerpo y convertir a serie
		    Serie nuevaSerie = new Gson().fromJson(ctx.body(), Serie.class);
		
		    boolean seAgrego = servicio.agregarSerie(nuevaSerie);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "Serie agregada correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar la serie"));
		    }
		});
    }
}
