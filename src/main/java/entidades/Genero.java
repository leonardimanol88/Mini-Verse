package entidades;

public class Genero {
	
	private int id;
	private String nombre;

	public Genero(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	public Genero(String nombre) {
		this.nombre = nombre;
	}
	
	public int getId() {return this.id;}
	public String getNombre() {return this.nombre;}

}
