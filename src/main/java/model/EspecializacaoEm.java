package model;

public class EspecializacaoEm {

    private int idEspecializacao;
    private int idAreaAtuacao;
    private String cpfMentor;
    private String especializacao;
    private int tempoExperiencia;

    public EspecializacaoEm() {
    }

    public int getIdEspecializacao() {
        return idEspecializacao;
    }

    public void setIdEspecializacao(int idEspecializacao) {
        this.idEspecializacao = idEspecializacao;
    }

    public int getIdAreaAtuacao() {
        return idAreaAtuacao;
    }

    public void setIdAreaAtuacao(int idAreaAtuacao) {
        this.idAreaAtuacao = idAreaAtuacao;
    }

    public String getCpfMentor() {
        return cpfMentor;
    }

    public void setCpfMentor(String cpfMentor) {
        this.cpfMentor = cpfMentor;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public int getTempoExperiencia() {
        return tempoExperiencia;
    }

    public void setTempoExperiencia(int tempoExperiencia) {
        this.tempoExperiencia = tempoExperiencia;
    }
}
