package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Mentorado;
import util.CompatibilidadeBanco;
import util.Conexao;

public class MentoradoDao {

    private static final Logger LOGGER =
            Logger.getLogger(MentoradoDao.class.getName());

    public boolean cadastrar(Mentorado mentorado) {
        String sql = "INSERT INTO Mentorado "
                + "(cpf_mentorado, nome, email, senha, telefone, formacao, "
                + "objetivos_profissionais, principais_duvidas, "
                + "expectativas, cidade, estado, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = prepararCadastro(conn, sql)) {

            stmt.setString(1, mentorado.getCpfMentorado());
            stmt.setString(2, mentorado.getNome());
            stmt.setString(3, mentorado.getEmail());
            stmt.setString(4, mentorado.getSenha());
            stmt.setString(5, mentorado.getTelefone());
            stmt.setString(6, mentorado.getFormacao());
            stmt.setString(7, mentorado.getObjetivosProfissionais());
            stmt.setString(8, mentorado.getPrincipaisDuvidas());
            stmt.setString(9, mentorado.getExpectativas());
            stmt.setString(10, mentorado.getCidade());
            stmt.setString(11, mentorado.getEstado());
            stmt.setString(12, mentorado.getStatus());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao cadastrar mentorado: "
                            + mentorado.getCpfMentorado(), e);
            return false;
        }
    }

    private PreparedStatement prepararCadastro(Connection conn, String sql)
            throws SQLException {
        CompatibilidadeBanco.garantirTelefoneMentorado(conn);
        return conn.prepareStatement(sql);
    }

    public ArrayList<Mentorado> listar() {
        ArrayList<Mentorado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mentorado ORDER BY nome";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarMentorado(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar mentorados", e);
        }

        return lista;
    }

    public Mentorado buscarPorCpf(String cpf) {
        return buscarUnico(
                "SELECT * FROM Mentorado WHERE cpf_mentorado = ?", cpf);
    }

    public Mentorado buscarPorEmail(String email) {
        return buscarUnico("SELECT * FROM Mentorado WHERE email = ?", email);
    }

    public Mentorado login(String email, String senha) {
        String sql = "SELECT * FROM Mentorado "
                + "WHERE email = ? AND senha = ? AND status = 'Ativo'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? criarMentorado(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro no login do mentorado", e);
            return null;
        }
    }

    public boolean cpfExiste(String cpf) {
        return existe("SELECT 1 FROM Mentorado WHERE cpf_mentorado = ?", cpf);
    }

    public boolean emailExiste(String email) {
        return existe("SELECT 1 FROM Mentorado WHERE email = ?", email);
    }

    public boolean atualizar(Mentorado mentorado) {
        String sql = "UPDATE Mentorado SET nome = ?, email = ?, senha = ?, "
                + "telefone = ?, formacao = ?, objetivos_profissionais = ?, "
                + "principais_duvidas = ?, expectativas = ?, cidade = ?, "
                + "estado = ?, status = ? WHERE cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentorado.getNome());
            stmt.setString(2, mentorado.getEmail());
            stmt.setString(3, mentorado.getSenha());
            stmt.setString(4, mentorado.getTelefone());
            stmt.setString(5, mentorado.getFormacao());
            stmt.setString(6, mentorado.getObjetivosProfissionais());
            stmt.setString(7, mentorado.getPrincipaisDuvidas());
            stmt.setString(8, mentorado.getExpectativas());
            stmt.setString(9, mentorado.getCidade());
            stmt.setString(10, mentorado.getEstado());
            stmt.setString(11, mentorado.getStatus());
            stmt.setString(12, mentorado.getCpfMentorado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao atualizar mentorado: "
                            + mentorado.getCpfMentorado(), e);
            return false;
        }
    }

    public boolean alterarStatus(String cpfMentorado, String status) {
        String sql = "UPDATE Mentorado SET status = ? "
                + "WHERE cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setString(2, cpfMentorado);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao alterar status do mentorado: "
                            + cpfMentorado, e);
            return false;
        }
    }

    public boolean alterarStatusSeAtual(String cpfMentorado,
                                        String statusAtual,
                                        String novoStatus) {
        String sql = "UPDATE Mentorado SET status = ? "
                + "WHERE cpf_mentorado = ? AND status = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setString(2, cpfMentorado);
            stmt.setString(3, statusAtual);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao alterar status do mentorado: "
                            + cpfMentorado, e);
            return false;
        }
    }

    public boolean excluir(String cpfMentorado) {
        String sql = "DELETE FROM Mentorado WHERE cpf_mentorado = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentorado);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao excluir mentorado: " + cpfMentorado, e);
            return false;
        }
    }

    private Mentorado buscarUnico(String sql, String valor) {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? criarMentorado(rs) : null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar mentorado", e);
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
            LOGGER.log(Level.SEVERE, "Erro ao verificar mentorado", e);
            return false;
        }
    }

    private Mentorado criarMentorado(ResultSet rs) throws SQLException {
        Mentorado mentorado = new Mentorado();
        mentorado.setCpfMentorado(rs.getString("cpf_mentorado"));
        mentorado.setNome(rs.getString("nome"));
        mentorado.setEmail(rs.getString("email"));
        mentorado.setSenha(rs.getString("senha"));
        mentorado.setTelefone(rs.getString("telefone"));
        mentorado.setFormacao(rs.getString("formacao"));
        mentorado.setObjetivosProfissionais(
                rs.getString("objetivos_profissionais"));
        mentorado.setPrincipaisDuvidas(rs.getString("principais_duvidas"));
        mentorado.setExpectativas(rs.getString("expectativas"));
        mentorado.setCidade(rs.getString("cidade"));
        mentorado.setEstado(rs.getString("estado"));
        mentorado.setStatus(rs.getString("status"));
        return mentorado;
    }
}
