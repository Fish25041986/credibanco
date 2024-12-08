package com.credibanco.prueba.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.credibanco.prueba.utils.EstadoTransaccion;

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
@Table(name="Transacciones")
public class Transaccion implements Serializable{
	
	private static final long serialVersionUID = -4310027227752446841L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
	private Long idTransaccion;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_tarjeta")
    private Tarjeta tarjeta;
    
    @Column(name="fecha_transaccion", nullable=false)
    private LocalDateTime fechaTransaccion;
    
    @Column(name="monto", precision=18, scale=2, nullable=false)
    private BigDecimal monto;
   
    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable = false, length = 10)
    private EstadoTransaccion estado;

}
