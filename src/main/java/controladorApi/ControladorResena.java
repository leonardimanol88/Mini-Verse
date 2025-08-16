package controladorApi;
import io.javalin.Javalin;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;



//import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
import objetosFront.DatosResena;
import objetosFront.EditarComentario;
import objetosFront.EditarResena;
import objetosFront.EliminarComentario;
import objetosFront.EliminarResena;
import objetosFront.DatosComentario;
import servicio.ServicioResena;
import objetosFront.ResenaConComentario;
import util.JWTUtil;

public class ControladorResena {

    public ControladorResena(Javalin app) {
    	
        ServicioResena servicio = new ServicioResena();
        
        
        app.get("/resenasConComentarios/{idCapitulo}", contexto -> {
        	contexto.req().setCharacterEncoding("UTF-8");
        	
            int idCapitulo = Integer.parseInt(contexto.pathParam("idCapitulo"));

            List<ResenaConComentario> resenasConComentarios = servicio.obtenerResenasConComentarios(idCapitulo);

            contexto.json(resenasConComentarios);
        });

        
		app.post("/hacerResena", contexto -> { 
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
		    
		    DatosResena datos = new Gson().fromJson(contexto.body(), DatosResena.class);
		    
		    boolean seAgregoResena = servicio.agregarResena(idUsuario, datos.getId(), datos.getContenido());
		    
		    if (seAgregoResena) {
		        contexto.json(Map.of("mensaje", "Resena agregada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar la resena"));
		    }

		});
		
		
		app.delete("/eliminarResena", contexto -> { 
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
		    
		    EliminarResena datos = new Gson().fromJson(contexto.body(), EliminarResena.class); 
		    
		    boolean seEliminoResena = servicio.eliminarResena(idUsuario, datos.getId());
		    
		    if (seEliminoResena) {
		        contexto.json(Map.of("mensaje", "Resena eliminada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo eliminar la resena"));
		    }

		});
		
		
		app.put("/editarResena", contexto -> { 
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
		    
		    
		    EditarResena datos = new Gson().fromJson(contexto.body(), EditarResena.class);

		    boolean actualizado = servicio.editarResena(idUsuario, datos.contenidoNuevo, datos.getId());

		    if (actualizado) {
		        contexto.status(200).result("Resena actualizada");
		    } else {
		        contexto.status(400).result("No se pudo actualizar la resena");
		    }

		});
		
		
		app.post("/hacerComentario", contexto -> { 
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
		    
		    DatosComentario datos = new Gson().fromJson(contexto.body(), DatosComentario.class); 
		    
		    boolean seAgregoComentario = servicio.agregarComentario(idUsuario, datos.getId(), datos.getContenido());
		    
		    if (seAgregoComentario) {
		        contexto.json(Map.of("mensaje", "Comentario agregada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar el comentario"));
		    }

		});
		
		
		app.delete("/eliminarComentario", contexto -> { 
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
		    
		    EliminarComentario datos = new Gson().fromJson(contexto.body(), EliminarComentario.class); 
		    
		    boolean seEliminoComentario = servicio.eliminarComentario(idUsuario, datos.getId());
		    
		    if (seEliminoComentario) {
		        contexto.json(Map.of("mensaje", "Comentario eliminado correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo eliminar el comentario"));
		    }

		});
		
		
		app.put("/editarComentario", contexto -> { 
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
		    
		    EditarComentario datos = new Gson().fromJson(contexto.body(), EditarComentario.class);
		    
		    boolean actualizado = servicio.editarComentario(idUsuario, datos.contenidoNuevo, datos.getId());

		    if (actualizado) {
		        contexto.status(200).result("Comentario actualizado");
		    } else {
		        contexto.status(400).result("No se pudo actualizar el comentario");
		    }

		});
		
		
		app.post("/guardarPuntuacionSerie", ctx -> {
			
			ctx.req().setCharacterEncoding("UTF-8");
		    
		    String header = ctx.header("Authorization");

		    if (header == null || !header.startsWith("Bearer ")) {
		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
		        return;
		    }
		    
		    String token = header.replace("Bearer ", "");
		    Integer idUsuario = JWTUtil.verificarToken(token);

		    if (idUsuario == null) {
		        ctx.status(401).json(Map.of("error", "Token invalido o expirado"));
		        return;
		    }
			
		   
		    int idSerie = Integer.parseInt(ctx.formParam("idSerie"));
		    double puntuacion = Double.parseDouble(ctx.formParam("puntuacion"));

		    servicio.guardarPuntuacionSerie(idUsuario, idSerie, puntuacion);
		    ctx.status(200).result("Puntuación guardada correctamente");
		});
		
		
		app.get("/obtenerPuntuacionSerieUsuario", contexto -> {
			
		    try {
		        contexto.req().setCharacterEncoding("UTF-8");
		        contexto.res().setCharacterEncoding("UTF-8");
		        contexto.contentType("application/json; charset=UTF-8");
		        
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

		        try {
		            int idSerie = Integer.parseInt(contexto.queryParam("idSerie"));
		            Optional<Double> puntuacion = servicio.obtenerPuntuacionSerieUsuario(idUsuario, idSerie);
		            contexto.json(Map.of("puntuacion", puntuacion.orElse(0.0)));
		        } catch (NumberFormatException e) {
		            contexto.status(400).json(Map.of("error", "idSerie debe ser un número válido"));
		        }
		        
		    } catch (Exception e) {
		        contexto.status(500).json(Map.of("error", "Error interno del servidor"));
		    }
		});
		
		
        app.post("/guardarPuntuacionCapitulo", ctx -> {
			
			ctx.req().setCharacterEncoding("UTF-8");
		    
		    String header = ctx.header("Authorization");

		    if (header == null || !header.startsWith("Bearer ")) {
		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
		        return;
		    }
		    
		    String token = header.replace("Bearer ", "");
		    Integer idUsuario = JWTUtil.verificarToken(token);

		    if (idUsuario == null) {
		        ctx.status(401).json(Map.of("error", "Token invalido o expirado"));
		        return;
		    }
			
		   
		    int idCapitulo = Integer.parseInt(ctx.formParam("idCapitulo"));
		    double puntuacion = Double.parseDouble(ctx.formParam("puntuacion"));

		    servicio.guardarPuntuacionCapitulo(idUsuario, idCapitulo, puntuacion);
		    ctx.status(200).result("Puntuación guardada correctamente");
		});
        
        
        
        app.get("/obtenerPuntuacionCapituloUsuario", contexto -> {
			
		    try {
		        contexto.req().setCharacterEncoding("UTF-8");
		        contexto.res().setCharacterEncoding("UTF-8");
		        contexto.contentType("application/json; charset=UTF-8");
		        
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

		        try {
		            int idCapitulo = Integer.parseInt(contexto.queryParam("idCapitulo"));
		            Optional<Double> puntuacion = servicio.obtenerPuntuacionCapituloUsuario(idUsuario, idCapitulo);
		            contexto.json(Map.of("puntuacion", puntuacion.orElse(0.0)));
		        } catch (NumberFormatException e) {
		            contexto.status(400).json(Map.of("error", "id debe ser un número válido"));
		        }
		        
		    } catch (Exception e) {
		        contexto.status(500).json(Map.of("error", "Error interno del servidor"));
		    }
		});
        
        
        app.post("/agregarFavorito/{idCapitulo}", contexto -> {
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
		    
		    int idCapitulo;
		    
		    try {
		        idCapitulo = Integer.parseInt(contexto.pathParam("idCapitulo"));
		    } catch (NumberFormatException e) {
		        contexto.status(400).json(Map.of("error", "id de capitulo invalido"));
		        return;
		    }

		    boolean agregado = servicio.agregarCapituloFavorito(idUsuario, idCapitulo);

		    if (agregado) {
		        contexto.status(200).result("Capitulo agregado a favoritos");
		    } else {
		        contexto.status(400).result("No se puede agregar mas de 10 capitulos favoritos");
		    }
		});
		
    }
}