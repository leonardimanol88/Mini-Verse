package controladorApi;
import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import entidades.Genero;
import entidades.Serie;
import entidades.Capitulo;
import entidades.Usuario;
import entidades.Resena;
import entidades.Comentario;
import objetosFront.ActualizarContrasena;
import objetosFront.EliminarUsuario;
import objetosFront.HacerResena;
import objetosFront.Login;
import servicio.ServicioUsuario;
import util.JWTUtil;

public class ControladorUsuario {

    public ControladorUsuario(Javalin app) {
        ServicioUsuario servicio = new ServicioUsuario();

       
		app.post("/registrarUsuario", contexto -> { 
		    contexto.req().setCharacterEncoding("UTF-8");
		
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
		
		    EliminarUsuario datos = new Gson().fromJson(contexto.body(), EliminarUsuario.class);
		
		    boolean seCambio = servicio.eliminarUsuario(idUsuario, datos.contrasena);
		
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
		        String token = JWTUtil.crearToken(usuario.getId(), usuario.getRol()); 
		        contexto.json(Map.of(
		            "mensaje", "Inicio de sesion exitoso",
		            "token", token,  
		            "rol", usuario.getRol() 
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
		
		
		app.get("/mostrarMiUsuario", contexto -> {  
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
		    
		    Usuario miUsuario = servicio.obtenerMiUsuario(idUsuario);
		    contexto.json(miUsuario); 
		   
		});
		
		
		app.get("/busquedaSeries", contexto -> {
			
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
		    
		    String nombre = contexto.queryParam("nombre");
		    if (nombre == null || nombre.isEmpty()) {
		        contexto.status(400).result("Falta el parametro de busqueda");
		        return;
		    }
		    
		    List<Serie> resultados = servicio.buscarPorNombre(nombre);
		    
		    if (resultados.isEmpty()) {
		        contexto.status(404).result("No se encontraron series con ese nombre");
		    } else {
		        contexto.json(resultados);
		    }
		   
		});
		
		
		app.post("/hacerResenaUsuario", contexto -> {
			
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
		
		    HacerResena datos = new Gson().fromJson(contexto.body(), HacerResena.class);
		    
		    boolean guardado = servicio.guardarResena(idUsuario, datos.contenido, datos.id_capitulo);
		    
		    if (guardado) {
		        contexto.status(200).result("Resena guardada correctamente");
		    } else {
		        contexto.status(500).result("Error al guardar la resena");
		    }
		});	
		
		
		app.get("/mostrarCapitulosFavoritos", contexto -> {
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
		  
		    ArrayList<Capitulo> obtenerFavoritos = servicio.obtenerTodosFavoritos(idUsuario);
		    contexto.json(obtenerFavoritos); 
		});
		
    }
}

