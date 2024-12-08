package com.credibanco.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.credibanco.prueba.entity.Cliente;

@Repository
public interface IclienteRepository extends JpaRepository<Cliente, Long>{

}
