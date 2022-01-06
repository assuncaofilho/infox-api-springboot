package com.example.algamoney.api.repository.ordemservico;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import com.example.algamoney.api.model.Categoria_;
import com.example.algamoney.api.model.OrdemServico;
import com.example.algamoney.api.model.OrdemServico_;
import com.example.algamoney.api.model.Cliente_;
import com.example.algamoney.api.repository.filter.OrdemServicoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

public class OrdemServicoRepositoryImpl implements OrdemServicoRepositoryQuery{

	@PersistenceContext
	private EntityManager entitymanager;
	
	/*Utilizar-se-á a Criteria do JPA*/
	@Override
	public Page<OrdemServico> filtrar(OrdemServicoFilter ordemServicoFilter, Pageable pageable) {
		
		/*CriteriaBuilder -> CriteriaQuery -> TypedQuery -> .getResultList()*/
		
		//obtendo o objeto CriteriaBuilder, a partir de um EntityManager
		CriteriaBuilder builder = entitymanager.getCriteriaBuilder();
		
		//CriteriaQuery define por sua vez a entidade principal a ser utilizada 
		//na query – aqui no caso a classe Lancamento
		//passa-se o retorno esperado da consulta
		CriteriaQuery<OrdemServico> criteriaQuery = builder.createQuery(OrdemServico.class);
		
		/*O próximo passo é estabelecer a raiz da consulta, ou seja, a classe principal da cláusula FROM. 
		 * Para isso utilizamos o método from() a partir da interface CriteriaQuery. 
		 * Isso equivale à declaração de uma variável de identificação, e formará a base para expressões 
		 * que será utilizada como o caminho para o resto da consulta.
		 * Equivalente ao alias l em from Lancamento l - Podemos usá-lo como ponto de partida para acessar atributos da entidade*/
		
		Root<OrdemServico> root = criteriaQuery.from(OrdemServico.class); 
		
		
		Predicate[] predicates = criarRestricoes(ordemServicoFilter, builder, root);
		criteriaQuery.where(predicates);
		
		TypedQuery<OrdemServico> typedQuery = entitymanager.createQuery(criteriaQuery);
		adicionarRestricoesDePaginacao(typedQuery, pageable);
		
		return new PageImpl<>(typedQuery.getResultList(), pageable, total(ordemServicoFilter)) ;
	}

	@Override
	public Page<ResumoLancamento> resumir(OrdemServicoFilter ordemServicoFilter, Pageable pageable) {
		CriteriaBuilder builder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<OrdemServico> root = criteria.from(OrdemServico.class);
		
		criteria.select(builder.construct(ResumoLancamento.class
				, root.get(OrdemServico_.codigo), root.get(OrdemServico_.descricao)
				, root.get(OrdemServico_.dataPagamento), root.get(OrdemServico_.dataPagamento)
				, root.get(OrdemServico_.valor), root.get(OrdemServico_.tipo)
				, root.get(OrdemServico_.categoria).get(Categoria_.nome)
				, root.get(OrdemServico_.cliente).get(Cliente_.nome)));
		
		Predicate[] predicates = criarRestricoes(ordemServicoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = entitymanager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(ordemServicoFilter));
	}


	private Predicate[] criarRestricoes(OrdemServicoFilter ordemServicoFilter, CriteriaBuilder builder,Root<OrdemServico> root) {
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(!ObjectUtils.isEmpty(ordemServicoFilter.getDescricao())) {
			predicates.add(builder.like(
					builder.lower(root.get(OrdemServico_.DESCRICAO)) , "%" + ordemServicoFilter.getDescricao() + "%"));
		}
		
		if(!ObjectUtils.isEmpty(ordemServicoFilter.getDataEntregaDe())) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get(OrdemServico_.DATA_ENTREGA), ordemServicoFilter.getDataEntregaDe()));
			
		}
		
		
		if(!ObjectUtils.isEmpty(ordemServicoFilter.getDataEntregaAte())) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get(OrdemServico_.DATA_ENTREGA), ordemServicoFilter.getDataEntregaAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
		//Predicate[] predicate = new Predicate[predicates.size()];
		//return predicates.toArray(predicate);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> typedQuery, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber(); // começa em 0
		int totalRegistrosPorPagina = pageable.getPageSize(); // pageable vem setado do Cliente;
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		typedQuery.setFirstResult(primeiroRegistroDaPagina); // o primeiro resultado inicia em 0;
		typedQuery.setMaxResults(totalRegistrosPorPagina); 
		
	}
	private Long total(OrdemServicoFilter ordemServicoFilter) {
		
		CriteriaBuilder builder = entitymanager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class); // tipo de retorno
		Root<OrdemServico> root = criteriaQuery.from(OrdemServico.class); // entidade na qual será realizada a consulta
		
		Predicate[] predicates = criarRestricoes(ordemServicoFilter, builder, root);
		criteriaQuery.where(predicates);
		
		criteriaQuery.select(builder.count(root));
		return entitymanager.createQuery(criteriaQuery).getSingleResult();
		
	}



}
