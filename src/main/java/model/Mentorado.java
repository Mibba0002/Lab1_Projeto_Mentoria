package model;

import java.util.Objects;

public class Mentorado {

    private String cpfMentorado;
    private String nome;
    private String email;
    private String senha;
    private String formacao;
    private String objetivosProfissionais;
    private String principaisDuvidas;
    private String expectativas;

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
                ", formacao='" + formacao + '\'' +
                ", objetivosProfissionais='" + objetivosProfissionais + '\'' +
                ", principaisDuvidas='" + principaisDuvidas + '\'' +
                ", expectativas='" + expectativas + '\'' +
                '}';
    }
}