package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;


import objetosFront.DatosResena;
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
		    
		    
		    DatosResena datos = new Gson().fromJson(contexto.body(), DatosResena.class); //aqui esta el id del capitulo y el contenido de resena
		    
		    boolean seAgregoResena = servicio.agregarResena(idUsuario, datos.getId(), datos.getContenido());
		    
		    if (seAgregoResena) {
		        contexto.json(Map.of("mensaje", "Resena agregada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar la resena"));
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
		
    }
}