package com.credibanco.prueba.utils;



import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class DateActual {
	
	public LocalDate obtenerFechaActual() {
	    return LocalDate.now();
	}
	
    public LocalDateTime fechaYhoraActual() {
        LocalDateTime fechaYhoraActual = LocalDateTime.now();
        return fechaYhoraActual;
    }

	public Date convertirALegacyDate(LocalDate date) {
	    return Date.valueOf(date);
	}
	
	public boolean esFechaMayorAHoy(LocalDate fecha) {
	    LocalDate hoy = LocalDate.now();
	    return fecha.isAfter(hoy);  // true si la fecha es después de hoy
	}
	
	public boolean verificarVeinticuatroHoras(LocalDateTime fechaTransaccion) {
	    LocalDateTime ahora = LocalDateTime.now();
	    Duration duracion = Duration.between(fechaTransaccion, ahora);

	    return duracion.toHours() < 24;
	}
	
}
