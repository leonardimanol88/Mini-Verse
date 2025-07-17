package util; ///clase para lo del login con jwt 

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;



public class JWTUtil {

    private static final String strin = "mar55"; 
    private static final Algorithm algoritmo = Algorithm.HMAC256(strin);

    
    
    
    public static String crearToken(int idUsuario) {
    	
        return JWT.create()
                .withClaim("id", idUsuario)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hora
                .sign(algoritmo);
    }

    
    public static Integer verificarToken(String token) {
    	
        try {
            JWTVerifier verificador = JWT.require(algoritmo).build();
            
            DecodedJWT jwt = verificador.verify(token); //si esta bien lo guarda en un objeto jwt
            return jwt.getClaim("id").asInt(); //accede al atributo y lo devuelve
        } 
        catch (Exception e) {
            return null;
        }
    }
}
