package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Mentor;
import util.CompatibilidadeBanco;
import util.Conexao;

public class MentorDao {

    private static final Logger LOGGER =
            Logger.getLogger(MentorDao.class.getName());

    public boolean cadastrar(Mentor mentor) {
        String sql = "INSERT INTO Mentor "
                + "(cpf_mentor, nome, email, senha, telefone, mini_biografia, "
                + "disponibilidade, formato_mentoria, link_portifolio, "
                + "redes_profissionais, status, cidade, estado, verificado, "
                + "data_verificacao, id_admin_verificador) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = prepararCadastro(conn, sql)) {

            preencherCadastro(stmt, mentor);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao cadastrar mentor: " + mentor.getCpfMentor(), e);
            return false;
        }
    }

    private PreparedStatement prepararCadastro(Connection conn, String sql)
            throws SQLException {
        CompatibilidadeBanco.garantirTelefoneMentor(conn);
        return conn.prepareStatement(sql);
    }

    public ArrayList<Mentor> listar() {
        ArrayList<Mentor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mentor ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarMentor(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar mentores", e);
        }

        return lista;
    }

    public ArrayList<Mentor> listarAtivos() {
        ArrayList<Mentor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mentor WHERE status = 'Ativo' "
                + "ORDER BY verificado DESC, nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarMentor(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar mentores ativos", e);
        }
        return lista;
    }

    public Mentor buscarPorCpf(String cpf) {
        return buscarUnico("SELECT * FROM Mentor WHERE cpf_mentor = ?", cpf);
    }

    public Mentor buscarPorEmail(String email) {
        return buscarUnico("SELECT * FROM Mentor WHERE email = ?", email);
    }

    public Mentor login(String email, String senha) {
        String sql = "SELECT * FROM Mentor "
                + "WHERE email = ? AND senha = ? AND status = 'Ativo'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? criarMentor(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro no login do mentor", e);
            return null;
        }
    }

    public boolean cpfExiste(String cpfMentor) {
        return existe("SELECT 1 FROM Mentor WHERE cpf_mentor = ?", cpfMentor);
    }

    public boolean emailExiste(String email) {
        return existe("SELECT 1 FROM Mentor WHERE email = ?", email);
    }

    public boolean atualizar(Mentor mentor) {
        String sql = "UPDATE Mentor SET nome = ?, email = ?, senha = ?, "
                + "telefone = ?, mini_biografia = ?, disponibilidade = ?, "
                + "formato_mentoria = ?, link_portifolio = ?, "
                + "redes_profissionais = ?, status = ?, cidade = ?, "
                + "estado = ?, verificado = ?, data_verificacao = ?, "
                + "id_admin_verificador = ? WHERE cpf_mentor = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentor.getNome());
            stmt.setString(2, mentor.getEmail());
            stmt.setString(3, mentor.getSenha());
            stmt.setString(4, mentor.getTelefone());
            stmt.setString(5, mentor.getMiniBiografia());
            stmt.setString(6, mentor.getDisponibilidade());
            stmt.setString(7, mentor.getFormatoMentoria());
            stmt.setString(8, mentor.getLinkPortifolio());
            stmt.setString(9, mentor.getRedesProfissionais());
            stmt.setString(10, mentor.getStatus());
            stmt.setString(11, mentor.getCidade());
            stmt.setString(12, mentor.getEstado());
            stmt.setBoolean(13, mentor.isVerificado());
            definirDataVerificacao(stmt, 14, mentor);
            definirAdminVerificador(stmt, 15, mentor);
            stmt.setString(16, mentor.getCpfMentor());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao atualizar mentor: " + mentor.getCpfMentor(), e);
            return false;
        }
    }

    public boolean verificarMentor(String cpfMentor, int idAdmin) {
        String sql = "UPDATE Mentor SET verificado = 1, "
                + "data_verificacao = CURRENT_TIMESTAMP, "
                + "id_admin_verificador = ? WHERE cpf_mentor = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAdmin);
            stmt.setString(2, cpfMentor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao verificar mentor: " + cpfMentor, e);
            return false;
        }
    }

    public boolean alterarStatus(String cpfMentor, String status) {
        String sql = "UPDATE Mentor SET status = ? WHERE cpf_mentor = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, cpfMentor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao alterar status do mentor: " + cpfMentor, e);
            return false;
        }
    }

    public boolean alterarStatusSeAtual(String cpfMentor,
                                        String statusAtual,
                                        String novoStatus) {
        String sql = "UPDATE Mentor SET status = ? "
                + "WHERE cpf_mentor = ? AND status = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setString(2, cpfMentor);
            stmt.setString(3, statusAtual);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao alterar status do mentor: " + cpfMentor, e);
            return false;
        }
    }

    public boolean excluir(String cpfMentor) {
        String sql = "DELETE FROM Mentor WHERE cpf_mentor = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao excluir mentor: " + cpfMentor, e);
            return false;
        }
    }

    private Mentor buscarUnico(String sql, String valor) {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? criarMentor(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar mentor", e);
            return null;
        }
    }

    private boolean existe(String sql, String valor) {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao verificar mentor", e);
            return false;
        }
    }

    private void preencherCadastro(PreparedStatement stmt, Mentor mentor)
            throws SQLException {

        stmt.setString(1, mentor.getCpfMentor());
        stmt.setString(2, mentor.getNome());
        stmt.setString(3, mentor.getEmail());
        stmt.setString(4, mentor.getSenha());
        stmt.setString(5, mentor.getTelefone());
        stmt.setString(6, mentor.getMiniBiografia());
        stmt.setString(7, mentor.getDisponibilidade());
        stmt.setString(8, mentor.getFormatoMentoria());
        stmt.setString(9, mentor.getLinkPortifolio());
        stmt.setString(10, mentor.getRedesProfissionais());
        stmt.setString(11, mentor.getStatus());
        stmt.setString(12, mentor.getCidade());
        stmt.setString(13, mentor.getEstado());
        stmt.setBoolean(14, mentor.isVerificado());
        definirDataVerificacao(stmt, 15, mentor);
        definirAdminVerificador(stmt, 16, mentor);
    }

    private void definirDataVerificacao(
            PreparedStatement stmt, int indice, Mentor mentor)
            throws SQLException {

        if (mentor.getDataVerificacao() == null) {
            stmt.setNull(indice, Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(indice,
                    Timestamp.valueOf(mentor.getDataVerificacao()));
        }
    }

    private void definirAdminVerificador(
            PreparedStatement stmt, int indice, Mentor mentor)
            throws SQLException {

        if (mentor.getIdAdminVerificador() == null) {
            stmt.setNull(indice, Types.INTEGER);
        } else {
            stmt.setInt(indice, mentor.getIdAdminVerificador());
        }
    }

    private Mentor criarMentor(ResultSet rs) throws SQLException {
        Mentor mentor = new Mentor();
        mentor.setCpfMentor(rs.getString("cpf_mentor"));
        mentor.setNome(rs.getString("nome"));
        mentor.setEmail(rs.getString("email"));
        mentor.setSenha(rs.getString("senha"));
        mentor.setTelefone(rs.getString("telefone"));
        mentor.setMiniBiografia(rs.getString("mini_biografia"));
        mentor.setDisponibilidade(rs.getString("disponibilidade"));
        mentor.setFormatoMentoria(rs.getString("formato_mentoria"));
        mentor.setLinkPortifolio(rs.getString("link_portifolio"));
        mentor.setRedesProfissionais(rs.getString("redes_profissionais"));
        mentor.setStatus(rs.getString("status"));
        mentor.setCidade(rs.getString("cidade"));
        mentor.setEstado(rs.getString("estado"));
        mentor.setVerificado(rs.getBoolean("verificado"));

        Timestamp data = rs.getTimestamp("data_verificacao");
        if (data != null) {
            mentor.setDataVerificacao(data.toLocalDateTime());
        }

        int idAdmin = rs.getInt("id_admin_verificador");
        if (!rs.wasNull()) {
            mentor.setIdAdminVerificador(idAdmin);
        }

        return mentor;
    }
}
