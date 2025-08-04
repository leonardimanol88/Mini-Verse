package entidades;

import java.time.LocalDateTime;

public class Resena {

	private int id;
	private String contenido;
	private LocalDateTime fecha_creacion;
	private int id_capitulo;
	private int id_usuario;
	
	public Resena (int id, String contenido, LocalDateTime fecha_creacion, int id_capitulo, int id_usuario) {
		
		this.id = id;
		this.contenido = contenido;
		this.fecha_creacion = fecha_creacion;
		this.id_capitulo = id_capitulo;
		this.id_usuario = id_usuario;
	}
	
	public Resena (String contenido, LocalDateTime fecha_creacion, int id_capitulo, int id_usuario) {
		this.contenido = contenido;
		this.fecha_creacion = fecha_creacion;
		this.id_capitulo = id_capitulo;
		this.id_usuario = id_usuario;
	}

    public int getId() { return this.id; }
    public String getContenido() { return this.contenido; }
    public LocalDateTime getFechaCreacion() { return this.fecha_creacion; }
    public int getIdCapitulo() { return this.id_capitulo; }
    public int getIdUsuario() { return this.id_usuario; }
}
