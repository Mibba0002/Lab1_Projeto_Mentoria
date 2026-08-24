package model;

public class InteresseEm {

    private int idAreaAtuacao;
    private String cpfMentorado;
    private String areaInteresse;
    private String nivelExperiencia;

    public InteresseEm() {
    }

    public int getIdAreaAtuacao() {
        return idAreaAtuacao;
    }

    public void setIdAreaAtuacao(int idAreaAtuacao) {
        this.idAreaAtuacao = idAreaAtuacao;
    }

    public String getCpfMentorado() {
        return cpfMentorado;
    }

    public void setCpfMentorado(String cpfMentorado) {
        this.cpfMentorado = cpfMentorado;
    }

    public String getAreaInteresse() {
        return areaInteresse;
    }

    public void setAreaInteresse(String areaInteresse) {
        this.areaInteresse = areaInteresse;
    }

    public String getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(String nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }
}
