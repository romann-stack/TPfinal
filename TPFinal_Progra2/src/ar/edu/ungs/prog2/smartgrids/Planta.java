package ar.edu.ungs.prog2.smartgrids;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class Planta {
    
    protected String nombre;
    protected String direccion;
    protected LocalDateTime  fechaPuestaFuncionamiento;
    protected double capacidadMaxima;
    private boolean activa;
    private List<PeriodoInactividad> historialInactividad;
    private PeriodoInactividad inactividadActual;
    
    public Planta(String nombre, String direccion,
            double capacidadMaxima, boolean activa, LocalDateTime fechaPuestaFuncionamiento) {

    	if (nombre == null || nombre.isBlank()) {
    		throw new IllegalArgumentException("Nombre vacío.");
    	}

    	if (direccion == null || direccion.isBlank()) {
    		throw new IllegalArgumentException("Dirección vacía.");
    	}

    	if (!Double.isFinite(capacidadMaxima) || capacidadMaxima <= 0) {
    		throw new IllegalArgumentException(
    				"La capacidad máxima debe ser mayor a cero."
    				);
    	}
    	if (fechaPuestaFuncionamiento == null) {
    		throw new IllegalArgumentException(
    				"La fecha de puesta en funcionamiento no puede ser null."
    				);
    	}
    	if (!activa) {
    	    this.inactividadActual =
    	        new PeriodoInactividad(fechaPuestaFuncionamiento);
    	}

 	this.nombre = nombre;
 	this.direccion = direccion;
 	this.capacidadMaxima = capacidadMaxima;
 	this.activa = activa;
 	this.fechaPuestaFuncionamiento = fechaPuestaFuncionamiento;
 	this.historialInactividad = new ArrayList<>();
}
    
    protected String nombre() {
		return this.nombre; 
    }
    
    protected String direccion() {
		return this.direccion; 
    }
    
    protected double capacidadMaxima() {
        return this.capacidadMaxima;
    }
    
    public boolean estaActiva() {
        return this.activa;
    }
    
    // Al ser una clase abstracta, estos métodos se definen sin cuerpo (sin llaves).
    // Cada subclase (Ej: PlantaSolar) deberá implementarlos obligatoriamente.
    public abstract double cuotaMinima();
    
    // Este método recibirá un registro de monitoreo para evaluar si cumplió.
    public abstract boolean cumpleCuota(Monitoreo registro);
    
    public void desactivar(String fechaHoraString) {
        if (this.activa) {
            try {
            	LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString);
            	this.inactividadActual = new PeriodoInactividad(fechaHora);      	
            	this.activa = false;
            }
            catch(DateTimeParseException d){
            	throw new IllegalArgumentException("Error: Formato de fecha inválido.");
            }
            // Abrimos un nuevo período de inactividad
           
        	}
        }
    
    public void activar(String fechaHoraString) {
        if (!this.activa) {
            try {
            	LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString);
                if (this.inactividadActual != null) {
                	if (fechaHora.isBefore(inactividadActual.inicio())) {
                	    throw new IllegalArgumentException(
                	        "La fecha de activación no puede ser anterior a la desactivación."
                	    );
                	}
                	this.inactividadActual.setFin(fechaHora);
                    this.historialInactividad.add(this.inactividadActual);
                    this.inactividadActual = null;    	
                }
                this.activa = true;
                } 
            catch(DateTimeParseException d){
            	throw new IllegalArgumentException("Error: Formato de fecha inválido.");
            }
        }
     }
    public List<PeriodoInactividad> historialInactividad() {
		return List.copyOf(this.historialInactividad);
	}
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return this.nombre + " (" +
               (this.activa ? "ACTIVA" : "INACTIVA") +
               "): " + this.fechaPuestaFuncionamiento.format(formatter);
    }
    public boolean estaActivaEn(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException(
                "La fecha no puede ser null."
            );
        }

        if (fecha.isBefore(fechaPuestaFuncionamiento)) {
            return false;
        }
        for (PeriodoInactividad periodo : historialInactividad) {
            if (!fecha.isBefore(periodo.inicio())
                    && fecha.isBefore(periodo.fin())) {
                return false;
            }
        }
        if (inactividadActual != null
                && !fecha.isBefore(inactividadActual.inicio())) {
            return false;
        }

        return true;
    }
}