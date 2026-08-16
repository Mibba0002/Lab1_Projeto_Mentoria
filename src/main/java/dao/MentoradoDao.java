package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Mentorado;
import util.Conexao;

public class MentoradoDao {

    private static final Logger LOGGER = Logger.getLogger(MentoradoDao.class.getName());
    

    // 											Cadastrar
    public boolean cadastrar(Mentorado mentorado) {

        String sql = "INSERT INTO Mentorado (cpf_mentorado, nome, email, senha, formacao, objetivos_profissionais, principais_duvidas, expectativas) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentorado.getCpfMentorado());
            stmt.setString(2, mentorado.getNome());
            stmt.setString(3, mentorado.getEmail());
            stmt.setString(4, mentorado.getSenha());
            stmt.setString(5, mentorado.getFormacao());
            stmt.setString(6, mentorado.getObjetivosProfissionais());
            stmt.setString(7, mentorado.getPrincipaisDuvidas());
            stmt.setString(8, mentorado.getExpectativas());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar mentorado: " + mentorado.getCpfMentorado(), e);
            return false;
        }
    }
    

    //										 Listar
    public ArrayList<Mentorado> listar() {

        ArrayList<Mentorado> lista = new ArrayList<>();

        String sql = "SELECT cpf_mentorado, nome, email, senha, formacao, objetivos_profissionais, principais_duvidas, expectativas FROM Mentorado";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Mentorado mentorado = new Mentorado();

                mentorado.setCpfMentorado(rs.getString("cpf_mentorado"));
                mentorado.setNome(rs.getString("nome"));
                mentorado.setEmail(rs.getString("email"));
                mentorado.setSenha(rs.getString("senha"));
                mentorado.setFormacao(rs.getString("formacao"));
                mentorado.setObjetivosProfissionais(rs.getString("objetivos_profissionais"));
                mentorado.setPrincipaisDuvidas(rs.getString("principais_duvidas"));
                mentorado.setExpectativas(rs.getString("expectativas"));

                lista.add(mentorado);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar mentorados", e);
        }

        return lista;
    }
    

    // 											Excluir
    public boolean excluir(String cpfMentorado) {

        String sql = "DELETE FROM Mentorado WHERE cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentorado);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir mentorado: " + cpfMentorado, e);
            return false;
        }
    }

    // 											Atualizar
    public boolean atualizar(Mentorado mentorado) {

        String sql = "UPDATE Mentorado SET nome=?, email=?, senha=?, formacao=?, objetivos_profissionais=?, principais_duvidas=?, expectativas=? WHERE cpf_mentorado=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentorado.getNome());
            stmt.setString(2, mentorado.getEmail());
            stmt.setString(3, mentorado.getSenha());
            stmt.setString(4, mentorado.getFormacao());
            stmt.setString(5, mentorado.getObjetivosProfissionais());
            stmt.setString(6, mentorado.getPrincipaisDuvidas());
            stmt.setString(7, mentorado.getExpectativas());
            stmt.setString(8, mentorado.getCpfMentorado());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar mentorado: " + mentorado.getCpfMentorado(), e);
            return false;
        }
    }
}