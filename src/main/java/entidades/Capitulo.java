package entidades;

import java.time.LocalTime;

public class Capitulo {

	private int id;
	private String titulo;
	private int numero;
	private LocalTime duracion;
	private int id_temporada;
	
	public Capitulo (int id, String titulo, int numero, LocalTime duracion, int id_temporada) {
		this.id = id;
		this.titulo = titulo;
		this.numero = numero;
		this.duracion = duracion;
		this.id_temporada = id_temporada;
	}
	
	public Capitulo (String titulo, int numero, LocalTime duracion, int id_temporada) {
		this.titulo = titulo;
		this.numero = numero;
		this.duracion = duracion;
		this.id_temporada = id_temporada;
	}
	
	
    public int getId() { return this.id; }
    public String getTitulo() { return this.titulo; }
    public int getNumero() { return this.numero; }
    public LocalTime getDuracion() { return this.duracion; }
    public int getIdTemporada() { return this.id_temporada; }
}

