package com.example.algamoney.api.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.example.algamoney.api.model.OrdemServico;
import com.example.algamoney.api.repository.OrdemServicoRepository;
import com.example.algamoney.api.repository.filter.OrdemServicoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;
import com.example.algamoney.api.service.OrdemServicoService;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class OrdemServicoResource {
	
	@Autowired
	private OrdemServicoRepository lancamentoRepository;
	
	@Autowired
	private OrdemServicoService lancamentoService; 
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') ")//and hasAuthority('SCOPE_read')")
	public Page<OrdemServico> pesquisar(OrdemServicoFilter lancamentoFilter, Pageable pageable){
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);	
	}
	
	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') ")//and hasAuthority('SCOPE_read')")
	public Page<ResumoLancamento> resumir(OrdemServicoFilter lancamentoFilter, Pageable pageable){  // Quem é o reponsável por injetar o valor do param em lancamentoFilter? Quem faz é o Angular ou o próprio Spring? 
																									 
		//int testePageNumber = pageable.getPageNumber();	
		//int testePageSize = pageable.getPageSize();
		//String teste = lancamentoFilter.getDescricao();
		
		return lancamentoRepository.resumir(lancamentoFilter, pageable);	
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') ")//and hasAuthority('SCOPE_read')")
	public ResponseEntity<OrdemServico> buscarPeloCodigo(@PathVariable Long codigo) {
		
		Optional<OrdemServico> lancamentoBuscado = lancamentoRepository.findById(codigo);
				
		return lancamentoBuscado.isPresent() == true ? ResponseEntity.ok(lancamentoBuscado.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') ")//and hasAuthority('SCOPE_write')")
	public ResponseEntity<OrdemServico> criar(@Valid @RequestBody OrdemServico lancamento, HttpServletResponse response){
	
		/*Caso ocorra exceção o @ExceptionHandler de PessoaInexistenteOuInativaException.class será acionado*/
		OrdemServico lancamentoSalvo = lancamentoService.salvar(lancamento); 
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO')")//and hasAuthority('SCOPE_write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}
	
	/*Como trata-se de uma exceção específica para Lançamento, trata-se a mesma na própria classe de LancamentoResource
	 * Isto é, no controlador de Lancamento*/
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
		
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = new ArrayList<Erro>();
		erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
	public ResponseEntity<OrdemServico> atualizar(@PathVariable Long codigo, @Valid @RequestBody OrdemServico lancamento) {
		try {
			OrdemServico lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
