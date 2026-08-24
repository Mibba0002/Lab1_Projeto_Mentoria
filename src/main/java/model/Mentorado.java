package model;

import java.util.Objects;

public class Mentorado {

    private String cpfMentorado;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String formacao;
    private String objetivosProfissionais;
    private String principaisDuvidas;
    private String expectativas;
    private String cidade;
    private String estado;
    private String status;

    //Construtores
    public Mentorado() {
    }

    public Mentorado(String cpfMentorado, String nome, String email, String senha,
                     String formacao, String objetivosProfissionais,
                     String principaisDuvidas, String expectativas) {

        this.cpfMentorado = cpfMentorado;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.formacao = formacao;
        this.objetivosProfissionais = objetivosProfissionais;
        this.principaisDuvidas = principaisDuvidas;
        this.expectativas = expectativas;
    }

    public Mentorado(String cpfMentorado, String nome, String email, String senha,
                     String formacao, String objetivosProfissionais,
                     String principaisDuvidas, String expectativas,
                     String cidade, String estado, String status) {

        this(cpfMentorado, nome, email, senha, formacao,
                objetivosProfissionais, principaisDuvidas, expectativas);
        this.cidade = cidade;
        this.estado = estado;
        this.status = status;
    }
    
    //Getters e Setters

    public String getCpfMentorado() {
        return cpfMentorado;
    }

    public void setCpfMentorado(String cpfMentorado) {
        this.cpfMentorado = cpfMentorado;
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

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getObjetivosProfissionais() {
        return objetivosProfissionais;
    }

    public void setObjetivosProfissionais(String objetivosProfissionais) {
        this.objetivosProfissionais = objetivosProfissionais;
    }

    public String getPrincipaisDuvidas() {
        return principaisDuvidas;
    }

    public void setPrincipaisDuvidas(String principaisDuvidas) {
        this.principaisDuvidas = principaisDuvidas;
    }

    public String getExpectativas() {
        return expectativas;
    }

    public void setExpectativas(String expectativas) {
        this.expectativas = expectativas;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   //Overrides, equals hashcode baseado no cpf e formato de exibição do objeto no terminal
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mentorado that = (Mentorado) o;
        return Objects.equals(cpfMentorado, that.cpfMentorado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpfMentorado);
    }

    @Override
    public String toString() {
        return "Mentorado{" +
                "cpfMentorado='" + cpfMentorado + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", formacao='" + formacao + '\'' +
                ", objetivosProfissionais='" + objetivosProfissionais + '\'' +
                ", principaisDuvidas='" + principaisDuvidas + '\'' +
                ", expectativas='" + expectativas + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
