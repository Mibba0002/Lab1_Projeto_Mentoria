package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;

import model.Encontro;
import util.Conexao;

public class EncontroDao {

    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrar(Encontro encontro) {

        String sql = "INSERT INTO Encontro "
                   + "(id_mentoria, data, horario, tipo_encontro, "
                   + "descricao, link_reuniao, status, local_encontro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, encontro.getIdMentoria());
            stmt.setDate(2, Date.valueOf(encontro.getData()));
            stmt.setTime(3, Time.valueOf(encontro.getHorario()));
            stmt.setString(4, encontro.getTipoEncontro());
            stmt.setString(5, encontro.getDescricao());
            stmt.setString(6, encontro.getLinkReuniao());
            stmt.setString(7, encontro.getStatus());
            stmt.setString(8, encontro.getLocalEncontro());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar encontro: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Encontro buscarPorId(int idEncontro) {

        String sql = "SELECT * FROM Encontro "
                   + "WHERE id_encontro = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEncontro);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return criarEncontro(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar encontro: "
                    + e.getMessage());
        }

        return null;
    }


    // ==========================================
    // BUSCAR POR MENTORIA
    // ==========================================

    public ArrayList<Encontro> buscarPorMentoria(int idMentoria) {

        ArrayList<Encontro> lista = new ArrayList<>();

        String sql = "SELECT * FROM Encontro "
                   + "WHERE id_mentoria = ? ORDER BY data, horario";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idMentoria);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarEncontro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar encontros da mentoria: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // LISTAR TODOS
    // ==========================================

    public ArrayList<Encontro> listar() {

        ArrayList<Encontro> lista = new ArrayList<>();

        String sql = "SELECT * FROM Encontro";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarEncontro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar encontros: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizar(Encontro encontro) {

        String sql = "UPDATE Encontro SET "
                   + "id_mentoria = ?, "
                   + "data = ?, "
                   + "horario = ?, "
                   + "tipo_encontro = ?, "
                   + "descricao = ?, "
                   + "link_reuniao = ?, "
                   + "status = ?, "
                   + "local_encontro = ? "
                   + "WHERE id_encontro = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, encontro.getIdMentoria());
            stmt.setDate(2, Date.valueOf(encontro.getData()));
            stmt.setTime(3, Time.valueOf(encontro.getHorario()));
            stmt.setString(4, encontro.getTipoEncontro());
            stmt.setString(5, encontro.getDescricao());
            stmt.setString(6, encontro.getLinkReuniao());
            stmt.setString(7, encontro.getStatus());
            stmt.setString(8, encontro.getLocalEncontro());
            stmt.setInt(9, encontro.getIdEncontro());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar encontro: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // REGISTRAR RESULTADO DO ENCONTRO
    // ==========================================

    public boolean registrarResultado(int idEncontro,
                                      String cpfMentor,
                                      String status,
                                      String motivo) {
        String sql = "UPDATE Encontro e "
                + "INNER JOIN Mentoria m ON m.id_mentoria = e.id_mentoria "
                + "SET e.status = ?, e.motivo_nao_realizacao = ? "
                + "WHERE e.id_encontro = ? AND m.cpf_mentor = ? "
                + "AND LOWER(TRIM(e.status)) IN ('agendado', 'pendente')";

        try (Connection conn = Conexao.conectar()) {
            garantirColunaMotivo(conn);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, status);
                if (motivo == null || motivo.isBlank()) {
                    stmt.setNull(2, Types.VARCHAR);
                } else {
                    stmt.setString(2, motivo);
                }
                stmt.setInt(3, idEncontro);
                stmt.setString(4, cpfMentor);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao registrar resultado do encontro: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluir(int idEncontro) {

        String sql = "DELETE FROM Encontro "
                   + "WHERE id_encontro = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEncontro);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir encontro: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // CRIAR OBJETO ENCONTRO
    // ==========================================

    private Encontro criarEncontro(ResultSet rs) throws SQLException {

        Encontro encontro = new Encontro();

        encontro.setIdEncontro(
            rs.getInt("id_encontro")
        );

        encontro.setIdMentoria(
            rs.getInt("id_mentoria")
        );

        Date data = rs.getDate("data");

        if (data != null) {
            encontro.setData(data.toLocalDate());
        }

        Time horario = rs.getTime("horario");

        if (horario != null) {
            encontro.setHorario(horario.toLocalTime());
        }

        encontro.setTipoEncontro(
            rs.getString("tipo_encontro")
        );

        encontro.setDescricao(
            rs.getString("descricao")
        );

        encontro.setLinkReuniao(
            rs.getString("link_reuniao")
        );

        encontro.setStatus(
            rs.getString("status")
        );

        encontro.setLocalEncontro(
            rs.getString("local_encontro")
        );

        try {
            encontro.setMotivoNaoRealizacao(
                rs.getString("motivo_nao_realizacao")
            );
        } catch (SQLException e) {
            // Compatibilidade com bancos criados antes desta funcionalidade.
            encontro.setMotivoNaoRealizacao(null);
        }

        return encontro;
    }

    private void garantirColunaMotivo(Connection conn) throws SQLException {
        String consulta = "SELECT 1 FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'Encontro' "
                + "AND COLUMN_NAME = 'motivo_nao_realizacao'";
        try (PreparedStatement stmt = conn.prepareStatement(consulta);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return;
        }
        try (PreparedStatement stmt = conn.prepareStatement(
                "ALTER TABLE Encontro ADD COLUMN "
                + "motivo_nao_realizacao TEXT NULL AFTER status")) {
            stmt.executeUpdate();
        }
    }
}
