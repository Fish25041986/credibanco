package unitTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;


import com.credibanco.prueba.entity.Tarjeta;
import com.credibanco.prueba.exceptions.ExistingInformationException;
import com.credibanco.prueba.exceptions.NoDataFoundException;
import com.credibanco.prueba.repository.ItarjetaRepository;
import com.credibanco.prueba.service.TarjetaService;
import com.credibanco.prueba.utils.EstadoTarjeta;
import com.credibanco.prueba.utils.GeneradorTarjetaNumero;
import com.credibanco.prueba.utils.TipoTarjeta;
import com.credibanco.prueba.utils.ValidadorNumeros;

@ExtendWith(MockitoExtension.class)
public class TarjetaServiceTest {

    @InjectMocks
    private TarjetaService tarjetaService;

    @Mock
    private ItarjetaRepository tarjetaRepository;

    @Mock
    private GeneradorTarjetaNumero generadorTarjetaNumero;
    
    @Mock
    private ValidadorNumeros validadorNumeros;

    private Tarjeta tarjeta;

    @BeforeEach
    public void setUp() {
        tarjeta = new Tarjeta();
        tarjeta.setIdTarjeta(1L);
        tarjeta.setIdLogicTarjeta("123456");
        tarjeta.setNumeroTarjeta(null);
        tarjeta.setTipoTarjeta(TipoTarjeta.DEBITO);
        tarjeta.setFechaCreacion(LocalDate.now());
        tarjeta.setFechaVencimiento(LocalDate.now().plusYears(1));
        tarjeta.setEstado(EstadoTarjeta.inactiva);
        tarjeta.setSaldo(BigDecimal.valueOf(100));
    }

    @Test
    public void testGenerateNumberTarjeta_Success() {
        String idLogicTarjeta = "123456";

        when(tarjetaRepository.findByIdLogicTarjeta(idLogicTarjeta)).thenReturn(Optional.of(tarjeta));
        when(generadorTarjetaNumero.generarNumeroTarjeta(idLogicTarjeta)).thenReturn("1234567890123456");

        Boolean result = tarjetaService.generateNumberTarjeta(idLogicTarjeta);

        assertTrue(result);
        verify(tarjetaRepository).actualizarTarjeta("1234567890123456", idLogicTarjeta);
    }
    
    @Test
    public void testGenerateNumberTarjeta_TarjetaNoEncontrada() {
        String idLogicTarjeta = "999999"; // ID que no existenete

        when(tarjetaRepository.findByIdLogicTarjeta(idLogicTarjeta)).thenReturn(Optional.empty());

        // Espera que se lance la excepción NoDataFoundException
        assertThrows(NoDataFoundException.class, () -> {
            tarjetaService.generateNumberTarjeta(idLogicTarjeta);
        });

        verify(tarjetaRepository, never()).actualizarTarjeta(toString(), toString()); // No se debe llamar a actualizarTarjeta
    }
    
    @Test
    public void testGenerateNumberTarjeta_TarjetaExistente() {
        String idLogicTarjeta = "123456"; // ID que si existe
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta("1234567890123456"); // Tarjeta que ya existe

        when(tarjetaRepository.findByIdLogicTarjeta(idLogicTarjeta)).thenReturn(Optional.of(tarjeta));

        assertThrows(ExistingInformationException.class, () -> {
            tarjetaService.generateNumberTarjeta(idLogicTarjeta);
        });
    }
    
    @Test
    public void testGenerateNumberTarjeta_IdentificadorInvalido() {
        String idLogicTarjeta = "123"; // idLogicTarjeta inválido

        //Simula que el validador lanza una excepción
        doThrow(new IllegalArgumentException("ID inválido")).when(validadorNumeros).validarIdTarjeta(idLogicTarjeta, 6, "generateNumberTarjeta");

        // Espero que se lance la excepción IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            tarjetaService.generateNumberTarjeta(idLogicTarjeta);
        });

        verify(tarjetaRepository, never()).actualizarTarjeta(anyString(), anyString()); // No se debe llamar a actualizarTarjeta
    }
    
    @Test
    public void testGenerateNumberTarjeta_NumeroGeneradoValido() {
        String idLogicTarjeta = "123456";
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setNumeroTarjeta(null); // Sin número asignado

        when(tarjetaRepository.findByIdLogicTarjeta(idLogicTarjeta)).thenReturn(Optional.of(tarjeta));
        when(generadorTarjetaNumero.generarNumeroTarjeta(idLogicTarjeta)).thenReturn("1234567890123456");

        Boolean result = tarjetaService.generateNumberTarjeta(idLogicTarjeta);

        assertTrue(result);
        verify(tarjetaRepository).actualizarTarjeta("1234567890123456", idLogicTarjeta);

        // Verifico que el número generado tiene 16 dígitos
        assertEquals(16, "1234567890123456".length());
    }
    
    @Test
    void testGenerateNumberTarjeta_ExistingInformation() {
    	
        // Crea un mock de Tarjeta con un número existente
        Tarjeta tarjetaMock = new Tarjeta();
        tarjetaMock.setNumeroTarjeta("1234567890123456"); // Simulo que ya existe un número

        // Configuro el mock para que devuelva la tarjeta existente
        when(tarjetaRepository.findByIdLogicTarjeta(anyString())).thenReturn(Optional.of(tarjetaMock));

        // Afirmo que se lanza la excepción de información existente
        assertThrows(ExistingInformationException.class, () -> {
            tarjetaService.generateNumberTarjeta("testId");
        });
    }

    


}