package com.credibanco.prueba.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.credibanco.prueba.utils.EstadoTarjeta;
import com.credibanco.prueba.utils.TipoTarjeta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="tarjetas")
public class Tarjeta implements Serializable {
    
    private static final long serialVersionUID = -4310027227752446841L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarjeta")
    private Long idTarjeta;
    
    @Column(name = "id_logic_tarjeta", length = 6, nullable = false, unique = true)
    private String idLogicTarjeta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    
    @Column(name = "numero_tarjeta", nullable = false, unique = true, length = 16)
    private String numeroTarjeta;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoTarjeta tipoTarjeta;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;
    
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTarjeta estado = EstadoTarjeta.inactiva;
    
    @Column(name = "saldo", precision = 18, scale = 2, nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;
}