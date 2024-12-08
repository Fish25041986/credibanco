package com.credibanco.prueba.dto.transaccion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransaccionDTO {
	
    private LocalDateTime fechaTransaccion;
	private BigDecimal monto;
	private String estado;
		
}
