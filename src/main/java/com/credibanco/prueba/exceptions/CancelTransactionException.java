package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class CancelTransactionException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public CancelTransactionException(String mensagge, String methodInError) {
        this.message = mensagge;
        this.methodInError = methodInError;
    }

}
