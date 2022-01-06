package com.example.algamoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrdemServico.class)
public abstract class OrdemServico_ {

	public static volatile SingularAttribute<OrdemServico, Long> codigo;
	public static volatile SingularAttribute<OrdemServico, TipoOrdemServico> tipo;
	public static volatile SingularAttribute<OrdemServico, String> situacao;
	public static volatile SingularAttribute<OrdemServico, String> observacao;
	public static volatile SingularAttribute<OrdemServico, LocalDate> dataPagamento;
	public static volatile SingularAttribute<OrdemServico, Date> dataRegistro;
	public static volatile SingularAttribute<OrdemServico, Categoria> categoria;
	public static volatile SingularAttribute<OrdemServico, BigDecimal> valor;
	public static volatile SingularAttribute<OrdemServico, String> descricao;
	public static volatile SingularAttribute<OrdemServico, String> defeito;
	public static volatile SingularAttribute<OrdemServico, Cliente> cliente;
	public static volatile SingularAttribute<OrdemServico, LocalDate> dataEntrega;
	public static volatile SingularAttribute<OrdemServico, String> equipamento;
	public static volatile SingularAttribute<OrdemServico, Usuario> usuario;
	public static volatile SingularAttribute<OrdemServico, String> servico;

	public static final String CODIGO = "codigo";
	public static final String TIPO = "tipo";
	public static final String SITUACAO = "situacao";
	public static final String OBSERVACAO = "observacao";
	public static final String DATA_PAGAMENTO = "dataPagamento";
	public static final String DATA_REGISTRO = "dataRegistro";
	public static final String CATEGORIA = "categoria";
	public static final String VALOR = "valor";
	public static final String DESCRICAO = "descricao";
	public static final String DEFEITO = "defeito";
	public static final String CLIENTE = "cliente";
	public static final String DATA_ENTREGA = "dataEntrega";
	public static final String EQUIPAMENTO = "equipamento";
	public static final String USUARIO = "usuario";
	public static final String SERVICO = "servico";

}

