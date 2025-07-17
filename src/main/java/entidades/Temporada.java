package entidades;

public class Temporada {

	private int id;
	private int numeroTemporada;
	private int id_serie;
	private String imagen_url;
	private String nombre;
	private String descripcion;
	
	public Temporada (int id, int numeroTemporada, int id_serie ,String imagen_url, String nombre, String descripcion) {
		this.id = id;
		this.numeroTemporada = numeroTemporada;
		this.id_serie = id_serie;
		this.imagen_url = imagen_url;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	/*
	public Temporada ( int numeroTemporada, int id_serie) {
		this.numeroTemporada = numeroTemporada;
		this.id_serie = id_serie;
	}
	*/
	
	public Temporada ( int id, int numeroTemporada ,String imagen_url, String nombre, String descripcion) {
		this.id = id;
		this.numeroTemporada = numeroTemporada;
		
		this.imagen_url = imagen_url;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	
    public int getId() { return this.id; }
    public int getNumeroTemporada() { return this.numeroTemporada; }
    public int getIdSerie() { return this.id_serie; }
    public String getImagenUrl() { return this.imagen_url; }
    public String getNombre() { return this.nombre; }
    public String getDescripcion() { return this.descripcion; }
}
