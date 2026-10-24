package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoTermico extends Monitoreo {
	private double cantidadCO2;
	private double combustibleUtilizado;
	public MonitoreoTermico(Planta planta, LocalDateTime fecha, double energiaGenerada, int cantidadIncidencias,
			 double cantidadCO2, double combustibleUtilizado ) {
		super(planta, fecha, energiaGenerada, cantidadIncidencias);
		if (cantidadCO2 < 0 || !Double.isFinite(cantidadCO2)) {
			throw new IllegalArgumentException("La cantidad de CO2 debe "
					+ "ser mayor o igual que cero y finito");
		}
		if (combustibleUtilizado < 0 || !Double.isFinite(combustibleUtilizado)) {
			throw new IllegalArgumentException("El combustible utilizado debe "
					+ "ser mayor o igual que cero y finito");
		}
		this.cantidadCO2 = cantidadCO2;
		this.combustibleUtilizado = combustibleUtilizado;
	}
	public double cantidadCO2() {
		return this.cantidadCO2;
	}
	public double combustibleUtilizado() {
		return this.combustibleUtilizado;
	}

}
