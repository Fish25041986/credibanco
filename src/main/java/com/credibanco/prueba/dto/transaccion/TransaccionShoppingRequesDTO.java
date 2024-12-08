package com.credibanco.prueba.dto.transaccion;

import java.math.BigDecimal;
import lombok.Data;


@Data
public class TransaccionShoppingRequesDTO {
	private String cardId;
	private BigDecimal price;
		
	public TransaccionShoppingRequesDTO(String cardId, BigDecimal price) {
		this.cardId = cardId;
		this.price = price;
	}

}
