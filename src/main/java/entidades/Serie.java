 package entidades;


public class Serie {
	
    private int id;
    private String nombre;
    private int estreno;
    private String sinopsis;
    private int id_genero;
    private String imagen_url;
    private int id_director;

    public Serie(int id, String nombre, int estreno, String sinopsis, int id_genero, String imagen_url, int id_director) {
        this.id = id;
        this.nombre = nombre;
        this.estreno = estreno;
        this.sinopsis = sinopsis;
        this.id_genero = id_genero;
        this.id_director = id_director;
        this.imagen_url = imagen_url;
    }

    public Serie(String nombre, int estreno, String sinopsis, int id_genero, int id_director, String imagen_url) {
        this.nombre = nombre;
        this.estreno = estreno;
        this.sinopsis = sinopsis;
        this.id_genero = id_genero;
        this.id_director = id_director;
        this.imagen_url = imagen_url;
    }

 
    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public int getEstreno() { return this.estreno; }
    public String getSinopsis() { return this.sinopsis; }
    public int getIdGenero() { return this.id_genero; }
    public String getimagenUrl() { return this.imagen_url; }
    public int getIdDirector() { return this.id_director;} 
    
    
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEstreno(int estreno) { this.estreno = estreno; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }
    public void setIdGenero(int idGenero) { this.id_genero = idGenero; }
    public void setimagenUrl(String imagenurl) { this.imagen_url = imagenurl;}
    public void setIdDirector(int idDirector) { this.id_director = idDirector;} 
}
