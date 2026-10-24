package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaEolica extends PlantaRenovable{
	private int aeroGeneradores;
	private double alturaTorres;
	private double velocidadMinima;
	public PlantaEolica(String nombre, String direccion, double capacidadMaxima, boolean activa,
			LocalDateTime fechaPuestaFuncionamiento, String fuentePrimaria, double eficienciaPromedio, 
			int aeroGeneradores, double alturaTorres, double velocidadMinima) {
		super(nombre, direccion, capacidadMaxima, activa, fechaPuestaFuncionamiento, fuentePrimaria, eficienciaPromedio);
		if (aeroGeneradores <= 0) {
			throw new IllegalArgumentException("Los aerogeneradores deben ser mayores que cero");
		}
		if (alturaTorres <= 0 || !Double.isFinite(alturaTorres)) {
			throw new IllegalArgumentException("La altura de las torres deben ser mayor que cero y finita");
		}
		if (velocidadMinima <= 0 || !Double.isFinite(velocidadMinima)) {
			throw new IllegalArgumentException("La velocidad minima debe ser mayor que cero y finito");
		}
		this.aeroGeneradores = aeroGeneradores;
		this.alturaTorres = alturaTorres;
		this.velocidadMinima = velocidadMinima;
		this.eficienciaPromedio = eficienciaPromedio;
		this.fuentePrimaria = fuentePrimaria;
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
	public int aeroGeneradores() {
		return this.aeroGeneradores;}
	public double alturaTorres() {
		return this.alturaTorres;
	}
	public double velocidadMinima() {
		return this.velocidadMinima;
	}
	public String fuentePrimaria() {
		return this.fuentePrimaria;
	}
	public double eficienciaPromedio() {
		return this.eficienciaPromedio;
	}
}
