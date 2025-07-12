package servicio;

import repositorio.RepositorioAdministrador;
import entidades.Serie;

public class ServicioAdministrador {

	RepositorioAdministrador repo = new RepositorioAdministrador();
	
	
	public boolean agregarSerie(Serie serie) {
		
		return repo.agregarSerie(serie);
		
	}

}
