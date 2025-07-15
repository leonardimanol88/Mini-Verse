package entidades;

public class Temporada {

	private int id;
	private int numeroTemporada;
	private int id_serie;
	
	public Temporada (int id, int numeroTemporada, int id_serie) {
		this.id = id;
		this.numeroTemporada = numeroTemporada;
		this.id_serie = id_serie;
	}
	
	public Temporada ( int numeroTemporada, int id_serie) {
		this.numeroTemporada = numeroTemporada;
		this.id_serie = id_serie;
	}
	
	
    public int getId() { return this.id; }
    public int getNumeroTemporada() { return this.numeroTemporada; }
    public int getIdSerie() { return this.id_serie; }
}
