package model;

import java.math.BigDecimal;

/**
 * Representa um serviço do tipo Consulta.
 *
 * Corresponde à tabela CONSULTA da base de dados e contém
 * os atributos clínicos específicos associados a uma consulta
 * veterinária.
 *
 * Esta classe estende Servico, herdando os atributos comuns
 * a todos os serviços médicos.
 */
public class Consulta extends Servico{
	private String motivo;
	private String diagnostico;
	private String medicacao;
	
	public Consulta(int idServico, BigDecimal preco, String estado,
            String tipoServico, String numLicenca,
            String localidade, int diaSemana,
            String motivo, String diagnostico, String medicacao) {

		super(idServico, preco, estado, tipoServico, numLicenca, localidade, diaSemana);
		this.motivo = motivo;
		this.diagnostico = diagnostico;
		this.medicacao = medicacao;
	}

	public int getId_servico() {
		return id_servico;
	}

	public void setId_servico(int id_servico) {
		this.id_servico = id_servico;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getMedicacao() {
		return medicacao;
	}

	public void setMedicacao(String medicacao) {
		this.medicacao = medicacao;
	}
	
	
}
