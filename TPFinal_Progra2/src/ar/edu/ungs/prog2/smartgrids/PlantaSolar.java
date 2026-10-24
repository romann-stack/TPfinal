package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaSolar extends PlantaRenovable {
	private double superficieCubierta;
	private int cantidadPaneles;
	private double horasDeRadiacion;
	public PlantaSolar(String nombre, String direccion, double capacidadMaxima, boolean activa,
			LocalDateTime fechaPuestaFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			double superficieCubierta, int cantidadPaneles, double horasDeRadiacion) {
		super(nombre, direccion, capacidadMaxima, activa, fechaPuestaFuncionamiento, fuentePrimaria, eficienciaPromedio);
		if (superficieCubierta < 0 || !Double.isFinite(superficieCubierta)) {
			throw new IllegalArgumentException("La superficie cubierta no puede ser negativa o infinito");
		}
		if (cantidadPaneles <= 0) {
			throw new IllegalArgumentException("La cantidad de paneles debe ser mayor que cero.");
		}
		if (horasDeRadiacion <= 0) {
			throw new IllegalArgumentException("Las horas de radiacion deben ser mayor que cero.");
		}
		this.superficieCubierta = superficieCubierta;
		this.cantidadPaneles = cantidadPaneles;
		this.horasDeRadiacion = horasDeRadiacion;
		this.fuentePrimaria = fuentePrimaria;
		this.eficienciaPromedio = eficienciaPromedio;
	}

	@Override
	public double cuotaMinima() {
		double cuotaMin = (this.capacidadMaxima * this.eficienciaPromedio * this.horasDeRadiacion)/24;
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
	public double superficieCubierta() {
		return this.superficieCubierta;
	}
	public int cantidadPaneles() {
		return this.cantidadPaneles;
	}
	public double horasDeRadiacion() {
		return this.horasDeRadiacion;
	}
	public String fuentePrimaria() {
		return this.fuentePrimaria;
	}
	public double eficienciaPromedio() {
		return this.eficienciaPromedio;
	}
}
