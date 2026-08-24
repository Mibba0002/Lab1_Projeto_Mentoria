package model;

import java.time.LocalDateTime;

public class DiarioBordo {
    private int idDiario;
    private int idMentoria;
    private String tipoAutor;
    private String cpfAutor;
    private String conteudo;
    private LocalDateTime dataRegistro;

    public int getIdDiario() { return idDiario; }
    public void setIdDiario(int idDiario) { this.idDiario = idDiario; }
    public int getIdMentoria() { return idMentoria; }
    public void setIdMentoria(int idMentoria) { this.idMentoria = idMentoria; }
    public String getTipoAutor() { return tipoAutor; }
    public void setTipoAutor(String tipoAutor) { this.tipoAutor = tipoAutor; }
    public String getCpfAutor() { return cpfAutor; }
    public void setCpfAutor(String cpfAutor) { this.cpfAutor = cpfAutor; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
