package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.EspecializacaoEm;
import util.Conexao;

public class EspecializacaoEmDao {

    public boolean cadastrar(EspecializacaoEm especializacao) {
        String sql = "INSERT INTO Especializacao_em "
                + "(id_area_atuacao, cpf_mentor, especializacao, "
                + "tempo_experiencia) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, especializacao.getIdAreaAtuacao());
            stmt.setString(2, especializacao.getCpfMentor());
            stmt.setString(3, especializacao.getEspecializacao());
            stmt.setInt(4, especializacao.getTempoExperiencia());

            if (stmt.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    especializacao.setIdEspecializacao(chaves.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar especialização: "
                    + e.getMessage());
            return false;
        }
    }

    public EspecializacaoEm buscarPorId(int idEspecializacao) {
        String sql = "SELECT * FROM Especializacao_em "
                + "WHERE id_especializacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEspecializacao);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarEspecializacao(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar especialização: "
                    + e.getMessage());
        }

        return null;
    }

    public ArrayList<EspecializacaoEm> buscarPorMentor(String cpfMentor) {
        ArrayList<EspecializacaoEm> lista = new ArrayList<>();
        String sql = "SELECT * FROM Especializacao_em "
                + "WHERE cpf_mentor = ? ORDER BY especializacao";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(criarEspecializacao(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar especializações: "
                    + e.getMessage());
        }

        return lista;
    }

    public ArrayList<EspecializacaoEm> listar() {
        ArrayList<EspecializacaoEm> lista = new ArrayList<>();
        String sql = "SELECT * FROM Especializacao_em "
                + "ORDER BY especializacao";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarEspecializacao(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar especializações: "
                    + e.getMessage());
        }

        return lista;
    }

    public boolean atualizar(EspecializacaoEm especializacao) {
        String sql = "UPDATE Especializacao_em SET id_area_atuacao = ?, "
                + "cpf_mentor = ?, especializacao = ?, "
                + "tempo_experiencia = ? WHERE id_especializacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, especializacao.getIdAreaAtuacao());
            stmt.setString(2, especializacao.getCpfMentor());
            stmt.setString(3, especializacao.getEspecializacao());
            stmt.setInt(4, especializacao.getTempoExperiencia());
            stmt.setInt(5, especializacao.getIdEspecializacao());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar especialização: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean excluir(int idEspecializacao) {
        String sql = "DELETE FROM Especializacao_em "
                + "WHERE id_especializacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEspecializacao);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir especialização: "
                    + e.getMessage());
            return false;
        }
    }

    private EspecializacaoEm criarEspecializacao(ResultSet rs)
            throws SQLException {

        EspecializacaoEm especializacao = new EspecializacaoEm();
        especializacao.setIdEspecializacao(
                rs.getInt("id_especializacao"));
        especializacao.setIdAreaAtuacao(rs.getInt("id_area_atuacao"));
        especializacao.setCpfMentor(rs.getString("cpf_mentor"));
        especializacao.setEspecializacao(rs.getString("especializacao"));
        especializacao.setTempoExperiencia(
                rs.getInt("tempo_experiencia"));
        return especializacao;
    }
}
