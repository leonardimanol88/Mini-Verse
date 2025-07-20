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
import entidades.Resena;
import entidades.Comentario;
import objetosFront.ActualizarContrasena;
import objetosFront.EliminarUsuario;
import objetosFront.FavoritaDatos;
import objetosFront.Login;
import servicio.ServicioUsuario;
import util.JWTUtil;

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

		    
		    
		    
		    ActualizarContrasena datos = new Gson().fromJson(contexto.body(), ActualizarContrasena.class);

		    boolean seCambio = servicio.actualizarContrasena(idUsuario, datos.contrasena, datos.nuevaContrasena);

		    if (seCambio) {
		        contexto.json(Map.of("mensaje", "Contrasena actualizada correctamente"));
		    } else {
		        contexto.status(400).json(Map.of("error", "No se pudo actualizar"));
		    }
		});

		
		
		app.delete("/eliminarMiUsuario", contexto -> { 
		    contexto.req().setCharacterEncoding("UTF-8");
		    
		    
		    
		    ///recibe lo del header , el token creado cuando se inicio sesion
		    String header = contexto.header("Authorization");

		    if (header == null || !header.startsWith("Bearer ")) {
		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
		        return;
		    }
            ///verifica el token
		    String token = header.replace("Bearer ", "");
		    Integer idUsuario = JWTUtil.verificarToken(token);

		    if (idUsuario == null) {
		        contexto.status(401).json(Map.of("error", "Token invalido o expirado"));
		        return;
		    }
		
		    
		    
		    
		    EliminarUsuario datos = new Gson().fromJson(contexto.body(), EliminarUsuario.class);
		
		    boolean seCambio = servicio.eliminarUsuario(idUsuario, datos.contrasena);
		
		    if (seCambio) {
		        contexto.json(Map.of("mensaje", "cuenta eliminada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo eliminar"));
		    }
		});
		
		
		/*  ///antiguo inicio de sesion
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
		*/
		
		app.post("/iniciarSesion", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");
		    
		    
		    

		    Login datos = new Gson().fromJson(contexto.body(), Login.class);

		    Usuario usuario = servicio.devolverUsuario(datos.correo, datos.contrasena);

		    if (usuario != null) {
		        String token = JWTUtil.crearToken(usuario.getId()); //se crea el token con el metodo de jwtutil
		        contexto.json(Map.of(
		            "mensaje", "Inicio de sesion exitoso",
		            "token", token  //devuelvo el token al front
		        ));
		    } else {
		        contexto.status(401).json(Map.of(
		            "error", "Correo o contrasena incorrecto"
		        ));
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
		
		
		app.get("/mostrarSeriesFavoritas", contexto -> {
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
		    
		    
		  
		    ArrayList<Serie> obtenerFavoritas = servicio.obtenerTodasFavoritas(idUsuario);
		    contexto.json(obtenerFavoritas); 
		});
		
		
		
		app.get("/mostrarUltimasResenas", contexto -> {
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
		    
		    
		  
		    ArrayList<Resena> obtenerUltimas = servicio.obtenerUltimasResenas(idUsuario);
		    contexto.json(obtenerUltimas); 
		   
		});
		
		
		app.get("/mostrarUltimosComentarios", contexto -> {
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
		    
		    
		  
		    ArrayList<Comentario> obtenerUltimas = servicio.obtenerUltimosComentarios(idUsuario);
		    contexto.json(obtenerUltimas); 
		});
		
		
		
		
		
		
		
		
    }
}

