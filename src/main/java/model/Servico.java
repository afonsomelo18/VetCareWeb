package model;

import java.math.BigDecimal;

/**
 * Classe abstrata que representa um Serviço Médico.
 *
 * Corresponde à tabela SERVICO da base de dados e contém
 * os atributos comuns a todos os tipos de serviços médicos.
 *
 * As subclasses (Consulta, Exame, Cirurgia, etc.) deverão
 * acrescentar os atributos específicos de cada tipo de serviço.
 */
public abstract class Servico {
	protected int id_servico;
	protected BigDecimal preco;
	protected String estado;        // ativo / inativo
    protected String tipoServico;   // consulta, exame, cirurgia, etc.
    protected String numLicenca;    // veterinário responsável
    protected String localidade;
    protected int diaSemana;
    
    public Servico(int idServico, BigDecimal preco, String estado,
            String tipoServico, String numLicenca,
            String localidade, int diaSemana) {

    	this.id_servico = idServico;
    	this.preco = preco;
    	this.estado = estado;
    	this.tipoServico = tipoServico;
    	this.numLicenca = numLicenca;
    	this.localidade = localidade;
    	this.diaSemana = diaSemana;
    }

	public int getId_servico() {
		return id_servico;
	}

	public void setId_servico(int id_servico) {
		this.id_servico = id_servico;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getNumLicenca() {
		return numLicenca;
	}

	public void setNumLicenca(String numLicenca) {
		this.numLicenca = numLicenca;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public int getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(int diaSemana) {
		this.diaSemana = diaSemana;
	}
    
}
