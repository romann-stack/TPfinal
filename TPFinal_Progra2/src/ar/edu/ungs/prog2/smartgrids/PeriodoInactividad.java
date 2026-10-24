package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.util.Objects;

public class PeriodoInactividad {

    private final LocalDateTime inicio;
    private LocalDateTime fin;

    public PeriodoInactividad(LocalDateTime fechaHora) {
        this.inicio = Objects.requireNonNull(
            fechaHora, "La fecha de inicio no puede ser null."
        );
        this.fin = null;
    }

    public LocalDateTime inicio() {
        return this.inicio;
    }

    public LocalDateTime fin() {
        return this.fin;
    }

    public void setFin(LocalDateTime fechaHora) {
        Objects.requireNonNull(
            fechaHora, "La fecha de fin no puede ser null."
        );

        if (this.fin != null) {
            throw new IllegalStateException(
                "El período de inactividad ya fue cerrado."
            );
        }

        if (fechaHora.isBefore(this.inicio)) {
            throw new IllegalArgumentException(
                "La fecha de fin no puede ser anterior al inicio."
            );
        }

        this.fin = fechaHora;
    }

    @Override
    public String toString() {
        return "PeriodoInactividad{" +
               "inicio=" + this.inicio +
               ", fin=" + this.fin +
               '}';
    }
}