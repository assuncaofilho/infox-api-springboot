package com.example.algamoney.api.repository.ordemservico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.algamoney.api.model.OrdemServico;
import com.example.algamoney.api.repository.filter.OrdemServicoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

public interface OrdemServicoRepositoryQuery {
	
	public Page<OrdemServico> filtrar(OrdemServicoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(OrdemServicoFilter lancamentoFilter, Pageable pageable);

}
