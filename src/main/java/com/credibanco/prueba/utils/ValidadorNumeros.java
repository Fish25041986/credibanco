package com.credibanco.prueba.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.credibanco.prueba.exceptions.InvalidNumberException;
import com.credibanco.prueba.exceptions.InvalidNumberLengthException;
import com.credibanco.prueba.exceptions.IsNullOrEmptyException;

@Component
public class ValidadorNumeros {

    private static final Logger logger = LoggerFactory.getLogger(ValidadorNumeros.class);

    /**
     * Valida un identificador de tarjeta. 
     * Comprueba si está vacío o nulo, si contiene solo números y si tiene la longitud correcta.
     *
     * @param idTarjeta Identificador a validar.
     * @param longitudEsperada Longitud esperada del identificador.
     * @param metodo Nombre del método que llama a esta validación (para log y excepciones).
     * @throws IsNullOrEmptyException Si el identificador está vacío o nulo.
     * @throws InvalidNumberException Si el identificador contiene caracteres no válidos.
     * @throws InvalidNumberLengthException Si la longitud del identificador es incorrecta.
     */
    public void validarIdTarjeta(String idTarjeta, int longitudEsperada, String metodo) {
        if (esIdTarjetaNuloOVacio(idTarjeta)) {
            logger.error("El idTarjeta está vacío o es nulo. Método: {}", metodo);
            throw new IsNullOrEmptyException("El identificador está vacío o es nulo. Método: " + metodo);
        }

        if (!esSoloNumeros(idTarjeta)) {
            logger.error("El idTarjeta contiene caracteres no válidos. Método: {}", metodo);
            throw new InvalidNumberException("El identificador contiene caracteres no válidos. Método: " + metodo);
        }

        if (contarCaracteres(idTarjeta) != longitudEsperada) {
            logger.error("Longitud inválida para el idTarjeta. Esperado: {}, Recibido: {}. Método: {}", 
                         longitudEsperada, contarCaracteres(idTarjeta), metodo);
            throw new InvalidNumberLengthException("Longitud inválida. Método: " + metodo);
        }
    }

    public boolean esIdTarjetaNuloOVacio(String idTarjeta) {
        return idTarjeta == null || idTarjeta.isEmpty();
    }

    public boolean esSoloNumeros(String cadena) {
        cadena = cadena.trim();
        for (char c : cadena.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public int contarCaracteres(String idInput) {
        return idInput.length();
    }

}


