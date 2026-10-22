package ar.edu.ungs.prog2.smartgrids;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public abstract class Planta {
    
    protected String nombre;
    protected String direccion;
    protected double capacidadMaxima;
    private boolean activa;
    private List<PeriodoInactividad> historialInactividad;
    private PeriodoInactividad inactividadActual;
    
    public Planta(String nombre, String direccion, double capacidadMaxima, boolean activa) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidadMaxima = capacidadMaxima;
        this.activa = activa;
        this.historialInactividad = new ArrayList<>();
    }
    
    public String nombre() {
        return this.nombre;
    }
    
    public String direccion() {
        return this.direccion;
    }
    
    public double capacidadMaxima() {
        return this.capacidadMaxima;
    }
    
    public boolean estaActiva() {
        return this.activa;
    }
    
    public void cambiarEstado(boolean activa) {
        this.activa = activa;
    }
    
    // Al ser una clase abstracta, estos métodos se definen sin cuerpo (sin llaves).
    // Cada subclase (Ej: PlantaSolar) deberá implementarlos obligatoriamente.
    public abstract double cuotaMinima();
    
    // Este método recibirá un registro de monitoreo para evaluar si cumplió.
    public abstract boolean cumpleCuota(Monitoreo registro);
    
    public void desactivar(String fechaHoraString) {
        if (this.activa) {
            this.activa = false;
            // Parseamos el string al objeto LocalDateTime
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString);
            
            // Abrimos un nuevo período de inactividad
            this.inactividadActual = new PeriodoInactividad(fechaHora);
        }
    }
    
    public void activar(String fechaHoraString) {
        if (!this.activa) {
            this.activa = true;
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString);
            
            // Cerramos el período actual y lo guardamos en el historial
            if (this.inactividadActual != null) {
                this.inactividadActual.setFin(fechaHora);
                this.historialInactividad.add(this.inactividadActual);
                this.inactividadActual = null; // Reseteamos para el futuro
            }
        }
    }
}