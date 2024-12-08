package com.credibanco.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.credibanco.prueba.entity.Transaccion;

import jakarta.transaction.Transactional;

@Repository
public interface ItransaccionRepository extends JpaRepository<Transaccion, Long>{
	
	//Update para anular la compra o transacción
	@Modifying
	@Transactional
	@Query(value = "UPDATE transacciones SET estado = :estado WHERE id_tarjeta = :idTarjeta AND id_transaccion = :idTransaccion", nativeQuery = true)
	int anularTransacción(@Param("estado") String estado, 
	                   @Param("idTarjeta") Long idTarjeta, 
	                   @Param("idTransaccion") Long idTransaccion);

}
