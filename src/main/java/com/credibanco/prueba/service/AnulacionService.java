package com.credibanco.prueba.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.credibanco.prueba.dto.anulacion.AnulacionRequestDTO;
import com.credibanco.prueba.entity.Tarjeta;
import com.credibanco.prueba.entity.Transaccion;
import com.credibanco.prueba.exceptions.CancelTransactionException;
import com.credibanco.prueba.exceptions.InternaServerErrorException;
import com.credibanco.prueba.exceptions.NoDataFoundException;
import com.credibanco.prueba.repository.ItarjetaRepository;
import com.credibanco.prueba.repository.ItransaccionRepository;
import com.credibanco.prueba.utils.DateActual;
import com.credibanco.prueba.utils.EstadoTransaccion;
import com.credibanco.prueba.utils.ValidadorNumeros;

@Service
public class AnulacionService {
	
	@Autowired
	ItarjetaRepository tarjetaRepository;
	
	@Autowired
	ItransaccionRepository transaccionRepository;
	
    @Autowired
    ValidadorNumeros validadorNumeros;
    
    @Autowired
    DateActual dateActual;
    
    private static final Logger logger = LoggerFactory.getLogger(TarjetaService.class);
	
	@Transactional
	public Boolean anularTransaccion(AnulacionRequestDTO anulacionRequestDTO ) {
		
        BigDecimal saldoActuaizado=null;
        
		//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(anulacionRequestDTO.getCardId(), 16, "anularTransaccion");

        //Verifico que si exista el información relacionada con el numeroTarjeta
        Tarjeta tarjeta = tarjetaRepository.findByNumeroTarjeta(anulacionRequestDTO.getCardId())
            .orElseThrow(() -> new NoDataFoundException("findByNumeroTarjeta"));
        
        //Verifico que si exista el información relacionada con el idTransaccion
        Transaccion transaccion= transaccionRepository.findById(anulacionRequestDTO.getTransactionId())
        		.orElseThrow(() -> new NoDataFoundException("findById"));
        
        //Verifico que la transaccion no este anulada
        if (transaccion.getEstado().equals(EstadoTransaccion.anulada)) {
            logger.error("Se esta intentando anular una compra de la cual ya pasaron 24 horas");
            throw new CancelTransactionException("Ya se encuentra anulada","anularTransaccion");
		}
        
        //Verifico que no hayan pasado 24 horas desde la compra
        if (!dateActual.verificarVeinticuatroHoras(transaccion.getFechaTransaccion())) {
            logger.error("Se esta intentando anular una compra de la cual ya pasaron 24 horas");
            throw new CancelTransactionException("Ya pasaron mas de 24 horas","verificarVeinticuatroHoras");
		}
        
        //Cambia el estado de la transaccion y actuliza el valor saldo de la tarjeta
        try {
            transaccionRepository.anularTransacción(EstadoTransaccion.anulada.name(), tarjeta.getIdTarjeta(), transaccion.getIdTransaccion());
        	saldoActuaizado= tarjeta.getSaldo().add(transaccion.getMonto());
            tarjetaRepository.actualizarSaldo(saldoActuaizado, tarjeta.getNumeroTarjeta(), tarjeta.getIdTarjeta());
            
            return true;
		} catch (Exception e) {
	           logger.error("Error al actualizar el estado de la compra.", e);
	           throw new InternaServerErrorException("anularTransaccion");
		}
		
	}
}
