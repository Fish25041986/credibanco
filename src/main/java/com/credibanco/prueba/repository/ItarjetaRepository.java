package com.credibanco.prueba.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.credibanco.prueba.entity.Tarjeta;

import jakarta.transaction.Transactional;

@Repository
public interface ItarjetaRepository extends JpaRepository<Tarjeta, Long>{
	
	Optional<Tarjeta> findByIdLogicTarjeta(String idLogicTarjeta);
	
	//Update para agregar el numero completo de la tarjeta
	@Modifying
	@Transactional
	@Query(value = "UPDATE tarjetas SET numero_tarjeta = :numeroTarjeta WHERE id_logic_tarjeta = :idLogicTarjeta", nativeQuery = true)
	int actualizarTarjeta(String numeroTarjeta, String idLogicTarjeta);

	
	Boolean existsByNumeroTarjeta(String numeroTarjeta);
	
	//Consulta por el numero completo de la tarjeta
	Optional<Tarjeta> findByNumeroTarjeta(String numeroTarjeta);
	
	//Update para activar la tarjeta
	@Modifying
	@Transactional
	@Query(value = "UPDATE tarjetas SET estado = :estadoTarjeta, fecha_creacion = :fechaCreacion, fecha_vencimiento = :fechaVencimiento WHERE numero_tarjeta = :numeroTarjeta AND id_tarjeta = :idTarjeta", nativeQuery = true)
	int activarTarjeta(@Param("estadoTarjeta") String estadoTarjeta, 
	                   @Param("fechaCreacion") Date fechaCreacion, 
	                   @Param("fechaVencimiento") Date fechaVencimiento, 
	                   @Param("numeroTarjeta") String numeroTarjeta, 
	                   @Param("idTarjeta") Long idTarjeta);
	
	//Update para bloquear la tarjeta
	@Modifying
	@Transactional
	@Query(value = "UPDATE tarjetas SET estado = :estadoTarjeta WHERE numero_tarjeta = :numeroTarjeta AND id_tarjeta = :idTarjeta", nativeQuery = true)
	int bloquearTarjeta(@Param("estadoTarjeta") String estadoTarjeta, 
	                   @Param("numeroTarjeta") String numeroTarjeta, 
	                   @Param("idTarjeta") Long idTarjeta);
	
	
	//Update para recargar la tarjeta o descontar el valor de la compra
	@Modifying
	@Transactional
	@Query(value = "UPDATE tarjetas SET saldo = :saldo WHERE numero_tarjeta = :numeroTarjeta AND id_tarjeta = :idTarjeta", nativeQuery = true)
	int actualizarSaldo(@Param("saldo") BigDecimal saldo, 
	                   @Param("numeroTarjeta") String numeroTarjeta, 
	                   @Param("idTarjeta") Long idTarjeta);
	
   
}
