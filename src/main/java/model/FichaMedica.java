package model;

import java.sql.Date;

/**
 * Representa a ficha médica de um paciente.
 */
public class FichaMedica {
	private int idPaciente;
	private String sexo;
	private String nome;
	private Date data_nasc;
	private String estadoReprodutivo;
	private String alergias;
	private Double peso;
	private String cor;
	private String caract;
	private String foto;
	private String transponder;
	
	
	public FichaMedica(int idPaciente, String sexo, String nome, Date data_nasc,String estadoReprodutivo, String alergias, Double peso,
			String cor, String caract, String foto, String transponder) {
		super();
		this.idPaciente = idPaciente;
		this.sexo = sexo;
		this.nome = nome;
		this.data_nasc = data_nasc;
		this.estadoReprodutivo = estadoReprodutivo;
		this.alergias = alergias;
		this.peso = peso;
		this.cor = cor;
		this.caract = caract;
		this.foto = foto;
		this.transponder = transponder;
	}


	public int getIdPaciente() {
		return idPaciente;
	}


	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}


	public String getSexo() {
		return sexo;
	}


	public void setSexo(String sexo) {
		this.sexo = sexo;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getEstadoReprodutivo() {
		return estadoReprodutivo;
	}


	public void setEstadoReprodutivo(String estadoReprodutivo) {
		this.estadoReprodutivo = estadoReprodutivo;
	}


	public String getAlergias() {
		return alergias;
	}


	public void setAlergias(String alergias) {
		this.alergias = alergias;
	}


	public Double getPeso() {
		return peso;
	}


	public void setPeso(Double peso) {
		this.peso = peso;
	}


	public String getCor() {
		return cor;
	}


	public void setCor(String cor) {
		this.cor = cor;
	}


	public String getCaract() {
		return caract;
	}


	public void setCaract(String caract) {
		this.caract = caract;
	}


	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}


	public String getTransponder() {
		return transponder;
	}


	public void setTransponder(String transponder) {
		this.transponder = transponder;
	}


	public Date getData_nasc() {
		return data_nasc;
	}


	public void setData_nasc(Date data_nasc) {
		this.data_nasc = data_nasc;
	}
	
	
	
	
}
