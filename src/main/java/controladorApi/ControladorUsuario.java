package controladorApi;


import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import com.google.gson.Gson;

import entidades.Usuario;
import objetosFront.ActualizarContrasena;
import objetosFront.EliminarUsuario;
import objetosFront.Login;
import servicio.ServicioUsuario;

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
		
		    boolean seCambio = servicio.actualizarContrasena(datos.id, datos.contrasena, datos.nuevaContrasena);
		
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

		    boolean acceso = servicio.iniciarSesion(datos.correo, datos.contrasena);

		    if (acceso) {
		        contexto.json(Map.of(
		            "mensaje", "Inicio de sesion exitoso"
		        ));
		    } else {
		        contexto.status(401).json(Map.of(
		            "error", "Correo o contrasena incorrectos"
		        ));
		    }
		});
		
		
		
		
    }
}

