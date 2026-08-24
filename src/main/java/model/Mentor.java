package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Mentor {
    private String cpfMentor;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String miniBiografia;
    private String disponibilidade;
    private String formatoMentoria;
    private String linkPortifolio;
    private String redesProfissionais;
    private String status;
    private String cidade;
    private String estado;
    private boolean verificado;
    private LocalDateTime dataVerificacao;
    private Integer idAdminVerificador;

    // Construtor vazio
    public Mentor() {
    }

    // Construtor completo
    public Mentor(String cpfMentor, String nome, String email, String senha,
                  String miniBiografia, String disponibilidade,
                  String formatoMentoria, String linkPortifolio,
                  String redesProfissionais, String status) {
        this.cpfMentor = cpfMentor;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.miniBiografia = miniBiografia;
        this.disponibilidade = disponibilidade;
        this.formatoMentoria = formatoMentoria;
        this.linkPortifolio = linkPortifolio;
        this.redesProfissionais = redesProfissionais;
        this.status = status;
    }

    public Mentor(String cpfMentor, String nome, String email, String senha,
                  String miniBiografia, String disponibilidade,
                  String formatoMentoria, String linkPortifolio,
                  String redesProfissionais, String status, String cidade,
                  String estado, boolean verificado,
                  LocalDateTime dataVerificacao,
                  Integer idAdminVerificador) {

        this(cpfMentor, nome, email, senha, miniBiografia,
                disponibilidade, formatoMentoria, linkPortifolio,
                redesProfissionais, status);
        this.cidade = cidade;
        this.estado = estado;
        this.verificado = verificado;
        this.dataVerificacao = dataVerificacao;
        this.idAdminVerificador = idAdminVerificador;
    }

    public String getCpfMentor() {
        return cpfMentor;
    }

    public void setCpfMentor(String cpfMentor) {
        this.cpfMentor = cpfMentor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMiniBiografia() {
        return miniBiografia;
    }

    public void setMiniBiografia(String miniBiografia) {
        this.miniBiografia = miniBiografia;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getFormatoMentoria() {
        return formatoMentoria;
    }

    public void setFormatoMentoria(String formatoMentoria) {
        this.formatoMentoria = formatoMentoria;
    }

    public String getLinkPortifolio() {
        return linkPortifolio;
    }

    public void setLinkPortifolio(String linkPortifolio) {
        this.linkPortifolio = linkPortifolio;
    }

    public String getRedesProfissionais() {
        return redesProfissionais;
    }

    public void setRedesProfissionais(String redesProfissionais) {
        this.redesProfissionais = redesProfissionais;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public LocalDateTime getDataVerificacao() {
        return dataVerificacao;
    }

    public void setDataVerificacao(LocalDateTime dataVerificacao) {
        this.dataVerificacao = dataVerificacao;
    }

    public Integer getIdAdminVerificador() {
        return idAdminVerificador;
    }

    public void setIdAdminVerificador(Integer idAdminVerificador) {
        this.idAdminVerificador = idAdminVerificador;
    }

    // equals e hashCode baseados no CPF 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mentor mentor = (Mentor) o;
        return Objects.equals(cpfMentor, mentor.cpfMentor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpfMentor);
    }

    // toString sem expor a senha 
    @Override
    public String toString() {
        return "Mentor{" +
                "cpfMentor='" + cpfMentor + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", miniBiografia='" + miniBiografia + '\'' +
                ", disponibilidade='" + disponibilidade + '\'' +
                ", formatoMentoria='" + formatoMentoria + '\'' +
                ", linkPortifolio='" + linkPortifolio + '\'' +
                ", redesProfissionais='" + redesProfissionais + '\'' +
                ", status='" + status + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", verificado=" + verificado +
                ", dataVerificacao=" + dataVerificacao +
                ", idAdminVerificador=" + idAdminVerificador +
                '}';
    }
}
