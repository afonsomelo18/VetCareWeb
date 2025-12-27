package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * Representa um paciente registado no sistema.
 */
public class Paciente {
	private int idPaciente;
	private String NIF_tutor;
	private String nomeEspecie;
	private String nomeRaca;
	private int idSistema;
	private int idProgenitor;
	private List<Progenitor> progenitores = new ArrayList<>();
	
	
	public Paciente(int idPaciente,  String nomeEspecie, String nomeRaca, String nIF_tutor,int idSistema,
			 int idProgenitor) {
		this.idPaciente = idPaciente;
		NIF_tutor = nIF_tutor;
		this.nomeEspecie = nomeEspecie;
		this.nomeRaca = nomeRaca;
		this.idSistema = idSistema;
		this.idProgenitor = idProgenitor;
	}


	public int getIdPaciente() {
		return idPaciente;
	}


	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}


	public String getNIF_tutor() {
		return NIF_tutor;
	}


	public void setNIF_tutor(String nIF_tutor) {
		NIF_tutor = nIF_tutor;
	}


	public String getNomeEspecie() {
		return nomeEspecie;
	}


	public void setNomeEspecie(String nomeEspecie) {
		this.nomeEspecie = nomeEspecie;
	}


	public String getNomeRaca() {
		return nomeRaca;
	}


	public void setNomeRaca(String nomeRaca) {
		this.nomeRaca = nomeRaca;
	}


	public int getIdSistema() {
		return idSistema;
	}


	public void setIdSistema(int idSistema) {
		this.idSistema = idSistema;
	}

	public int getIdProgenitor() {
		return idProgenitor;
	}


	public void setIdProgenitor(int idProgenitor) {
		this.idProgenitor = idProgenitor;
	}
	
	public List<Progenitor> getProgenitores() { return progenitores; }
    public void setProgenitores(List<Progenitor> progenitores) { this.progenitores = progenitores; }
}
