package ar.edu.ungs.prog2.smartgrids;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SmartGrids implements ISmartGrids {
    private LinkedList<Planta> plantas;
    private Map<String, Planta> plantasPorNombre;
    private LinkedList<Monitoreo> monitoreos;

    public SmartGrids() {
        this.plantas = new LinkedList<>();
        this.monitoreos = new LinkedList<>();
        this.plantasPorNombre = new HashMap<>();
    }
    private Planta buscarPlanta(String nombre) {
    	return plantasPorNombre.get(nombre);
    }

    private void agregarPlanta(Planta planta) {
        if (planta == null) {
            throw new IllegalArgumentException(
                "La planta no puede ser null."
            );
        }

        if (buscarPlanta(planta.nombre()) != null) {
            throw new IllegalArgumentException(
                "Ya existe una planta con ese nombre."
            );
        }

        plantas.add(planta);
        plantasPorNombre.put(planta.nombre(), planta);
    }
	@Override
	public void registrarPlantaSolar(String nombre, String direccion, double capacidadMaxima,
			String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria, double eficienciaPromedio,
			double superficieCubierta, int cantidadPaneles, double horasRadiacionDiaria) {
		    LocalDateTime fecha = LocalDateTime.parse(
		        fechaHoraPuestaEnFuncionamiento
		    );

		    PlantaSolar planta = new PlantaSolar(
		        nombre, direccion, capacidadMaxima, true, fecha,
		        fuentePrimaria, eficienciaPromedio, superficieCubierta,
		        cantidadPaneles, horasRadiacionDiaria
		    );
		    agregarPlanta(planta);
		}

	@Override
	public void registrarPlantaEolica(
	        String nombre, String direccion, double capacidadMaxima,
	        String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria,
	        double eficienciaPromedio, int cantidadAerogeneradores,
	        double alturaTorres, double velocidadMinimaViento) {

	    LocalDateTime fecha = LocalDateTime.parse(
	        fechaHoraPuestaEnFuncionamiento
	    );

	    PlantaEolica planta = new PlantaEolica(
	        nombre, direccion, capacidadMaxima, true, fecha,
	        fuentePrimaria, eficienciaPromedio, cantidadAerogeneradores,
	        alturaTorres, velocidadMinimaViento
	    );

	    agregarPlanta(planta);
	}

	@Override
	public void registrarPlantaHidraulica(
	        String nombre, String direccion, double capacidadMaxima,
	        String fechaHoraPuestaEnFuncionamiento, String fuentePrimaria,
	        double eficienciaPromedio, int cantidadTurbinas,
	        double capacidadEmbalse) {

	    LocalDateTime fecha = LocalDateTime.parse(
	        fechaHoraPuestaEnFuncionamiento
	    );

	    PlantaHidraulica planta = new PlantaHidraulica(
	        nombre, direccion, capacidadMaxima, true, fecha,
	        fuentePrimaria, eficienciaPromedio, cantidadTurbinas,
	        capacidadEmbalse
	    );

	    agregarPlanta(planta);
	}

	@Override
	public void registrarPlantaTermica(
	        String nombre, String direccion, double capacidadMaxima,
	        String fechaHoraPuestaEnFuncionamiento, String tipoCombustible,
	        double objetivoEmisionesCo2PorHora,
	        double capacidadAlmacenamientoCombustible) {

	    LocalDateTime fecha = LocalDateTime.parse(
	        fechaHoraPuestaEnFuncionamiento
	    );

	    PlantaTermica planta = new PlantaTermica(
	        nombre, direccion, capacidadMaxima, true, fecha,
	        tipoCombustible, objetivoEmisionesCo2PorHora,
	        capacidadAlmacenamientoCombustible
	    );

	    agregarPlanta(planta);
	}

	@Override
	public List<String> consultarPlantas() {
		List<String> listaPlantas = new LinkedList<>();
		for (Planta p : plantas) {
			listaPlantas.add(p.toString());
		}
		return listaPlantas;
	}
	@Override
	public double capacidadMinimaCuota(String nombre) { 
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe.");
		}
		return p.cuotaMinima();
			}

	@Override
	public void activarPlanta(String nombre, String fechaHora) {
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe.");
		}
		p.activar(fechaHora);
	}

	@Override
	public void desactivarPlanta(String nombre, String fechaHora) {
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe.");
		}
		p.desactivar(fechaHora);
	}

	@Override
	public boolean estaActiva(String nombre) {
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe.");
		}
		return p.estaActiva();
	}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias) {
		Planta p = buscarPlanta(nombre);
		if (p == null || !p.estaActiva()) {
			throw new RuntimeException ("La planta no existe o está inactiva.");
		}
		if (fechaHora == null || fechaHora.isBlank()) {
			throw new RuntimeException ("La fecha está vacía o mal ingresada.");
		}
		LocalDateTime fecha = parsearFechaHora(fechaHora);
		if (!p.estaActiva()) {
		    throw new RuntimeException("La planta está inactiva.");
		}
		if (!p.estaActivaEn(fecha)) {
		    throw new RuntimeException("La planta estaba inactiva en esa fecha y hora.");
		}	
		if (fecha.isBefore(p.fechaPuestaFuncionamiento)){
			throw new RuntimeException ("La fecha no puede ser anterior a la puesta en funcionamiento.");
		}
		if (!Double.isFinite(energiaGenerada)
		        || energiaGenerada < 0) {
		    throw new RuntimeException(
		        "La energía generada debe ser finita y mayor o igual que cero."
		    );
		}
		if (incidencias < 0) {
			throw new RuntimeException ("Las incidencias deben ser mayores o iguales que cero.");
		}
		if (!(p instanceof PlantaSolar || p instanceof PlantaHidraulica)) {
		    throw new IllegalArgumentException(
		        "Este método solo admite plantas solares e hidráulicas."
		    );
		}
		for (Monitoreo m : monitoreos) {
		    if (m.planta().equals(p)
		            && m.fecha().toLocalDate().equals(fecha.toLocalDate())
		            && m.fecha().getHour() == fecha.getHour()) {

		        throw new RuntimeException(
		            "Ya existe un registro para esa hora."
		        );
		    }
		}
		Monitoreo m = new Monitoreo (p, fecha,energiaGenerada, incidencias);
		monitoreos.add(m);
}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
		double velocidadViento) {
		Planta p = buscarPlanta(nombre);
		if (p == null || !p.estaActiva()) {
			throw new RuntimeException ("La planta no existe o está inactiva.");
		}
		if (fechaHora == null || fechaHora.isBlank()) {
			throw new RuntimeException ("La fecha está vacía o mal ingresada.");
		}
		LocalDateTime fecha = parsearFechaHora(fechaHora);
		if (!p.estaActiva()) {
			    throw new RuntimeException("La planta está inactiva.");
			}
		if (!p.estaActivaEn(fecha)) {
			    throw new RuntimeException("La planta estaba inactiva en esa fecha y hora.");
			}	
		if (fecha.isBefore(p.fechaPuestaFuncionamiento)){
			throw new RuntimeException ("La fecha no puede ser anterior a la puesta en funcionamiento.");
		}
		if (!Double.isFinite(energiaGenerada)
		        || energiaGenerada < 0) {
		    throw new RuntimeException(
		        "La energía generada debe ser finita y mayor o igual que cero."
		    );
		}
		if (incidencias < 0) {
			throw new RuntimeException ("Las incidencias deben ser mayores o iguales que cero.");
		}
		if (!Double.isFinite(velocidadViento) || velocidadViento < 0) {
			throw new RuntimeException ("La velocidad del viento debe ser mayor o igual que cero.");
		}
		if (!(p instanceof PlantaEolica)) {
		    throw new IllegalArgumentException(
		        "Este método solo admite plantas eólicas."
		    );
		}
		for (Monitoreo m : monitoreos) {
		    if (m.planta().equals(p)
		            && m.fecha().toLocalDate().equals(fecha.toLocalDate())
		            && m.fecha().getHour() == fecha.getHour()) {

		        throw new RuntimeException(
		            "Ya existe un registro para esa hora."
		        );
		    }
		}
		Monitoreo m = new MonitoreoEolico (p, fecha,energiaGenerada, incidencias,velocidadViento);
		monitoreos.add(m);
}

	@Override
	public void registrarMonitoreo(String nombre, String fechaHora, double energiaGenerada, int incidencias,
			double co2Emitido, double combustibleUtilizado) {
		Planta p = buscarPlanta(nombre);
		if (p == null || !p.estaActiva()) {
			throw new RuntimeException ("La planta no existe o está inactiva.");
		}
		if (fechaHora == null || fechaHora.isBlank()) {
			throw new RuntimeException ("La fecha está vacía o mal ingresada.");
		}
		LocalDateTime fecha = parsearFechaHora(fechaHora);
		if (!p.estaActiva()) {
			    throw new RuntimeException("La planta está inactiva.");
			}

		if (!p.estaActivaEn(fecha)) {
			    throw new RuntimeException("La planta estaba inactiva en esa fecha y hora.");
			}	

		if (fecha.isBefore(p.fechaPuestaFuncionamiento)){
			throw new RuntimeException ("La fecha no puede ser anterior a la puesta en funcionamiento.");
		}
		if (!Double.isFinite(energiaGenerada)
		        || energiaGenerada < 0) {
		    throw new RuntimeException(
		        "La energía generada debe ser finita y mayor o igual que cero."
		    );
		}
		if (incidencias < 0) {
			throw new RuntimeException ("Las incidencias deben ser mayores o iguales que cero.");
		}
		if (!Double.isFinite(co2Emitido) || co2Emitido < 0) {
			throw new RuntimeException ("El CO2 emitido debe ser mayor o igual que cero.");
		}
		if (!Double.isFinite(combustibleUtilizado) || combustibleUtilizado < 0) {
			throw new RuntimeException ("El combustible utilizado debe ser mayor o igual que cero.");
		}
		if (!(p instanceof PlantaTermica)) {
		    throw new IllegalArgumentException(
		        "Este método solo admite plantas térmicas."
		    );
		}
		for (Monitoreo m : monitoreos) {
		    if (m.planta().equals(p)
		            && m.fecha().toLocalDate().equals(fecha.toLocalDate())
		            && m.fecha().getHour() == fecha.getHour()) {

		        throw new RuntimeException(
		            "Ya existe un registro para esa hora."
		        );
		    }
		}
		Monitoreo m = new MonitoreoTermico (p, fecha,energiaGenerada, incidencias, co2Emitido, combustibleUtilizado);
		monitoreos.add(m);
}

	@Override
	public double totalGenerado(String nombre) {
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe o está inactiva.");
		}
		double total = 0;
		for (Monitoreo m : monitoreos) {
			if (m.planta().equals(p)) {
				total += m.energiaGenerada();
			}
		}
		
		return total;
	}

	@Override
	public int[] incidenciasPorHora(String nombre) {
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe o está inactiva.");
		}
		int[] listaIncidenciasPorHora = new int[24];
		for (Monitoreo m : monitoreos) {
			if (m.planta().equals(p)) {
				int hora = m.fecha().getHour();
				listaIncidenciasPorHora[hora] += m.cantidadIncidencias();
			}
		}
		return listaIncidenciasPorHora;
	}
	@Override
	public int[] histogramaCumplimiento(String nombre, String fechaDesde, String fechaHasta) {
		Planta p = buscarPlanta(nombre);
		if (p == null) {
			throw new RuntimeException ("La planta no existe o está inactiva.");
		}
		LocalDate desde = parsearFecha(fechaDesde);
		LocalDate hasta = parsearFecha(fechaHasta);

		if (desde.isAfter(hasta)) {
		    throw new RuntimeException(
		        "La fecha desde no puede ser posterior a la fecha hasta."
		    );
		}
	    int[] histograma = new int[24];

	    for (Monitoreo m : monitoreos) {
	        LocalDate diaRegistro = m.fecha().toLocalDate();
	        if (m.planta().equals(p) && !diaRegistro.isBefore(desde) 
	        	&& !diaRegistro.isAfter(hasta) && p.cumpleCuota(m)) {
	            int hora = m.fecha().getHour();
	            histograma[hora]++;
	        }
	    }
	    return histograma;
	}
	@Override
	public Map<String, Integer> plantasRenovablesPorFuente() {
		Map <String, Integer> plantasRenovables = new TreeMap<>();
		for (Planta p : plantas) {
			if (p instanceof PlantaRenovable) {
				String fuente = ((PlantaRenovable) p).fuentePrimaria();
				plantasRenovables.put(fuente,plantasRenovables.getOrDefault(fuente, 0) + 1
					);
			}
		}
		return plantasRenovables;
	}

	@Override
	public List<String> plantasQueNuncaCumplieronCuota() {
		List<String> plantasQueNuncaCumplieron = new LinkedList<>();
		for (Planta p : plantas) {
			boolean noCumplio = true;
			for(Monitoreo m : monitoreos) {
				if (m.planta().equals(p)) {
					if (p.cumpleCuota(m)) {
						noCumplio = false;
						break;
					}
				}
			}
			if (noCumplio) {
				plantasQueNuncaCumplieron.add(p.nombre);
			}
		}
		return plantasQueNuncaCumplieron;
	}

	@Override
	public String horasSinVelocidadMinimaPorPlantaEolica() {
	    StringBuilder resultado = new StringBuilder();
	    resultado.append("Plantas Eolicas: Horas con poco viento\n");

	    for (Planta p : plantas) {
	        if (p instanceof PlantaEolica) {
	            PlantaEolica eolica = (PlantaEolica) p;
	            double minima = eolica.velocidadMinima();

	            double[] velocidades = new double[24];
	            boolean[] hayRegistro = new boolean[24];
	            boolean[] cumplioMinimo = new boolean[24];

	            for (Monitoreo m : monitoreos) {
	                if (m instanceof MonitoreoEolico
	                        && m.planta().equals(p)) {

	                    int hora = m.fecha().getHour();
	                    double velocidad =
	                            ((MonitoreoEolico) m).velocidadVientoPromedio();

	                    if (!hayRegistro[hora]
	                            || velocidad > velocidades[hora]) {
	                        velocidades[hora] = velocidad;
	                    }

	                    hayRegistro[hora] = true;

	                    if (velocidad >= minima) {
	                        cumplioMinimo[hora] = true;
	                    }
	                }
	            }

	            resultado.append("  - ")
	                     .append(p.nombre())
	                     .append(": ")
	                     .append(minima)
	                     .append(" m/s\n");

	            for (int hora = 0; hora < 24; hora++) {
	                if (!cumplioMinimo[hora]) {
	                    resultado.append("          ");

	                    if (hora < 10) {
	                        resultado.append("0");
	                    }

	                    resultado.append(hora)
	                             .append(": ")
	                             .append(velocidades[hora])
	                             .append(" m/s\n");
	                }
	            }
	        }
	    }

	    return resultado.toString();
	}
	@Override
	public int limpiarMonitoreosSinDatos() {
		int registrosBorrados = 0;
		Iterator<Monitoreo> it = monitoreos.iterator();
		while(it.hasNext()) {
			Monitoreo m = it.next();
			if (m.cantidadIncidencias() == 0 && m.energiaGenerada() == 0) {
				it.remove();
				registrosBorrados += 1;
			}
		}
		return registrosBorrados;
	}
	@Override
	public List<String> plantasOrdenadasPorEnergiaGenerada() {
	    List<String> plantasOrdenadas = new LinkedList<>();
	    List<Planta> plantasAux = new LinkedList<>();
	    for (Planta p : plantas) {
	        plantasAux.add(p);
	    }
	    for (int i = 0; i < plantasAux.size() - 1; i++) {
	        for (int j = 0; j < plantasAux.size() - 1 - i; j++) {

	            double energia1 =
	                totalGenerado(plantasAux.get(j).nombre);
	            double energia2 =
	                totalGenerado(plantasAux.get(j + 1).nombre);

	            if (energia1 < energia2) {
	                Planta aux = plantasAux.get(j);
	                plantasAux.set(j, plantasAux.get(j + 1));
	                plantasAux.set(j + 1, aux);
	            }
	        }
	    }
	    for (Planta p : plantasAux) {
	        String formato =
	            p.nombre + ": " + totalGenerado(p.nombre);
	        plantasOrdenadas.add(formato);
	    }

	    return plantasOrdenadas;
	}
	@Override
	public List<String> plantasInactivasEn(String fechaHora) {
		List<String> plantasInactivas = new LinkedList <>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime fecha = parsearFechaHora(fechaHora);
		fecha.format(formatter);
		for (Planta p : plantas) {
			if (!p.estaActivaEn(fecha)) {
				plantasInactivas.add(p.nombre);
			}
		}
		return plantasInactivas;
	}
	// Método auxiliar para parsear la fecha y hora:
	private LocalDateTime parsearFechaHora(String fechaHora) {
	    if (fechaHora == null || fechaHora.isBlank()) {
	        throw new IllegalArgumentException(
	            "La fecha y hora no pueden estar vacías."
	        );
	    }

	    try {
	        return LocalDateTime.parse(fechaHora);
	    } catch (DateTimeParseException e) {
	        throw new IllegalArgumentException(
	            "El formato de fecha y hora es inválido. "
	            + "Ejemplo válido: 2026-10-23T14:30:00.",
	            e
	        );
	    }
	}
	// Método auxiliar para parsear la fecha:
	private LocalDate parsearFecha(String fecha) {
	    if (fecha == null || fecha.isBlank()) {
	        throw new IllegalArgumentException(
	            "La fecha no puede estar vacía."
	        );
	    }

	    try {
	        return LocalDate.parse(fecha);
	    } catch (DateTimeParseException e) {
	        throw new IllegalArgumentException(
	            "El formato de fecha es inválido. "
	            + "Ejemplo válido: 2026-03-01.",
	            e
	        );
	    }
	}
	// Método toString de SmartGrids.java:
	@Override
	public String toString() {
	    StringBuilder resultado = new StringBuilder();

	    resultado.append("=== Estado de SmartGrids ===\n");

	    resultado.append("Plantas registradas:\n");
	    for (Planta p : plantas) {
	        resultado.append("  - ")
	                 .append(p)
	                 .append("\n");
	    }

	    resultado.append("Monitoreos registrados:\n");
	    for (Monitoreo m : monitoreos) {
	        resultado.append("  - ")
	                 .append(m)
	                 .append("\n");
	    }

	    return resultado.toString();
	}
}
