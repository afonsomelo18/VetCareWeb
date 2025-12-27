package model;

public class Progenitor {
	private Paciente paciente;
    private String tipo; // "pai" ou "mae"
    
    /**
     * @param paciente progenitor
     * @param tipo tipo de filiação ("pai" ou "mae")
     */
    public Progenitor(Paciente paciente, String tipo) {
        this.paciente = paciente;
        this.tipo = tipo;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public String getTipo() {
        return tipo;
    }

}
