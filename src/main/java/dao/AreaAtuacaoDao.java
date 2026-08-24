package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.AreaAtuacao;
import util.Conexao;

public class AreaAtuacaoDao {

    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrar(AreaAtuacao area) {

        String sql = "INSERT INTO area_atuacao (nome_area) VALUES (?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, area.getNomeArea());

            if (stmt.executeUpdate() == 0) {
                return false;
            }

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    area.setIdAreaAtuacao(chaves.getInt(1));
                }
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar área de atuação: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public AreaAtuacao buscarPorId(int id) {

        String sql = "SELECT * FROM area_atuacao "
                   + "WHERE id_area_atuacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return criarArea(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar área de atuação: "
                    + e.getMessage());
        }

        return null;
    }

    public AreaAtuacao buscarPorNome(String nomeArea) {
        String sql = "SELECT * FROM area_atuacao WHERE nome_area = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeArea);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarArea(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar área de atuação: "
                    + e.getMessage());
        }

        return null;
    }


    // ==========================================
    // LISTAR
    // ==========================================

    public ArrayList<AreaAtuacao> listar() {

        ArrayList<AreaAtuacao> lista = new ArrayList<>();

        String sql = "SELECT * FROM area_atuacao ORDER BY nome_area";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                AreaAtuacao area = criarArea(rs);

                lista.add(area);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar áreas de atuação: "
                    + e.getMessage());
        }

        return lista;
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizar(AreaAtuacao area) {

        String sql = "UPDATE area_atuacao "
                   + "SET nome_area = ? "
                   + "WHERE id_area_atuacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, area.getNomeArea());
            stmt.setInt(2, area.getIdAreaAtuacao());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar área de atuação: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluir(int id) {

        String sql = "DELETE FROM area_atuacao "
                   + "WHERE id_area_atuacao = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir área de atuação: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean estaEmUso(int id) {
        String sql = "SELECT ("
                + "(SELECT COUNT(*) FROM Especializacao_em "
                + "WHERE id_area_atuacao = ?) + "
                + "(SELECT COUNT(*) FROM interesse_em "
                + "WHERE id_area_atuacao = ?)) AS total";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setInt(2, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao verificar uso da área: "
                    + e.getMessage());
            return true;
        }
    }


    // ==========================================
    // VERIFICAR SE EXISTE
    // ==========================================

    public boolean existe(String nomeArea) {

        String sql = "SELECT id_area_atuacao "
                   + "FROM area_atuacao "
                   + "WHERE nome_area = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeArea);

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erro ao verificar área de atuação: "
                    + e.getMessage());
            return false;
        }
    }


    // ==========================================
    // CRIAR OBJETO
    // ==========================================

    private AreaAtuacao criarArea(ResultSet rs) throws SQLException {

        AreaAtuacao area = new AreaAtuacao();

        area.setIdAreaAtuacao(
            rs.getInt("id_area_atuacao")
        );

        area.setNomeArea(
            rs.getString("nome_area")
        );

        return area;
    }
}
