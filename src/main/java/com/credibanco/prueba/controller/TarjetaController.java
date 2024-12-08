package com.credibanco.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.prueba.config.ApiRoutes;
import com.credibanco.prueba.dto.tarjeta.TarjetaMontoRequesDTO;
import com.credibanco.prueba.dto.tarjeta.TarjetaRequesDTO;
import com.credibanco.prueba.service.TarjetaService;

@RestController
@RequestMapping(ApiRoutes.CARD)
public class TarjetaController {


	@Autowired
	TarjetaService tarjetaService;
	
	
	@GetMapping("/{productId}/number")
	public ResponseEntity<Boolean> generarNumeroTarjeta(@PathVariable("productId") String productId){
		boolean numeroGenerado=tarjetaService.generateNumberTarjeta(productId);
		return new ResponseEntity<Boolean>(numeroGenerado, HttpStatus.OK);
	}
	
	@PostMapping("/enroll")
	public ResponseEntity<Boolean> activarTarjeta(@RequestBody TarjetaRequesDTO cardId){
		String numeroTarjeta = cardId.getCardId();
		boolean tarjetaActiva= tarjetaService.activarTarjeta(numeroTarjeta);
		return new ResponseEntity<Boolean>(tarjetaActiva, HttpStatus.OK);
	}
	
	@DeleteMapping("/{cardId}")
	public ResponseEntity<Boolean> bloquearTarjeta(@PathVariable("cardId") String cardId){
		boolean tarjetaBloqueada=tarjetaService.bloquearTarjeta(cardId);
		return new ResponseEntity<Boolean>(tarjetaBloqueada, HttpStatus.OK);
	}
	
	@PostMapping("/balance")
	public ResponseEntity<Boolean> recargarSaldo(@RequestBody TarjetaMontoRequesDTO tarjetaMontoRequesDTO){
		boolean recargarSaldo=tarjetaService.recargaSaldo(tarjetaMontoRequesDTO);
		return new ResponseEntity<Boolean>(recargarSaldo, HttpStatus.OK);
	}
	
	@GetMapping("/balance/{cardId}")
	public ResponseEntity<String> consultaSaldo(@PathVariable("cardId") String cardId){
		String saldoConsultado= tarjetaService.consultaSaldo(cardId);
		return new ResponseEntity<String>(saldoConsultado, HttpStatus.OK);
	}
	

}
