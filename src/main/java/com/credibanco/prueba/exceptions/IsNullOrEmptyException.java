package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class IsNullOrEmptyException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public IsNullOrEmptyException(String methodInError) {
        this.message = "El identificador no puede estar vacio";
        this.methodInError = methodInError;
    }	

}
