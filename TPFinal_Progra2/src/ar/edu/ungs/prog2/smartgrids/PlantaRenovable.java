package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;

public abstract class PlantaRenovable extends Planta {

    protected String fuentePrimaria;
    protected double eficienciaPromedio;

    public PlantaRenovable(
            String nombre,
            String direccion,
            double capacidadMaxima,
            boolean activa,
            LocalDateTime fechaPuestaFuncionamiento,
            String fuentePrimaria,
            double eficienciaPromedio) {

        super(nombre, direccion, capacidadMaxima, activa,
              fechaPuestaFuncionamiento);

        if (fuentePrimaria == null || fuentePrimaria.isBlank()) {
            throw new IllegalArgumentException(
                "La fuente primaria no puede estar vacía."
            );
        }

        if (!Double.isFinite(eficienciaPromedio)
                || eficienciaPromedio <= 0
                || eficienciaPromedio > 1) {
            throw new IllegalArgumentException(
                "La eficiencia debe ser mayor que 0 y menor o igual a 1."
            );
        }
        this.fuentePrimaria = fuentePrimaria;
        this.eficienciaPromedio = eficienciaPromedio;
    }

	protected abstract String fuentePrimaria();
}