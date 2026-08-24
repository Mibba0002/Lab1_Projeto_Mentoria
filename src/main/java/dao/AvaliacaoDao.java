package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Avaliacao;
import util.Conexao;

public class AvaliacaoDao {

    private static volatile boolean estruturaVerificada;

    public boolean cadastrarParaMentoriaFinalizada(int idMentoria,
                                                    String cpfMentorado,
                                                    int nota,
                                                    String comentario) {
        if (!garantirEstrutura()) return false;
        String sql = "INSERT INTO Avaliacao (id_mentoria, nota, comentario) "
                + "SELECT m.id_mentoria, ?, ? FROM Mentoria m "
                + "WHERE m.id_mentoria = ? AND m.cpf_mentorado = ? "
                + "AND LOWER(TRIM(m.status)) = 'finalizada'";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nota);
            stmt.setString(2, comentario);
            stmt.setInt(3, idMentoria);
            stmt.setString(4, cpfMentorado);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar avaliação: " + e.getMessage());
            return false;
        }
    }

    public boolean existePorMentoria(int idMentoria) {
        if (!garantirEstrutura()) return false;
        String sql = "SELECT 1 FROM Avaliacao WHERE id_mentoria = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMentoria);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar avaliação: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<Avaliacao> listarPorMentor(String cpfMentor) {
        return listar("WHERE m.cpf_mentor = ?", cpfMentor);
    }

    public ArrayList<Avaliacao> listarPorMentorado(String cpfMentorado) {
        return listar("WHERE m.cpf_mentorado = ?", cpfMentorado);
    }

    public double mediaPorMentor(String cpfMentor) {
        if (!garantirEstrutura()) return 0;
        String sql = "SELECT COALESCE(AVG(a.nota), 0) media FROM Avaliacao a "
                + "INNER JOIN Mentoria m ON m.id_mentoria = a.id_mentoria "
                + "WHERE m.cpf_mentor = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpfMentor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getDouble("media") : 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao calcular média: " + e.getMessage());
            return 0;
        }
    }

    public int contarPorMentor(String cpfMentor) {
        if (!garantirEstrutura()) return 0;
        String sql = "SELECT COUNT(*) total FROM Avaliacao a "
                + "INNER JOIN Mentoria m ON m.id_mentoria = a.id_mentoria "
                + "WHERE m.cpf_mentor = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpfMentor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao contar avaliações: " + e.getMessage());
            return 0;
        }
    }

    private ArrayList<Avaliacao> listar(String filtro, String cpf) {
        ArrayList<Avaliacao> lista = new ArrayList<>();
        if (!garantirEstrutura()) return lista;
        String sql = "SELECT a.*, m.cpf_mentor, m.cpf_mentorado, "
                + "mr.nome nome_mentor, md.nome nome_mentorado "
                + "FROM Avaliacao a "
                + "INNER JOIN Mentoria m ON m.id_mentoria = a.id_mentoria "
                + "INNER JOIN Mentor mr ON mr.cpf_mentor = m.cpf_mentor "
                + "INNER JOIN Mentorado md ON md.cpf_mentorado = m.cpf_mentorado "
                + filtro + " ORDER BY a.data_avaliacao DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(criar(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar avaliações: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Bancos criados antes da funcionalidade de feedback ainda não possuem
     * esta tabela. Prepará-la aqui torna a atualização segura também para quem
     * já estava testando o sistema, sem apagar qualquer dado existente.
     */
    private boolean garantirEstrutura() {
        if (estruturaVerificada) return true;
        synchronized (AvaliacaoDao.class) {
            if (estruturaVerificada) return true;
            String sql = "CREATE TABLE IF NOT EXISTS Avaliacao ("
                    + "id_avaliacao INT NOT NULL AUTO_INCREMENT, "
                    + "id_mentoria INT NOT NULL, "
                    + "nota TINYINT NOT NULL, "
                    + "comentario TEXT NULL, "
                    + "data_avaliacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + "PRIMARY KEY (id_avaliacao), "
                    + "UNIQUE KEY uq_avaliacao_mentoria (id_mentoria), "
                    + "CONSTRAINT fk_avaliacao_mentoria "
                    + "FOREIGN KEY (id_mentoria) REFERENCES Mentoria (id_mentoria) "
                    + "ON DELETE CASCADE ON UPDATE CASCADE"
                    + ") ENGINE=InnoDB";
            try (Connection conn = Conexao.conectar();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
                estruturaVerificada = true;
                return true;
            } catch (SQLException e) {
                System.out.println("Erro ao preparar tabela de avaliações: "
                        + e.getMessage());
                return false;
            }
        }
    }

    private Avaliacao criar(ResultSet rs) throws SQLException {
        Avaliacao item = new Avaliacao();
        item.setIdAvaliacao(rs.getInt("id_avaliacao"));
        item.setIdMentoria(rs.getInt("id_mentoria"));
        item.setNota(rs.getInt("nota"));
        item.setComentario(rs.getString("comentario"));
        item.setDataAvaliacao(rs.getTimestamp("data_avaliacao").toLocalDateTime());
        item.setCpfMentor(rs.getString("cpf_mentor"));
        item.setCpfMentorado(rs.getString("cpf_mentorado"));
        item.setNomeMentor(rs.getString("nome_mentor"));
        item.setNomeMentorado(rs.getString("nome_mentorado"));
        return item;
    }
}
