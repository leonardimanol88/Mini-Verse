package entidades;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Comentario {

	private int id;
	private String contenido;
	private LocalDateTime fecha_creacion;
	private int id_usuario;
	private int id_resena;
	private String nombreUsuario;
	
	public Comentario (int id, String contenido, LocalDateTime fecha_creacion, int id_usuario, int id_resena) {
		
		this.id = id;
		this.contenido = contenido;
		this.fecha_creacion = fecha_creacion;
		this.id_usuario = id_usuario;
		this.id_resena = id_resena;
		
	}
	
	public Comentario (String contenido, LocalDateTime fecha_creacion,int id_usuario, int id_resena) {
		this.contenido = contenido;
		this.fecha_creacion = fecha_creacion;
		this.id_usuario = id_usuario;
		this.id_resena = id_resena;
	}
	
	
    public int getId() { return this.id; }
    public String getContenido() { return this.contenido; }
    public LocalDateTime getFechaCreacion() { return this.fecha_creacion; }
    public int getIdResena() { return this.id_resena; }
    public int getIdUsuario() { return this.id_usuario; }
    
    
    public String getNombreUsuario() { return this.nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}
