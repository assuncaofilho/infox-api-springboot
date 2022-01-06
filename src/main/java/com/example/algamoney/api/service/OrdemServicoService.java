package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.OrdemServico;
import com.example.algamoney.api.model.Cliente;
import com.example.algamoney.api.repository.ClienteRepository;
import com.example.algamoney.api.repository.OrdemServicoRepository;
import com.example.algamoney.api.repository.ClienteRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class OrdemServicoService {
	
	@Autowired
	private ClienteRepository clienteRepository; //
	
	@Autowired
	private OrdemServicoRepository lancamentoRepository;

	public OrdemServico salvar(OrdemServico ordemServico) {
		
		Optional<Cliente> pessoa = clienteRepository.findById(ordemServico.getCliente().getCodigo());
		if (!pessoa.isPresent() || (pessoa.isPresent() && pessoa.get().isInativo())) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(ordemServico);
	}
	
	public OrdemServico atualizar(Long codigo, OrdemServico ordemServico) {
		OrdemServico lancamentoSalvo = buscarLancamentoExistente(codigo);
		if (!ordemServico.getCliente().equals(lancamentoSalvo.getCliente())) {
			validarPessoa(ordemServico); // verificando se a pessoa passada na requisição existe no banco; se não existir já lança exception;
		}

		BeanUtils.copyProperties(ordemServico, lancamentoSalvo, "codigo"); // excetua-se o código do lançamento;

		return lancamentoRepository.save(lancamentoSalvo);
	}
	
	private void validarPessoa(OrdemServico ordemServico) {
		Cliente cliente = null;
		if (ordemServico.getCliente().getCodigo() != null) {
			cliente = clienteRepository.findById(ordemServico.getCliente().getCodigo()).get();
		}

		if (cliente == null || cliente.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}
	
	private OrdemServico buscarLancamentoExistente(Long codigo) {
		Optional<OrdemServico> lancamentoSalvoOpt = lancamentoRepository.findById(codigo);
		if (!lancamentoSalvoOpt.isPresent()) {
			throw new IllegalArgumentException();
		}
		return lancamentoSalvoOpt.get();
	}
	

}
