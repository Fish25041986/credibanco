package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class StatusCardException extends RuntimeException{
	
	private String methodInError;
	private String message;
	private String status;

	public StatusCardException(String methodInError, String status) {
        this.methodInError = methodInError;
		this.status=status;
        this.message = "La tarjeta esta " + status ;
    }

}
