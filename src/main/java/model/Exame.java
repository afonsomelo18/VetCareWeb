package model;

import java.math.BigDecimal;

public class Exame extends Servico{
	
	private String tipo;
	private String notas;
	
	public Exame(int idServico, BigDecimal preco, String estado, String tipoServico, String numLicenca,
			String localidade, int diaSemana, String tipo, String notas) {
		super(idServico, preco, estado, tipoServico, numLicenca, localidade, diaSemana);
		this.tipo = tipo;
		this.notas = notas;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

}
