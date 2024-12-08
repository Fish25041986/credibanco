package com.credibanco.prueba.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.credibanco.prueba.dto.transaccion.TransaccionDTO;
import com.credibanco.prueba.dto.transaccion.TransaccionShoppingRequesDTO;
import com.credibanco.prueba.entity.Tarjeta;
import com.credibanco.prueba.entity.Transaccion;
import com.credibanco.prueba.exceptions.InternaServerErrorException;
import com.credibanco.prueba.exceptions.MonetaryAmountException;
import com.credibanco.prueba.exceptions.NoDataFoundException;
import com.credibanco.prueba.exceptions.StatusCardException;
import com.credibanco.prueba.repository.ItarjetaRepository;
import com.credibanco.prueba.repository.ItransaccionRepository;
import com.credibanco.prueba.utils.DateActual;
import com.credibanco.prueba.utils.EstadoTarjeta;
import com.credibanco.prueba.utils.EstadoTransaccion;
import com.credibanco.prueba.utils.ValidadorNumeros;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
	
    private static final Logger logger = LoggerFactory.getLogger(TarjetaService.class);
	
	@Autowired
	ItransaccionRepository transaccionRepository;
	
	@Autowired
	ItarjetaRepository tarjetaRepository;
	
    @Autowired
    ValidadorNumeros validadorNumeros;
    
	@Autowired
    DateActual dateActual;
    
   
    /**
     * Registra los datos de la compra  y ademas actualiza el valor del saldo de la tarjeta, 
     * valida el transaccionShoppingRequesDTO entrante, verifica la existencia en el repositorio 
     * y actualiza la base de datos con los nuevos datos de la tarjeta.
     *
     * @param transaccionShoppingRequesDTO El identificador  de la tarjeta.
     * @return {@code true} si la transaccion fue actualizada correctamente.
     * @throws: Se envia el metodo que esta en error, el mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
	@Transactional
	public String compras(TransaccionShoppingRequesDTO transaccionShoppingRequesDTO) {
		
		BigDecimal nuevoSaldo=null;
		Transaccion transaccionSave = new Transaccion();

    	//@Component para validar diferentes aspectos del identificador entrante
        validadorNumeros.validarIdTarjeta(transaccionShoppingRequesDTO.getCardId(), 16, "compras");
        
        //Verifico que si exista  información relacionada con el numeroTarjeta
        Tarjeta tarjeta = tarjetaRepository.findByNumeroTarjeta(transaccionShoppingRequesDTO.getCardId())
            .orElseThrow(() -> new NoDataFoundException("findByNumeroTarjeta"));
        
       
      //Verifico si la tarjeta esta inactiva o bloqueada
        if (tarjeta.getEstado() == EstadoTarjeta.bloqueada) {
            logger.error("Tarjeta bloqueada.");
            throw new StatusCardException("La tarjeta esta bloqueada", EstadoTarjeta.bloqueada.toString());
		}else if(tarjeta.getEstado() == EstadoTarjeta.inactiva) {
            logger.error("Tarjeta inactiva.");
            throw new StatusCardException("La tarjeta esta inactiva", EstadoTarjeta.inactiva.toString());
		}
        
        //Verifico si el valor de la compra es mayor al saldo
        if (tarjeta.getSaldo().compareTo(transaccionShoppingRequesDTO.getPrice()) <= 0) {
       	    logger.error("El usuario no tiene saldo suficiente.");
            throw new MonetaryAmountException("El valor de la compra sobrepasa el saldo actual.","compras");	
		}
        
        //Verifico que la tarjeta no esté vencida
        if (!dateActual.esFechaMayorAHoy(tarjeta.getFechaVencimiento())) {
            logger.error("Tarjeta vencida.");
            throw new StatusCardException("La tarjeta esta vencida", EstadoTarjeta.vencida.toString());
        }

        
        //Guardo la información de la transaccion y ademas actualizo el saldo de la tarjeta
        try {
  
        	transaccionSave.setTarjeta(tarjeta);
        	transaccionSave.setMonto(transaccionShoppingRequesDTO.getPrice());
        	transaccionSave.setFechaTransaccion(dateActual.fechaYhoraActual());
        	transaccionSave.setEstado(EstadoTransaccion.activa);
        	
			transaccionRepository.save(transaccionSave);
			
			nuevoSaldo=tarjeta.getSaldo().subtract(transaccionShoppingRequesDTO.getPrice());
			tarjetaRepository.actualizarSaldo(nuevoSaldo, tarjeta.getNumeroTarjeta(), tarjeta.getIdTarjeta());
			
			return "Compra Aprobada";
		} catch (Exception e) {
		    logger.error("Error al guardar la compra.", e);
		    throw new InternaServerErrorException("compras");
		}
        
		
	}
	
    /**
     * Consulta el valor, fecha y estado de la transacion po su id, 
     * verifica la existencia en el repositorio de la transaccion
     *
     * @param idTransaccion El identificador  de la transaccion.
     * @return {@code TransaccionDTO} si existe información.
     * @throws: Se envia el metodo que esta en error, el mansaje del error
     * ya se encuentra en la clase del error correspondiente
     * IsNullOrEmptyException: Se lanza si el string entrante es vacio
     * InvalidNumberException: Se lanza si el string entrante contiene letras, simbolo.
     * InvalidNumberLengthException: Se lanza si no se cumple con la cantidad de digitos requeridos para los identificadores
     * NoDataFoundException: Se lanza si no se encuentra información relacionada con el identificador ingresado.
     * InternalServerException: Se lanza si ocurre un error al persistir ya sea en el save o update.
     */
	public TransaccionDTO searchTransaccion(Long idTransaccion) {
		
        TransaccionDTO transaccionDTOconvert= new TransaccionDTO();
		
        //Verifico que si exista el información relacionada con el idTransaccion
        Transaccion transaccion= transaccionRepository.findById(idTransaccion)
        		.orElseThrow(() -> new NoDataFoundException("findById"));
        
        //Convierto la entidad Transaccion a TransaccionDTO
        try {
            transaccionDTOconvert.setFechaTransaccion(transaccion.getFechaTransaccion());
            transaccionDTOconvert.setMonto(transaccion.getMonto());
            transaccionDTOconvert.setEstado(transaccion.getEstado().name());
		} catch (Exception e) {
            logger.error("Error al consultar la transaccion.", e);
            throw new InternaServerErrorException("Ocurrio un error en el porceso de consulta de la información.");
		}

        
        return transaccionDTOconvert;
		
	}


}
