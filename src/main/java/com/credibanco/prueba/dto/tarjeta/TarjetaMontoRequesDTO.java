package com.credibanco.prueba.dto.tarjeta;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TarjetaMontoRequesDTO {
	private String cardId;
	private BigDecimal balance;
}
