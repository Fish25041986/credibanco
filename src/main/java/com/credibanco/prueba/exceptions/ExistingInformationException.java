package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class ExistingInformationException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public ExistingInformationException(String methodInError) {
        this.message = "Ya existe información registrada y no se puede modificar";
        this.methodInError = methodInError;
    }

}
