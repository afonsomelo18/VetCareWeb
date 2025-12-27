package model;

import java.sql.Date;
import java.sql.Timestamp;

/*
 * Representa o registo clinico do animal
 */
public class Agendamento {
	private int id_agendamento;
	private int id_paciente;
	private int id_servico;
	private String localidade;
	private int dia_semana;
	private String nif_tutor;
	private Timestamp data_hora;
	private String estado;
	private String obs;
	
	public Agendamento(int id_agendamento, int id_paciente, int id_servico, String localidade, int dia_semana,
			String nif_tutor, Timestamp data_hora, String estado, String obs) {
		super();
		this.id_agendamento = id_agendamento;
		this.id_paciente = id_paciente;
		this.id_servico = id_servico;
		this.localidade = localidade;
		this.dia_semana = dia_semana;
		this.nif_tutor = nif_tutor;
		this.data_hora = data_hora;
		this.estado = estado;
		this.obs = obs;
	}

	public int getId_agendamento() {
		return id_agendamento;
	}

	public void setId_agendamento(int id_agendamento) {
		this.id_agendamento = id_agendamento;
	}

	public int getId_paciente() {
		return id_paciente;
	}

	public void setId_paciente(int id_paciente) {
		this.id_paciente = id_paciente;
	}

	public int getId_servico() {
		return id_servico;
	}

	public void setId_servico(int id_servico) {
		this.id_servico = id_servico;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public int getDia_semana() {
		return dia_semana;
	}

	public void setDia_semana(int dia_semana) {
		this.dia_semana = dia_semana;
	}

	public String getNif_tutor() {
		return nif_tutor;
	}

	public void setNif_tutor(String nif_tutor) {
		this.nif_tutor = nif_tutor;
	}

	public Timestamp getData_hora() {
		return data_hora;
	}

	public void setData_hora(Timestamp data_hora) {
		this.data_hora = data_hora;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	
	
	
}
