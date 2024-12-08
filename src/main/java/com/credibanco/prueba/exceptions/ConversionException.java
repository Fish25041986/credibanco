package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class ConversionException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public ConversionException(String methodInError) {
        this.message = "Error en conversión";
        this.methodInError = methodInError;
    }

}
