package model;

import java.math.BigDecimal;

public class Terapeutico extends Servico{
	private String tipo_ferida;
	private String cuidados;
	
	public Terapeutico(int idServico, BigDecimal preco, String estado, String tipoServico, String numLicenca,
			String localidade, int diaSemana, String tipo_ferida, String cuidados) {
		super(idServico, preco, estado, tipoServico, numLicenca, localidade, diaSemana);
		this.tipo_ferida = tipo_ferida;
		this.cuidados = cuidados;
	}

	public String getTipo_ferida() {
		return tipo_ferida;
	}

	public void setTipo_ferida(String tipo_ferida) {
		this.tipo_ferida = tipo_ferida;
	}

	public String getCuidados() {
		return cuidados;
	}

	public void setCuidados(String cuidados) {
		this.cuidados = cuidados;
	}
	
	
}
