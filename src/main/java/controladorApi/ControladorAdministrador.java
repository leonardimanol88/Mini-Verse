package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

import entidades.Serie;
import entidades.Genero;
import entidades.Usuario;
import entidades.Resena;
import entidades.Comentario;
import entidades.Director;
import objetosFront.AgregarSeries;
import objetosFront.AgregarDirector;
import objetosFront.AgregarGenero;
import objetosFront.AgregarTemporada;
import objetosFront.Login;
import objetosFront.CapituloEstadistica;
import objetosFront.CapituloEstadisticaEdad;
import objetosFront.AgregarCapitulo;
import servicio.ServicioAdministrador;
import util.JWTUtil;


public class ControladorAdministrador {

    public ControladorAdministrador(Javalin app) {
        ServicioAdministrador servicio = new ServicioAdministrador();
        
        
//        app.post("/admin/iniciarSesion", contexto -> {
//        	
//        	
//            contexto.req().setCharacterEncoding("UTF-8");
//
//            Login datos = new Gson().fromJson(contexto.body(), Login.class);
//            Usuario usuario = servicio.devolverUsuario(datos.correo, datos.contrasena);
//
//            
//            if (usuario != null && "admin".equals(usuario.getRol())) {
//            	
//            	
//            	
//                String token = JWTUtil.crearToken(usuario.getId());
//                contexto.json(Map.of(
//                    "mensaje", "Inicio de sesion exitoso",
//                    "token", token
//                ));
//            } else {
//                contexto.status(401).json(Map.of(
//                    "error", "no eres administrador"
//                ));
//            }
//        });

        
	    
        app.post("/admin/agregarDirector", contexto -> {///////////
		    contexto.req().setCharacterEncoding("UTF-8");
		    
//		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		
		    
		    AgregarDirector nuevoDirector = new Gson().fromJson(contexto.body(), AgregarDirector.class);
		
		    boolean seAgrego = servicio.agregarDirector(nuevoDirector.nombre, nuevoDirector.biografia);
		
		    if (seAgrego) {
		        contexto.json(Map.of("mensaje", "Director agregado correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar el director"));
		    } 
		});
        
		
		
		app.post("/admin/agregarGenero", ctx -> {/////////
		    ctx.req().setCharacterEncoding("UTF-8");
		
		    
//            String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		    
		    
		    
		    AgregarGenero nuevoGenero = new Gson().fromJson(ctx.body(), AgregarGenero.class);
		
		    boolean seAgrego = servicio.agregarGenero(nuevoGenero.nombre);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "genero agregado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar el genero"));
		    } 
		});
		
        app.get("/admin/obtenerGeneros", ctx -> { ////////
        	ctx.req().setCharacterEncoding("UTF-8");
        	
//            String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
			
		    

		    ArrayList<Genero> obtener = servicio.obtenerGeneros();
		    
		    ctx.json(obtener); 
		});
        
		
        app.delete("/admin/eliminarGenero/{nombre}", ctx -> {///////
        	ctx.req().setCharacterEncoding("UTF-8");
        	
//           String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
			
		    String nombre = ctx.pathParam("nombre");

		    boolean seElimino = servicio.eliminarGenero(nombre);

		    if (seElimino) {
		        ctx.json(Map.of("mensaje", "genero eliminado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo eliminar el genero"));
		    }
		});

        ///////////////////////
		
		app.post("/admin/agregarSerie", contexto -> {/////////
		contexto.req().setCharacterEncoding("UTF-8");               

//		String header = contexto.header("Authorization");
//	    
//	    if (header == null || !header.startsWith("Bearer ")) {
//	        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//	        return;
//	    }
//	    String token = header.replace("Bearer ", "").trim();
//	    String rol = JWTUtil.obtenerRol(token);
//
//	    if (rol == null || !rol.equals("admin")) {
//	        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//	        return;
//	    }
		
		    AgregarSeries nuevaSerie = new Gson().fromJson(contexto.body(), AgregarSeries.class);
		
		    boolean seAgrego = servicio.agregarSerie(nuevaSerie.nombre, nuevaSerie.estreno, nuevaSerie.sinopsis, nuevaSerie.idGenero, nuevaSerie.idDirector, nuevaSerie.imagenUrl);
		
		    if (seAgrego) {
		        contexto.json(Map.of("mensaje", "Serie agregada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo agregar la serie"));
		    } 
		});
		
		
		
		app.get("/admin/obtenerSeries", ctx -> { //////
			ctx.req().setCharacterEncoding("UTF-8");
			
//            String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		   

		    ArrayList<Serie> obtener = servicio.obtenerSeries();
		    
		    ctx.json(obtener); 
		});
		
		
		
		app.delete("/admin/eliminarSerie/{nombre}", ctx -> {//////
			ctx.req().setCharacterEncoding("UTF-8");
//			
//           String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
			
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
		    
		    
//           String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		
		    
		    AgregarTemporada nuevaTemporada = new Gson().fromJson(ctx.body(), AgregarTemporada.class);
		
		    boolean seAgrego = servicio.agregarTemporada(nuevaTemporada.numero, nuevaTemporada.nombreSerie, nuevaTemporada.imagen_url, nuevaTemporada.nombreTemporada, nuevaTemporada.descripcion);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "Temporada agregada correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar la temporada"));
		    } 
		});
		
		
        app.delete("/admin/eliminarTemporada", ctx -> {////
        	ctx.req().setCharacterEncoding("UTF-8");
        	
//           String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
			
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
		    
		    
//            String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		
		    
		    AgregarCapitulo nuevoCapitulo = new Gson().fromJson(ctx.body(), AgregarCapitulo.class);
		
		    boolean seAgrego = servicio.agregarCapitulo(nuevoCapitulo.titulo, nuevoCapitulo.numero, nuevoCapitulo.duracion, nuevoCapitulo.nombreSerie, nuevoCapitulo.temporada);
		
		    if (seAgrego) {
		        ctx.json(Map.of("mensaje", "Capitulo agregado correctamente"));
		    } else {
		        ctx.status(500).json(Map.of("error", "No se pudo agregar el capitulo"));
		    } 
		});
        
        
        
        app.delete("/admin/eliminarCapitulo", ctx -> {//////
        	ctx.req().setCharacterEncoding("UTF-8");
        	
//            String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
        	
			
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
		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

		  
		    ArrayList<Usuario> obtener = servicio.obtenerUsuarios();
		    contexto.json(obtener); 
		});
		
		
		app.get("/admin/mostrarSeriesAdmin", contexto -> {
		    contexto.req().setCharacterEncoding("UTF-8");
		    
//           String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

		  
		    ArrayList<Serie> obtener = servicio.obtenerSeries();
		    contexto.json(obtener); 
		});
		
		
//		
//		app.get("/admin/gestionResenas", contexto -> {/////
//		
//			
//		    contexto.req().setCharacterEncoding("UTF-8");
//		    
//		    
//		    
//		    String header = contexto.header("Authorization");
//
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//
//		    String token = header.replace("Bearer ", "");
//		    Integer idUsuario = JWTUtil.verificarToken(token);
//
//		    if (idUsuario == null) {
//		        contexto.status(401).json(Map.of("error", "Token invalido o expirado"));
//		        return;
//		    }
//		    
//		    
//
//		  
//		    ArrayList<Usuario> obtener = servicio.obtenerUsuarios();
//		    ArrayList<Resena> obtenerResenas = servicio.obtenerResenas(idUsuario);
//		    contexto.json(obtener); 
//		});
//		
		
		
		app.get("/admin/capitulosMasResenados", contexto -> { //da una lista de capitulos desde los mas resenados hasta los menos
		    contexto.req().setCharacterEncoding("UTF-8");
		    
		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

		  
		    List<CapituloEstadistica> obtener = servicio.obtenerCapitulosMasResenados(); 
		    contexto.json(obtener);                                                     
		});
		
		
		
		app.get("/admin/capitulosMasResenadosEdad", contexto -> { ////da una lista de capitulos desde los mas resenados hasta los menos
		    contexto.req().setCharacterEncoding("UTF-8");         /// pero ahora en cada capitulo tambien da la edad promerio de los resenadores en cada capitulo

//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		    
		    List<CapituloEstadisticaEdad> obtener = servicio.obtenerCapitulosMasResenadosEdad();
		    contexto.json(obtener); 
		});
		
		
		
        app.get("/admin/obtenerDirectores", ctx -> {
			
			
		    ctx.res().setCharacterEncoding("UTF-8");
		    
		    
		    
//           String header = ctx.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        ctx.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        ctx.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

		    ArrayList<Director> obtener = servicio.obtenerDirectores();
		    
		    ctx.json(obtener); 
		});
        
        
        
        ///////
        
        
        app.get("/admin/busquedaUsuarios", contexto -> {
			
		    contexto.req().setCharacterEncoding("UTF-8");
		    
		    
//		    String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//           
//		    
//		    String token = header.replace("Bearer ", "");
//		    Integer idUsuario = JWTUtil.verificarToken(token);
//
//		    if (idUsuario == null) {
//		        contexto.status(401).json(Map.of("error", "Token invalido o expirado"));
//		        return;
//		    }
//		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		    
		    String nombre = contexto.queryParam("nombre");
		    if (nombre == null || nombre.isEmpty()) {
		        contexto.status(400).result("Falta el parametro de busqueda");
		        return;
		    }
		    
		    List<Usuario> resultados = servicio.buscarUsuariosPorNombre(nombre);
		    
		    if (resultados.isEmpty()) {
		        contexto.status(404).result("No se encontraron usuarios con ese nombre");
		    } else {
		        contexto.json(resultados);
		    }
		   
		});
        
        
        
        app.get("/admin/mostrarUsuario", contexto -> {/////
		    contexto.req().setCharacterEncoding("UTF-8");
		    
		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

		  
            String idParam = contexto.queryParam("id");
		    
		    
		    if (idParam == null || idParam.isEmpty()) {
		        contexto.status(400).result("El parametro de usuario es requerido");
		        return;
		    }

		   
		    	
		        int idUsuario = Integer.parseInt(idParam); 
		        Usuario u = servicio.mostrarUsuario(idUsuario);
		        
		        contexto.json(u);
		    
		});
        
        
        
       app.get("/admin/obtenerResenasdelUsuario", contexto -> { ////////
			
			
		    contexto.res().setCharacterEncoding("UTF-8");
		    
		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
//		    
		    
		    String idParam = contexto.queryParam("id");
		    
		    
		    if (idParam == null || idParam.isEmpty()) {
		        contexto.status(400).result("El parametro de usuario es requerido");
		        return;
		    }

		    try {
		    	
		        int idUsuario = Integer.parseInt(idParam); 
		        List<Resena> resenas = servicio.obtenerResenasPorUsuario(idUsuario);
		        
		        contexto.json(resenas);
		    } catch (NumberFormatException e) {
		        contexto.status(400).result("El parametro id debe ser un numero entero ");
		    }
		});
      
      
      app.get("/admin/obtenerComentariosdelUsuario", contexto -> { ////////
			
			
		    contexto.res().setCharacterEncoding("UTF-8");
		    
//            String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
		    
		    
		    String idParam = contexto.queryParam("id");
		    
		    
		    if (idParam == null || idParam.isEmpty()) {
		        contexto.status(400).result("El parametro de usuario es requerido");
		        return;
		    }

		    try {
		    	
		        int idUsuario = Integer.parseInt(idParam); 
		        List<Comentario> comentarios = servicio.obtenerComentariosPorUsuario(idUsuario);
		        
		        contexto.json(comentarios);
		    } catch (NumberFormatException e) {
		        contexto.status(400).result("El parametro id debe ser un numero entero ");
		    }
		});
        
      
      
      app.delete("/admin/eliminarUsuario", contexto -> {////
			
    	  
//    	  String header = contexto.header("Authorization");
		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

    	    String idParam = contexto.queryParam("id");
		    
		    
		    if (idParam == null || idParam.isEmpty()) {
		        contexto.status(400).result("El parametro de usuario es requerido");
		        return;
		    }
		    	
		    int idUsuario = Integer.parseInt(idParam); 
		
		
		    
		    
		    boolean seElimino = servicio.eliminarUsuario(idUsuario);

		    if (seElimino) {
		        contexto.json(Map.of("mensaje", "usuario eliminado correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo eliminar el usuario"));
		    }  
		});
      
      
      app.delete("/admin/eliminarResena", contexto -> {////
			
//    	  String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }

  	    String idParam = contexto.queryParam("id");
		    
		    
		    if (idParam == null || idParam.isEmpty()) {
		        contexto.status(400).result("El parametro id de resena es requerido");
		        return;
		    }
		    	
		    int idResena = Integer.parseInt(idParam); 
		
		
		    
		    
		    boolean seElimino = servicio.eliminarResena(idResena);

		    if (seElimino) {
		        contexto.json(Map.of("mensaje", "resena eliminada correctamente"));
		    } else {
		        contexto.status(500).json(Map.of("error", "No se pudo eliminar la resena"));
		    }  
		});
      
      
      app.delete("/admin/eliminarComentario", contexto -> {////
			

//    	  String header = contexto.header("Authorization");
//		    
//		    if (header == null || !header.startsWith("Bearer ")) {
//		        contexto.status(401).json(Map.of("error", "falta el token de autorizacion"));
//		        return;
//		    }
//		    String token = header.replace("Bearer ", "").trim();
//		    String rol = JWTUtil.obtenerRol(token);
//
//		    if (rol == null || !rol.equals("admin")) {
//		        contexto.status(403).json(Map.of("error", "No autorizado solo admins"));
//		        return;
//		    }
//    	  
    	    String idParam = contexto.queryParam("id");
  		    
  		    
  		    if (idParam == null || idParam.isEmpty()) {
  		        contexto.status(400).result("El parametro id de comentario es requerido");
  		        return;
  		    }
  		    	
  		    int idComentario = Integer.parseInt(idParam); 
  		
  		
  		    
  		    
  		    boolean seElimino = servicio.eliminarComentario(idComentario);

  		    if (seElimino) {
  		        contexto.json(Map.of("mensaje", "comentario eliminado correctamente"));
  		    } else {
  		        contexto.status(500).json(Map.of("error", "No se pudo eliminar el comentario"));
  		    }  
  		});
      
        
        
        
//        
//       app.get("/admin/obtenerResenasPorUsuario", ctx -> {
//			
//			
//		    ctx.res().setCharacterEncoding("UTF-8");
//		    
//		    int idUsuario = ctx.pathParam("idUsuario");
//
//		    ArrayList<Director> obtener = servicio.obtenerResenasPorUsuario(idUsuario);
//		    
//		    ctx.json(obtener); 
//		});
		
        
		
		
    }
}
