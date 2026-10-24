package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaHidraulica extends PlantaRenovable {
	private int cantidadTurbinas;
	private double cantidadEmbalse;
	public PlantaHidraulica(String nombre, String direccion, double capacidadMaxima, boolean activa,
			LocalDateTime fechaPuestaFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			int cantidadTurbinas, double cantidadEmbalse) {
		super(nombre, direccion, capacidadMaxima, activa, fechaPuestaFuncionamiento, fuentePrimaria, eficienciaPromedio);
		if (cantidadTurbinas <= 0) {
			throw new IllegalArgumentException("La cantidad de turbinas debe ser mayor que cero");
		}
		if (cantidadEmbalse <= 0 || !Double.isFinite(cantidadEmbalse)) {
			throw new IllegalArgumentException("La cantidad de embalse debe ser mayor que cero y finito");
		}
		this.cantidadTurbinas = cantidadTurbinas;
		this.cantidadEmbalse = cantidadEmbalse;
		this.eficienciaPromedio = eficienciaPromedio;
	}
	@Override
	public double cuotaMinima() {
		double cuotaMin = this.capacidadMaxima * this.eficienciaPromedio;
		return cuotaMin;
	}
	@Override
	public boolean cumpleCuota(Monitoreo registro) {
		if (registro == null) {
		    throw new IllegalArgumentException(
		        "El monitoreo no puede ser null."
		    );
		}
			return registro.energiaGenerada() >= cuotaMinima();
	}
	public int cantidadTurbinas() {
		return this.cantidadTurbinas;}
	public double cantidadEmbalse() {
		return this.cantidadEmbalse;
	}
	public String fuentePrimaria() {
		return this.fuentePrimaria;
	}
	public double eficienciaPromedio() {
		return this.eficienciaPromedio;
	}
}
