package model;

import java.util.Objects;

public class Admin {

    private int idAdmin;
    private String nome;
    private String email;
    private String senha;
    private String status;
    private String nivelAcesso;

    // Construtor vazio
    public Admin() {
    }

    // Construtor completo
    public Admin(int idAdmin, String nome, String email, String senha,
                 String status, String nivelAcesso) {

        this.idAdmin = idAdmin;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.status = status;
        this.nivelAcesso = nivelAcesso;
    }

    /*
     * Construtor sem o ID.
     * Pode ser utilizado ao cadastrar um novo administrador,
     * pois o id_admin é AUTO_INCREMENT no banco.
     */
    public Admin(String nome, String email, String senha,
                 String status, String nivelAcesso) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.status = status;
        this.nivelAcesso = nivelAcesso;
    }

    // Getters e Setters

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    // equals e hashCode baseados no ID

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Admin admin = (Admin) o;

        return idAdmin == admin.idAdmin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAdmin);
    }

    // toString sem mostrar a senha

    @Override
    public String toString() {
        return "Admin{" +
                "idAdmin=" + idAdmin +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", nivelAcesso='" + nivelAcesso + '\'' +
                '}';
    }
}