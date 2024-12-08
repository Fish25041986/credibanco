package com.credibanco.prueba.dto.tarjeta;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.credibanco.prueba.utils.EstadoTarjeta;
import com.credibanco.prueba.utils.TipoTarjeta;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TarjetaDTO {
	
	private Long idTarjeta;
	private String nombre;
	private String numeroTarjeta;
	private TipoTarjeta tipoTarjeta;
	private LocalDate  fechaEmision;
	private LocalDate fechaVencimiento;
	private EstadoTarjeta estado;
	private BigDecimal saldo;

}
