package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Admin;
import util.Conexao;

public class AdminDao {

    private static final Logger LOGGER =
            Logger.getLogger(AdminDao.class.getName());

    // Cadastrar
    public boolean cadastrar(Admin admin) {

        String sql = "INSERT INTO admin "
                + "(nome, email, senha, status, nivel_acesso) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNome());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getSenha());
            stmt.setString(4, admin.getStatus());
            stmt.setString(5, admin.getNivelAcesso());

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erro ao cadastrar administrador: " + admin.getEmail(),
                    e
            );

            return false;
        }
    }

    // Listar
    public ArrayList<Admin> listar() {

        ArrayList<Admin> lista = new ArrayList<>();

        String sql = "SELECT id_admin, nome, email, senha, "
                + "status, nivel_acesso FROM admin";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Admin admin = new Admin();

                admin.setIdAdmin(rs.getInt("id_admin"));
                admin.setNome(rs.getString("nome"));
                admin.setEmail(rs.getString("email"));
                admin.setSenha(rs.getString("senha"));
                admin.setStatus(rs.getString("status"));
                admin.setNivelAcesso(rs.getString("nivel_acesso"));

                lista.add(admin);
            }

        } catch (SQLException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erro ao listar administradores",
                    e
            );
        }

        return lista;
    }

    // Buscar pelo ID
    public Admin buscarPorId(int idAdmin) {

        String sql = "SELECT id_admin, nome, email, senha, "
                + "status, nivel_acesso "
                + "FROM admin WHERE id_admin = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAdmin);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Admin admin = new Admin();

                    admin.setIdAdmin(rs.getInt("id_admin"));
                    admin.setNome(rs.getString("nome"));
                    admin.setEmail(rs.getString("email"));
                    admin.setSenha(rs.getString("senha"));
                    admin.setStatus(rs.getString("status"));
                    admin.setNivelAcesso(rs.getString("nivel_acesso"));

                    return admin;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erro ao buscar administrador de ID: " + idAdmin,
                    e
            );
        }

        return null;
    }

    // Buscar pelo e-mail
    public Admin buscarPorEmail(String email) {
        String sql = "SELECT id_admin, nome, email, senha, "
                + "status, nivel_acesso FROM admin WHERE email = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarAdmin(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao buscar administrador: " + email, e);
        }

        return null;
    }

    // Login
    public Admin login(String email, String senha) {
        String sql = "SELECT id_admin, nome, email, senha, "
                + "status, nivel_acesso FROM admin "
                + "WHERE email = ? AND senha = ? AND status = 'Ativo'";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarAdmin(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro no login do administrador", e);
        }

        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM admin WHERE email = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao verificar e-mail do administrador", e);
            return false;
        }
    }

    // Atualizar
    public boolean atualizar(Admin admin) {

        String sql = "UPDATE admin SET "
                + "nome = ?, "
                + "email = ?, "
                + "senha = ?, "
                + "status = ?, "
                + "nivel_acesso = ? "
                + "WHERE id_admin = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNome());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getSenha());
            stmt.setString(4, admin.getStatus());
            stmt.setString(5, admin.getNivelAcesso());
            stmt.setInt(6, admin.getIdAdmin());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erro ao atualizar administrador de ID: "
                            + admin.getIdAdmin(),
                    e
            );

            return false;
        }
    }

    // Excluir
    public boolean excluir(int idAdmin) {

        String sql = "DELETE FROM admin WHERE id_admin = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAdmin);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Erro ao excluir administrador de ID: " + idAdmin,
                    e
            );

            return false;
        }
    }

    public boolean alterarStatus(int idAdmin, String status) {
        String sql = "UPDATE admin SET status = ? WHERE id_admin = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, idAdmin);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erro ao alterar status do administrador: " + idAdmin,
                    e);
            return false;
        }
    }

    public int contarPorNivel(String nivelAcesso) {
        String sql = "SELECT COUNT(*) total FROM admin "
                + "WHERE UPPER(nivel_acesso) = UPPER(?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nivelAcesso);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao contar administradores", e);
            return 0;
        }
    }

    private Admin criarAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setIdAdmin(rs.getInt("id_admin"));
        admin.setNome(rs.getString("nome"));
        admin.setEmail(rs.getString("email"));
        admin.setSenha(rs.getString("senha"));
        admin.setStatus(rs.getString("status"));
        admin.setNivelAcesso(rs.getString("nivel_acesso"));
        return admin;
    }
}
