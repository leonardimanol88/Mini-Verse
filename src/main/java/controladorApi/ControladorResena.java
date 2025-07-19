package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;
import objetosFront.AgregarDirector;

import java.util.Map;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objetosFront.DatosResena;
import servicio.ServicioResena;
import util.JWTUtil;

public class ControladorResena {

    public ControladorResena(Javalin app) {
    	
        ServicioResena servicio = new ServicioResena();


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
		
    }
}