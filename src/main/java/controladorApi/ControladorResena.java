package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Map;
import com.google.gson.Gson;

import entidades.Resena;

import java.util.ArrayList;
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
		    
		    
		    DatosResena datos = new Gson().fromJson(contexto.body(), DatosResena.class); //aqui esta el id del capitulo y el contenido de resena
		    
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
		    
//		    if (!servicio.validarIdUsuarioResena(idUsuario, datos.getId())) {
//		    	 contexto.status(401).json(Map.of("error", "Esta resena no es aplicable a editar"));
//			        return;
//		    	
//		    }

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
		
    }
}