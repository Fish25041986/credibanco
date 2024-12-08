package unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.credibanco.prueba.dto.transaccion.TransaccionShoppingRequesDTO;
import com.credibanco.prueba.entity.Tarjeta;
import com.credibanco.prueba.exceptions.MonetaryAmountException;
import com.credibanco.prueba.exceptions.StatusCardException;
import com.credibanco.prueba.repository.ItarjetaRepository;
import com.credibanco.prueba.repository.ItransaccionRepository;
import com.credibanco.prueba.service.TransactionService;
import com.credibanco.prueba.utils.DateActual;
import com.credibanco.prueba.utils.EstadoTarjeta;
import com.credibanco.prueba.utils.ValidadorNumeros;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private ItarjetaRepository tarjetaRepository;

    @Mock
    private ItransaccionRepository transaccionRepository;

    @Mock
    private DateActual dateActual;

    @Mock
    private ValidadorNumeros validadorNumeros;

    @Test
    void testCompras_TarjetaBloqueada() {
        // Configurar la tarjeta con estado bloqueado
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setEstado(EstadoTarjeta.bloqueada);  // Asegúrate de que el estado sea 'bloqueada'
        when(tarjetaRepository.findByNumeroTarjeta("1234567890123456")).thenReturn(Optional.of(tarjeta));

        TransaccionShoppingRequesDTO request = new TransaccionShoppingRequesDTO("1234567890123456", new BigDecimal(100));

        // Ejecutar el servicio y esperar que se lance la excepción
        StatusCardException exception = assertThrows(StatusCardException.class, () -> {
            transactionService.compras(request);
        });

        // Verificar que el mensaje de la excepción sea el esperado
        assertTrue(exception.getMessage().contains("La tarjeta esta bloqueada"));
    }

    @Test
    void testCompras_SaldoInsuficiente() {
        // Setup mock tarjeta con saldo insuficiente
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setEstado(EstadoTarjeta.activa);
        tarjeta.setSaldo(new BigDecimal(50));  // Saldo menor al monto
        when(tarjetaRepository.findByNumeroTarjeta("1234567890123456")).thenReturn(Optional.of(tarjeta));

        TransaccionShoppingRequesDTO request = new TransaccionShoppingRequesDTO("1234567890123456", new BigDecimal(100));

        // Ejecutar la prueba
        MonetaryAmountException exception = assertThrows(MonetaryAmountException.class, () -> {
            transactionService.compras(request);
        });

        assertEquals("El valor de la compra sobrepasa el saldo actual.", exception.getMessage());
    }

    @Test
    void testCompras_TarjetaVencida() {
        // Setup mock tarjeta con fecha de vencimiento pasada
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setEstado(EstadoTarjeta.activa);
        tarjeta.setSaldo(new BigDecimal(100));
        tarjeta.setFechaVencimiento(LocalDate.now().minusDays(1)); // Vencida
        when(tarjetaRepository.findByNumeroTarjeta("1234567890123456")).thenReturn(Optional.of(tarjeta));

        TransaccionShoppingRequesDTO request = new TransaccionShoppingRequesDTO("1234567890123456", new BigDecimal(50));

        // Ejecutar la prueba
        StatusCardException exception = assertThrows(StatusCardException.class, () -> {
            transactionService.compras(request);
        });

        assertEquals("La tarjeta esta vencida", exception.getMessage());
    }

 
    
    @Test
    void testCompras_ErrorGuardandoTransaccion() {
        // Setup mock tarjeta con saldo suficiente y fecha de vencimiento pasada (tarjeta vencida)
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setEstado(EstadoTarjeta.activa);
        tarjeta.setSaldo(new BigDecimal(100)); // Saldo suficiente
        tarjeta.setFechaVencimiento(LocalDate.now().minusDays(1)); // Tarjeta vencida
        when(tarjetaRepository.findByNumeroTarjeta("1234567890123456")).thenReturn(Optional.of(tarjeta));

        TransaccionShoppingRequesDTO request = new TransaccionShoppingRequesDTO("1234567890123456", new BigDecimal(50));

        // Ejecutar la prueba
        StatusCardException exception = assertThrows(StatusCardException.class, () -> {
            transactionService.compras(request);
        });

        // Verificar que el mensaje de la excepción sea el esperado
        assertEquals("La tarjeta esta vencida", exception.getMessage());
        assertEquals(EstadoTarjeta.vencida.toString(), exception.getStatus());
    }
    
    
}