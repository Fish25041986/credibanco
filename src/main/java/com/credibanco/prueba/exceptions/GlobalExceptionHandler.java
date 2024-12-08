package com.credibanco.prueba.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	 //Cuando  no se encuentra datos
	 @ExceptionHandler(NoDataFoundException.class)
	    public ResponseEntity<ErrorResponse> handleNoDataFoundException(NoDataFoundException ex, WebRequest request) {
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }

    //Cuando por alguna extraña causa no se puede convertir una entidad a DTO o viceversa
	 @ExceptionHandler(ConversionException.class)
	    public ResponseEntity<ErrorResponse> handleConversionException(ConversionException ex, WebRequest request) {
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    
	 }
	 
	 //Cuando el identificador ingresado contine letras, simbolos, o caracteres especiales
	 @ExceptionHandler(InvalidNumberException.class)
	 public ResponseEntity<ErrorResponse>  handleInvalidNumberException(InvalidNumberException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	 //Cuando se identifica valores string en vacios o nulos
	 @ExceptionHandler(IsNullOrEmptyException.class)
	 public ResponseEntity<ErrorResponse>  handleIsNullOrEmptyException(IsNullOrEmptyException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	 //Cuando no cumple con la cantidad de digitos requeridos
	 @ExceptionHandler(InvalidNumberLengthException.class)
	 public ResponseEntity<ErrorResponse>  handleInvalidNumberLengthException(InvalidNumberLengthException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	 //Cuando ocurre un error al guardar, modificar o eliminar
	 @ExceptionHandler(InternaServerErrorException.class)
	 public ResponseEntity<ErrorResponse>  handleInternaServerErrorException(InternaServerErrorException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	 }
	 
	 //Cuando la tarjeta ya se encuentraba activada o bloqueda
	 @ExceptionHandler(StatusCardException.class)
	 public ResponseEntity<ErrorResponse>  handleStatusCardException(StatusCardException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	 //Cuando ya existe informacion registra y esta no se puede modificar
	 @ExceptionHandler(ExistingInformationException.class)
	 public ResponseEntity<ErrorResponse>  handleExistingInformationException(ExistingInformationException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	 //Cuando el monto ingresado es menor o igual a cero
	 @ExceptionHandler(MonetaryAmountException.class)
	 public ResponseEntity<ErrorResponse>  handleMonetaryAmountException(MonetaryAmountException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setMethodInError(ex.getMethodInError());
	        errorResponse.setError(ex.getMessage());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	 //Esta exception se crea generica para mostrar el mensaje y metodo en error, en 
	 //algun porceso de AnulacionService
	 @ExceptionHandler(CancelTransactionException.class)
	 public ResponseEntity<ErrorResponse>  handleCancelTransactionException(CancelTransactionException ex, WebRequest request){
	        ErrorResponse errorResponse = new ErrorResponse();
	        errorResponse.setTimestamp(LocalDateTime.now());
	        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	        errorResponse.setPath(request.getDescription(false));
	        errorResponse.setError(ex.getMessage());
	        errorResponse.setMethodInError(ex.getMethodInError());

	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	 }
	 
	
}
