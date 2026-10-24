package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public class PlantaTermica extends Planta{
		private String tipoCombustible;
		private double objetivoEmisionesCO2;
		private double capacidadAlmacenamientoCombustible;
		/*
		Atributos propios: tipo de combustible, objetivo de emisiones de CO2 por hora y capacidad de 
		almacenamiento de combustible. 
		• cuotaMinima() : double. Es la mitad de la capacidad máxima. 
		5 
		• cumpleCuota(registro) : boolean. Redefinido: pide energía mayor o igual a cuotaMinima() y 
		CO2 menor al objetivo por hora. El registro que recibe es un RegistroTermico.*/
	public PlantaTermica(String nombre, String direccion, double capacidadMaxima, boolean activa,
			LocalDateTime fechaPuestaFuncionamiento,  String tipoCombustible, double objetivoEmisionesCO2,
			 double capacidadAlmacenamientoCombustible) {
		super(nombre, direccion, capacidadMaxima, activa, fechaPuestaFuncionamiento);
		if (tipoCombustible == null || tipoCombustible.isBlank()) {
			throw new IllegalArgumentException("El tipo de combustible debe figurar.");
		}
		if (objetivoEmisionesCO2 < 0 || !Double.isFinite(objetivoEmisionesCO2)) {
			throw new IllegalArgumentException("El objetivo de emisiones de CO2 debe "
					+ "ser mayor o igual que cero y finito");
		}
		if (capacidadAlmacenamientoCombustible < 0 || !Double.isFinite(capacidadAlmacenamientoCombustible)) {
			throw new IllegalArgumentException("La capacidad de almacenamiento de combustible debe "
					+ "ser mayor o igual que cero y finito");
		}
		this.tipoCombustible = tipoCombustible;
		this.objetivoEmisionesCO2 = objetivoEmisionesCO2;
		this.capacidadAlmacenamientoCombustible = capacidadAlmacenamientoCombustible;
	}

	@Override
	public double cuotaMinima() {
		double cuotaMin = this.capacidadMaxima/2;
		return cuotaMin;
	}
	@Override
	public boolean cumpleCuota(Monitoreo registro) {
	    if (registro == null) {
	        throw new IllegalArgumentException(
	            "El monitoreo no puede ser null."
	        );
	    }

	    if (!(registro instanceof MonitoreoTermico)) {
	        throw new IllegalArgumentException(
	            "El registro debe ser un monitoreo térmico."
	        );
	    }
	    MonitoreoTermico registroTermico = (MonitoreoTermico) registro;

	    return registroTermico.energiaGenerada() >= cuotaMinima()
	        && registroTermico.cantidadCO2() < this.objetivoEmisionesCO2;
	}
	public String tipoCombustible() {
		return this.tipoCombustible;
	}
	public double objetivoEmisionesCO2() {
		return this.objetivoEmisionesCO2;
	}
	public double capacidadAlmacenamientoCombustible() {
		return this.capacidadAlmacenamientoCombustible;
	}
}
