package entidades;

public class Director {
	
	private int id;
    private String nombre;
    private String biografia;


	public Director(int id, String nombre, String biografia) {
		this.id = id;
		this.nombre = nombre;
		this.biografia = biografia;
	}
	
	public Director(String nombre, String biografia) {
		this.nombre = nombre;
		this.biografia = biografia;
	}
	
	
	public int getId() {return this.id;}
	public String getNombre() {return this.nombre;}
	public String getBiografia() {return this.biografia;}
	
	
	public void setId(int id) {this.id = id;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public void setBiografia(String biografia) {this.biografia = biografia;}
}
