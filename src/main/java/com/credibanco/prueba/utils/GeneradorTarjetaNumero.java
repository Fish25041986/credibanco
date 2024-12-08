package com.credibanco.prueba.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;


@Component
public class GeneradorTarjetaNumero {
	
    public String generarNumeroTarjeta(String idTarjeta) {
    	
        StringBuilder numeroTarjeta = new StringBuilder(idTarjeta);
        ThreadLocalRandom.current().ints(10, 0, 10).forEach(numeroTarjeta::append);
        return numeroTarjeta.toString();
        
    }
}
