package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class InternaServerErrorException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public InternaServerErrorException(String methodInError) {
        this.message = "Ocurrio un error inesperado, por favor vuelve a intentarlo";
        this.methodInError = methodInError;
    }

}
