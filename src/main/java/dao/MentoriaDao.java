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
                   + "(cpf_mentorado, id_especializacao, status, "
                   + "data_inicio, data_fim, objetivos_definidos, "
                   + "depoimentos, id_area_atuacao, cpf_mentor) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentoria.getCpfMentorado());
            stmt.setInt(2, mentoria.getIdEspecializacao());
            stmt.setString(3, mentoria.getStatus());

            if (mentoria.getDataInicio() != null) {
                stmt.setDate(4, Date.valueOf(mentoria.getDataInicio()));
            } else {
                stmt.setDate(4, null);
            }

            if (mentoria.getDataFim() != null) {
                stmt.setDate(5, Date.valueOf(mentoria.getDataFim()));
            } else {
                stmt.setDate(5, null);
            }

            stmt.setString(6, mentoria.getObjetivosDefinidos());
            stmt.setString(7, mentoria.getDepoimentos());
            stmt.setInt(8, mentoria.getIdAreaAtuacao());
            stmt.setString(9, mentoria.getCpfMentor());

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
                   + "cpf_mentorado = ?, "
                   + "id_especializacao = ?, "
                   + "status = ?, "
                   + "data_inicio = ?, "
                   + "data_fim = ?, "
                   + "objetivos_definidos = ?, "
                   + "depoimentos = ?, "
                   + "id_area_atuacao = ?, "
                   + "cpf_mentor = ? "
                   + "WHERE id_mentoria = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentoria.getCpfMentorado());
            stmt.setInt(2, mentoria.getIdEspecializacao());
            stmt.setString(3, mentoria.getStatus());

            if (mentoria.getDataInicio() != null) {
                stmt.setDate(4, Date.valueOf(mentoria.getDataInicio()));
            } else {
                stmt.setDate(4, null);
            }

            if (mentoria.getDataFim() != null) {
                stmt.setDate(5, Date.valueOf(mentoria.getDataFim()));
            } else {
                stmt.setDate(5, null);
            }

            stmt.setString(6, mentoria.getObjetivosDefinidos());
            stmt.setString(7, mentoria.getDepoimentos());
            stmt.setInt(8, mentoria.getIdAreaAtuacao());
            stmt.setString(9, mentoria.getCpfMentor());
            stmt.setInt(10, mentoria.getIdMentoria());

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

        mentoria.setIdAreaAtuacao(
                rs.getInt("id_area_atuacao")
        );

        mentoria.setCpfMentor(
                rs.getString("cpf_mentor")
        );

        return mentoria;
    }
}