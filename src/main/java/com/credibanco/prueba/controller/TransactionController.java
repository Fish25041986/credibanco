package com.credibanco.prueba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.credibanco.prueba.config.ApiRoutes;
import com.credibanco.prueba.dto.anulacion.AnulacionRequestDTO;
import com.credibanco.prueba.dto.transaccion.TransaccionDTO;
import com.credibanco.prueba.dto.transaccion.TransaccionShoppingRequesDTO;
import com.credibanco.prueba.service.AnulacionService;
import com.credibanco.prueba.service.TransactionService;


@RestController
@RequestMapping(ApiRoutes.TRANSACTION)
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AnulacionService anulacionService;
	
	@PostMapping("/purchase")
	public String compras(@RequestBody TransaccionShoppingRequesDTO transaccionShoppingRequesDTO) {
        String resultCompra=transactionService.compras(transaccionShoppingRequesDTO);
		return resultCompra;
	}
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<TransaccionDTO> searchTransaccion(@PathVariable("transactionId") Long transactionId) {
		TransaccionDTO transaccionDTO=transactionService.searchTransaccion(transactionId);
		return new ResponseEntity<TransaccionDTO>(transaccionDTO, HttpStatus.OK);
	}
	
	@PostMapping("/anulation")
	public ResponseEntity<Boolean> anularCompra(@RequestBody AnulacionRequestDTO anulacionRequestDTO) {
        Boolean resultAnulacion=anulacionService.anularTransaccion(anulacionRequestDTO);
		return new ResponseEntity<Boolean>(resultAnulacion, HttpStatus.OK);
	}
	

}
