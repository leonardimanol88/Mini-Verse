package entidades;

import java.time.LocalTime;

public class Capitulo {

	private int id;
	private String titulo;
	private int numero;
	private LocalTime duracion;
	private int id_temporada;
	private String sinopsis;
	private String imagen_url;
	
	public Capitulo (int id, String titulo, int numero, LocalTime duracion, int id_temporada, String sinopsis, String imagen_url) {
		this.id = id;
		this.titulo = titulo;
		this.numero = numero;
		this.duracion = duracion;
		this.id_temporada = id_temporada;
		this.sinopsis = sinopsis;
		this.imagen_url = imagen_url;
	}
	
	public Capitulo (String titulo, int numero, LocalTime duracion, int id_temporada, String sinopsis, String imagen_url) {
		this.titulo = titulo;
		this.numero = numero;
		this.duracion = duracion;
		this.id_temporada = id_temporada;
		this.sinopsis = sinopsis;
		this.imagen_url = imagen_url;
	}
	
	
    public int getId() { return this.id; }
    public String getTitulo() { return this.titulo; }
    public int getNumero() { return this.numero; }
    public LocalTime getDuracion() { return this.duracion; }
    public int getIdTemporada() { return this.id_temporada; }
    public String getSinopsis() { return this.sinopsis; }
    public String getImagenUrl() { return this.imagen_url; }
}

