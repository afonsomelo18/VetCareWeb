package model;

public class Tutor {
	private String nif;
	private String contacto;
	private String pref_linguistica;
	private String tipo;
	private int idFreguesia;
    private int idConcelho;
    private String nome;
    
    public Tutor(String nif, String contacto, String pref_linguistica, String tipo, int idFreguesia, int idConcelho, String nome) {
        this.nif = nif;
        this.contacto = contacto;
        this.pref_linguistica = pref_linguistica;
        this.tipo = tipo;
        this.idFreguesia = idFreguesia;
        this.idConcelho = idConcelho;
        this.nome = nome;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getLingua() {
        return pref_linguistica;
    }

    public void setLingua(String lingua) {
        this.pref_linguistica = lingua;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdFreguesia() {
        return idFreguesia;
    }

    public void setIdFreguesia(int idDistrito) {
        this.idFreguesia = idDistrito;
    }

    public int getIdConcelho() {
        return idConcelho;
    }

    public void setIdConcelho(int idConcelho) {
        this.idConcelho = idConcelho;
    }
    
    public String getNome() {
    	return this.nome;
    }
    
    public void setNome(String nome) {
    	this.nome = nome;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "nif='" + nif + '\'' +
                ", contacto='" + contacto + '\'' +
                ", nacionalidade='" + pref_linguistica + '\'' +
                ", tipo='" + tipo + '\'' +
                ", idDistrito=" + idFreguesia +
                ", idConcelho=" + idConcelho +
                ", Nome=" + nome +
                '}';
    }
}
