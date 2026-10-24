package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class Monitoreo {
	private Planta planta; 
	private LocalDateTime fecha;
	private double energiaGenerada;
	private int cantidadIncidencias;
	public Monitoreo(Planta planta, LocalDateTime fecha,
			double energiaGenerada,int cantidadIncidencias) {
		if (planta == null) {
			throw new IllegalArgumentException("La planta no puede ser null.");
		}
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha no puede ser null.");
		}
		if (!Double.isFinite(energiaGenerada) || energiaGenerada < 0) {
			throw new IllegalArgumentException("La energia generada"
					+ " no puede ser infinita o negativa.");
		}
		if (cantidadIncidencias < 0) {
			throw new IllegalArgumentException("La cantidad de incidencias "
					+ "no puede ser negativa.");
		}
		this.planta = planta;
		this.fecha = fecha;
		this.energiaGenerada = energiaGenerada;
		this.cantidadIncidencias = cantidadIncidencias;
	}		
	public int cantidadIncidencias() {
		return this.cantidadIncidencias;
	}
	public double energiaGenerada() {
		return this.energiaGenerada;
	}
	public LocalDateTime fecha() {
		return this.fecha;
		}
	public Planta planta() {
		return this.planta;
	}
	@Override
	public String toString() {
	    return "Monitoreo{" +
	            "planta=" + planta.nombre() +
	            ", fecha=" + fecha +
	            ", energiaGenerada=" + energiaGenerada +
	            ", cantidadIncidencias=" + cantidadIncidencias +
	            '}';
	}
}
