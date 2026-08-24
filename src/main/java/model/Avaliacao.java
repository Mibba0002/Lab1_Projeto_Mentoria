package model;

import java.time.LocalDateTime;

public class Avaliacao {
    private int idAvaliacao;
    private int idMentoria;
    private int nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
    private String cpfMentor;
    private String cpfMentorado;
    private String nomeMentor;
    private String nomeMentorado;

    public int getIdAvaliacao() { return idAvaliacao; }
    public void setIdAvaliacao(int idAvaliacao) { this.idAvaliacao = idAvaliacao; }
    public int getIdMentoria() { return idMentoria; }
    public void setIdMentoria(int idMentoria) { this.idMentoria = idMentoria; }
    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDateTime dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
    public String getCpfMentor() { return cpfMentor; }
    public void setCpfMentor(String cpfMentor) { this.cpfMentor = cpfMentor; }
    public String getCpfMentorado() { return cpfMentorado; }
    public void setCpfMentorado(String cpfMentorado) { this.cpfMentorado = cpfMentorado; }
    public String getNomeMentor() { return nomeMentor; }
    public void setNomeMentor(String nomeMentor) { this.nomeMentor = nomeMentor; }
    public String getNomeMentorado() { return nomeMentorado; }
    public void setNomeMentorado(String nomeMentorado) { this.nomeMentorado = nomeMentorado; }
}
