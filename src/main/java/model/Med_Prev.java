package model;

import java.math.BigDecimal;
import java.sql.Date;

public class Med_Prev extends Servico{
	private String tipo;
	private String produto;
	private String fabricante;
	private Date prox_aplicacao;
	
	public Med_Prev(int idServico, BigDecimal preco, String estado, String tipoServico, String numLicenca,
			String localidade, int diaSemana, String tipo, String produto, String fabricante, Date prox_aplicacao) {
		super(idServico, preco, estado, tipoServico, numLicenca, localidade, diaSemana);
		this.tipo = tipo;
		this.produto = produto;
		this.fabricante = fabricante;
		this.prox_aplicacao = prox_aplicacao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public Date getProx_aplicacao() {
		return prox_aplicacao;
	}

	public void setProx_aplicacao(Date prox_aplicacao) {
		this.prox_aplicacao = prox_aplicacao;
	}
	
	
	
}
