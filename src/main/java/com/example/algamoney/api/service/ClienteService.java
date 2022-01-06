package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Cliente;
import com.example.algamoney.api.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente atualizar(Long codigo, Cliente cliente) {
		
		Cliente clienteSalvo = buscarPessoaPeloCodigo(codigo);
		BeanUtils.copyProperties(cliente, clienteSalvo, "codigo");
		
		return clienteRepository.save(clienteSalvo);
	}


	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		
		Cliente clienteSalvo = buscarPessoaPeloCodigo(codigo);
		clienteSalvo.setAtivo(ativo);
		clienteRepository.save(clienteSalvo);
		
	}
	
	public Cliente buscarPessoaPeloCodigo(Long codigo) {
		Optional<Cliente> clienteSalvoOpt = clienteRepository.findById(codigo);
		if(clienteSalvoOpt.isPresent() == false) {
			throw new EmptyResultDataAccessException(1);
		}
		Cliente clienteSalvo = clienteSalvoOpt.get();
		return clienteSalvo;
	}
}
