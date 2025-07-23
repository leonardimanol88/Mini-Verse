package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Map;
import com.google.gson.Gson;

import entidades.Serie;
import entidades.Genero;
import entidades.Usuario;
import objetosFront.AgregarSeries;
import objetosFront.AgregarDirector;
import objetosFront.AgregarGenero;
import objetosFront.AgregarTemporada;
import objetosFront.AgregarCapitulo;
import servicio.ServicioAdministrador;


public class ControladorAdministrador {

    public ControladorAdministrador(Javalin app) {
        ServicioAdministrador servicio = new ServicioAdministrador();
        
        
        
	    app.before("/admin/*", contexto -> {/////////
			
		    Usuario u = contexto.sessionAttribute("usuario");
		    
		    if (u == null || !"admin".equals(u.getRol())) {
		        contexto.status(403).result("Acceso denegado");
		        return;
		    }
		});
        
        
	    
        app.post("/admin/agregarDirector", ctx -> {///////////
		    ctx.req().setCharacterEncoding("UTF-8");
		
		    
		    AgregarDirector nuevoDirector = new Gson().fromJson(ctx.body(), AgregarDirector.class);
		
		    boolean seAgrego = servicio.agregarDirector(nuevoDirector.nombre, nuevoDirector.biografia);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "Director agregado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar el director"));
		    } 
		});
        
		
		
		app.post("/admin/agregarGenero", ctx -> {/////////
		    ctx.req().setCharacterEncoding("UTF-8");
		
		    
		    
		    
		    AgregarGenero nuevoGenero = new Gson().fromJson(ctx.body(), AgregarGenero.class);
		
		    boolean seAgrego = servicio.agregarGenero(nuevoGenero.nombre);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "genero agregado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar el genero"));
		    } 
		});
		
        app.get("/admin/obtenerGeneros", ctx -> { ////////
			
			
		    ctx.res().setCharacterEncoding("UTF-8");

		    ArrayList<Genero> obtener = servicio.obtenerGeneros();
		    
		    ctx.json(obtener); 
		});
		
        app.delete("/admin/eliminarGenero/{nombre}", ctx -> {///////
			
		    String nombre = ctx.pathParam("nombre");

		    boolean seElimino = servicio.eliminarGenero(nombre);

		    if (seElimino) {
		        ctx.json(Map.of("mensaje", "genero eliminado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo eliminar el genero"));
		    }
		});

        
		
		app.post("/admin/agregarSerie", contexto -> {/////////
		    contexto.req().setCharacterEncoding("UTF-8");
		
		    
		    
		    // lee JSON del cuerpo y convertir a serie
		    AgregarSeries nuevaSerie = new Gson().fromJson(contexto.body(), AgregarSeries.class);
		
		    boolean seAgrego = servicio.agregarSerie(nuevaSerie.nombre, nuevaSerie.estreno, nuevaSerie.sinopsis, nuevaSerie.id_genero, nuevaSerie.id_director, nuevaSerie.imagen_url);
		
		    if (seAgrego) {
		        contexto.json(Map.of("mensaje", "Serie agregada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar la serie"));
		    } 
		});
		
		app.get("/admin/obtenerSeries", ctx -> { //////
		    ctx.res().setCharacterEncoding("UTF-8");

		    ArrayList<Serie> obtener = servicio.obtenerSeries();
		    
		    ctx.json(obtener); 
		});
		
		app.delete("/admin/eliminarSerie/{nombre}", ctx -> {//////
			
		    String nombre = ctx.pathParam("nombre");

		    boolean seElimino = servicio.eliminarSerie(nombre);

		    if (seElimino) {
		        ctx.json(Map.of("mensaje", "Serie eliminada correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo eliminar la serie"));
		    }
		});
	
		
		
		app.post("/admin/agregarTemporada", ctx -> {  //formulario numero , nombre de la serie, link imagen
		    ctx.req().setCharacterEncoding("UTF-8");
		
		    
		    AgregarTemporada nuevaTemporada = new Gson().fromJson(ctx.body(), AgregarTemporada.class);
		
		    boolean seAgrego = servicio.agregarTemporada(nuevaTemporada.numero, nuevaTemporada.nombreSerie, nuevaTemporada.imagen_url, nuevaTemporada.nombreTemporada, nuevaTemporada.descripcion);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "Temporada agregada correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar la temporada"));
		    } 
		});
		
        app.delete("/admin/eliminarTemporada", ctx -> {////
			
        	AgregarTemporada datosEliminar = new Gson().fromJson(ctx.body(), AgregarTemporada.class);

		    boolean seElimino = servicio.eliminarTemporada(datosEliminar.numero, datosEliminar.nombreSerie);

		    if (seElimino) {
		        ctx.json(Map.of("mensaje", "Temporada eliminada correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo eliminar la temporada"));
		    }
		});
        
        
        
        app.post("/admin/agregarCapitulo", ctx -> { //formulario titulo, numero, duracion(solo 00:00:00), nombre de la serie, numero de temporada )
		    ctx.req().setCharacterEncoding("UTF-8");
		
		    
		    AgregarCapitulo nuevoCapitulo = new Gson().fromJson(ctx.body(), AgregarCapitulo.class);
		
		    boolean seAgrego = servicio.agregarCapitulo(nuevoCapitulo.titulo, nuevoCapitulo.numero, nuevoCapitulo.duracion, nuevoCapitulo.nombreSerie, nuevoCapitulo.temporada);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "Capitulo agregado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar el capitulo"));
		    } 
		});
        
        app.delete("/admin/eliminarCapitulo", ctx -> {//////
        	
        	
			
        	AgregarCapitulo datosEliminar = new Gson().fromJson(ctx.body(), AgregarCapitulo.class);
        	 if (datosEliminar == null || datosEliminar.titulo == null || datosEliminar.titulo.trim().isEmpty() ||
        	     datosEliminar.nombreSerie == null || datosEliminar.nombreSerie.trim().isEmpty() ||
        	     datosEliminar.temporada <= 0) {

        	            ctx.status(400).json(Map.of("error", "datos de entrada invalidos"));
        	            return;
        	        }

		    boolean seElimino = servicio.eliminarCapitulo(datosEliminar.titulo, datosEliminar.nombreSerie, datosEliminar.temporada);

		    if (seElimino) {
		        ctx.json(Map.of("mensaje", "calitulo eliminado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo eliminar el capitulo"));
		    }  
		    
		});
		
		
		
	
		
		
		app.get("/admin/mostrarUsuarios", contexto -> {/////
		    contexto.req().setCharacterEncoding("UTF-8");

		  
		    ArrayList<Usuario> obtener = servicio.obtenerUsuarios();
		    contexto.json(obtener); 
		});
		
		
		app.get("/admin/mostrarSeriesAdmin", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");

		  
		    ArrayList<Serie> obtener = servicio.obtenerSeries();
		    contexto.json(obtener); 
		});
		
		
		
		
		
    }
}
