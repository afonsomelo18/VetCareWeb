package model;

import java.math.BigDecimal;
import java.time.Duration;

/*
* Representa um serviço do tipo Cirurgia.
*
* Contém os dados clínicos específicos associados
* a um procedimento cirúrgico.
*/
public class Cirurgia extends Servico{
	private String descricao;
	private Duration duracao;
	private String procedimento;
	
	public Cirurgia(int idServico, BigDecimal preco, String estado,
            String tipoServico, String numLicenca,
            String localidade, int diaSemana,
            String descricao, Duration duracao, String procedimento) {
		
		super(idServico, preco, estado, tipoServico, numLicenca, localidade, diaSemana);
		this.descricao = descricao;
		this.duracao = duracao;
		this.procedimento = procedimento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Duration getDuracao() {
		return duracao;
	}

	public void setDuracao(Duration duracao) {
		this.duracao = duracao;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}
	
	
}
