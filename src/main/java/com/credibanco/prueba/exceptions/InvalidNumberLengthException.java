package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class InvalidNumberLengthException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public InvalidNumberLengthException(String methodInError) {
        this.message = "El número identificador no contiene la cantidad de digitos correcta";
        this.methodInError = methodInError;
    }

}
