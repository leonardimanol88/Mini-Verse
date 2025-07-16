package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;

import entidades.Genero;
import entidades.Serie;
import entidades.Usuario;
import objetosFront.ActualizarContrasena;
import objetosFront.EliminarUsuario;
import objetosFront.Login;
import servicio.ServicioUsuario;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class ControladorUsuario {

    public ControladorUsuario(Javalin app) {
        ServicioUsuario servicio = new ServicioUsuario();

       //guardar
		app.post("/registrarUsuario", contexto -> { //cuando se haga una peticion post a la ruta /agregarUsuario
		    contexto.req().setCharacterEncoding("UTF-8");
		
		    
		    
		    // lee JSON del cuerpo y convertir a serie
		    Usuario nuevoUsuario = new Gson().fromJson(contexto.body(), Usuario.class);
		
		    boolean seAgrego = servicio.registrarUsuario(nuevoUsuario);
		
		    if (seAgrego) {
		        contexto.json(Map.of("mensaje", "usuario agregado correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar el usuario"));
		    }
		});
		
		
		app.put("/actualizarContrasena", contexto -> { 
		    contexto.req().setCharacterEncoding("UTF-8");
		
		    
		    
		    ActualizarContrasena datos = new Gson().fromJson(contexto.body(), ActualizarContrasena.class);
		    
		    Usuario usuario = servicio.devolverUsuario(datos.correo, datos.contrasena);
		    int idUsuario = usuario.getId();
		  
		    boolean seCambio = servicio.actualizarContrasena(idUsuario, datos.contrasena, datos.nuevaContrasena);
		
		    if (seCambio) {
		        contexto.json(Map.of("mensaje", "contrasena actualizada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo actualizar"));
		    }
		});
		
		
		app.delete("/eliminarUsuario", contexto -> { 
		    contexto.req().setCharacterEncoding("UTF-8");
		
		    
		    
		    
		    EliminarUsuario datos = new Gson().fromJson(contexto.body(), EliminarUsuario.class);
		
		    boolean seCambio = servicio.eliminarUsuario(datos.id, datos.contrasena);
		
		    if (seCambio) {
		        contexto.json(Map.of("mensaje", "cuenta eliminada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo eliminar"));
		    }
		});
		
		
		app.post("/iniciarSesion", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");

		    
		    
		    
		    Login datos = new Gson().fromJson(contexto.body(), Login.class);

		    Usuario usuario = servicio.devolverUsuario(datos.correo, datos.contrasena);
		    

		    if (usuario != null) {
		    	
		    	int token = usuario.getId();
		        contexto.json(Map.of(
		            "mensaje", "Inicio de sesion exitoso",
		            "token", token
		        ));

		    } else {
		        contexto.status(500).json(Map.of(
		            "error", "Correo o contrasena incorrecto"));
		    }
		});

		
		
		
		app.get("/mostrarSeriesporGenero", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");

		    
		    String genero = contexto.queryParam("genero"); 

		    ArrayList<Serie> obtener = servicio.obtenerSeriesporGenero(genero);
		    contexto.json(obtener); 
		});
		
		
		
		app.get("/obtenerGenerosparaUsuario", ctx -> { 
		    ctx.res().setCharacterEncoding("UTF-8");

		    ArrayList<Genero> obtener = servicio.obtenerGeneros();
		    
		    ctx.json(obtener); 
		});
		
		
		
		app.get("/mostrarSeries", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");

		  
		    ArrayList<Serie> obtener = servicio.obtenerTodasSeries();
		    contexto.json(obtener); 
		});
		
		
		
    }
}

