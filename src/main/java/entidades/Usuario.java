package entidades;


public class Usuario {
	
    private int id;
    private String nombre;
    private String correo;
    private String contrasena;
    private int edad;
    
    //doble constructor uno para acceder a un usurio ya creado(que en base de datos ya tenga un id) y otro para cuando se crea un nuevo
    public Usuario(int id, String nombre, String correo, String contrasena, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.edad = edad;
    }
    

    public Usuario(String nombre, String correo, String contrasena, int edad) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.edad = edad;
    }

    
    public int getId(){return this.id;}
    public String getNombre() { return this.nombre; }
    public String getCorreo() { return this.correo; }
    public String getContrasena() { return this.contrasena; }
    public int getEdad() { return this.edad; }
}
