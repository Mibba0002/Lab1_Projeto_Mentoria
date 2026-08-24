package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.DiarioBordo;
import util.Conexao;

public class DiarioBordoDao {

    public boolean cadastrar(DiarioBordo registro) {
        String sql = "INSERT INTO Diario_bordo "
                + "(id_mentoria, tipo_autor, cpf_autor, conteudo, "
                + "data_registro) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, registro.getIdMentoria());
            stmt.setString(2, registro.getTipoAutor());
            stmt.setString(3, registro.getCpfAutor());
            stmt.setString(4, registro.getConteudo());
            if (stmt.executeUpdate() == 0) return false;
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) registro.setIdDiario(rs.getInt(1));
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar diário: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<DiarioBordo> buscarPorMentoria(int idMentoria) {
        ArrayList<DiarioBordo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Diario_bordo WHERE id_mentoria = ? "
                + "ORDER BY data_registro DESC, id_diario DESC";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMentoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(criar(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar diário: " + e.getMessage());
        }
        return lista;
    }

    private DiarioBordo criar(ResultSet rs) throws SQLException {
        DiarioBordo registro = new DiarioBordo();
        registro.setIdDiario(rs.getInt("id_diario"));
        registro.setIdMentoria(rs.getInt("id_mentoria"));
        registro.setTipoAutor(rs.getString("tipo_autor"));
        registro.setCpfAutor(rs.getString("cpf_autor"));
        registro.setConteudo(rs.getString("conteudo"));
        Timestamp data = rs.getTimestamp("data_registro");
        if (data != null) registro.setDataRegistro(data.toLocalDateTime());
        return registro;
    }
}
