package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class NoDataFoundException extends RuntimeException{

	private String methodInError;
	private String message;

	public NoDataFoundException(String methodInError) {
        this.message = "No existe información en el momento";
        this.methodInError = methodInError;
    }	
	
}