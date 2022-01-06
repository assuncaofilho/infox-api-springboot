package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.OrdemServico;
import com.example.algamoney.api.repository.ordemservico.OrdemServicoRepositoryQuery;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long>, OrdemServicoRepositoryQuery{

}
