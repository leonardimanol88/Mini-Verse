package entidades;

import java.time.LocalDateTime;

public class PuntuacionSerie {

	private int idUsuario;
	private int idSerie;
	private double puntuacion;
	private LocalDateTime fecha;
	
	
	public PuntuacionSerie(int idUsuario, int idSerie, double puntuacion, LocalDateTime fecha) {
		this.idUsuario = idUsuario;
		this.idSerie = idSerie;
		this.puntuacion = puntuacion;
		this.fecha = fecha;
	}


	public int getIdUsuario() {
		return idUsuario;
	}


	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}


	public int getIdSerie() {
		return idSerie;
	}


	public void setIdSerie(int idSerie) {
		this.idSerie = idSerie;
	}


	public double getPuntuacion() {
		return puntuacion;
	}


	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}


	public LocalDateTime getFecha() {
		return fecha;
	}


	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	
	
	

}
