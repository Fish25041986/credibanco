package com.credibanco.prueba.utils;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.credibanco.prueba.exceptions.MonetaryAmountException;

@Component
public class BigDecimalValidador {

    private static final Logger logger = LoggerFactory.getLogger(BigDecimalValidador.class);

    /**
     * Valida que un monto no sea menor o igual a cero.
     *
     * @param monto El monto a validar.
     * @param metodo El nombre del método que llama a esta validación (para log y excepciones).
     * @throws MonetaryAmountException Si el monto es inválido.
     */
    public void validarMonto(BigDecimal monto, String metodo) {
        if (monto == null) {
            logger.error("El monto es nulo. Método: {}", metodo);
            throw new IllegalArgumentException("El monto no puede ser nulo. Método: " + metodo);
        }

        if (esMenorOIgualACero(monto)) {
            logger.error("El monto es menor o igual a cero. Método: {}", metodo);
            throw new MonetaryAmountException("El monto no puede ser menor o igual a cero. Método: " + metodo, metodo);
        }
    }

    /**
     * Verifica si un monto es menor o igual a cero.
     *
     * @param numero Monto a verificar.
     * @return true si el monto es menor o igual a cero.
     */
    public boolean esMenorOIgualACero(BigDecimal numero) {
        return numero.compareTo(BigDecimal.ZERO) <= 0;
    }
}
