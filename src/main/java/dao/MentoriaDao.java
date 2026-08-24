package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Mentoria;
import util.Conexao;

public class MentoriaDao {

    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrar(Mentoria mentoria) {

        String sql = "INSERT INTO Mentoria "
                   + "(cpf_mentor, cpf_mentorado, id_especializacao, status, "
                   + "data_inicio, data_fim, objetivos_definidos, "
                   + "depoimentos) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentoria.getCpfMentor());
            stmt.setString(2, mentoria.getCpfMentorado());
            stmt.setInt(3, mentoria.getIdEspecializacao());
            stmt.setString(4, mentoria.getStatus());

            if (mentoria.getDataInicio() != null) {
                stmt.setDate(5, Date.valueOf(mentoria.getDataInicio()));
            } else {
                stmt.setDate(5, null);
            }

            if (mentoria.getDataFim() != null) {
                stmt.setDate(6, Date.valueOf(mentoria.getDataFim()));
            } else {
                stmt.setDate(6, null);
            }

            stmt.setString(7, mentoria.getObjetivosDefinidos());
            stmt.setString(8, mentoria.getDepoimentos());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar mentoria: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Mentoria buscarPorId(int idMentoria) {

        String sql = "SELECT * FROM Mentoria "
                   + "WHERE id_mentoria = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMentoria);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return criarMentoria(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar mentoria: "
                    + e.getMessage());
        }

        return null;
    }


    // ==========================================
    // BUSCAR POR MENTOR
    // ==========================================

    public ArrayList<Mentoria> buscarPorMentor(String cpfMentor) {

        ArrayList<Mentoria> lista = new ArrayList<>();

        String sql = "SELECT * FROM Mentoria "
                   + "WHERE cpf_mentor = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentor);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarMentoria(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar mentorias do mentor: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // BUSCAR POR MENTORADO
    // ==========================================

    public ArrayList<Mentoria> buscarPorMentorado(String cpfMentorado) {

        ArrayList<Mentoria> lista = new ArrayList<>();

        String sql = "SELECT * FROM Mentoria "
                   + "WHERE cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentorado);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarMentoria(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar mentorias do mentorado: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // LISTAR TODAS
    // ==========================================

    public ArrayList<Mentoria> listar() {

        ArrayList<Mentoria> lista = new ArrayList<>();

        String sql = "SELECT * FROM Mentoria";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarMentoria(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar mentorias: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizar(Mentoria mentoria) {

        String sql = "UPDATE Mentoria SET "
                   + "cpf_mentor = ?, "
                   + "cpf_mentorado = ?, "
                   + "id_especializacao = ?, "
                   + "status = ?, "
                   + "data_inicio = ?, "
                   + "data_fim = ?, "
                   + "objetivos_definidos = ?, "
                   + "depoimentos = ? "
                   + "WHERE id_mentoria = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentoria.getCpfMentor());
            stmt.setString(2, mentoria.getCpfMentorado());
            stmt.setInt(3, mentoria.getIdEspecializacao());
            stmt.setString(4, mentoria.getStatus());

            if (mentoria.getDataInicio() != null) {
                stmt.setDate(5, Date.valueOf(mentoria.getDataInicio()));
            } else {
                stmt.setDate(5, null);
            }

            if (mentoria.getDataFim() != null) {
                stmt.setDate(6, Date.valueOf(mentoria.getDataFim()));
            } else {
                stmt.setDate(6, null);
            }

            stmt.setString(7, mentoria.getObjetivosDefinidos());
            stmt.setString(8, mentoria.getDepoimentos());
            stmt.setInt(9, mentoria.getIdMentoria());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar mentoria: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluir(int idMentoria) {

        String sql = "DELETE FROM Mentoria "
                   + "WHERE id_mentoria = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMentoria);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir mentoria: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean existePendente(String cpfMentor, String cpfMentorado) {
        String sql = "SELECT 1 FROM Mentoria WHERE cpf_mentor = ? "
                + "AND cpf_mentorado = ? AND status = 'Pendente'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentor);
            stmt.setString(2, cpfMentorado);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar solicitação: "
                    + e.getMessage());
            return true;
        }
    }

    public boolean alterarStatusSePendente(int idMentoria,
                                           String cpfMentor,
                                           String novoStatus) {
        String sql = "UPDATE Mentoria SET status = ?, "
                + "data_inicio = CASE WHEN ? = 'Ativa' "
                + "THEN CURRENT_DATE ELSE data_inicio END "
                + "WHERE id_mentoria = ? AND cpf_mentor = ? "
                + "AND status = 'Pendente'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setString(2, novoStatus);
            stmt.setInt(3, idMentoria);
            stmt.setString(4, cpfMentor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao responder solicitação: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean finalizarSeAtiva(int idMentoria, String cpfMentor) {
        String sql = "UPDATE Mentoria SET status = 'Finalizada', "
                + "data_fim = CURRENT_DATE "
                + "WHERE id_mentoria = ? AND cpf_mentor = ? "
                + "AND status = 'Ativa'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMentoria);
            stmt.setString(2, cpfMentor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao finalizar mentoria: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // CRIAR OBJETO MENTORIA
    // ==========================================

    private Mentoria criarMentoria(ResultSet rs)
            throws SQLException {

        Mentoria mentoria = new Mentoria();

        mentoria.setIdMentoria(
                rs.getInt("id_mentoria")
        );

        mentoria.setCpfMentorado(
                rs.getString("cpf_mentorado")
        );

        mentoria.setIdEspecializacao(
                rs.getInt("id_especializacao")
        );

        mentoria.setStatus(
                rs.getString("status")
        );

        Date dataInicio = rs.getDate("data_inicio");

        if (dataInicio != null) {
            mentoria.setDataInicio(
                    dataInicio.toLocalDate()
            );
        }

        Date dataFim = rs.getDate("data_fim");

        if (dataFim != null) {
            mentoria.setDataFim(
                    dataFim.toLocalDate()
            );
        }

        mentoria.setObjetivosDefinidos(
                rs.getString("objetivos_definidos")
        );

        mentoria.setDepoimentos(
                rs.getString("depoimentos")
        );

        mentoria.setCpfMentor(
                rs.getString("cpf_mentor")
        );

        return mentoria;
    }
}
