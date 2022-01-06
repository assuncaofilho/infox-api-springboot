package com.example.algamoney.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	public Page<Cliente> findByNomeContaining(String nome, Pageable pageable);

}
