package com.credibanco.prueba.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class MonetaryAmountException extends RuntimeException{
	
	private String methodInError;
	private String message;

	public MonetaryAmountException(String message, String methodInError) {
        this.message = message;
        this.methodInError = methodInError;
    }	

}
