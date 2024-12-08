package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class InvalidNumberException  extends RuntimeException{
	
	private String methodInError;
	private String message;

	public InvalidNumberException(String methodInError) {
        this.message = "El numero contiene letras, símbolos o espacios";
        this.methodInError = methodInError;
    }	

}
