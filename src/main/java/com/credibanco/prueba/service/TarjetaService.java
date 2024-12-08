package com.credibanco.prueba.service;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.prueba.dto.tarjeta.TarjetaMontoRequesDTO;
import com.credibanco.prueba.entity.Tarjeta;
import com.credibanco.prueba.exceptions.ExistingInformationException;
import com.credibanco.prueba.exceptions.InternaServerErrorException;
import com.credibanco.prueba.exceptions.NoDataFoundException;
import com.credibanco.prueba.exceptions.StatusCardException;
import com.credibanco.prueba.repository.ItarjetaRepository;
import com.credibanco.prueba.utils.BigDecimalValidador;
import com.credibanco.prueba.utils.DateActual;
import com.credibanco.prueba.utils.EstadoTarjeta;
import com.credibanco.prueba.utils.GeneradorTarjetaNumero;
import com.credibanco.prueba.utils.ValidadorNumeros;

import jakarta.transaction.Transactional;

@Service
public class TarjetaService {
	
    private static final Logger logger = LoggerFactory.getLogger(TarjetaService.class);
	
	@Autowired
	ItarjetaRepository tarjetaRepository;
	
	@Autowired
    DateActual dateActual;
    
    @Autowired
    ValidadorNumeros validadorNumeros;
    
    @Autowired
    GeneradorTarjetaNumero generadorTarjetaNumero;
    
    @Autowired
    BigDecimalValidador bigDecimalValidador;
    

    
    
    
    /**
     * Genera un número de tarjeta para el identificador numero_tarjeta, 
     * valida el idLogicTarjeta entrante, verifica la existencia en el repositorio 
     * y actualiza la base de datos con los nuevos datos de la tarjeta.
     *
     * @param idLogicTarjeta El identificador lógico de la tarjeta.
     * @return {@code true} si la tarjeta fue actualizada correctamente.
     * @throws: Se envia el metodo que esta en error, el mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
    @Transactional
    public Boolean generateNumberTarjeta(String idLogicTarjeta) {
    	
    	//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(idLogicTarjeta, 6, "generateNumberTarjeta");

        //Verifico que si exista el información relacionada con el idLogicTarjeta
        Tarjeta tarjeta= tarjetaRepository.findByIdLogicTarjeta(idLogicTarjeta)
        		.orElseThrow(() -> new NoDataFoundException("findByIdLogicTarjeta"));
        
        
        //verifico que la tarjeta no tenga un numero_tarjeta ya registrada
        if (tarjeta.getNumeroTarjeta() != null && !String.valueOf(tarjeta.getNumeroTarjeta()).trim().isEmpty()) {
            logger.warn("Se está intentando registrar un número de tarjeta a una ya existente");
            throw new ExistingInformationException("findByIdLogicTarjeta");
        }
        
        // Generar el número de tarjeta
        String numeroGenerado = generadorTarjetaNumero.generarNumeroTarjeta(idLogicTarjeta);
        if (numeroGenerado == null || numeroGenerado.isEmpty()) {
            logger.error("Número de tarjeta generado es nulo o vacío.");
            throw new InternaServerErrorException("Error al generar el número de tarjeta.");
        }

        // Guardar el número generado de 16 dígitos junto con la fecha
        try {
            tarjetaRepository.actualizarTarjeta(numeroGenerado, idLogicTarjeta);
            return true;
        } catch (Exception e) {
            logger.error("Error al actualizar la tarjeta.", e);
            throw new InternaServerErrorException("Error al actualizar la tarjeta.");
        }
    }
    
    
    /**
     * actualiza el estado de la tarjeta de inactivo a activo, 
     * valida el numeroTarjeta entrante, verifica la existencia en el repositorio 
     * y actualiza la base de datos con el nuevo estado de la tarjeta.
     *
     * @param numeroTarjeta El identificadorde 16 digitos de la tarjeta.
     * @return {@code true} si la tarjeta fue actualizada correctamente.
     * @throws: Se envia el metodo que esta en error yel mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
    @Transactional
    public Boolean activarTarjeta(String numeroTarjeta) {

    	//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(numeroTarjeta, 16, "activarTarjeta");

        //Verifico que si exista el información relacionada con el numeroTarjeta
        Tarjeta tarjeta = tarjetaRepository.findByNumeroTarjeta(numeroTarjeta)
            .orElseThrow(() -> new NoDataFoundException("findByNumeroTarjeta"));
        
        //Verifico que si la tarjeta ya estaba activada
        if (tarjeta.getEstado().equals(EstadoTarjeta.activa)) {
            throw new StatusCardException("La tarjeta ya está activa.",EstadoTarjeta.activa.toString());
        }
        
        //Verifico que la tarjeta esta bloqueada
        if (tarjeta.getEstado().equals(EstadoTarjeta.bloqueada)) {
            throw new StatusCardException("La tarjeta está bloqueada.", EstadoTarjeta.bloqueada.toString());
        }

        //Actualizo la información en la base de datos
        try {
            tarjetaRepository.activarTarjeta(
            		EstadoTarjeta.activa.name(), 
                    dateActual.convertirALegacyDate(dateActual.obtenerFechaActual()),
                    dateActual.convertirALegacyDate(dateActual.obtenerFechaActual().plusYears(3)),
            		numeroTarjeta, tarjeta.getIdTarjeta());
            return true;
        } catch (Exception e) {
            logger.error("Error al activar la tarjeta.", e);
            throw new InternaServerErrorException("activarTarjeta");
        }
    }
    
    
    /**
     * actualiza el estado de la tarjeta de activo a bloqueado, 
     * valida el numeroTarjeta entrante, verifica la existencia en el repositorio 
     * y actualiza la base de datos con el nuevo estado de la tarjeta.
     *
     * @param numeroTarjeta El identificadorde 16 digitos de la tarjeta.
     * @return {@code true} si la tarjeta fue actualizada correctamente.
     * @throws: Se envia el metodo que esta en error yel mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
    @Transactional
    public Boolean bloquearTarjeta(String numeroTarjeta) {

    	//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(numeroTarjeta, 16, "bloquearTarjeta");
        
        //Verifico que si exista el información relacionada con el numeroTarjeta
        Tarjeta tarjeta = tarjetaRepository.findByNumeroTarjeta(numeroTarjeta)
                .orElseThrow(() -> new NoDataFoundException("findByNumeroTarjeta"));
        
        //Valido que la tarjeta ya estuviese bloqueada
        if (tarjeta.getEstado() == EstadoTarjeta.bloqueada) {
            logger.error("Tarjeta bloqueada.");
            throw new StatusCardException("La tarjeta está ya esta bloqueada.", EstadoTarjeta.bloqueada.toString());
		}
        
        //Actualizo la información en la base de datos
        try {
			tarjetaRepository.bloquearTarjeta(EstadoTarjeta.bloqueada.name(), numeroTarjeta, tarjeta.getIdTarjeta());
            return true;
        } catch (Exception e) {
            logger.error("Error al bloquear la tarjeta.", e);
            throw new InternaServerErrorException("bloquearTarjeta");
		}
    }
    
    /**
     * Actualiza le saldo de la tarjeta, 
     * valida el tarjetaMontoRequesDTO entrante, verifica la existencia en el repositorio 
     * y actualiza la base de datos con los nuevos datos de la tarjeta.
     *
     * @param tarjetaMontoRequesDTO, que contiene El numeroTarjeta y el monto.
     * @return {@code true} si la tarjeta fue actualizada correctamente.
     * @throws: Se envia el metodo que esta en error, el mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * MonetaryAmountException: Se lanza cuando el monto ingresado en menor o igual a cero
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
    @Transactional
    public boolean recargaSaldo(TarjetaMontoRequesDTO tarjetaMontoRequesDTO) {
    	
        BigDecimal saldoActuaizado=null;

    	//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(tarjetaMontoRequesDTO.getCardId(), 16, "recargaSaldo");
        
        //@Component para validar diferentes aspectos del monto entrante
        bigDecimalValidador.validarMonto(tarjetaMontoRequesDTO.getBalance(), "recargaSaldo");

        
        //Verifico que si existe la tarjeta
        Tarjeta tarjeta = tarjetaRepository.findByNumeroTarjeta(tarjetaMontoRequesDTO.getCardId())
                .orElseThrow(() -> new NoDataFoundException("findByNumeroTarjeta"));
        
        //Valido si la tarjeta esta bloqueada o inactiva
        if (tarjeta.getEstado() == EstadoTarjeta.bloqueada || tarjeta.getEstado() == EstadoTarjeta.inactiva) {
            logger.error("Tarjeta bloqueada o inactiva.");
            throw new StatusCardException("La tarjeta esta bloquead o inactiva.", EstadoTarjeta.bloqueada.toString());
		}
        
        //Verifico que la tarjeta no esté vencida
        if (!dateActual.esFechaMayorAHoy(tarjeta.getFechaVencimiento())) {
            logger.error("Tarjeta vencida.");
            throw new StatusCardException("La tarjeta esta vencida", EstadoTarjeta.vencida.toString());
        }
        
        try {
        	saldoActuaizado=tarjetaMontoRequesDTO.getBalance().add(tarjeta.getSaldo());
        	tarjetaRepository.actualizarSaldo(saldoActuaizado, tarjetaMontoRequesDTO.getCardId(), tarjeta.getIdTarjeta());
			return true;
		} catch (Exception e) {
           logger.error("Error al recargar la tarjeta.", e);
            throw new InternaServerErrorException("recargaSaldo");
		} 
    }
    
    /**
     * Consulta el saldo de la tarjeta, 
     * valida el numeroTarjeta entrante, verifica la existencia en el repositorio 
     *
     * @param numeroTarjeta, que contiene el numeroTarjeta.
     * @return {@code String} si existe información de la tarjeta.
     * @throws: Se envia el metodo que esta en error, el mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * MonetaryAmountException: Se lanza cuando el monto ingresado en menor o igual a cero
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
    @Transactional
    public String consultaSaldo(String numeroTarjeta) {

    	//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(numeroTarjeta, 16, "consultaSaldo");
        
        //Verifico que si existe la tarjeta
        Tarjeta tarjeta = tarjetaRepository.findByNumeroTarjeta(numeroTarjeta)
                .orElseThrow(() -> new NoDataFoundException("findByNumeroTarjeta"));
        
        //Verifico que la tarjeta no este inactiva o bloqueda
        if (tarjeta.getEstado()== EstadoTarjeta.bloqueada || tarjeta.getEstado() == EstadoTarjeta.inactiva) {
            logger.error("Tarjeta bloqueada o inactiva.");
            throw new StatusCardException("La tarjeta esta bloquead o inactiva.", EstadoTarjeta.bloqueada.toString());
		}
        
        try {
			return tarjeta.getSaldo().toString();
		} catch (Exception e) {
           logger.error("Ocurrio un error", e);
           throw new InternaServerErrorException("consultaSaldo");
		}
        
        
    }


    

}
