package objetosFront;

import java.time.LocalDateTime;
import java.util.List;

import entidades.Comentario;

public class ResenaConComentario {
	
    private int id;
    private String contenido;
    private String nombreUsuario; 
    private int idUsuario;
    private LocalDateTime fechaCreacion;
    
    private List<Comentario> comentarios;
    
    
    

//	public ResenaConComentario(int id, String contenido, String nombreUsuario, List<Comentario> comentarios) {
//		
//		this.id = id;
//		this.contenido = contenido;
//		this.nombreUsuario = nombreUsuario;
//		this.comentarios = comentarios;
//	}
	
	
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	
	public int getIdUsuario() {return idUsuario;}
	public void setIdUsuario(int idUsuario) {this.idUsuario = idUsuario;}

	public String getContenido() {return contenido;}
    public void setContenido(String contenido) {this.contenido = contenido;}

	public String getNombreUsuario() {return nombreUsuario;}
	public void setNombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario;}
	
	public LocalDateTime getFechaCreacion() {return fechaCreacion;}
	public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}

	public List<Comentario> getComentarios() {return comentarios;}
	public void setComentarios(List<Comentario> comentarios) {this.comentarios = comentarios;}

    
}
