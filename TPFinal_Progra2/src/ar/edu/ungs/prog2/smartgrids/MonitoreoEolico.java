package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class MonitoreoEolico extends Monitoreo{
	private double velocidadVientoPromedio;
	public MonitoreoEolico(Planta planta, LocalDateTime fecha, double energiaGenerada, int cantidadIncidencias,
			double velocidadVientoPromedio) {
		super(planta, fecha, energiaGenerada, cantidadIncidencias);
		if (velocidadVientoPromedio < 0 || !Double.isFinite(velocidadVientoPromedio)) {
			throw new IllegalArgumentException("La velocidad promedio del viento debe "
					+ "ser mayor o igual que cero y finito");
		}
		this.velocidadVientoPromedio = velocidadVientoPromedio;
	}
	public double velocidadVientoPromedio() {
		return this.velocidadVientoPromedio;
	}	
}
