package main;

import io.javalin.Javalin;
import org.mindrot.jbcrypt.BCrypt;
import io.javalin.http.staticfiles.Location;
import controladorApi.ControladorUsuario;
import entidades.Usuario;
import controladorApi.ControladorAdministrador;
import controladorApi.ControladorSerie;
import controladorApi.ControladorResena;

public class Main {
    public static void main(String[] args) {
    	
//    	
          
          Javalin app = Javalin.create(config -> {
       	   
        	  
        	   
        	 /*
        	   //configuraciones 
        	    config.staticFiles.add(staticFileConfig -> {
        	        staticFileConfig.hostedPath = "/";
        	        staticFileConfig.directory = "/public";
        	        staticFileConfig.location = Location.CLASSPATH;
        	    });
        	  */
        	  
        	    config.plugins.enableCors(cors -> {
        	    	cors.add(it -> {
                        it.anyHost();
                        it.exposeHeader("Authorization");
                        it.allowCredentials = true;
         	   
        	        });
        	    });
        	    
        	    
        	    
        	    
           
       });
          
//       app.before("/admin/*", contexto -> {/////////
//  			
//  		    Usuario u = contexto.sessionAttribute("usuario");
//  		    
//  		    if (u == null || !"admin".equals(u.getRol())) {
//  		        contexto.status(403).result("Acceso denegado");
//  		        return;
//  		    }
//  		});
          
          
               
       new ControladorUsuario(app); //registrar rutas //al probarsolo se pone la ruta del api (endpoints /actualizarContrasena)
       new ControladorAdministrador(app);
       new ControladorSerie(app);
       new ControladorResena(app);       
       app.start("0.0.0.0", 7002);
     
    
          
        System.out.println("Servidor iniciado en http://44.209.91.221:7002");
        

    }
}
