package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.InteresseEm;
import util.Conexao;

public class InteresseEmDao {

    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrar(InteresseEm interesse) {

        String sql = "INSERT INTO Interesse "
                   + "(id_area_atuacao, cpf_mentorado, nivel_experiencia) "
                   + "VALUES (?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(
                1,
                interesse.getIdAreaAtuacao()
            );

            stmt.setString(
                2,
                interesse.getCpfMentorado()
            );

            stmt.setString(
                3,
                interesse.getNivelExperiencia()
            );

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar interesse: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // BUSCAR POR MENTORADO
    // ==========================================

    public ArrayList<InteresseEm> buscarPorMentorado(String cpfMentorado) {

        ArrayList<InteresseEm> lista = new ArrayList<>();

        String sql = "SELECT * FROM Interesse "
                   + "WHERE cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentorado);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                InteresseEm interesse = criarInteresse(rs);

                lista.add(interesse);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar interesses: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // LISTAR TODOS
    // ==========================================

    public ArrayList<InteresseEm> listar() {

        ArrayList<InteresseEm> lista = new ArrayList<>();

        String sql = "SELECT * FROM Interesse";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                InteresseEm interesse = criarInteresse(rs);

                lista.add(interesse);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar interesses: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizar(InteresseEm interesse) {

        String sql = "UPDATE Interesse "
                   + "SET nivel_experiencia = ? "
                   + "WHERE id_area_atuacao = ? "
                   + "AND cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(
                1,
                interesse.getNivelExperiencia()
            );

            stmt.setInt(
                2,
                interesse.getIdAreaAtuacao()
            );

            stmt.setString(
                3,
                interesse.getCpfMentorado()
            );

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar interesse: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluir(int idAreaAtuacao, String cpfMentorado) {

        String sql = "DELETE FROM Interesse "
                   + "WHERE id_area_atuacao = ? "
                   + "AND cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAreaAtuacao);
            stmt.setString(2, cpfMentorado);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir interesse: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // VERIFICAR SE JÁ EXISTE
    // ==========================================

    public boolean existe(int idAreaAtuacao, String cpfMentorado) {

        String sql = "SELECT * FROM Interesse "
                   + "WHERE id_area_atuacao = ? "
                   + "AND cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAreaAtuacao);
            stmt.setString(2, cpfMentorado);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erro ao verificar interesse: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // CRIAR OBJETO
    // ==========================================

    private InteresseEm criarInteresse(ResultSet rs)
            throws SQLException {

        InteresseEm interesse = new InteresseEm();

        interesse.setIdAreaAtuacao(
            rs.getInt("id_area_atuacao")
        );

        interesse.setCpfMentorado(
            rs.getString("cpf_mentorado")
        );

        interesse.setNivelExperiencia(
            rs.getString("nivel_experiencia")
        );

        return interesse;
    }
}