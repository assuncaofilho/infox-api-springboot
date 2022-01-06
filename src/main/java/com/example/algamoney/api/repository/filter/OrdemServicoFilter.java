package com.example.algamoney.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;


public class OrdemServicoFilter {
	
	private String descricao;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataEntregaDe;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataEntregaAte;
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public LocalDate getDataEntregaDe() {
		return dataEntregaDe;
	}
	public void setDataEntregaDe(LocalDate dataEntregaDe) {
		this.dataEntregaDe = dataEntregaDe;
	}
	public LocalDate getDataEntregaAte() {
		return dataEntregaAte;
	}
	public void setDataEntregaAte(LocalDate dataEntregaAte) {
		this.dataEntregaAte = dataEntregaAte;
	}
	
	
	
	

}
