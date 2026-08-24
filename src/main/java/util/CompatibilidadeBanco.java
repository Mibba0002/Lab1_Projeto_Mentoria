package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mantém compatibilidade com o DER original usado no início do trabalho.
 * As colunas complementares são adicionadas sem apagar dados já cadastrados.
 */
public final class CompatibilidadeBanco {

    private CompatibilidadeBanco() {
    }

    public static void garantirTelefoneMentor(Connection conn)
            throws SQLException {
        garantirColuna(conn, "Mentor", "telefone", "VARCHAR(20) NULL");
    }

    public static void garantirTelefoneMentorado(Connection conn)
            throws SQLException {
        garantirColuna(conn, "Mentorado", "telefone", "VARCHAR(20) NULL");
    }

    private static void garantirColuna(Connection conn,
                                       String tabela,
                                       String coluna,
                                       String definicao) throws SQLException {
        String consulta = "SELECT 1 FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? "
                + "AND COLUMN_NAME = ?";
        try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, tabela);
            stmt.setString(2, coluna);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return;
            }
        }

        String alteracao = "ALTER TABLE `" + tabela + "` ADD COLUMN `"
                + coluna + "` " + definicao + " AFTER `senha`";
        try (PreparedStatement stmt = conn.prepareStatement(alteracao)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Evita falha se duas requisições tentarem criar a coluna juntas.
            if (!colunaExiste(conn, tabela, coluna)) throw e;
        }
    }

    private static boolean colunaExiste(Connection conn,
                                        String tabela,
                                        String coluna) throws SQLException {
        String consulta = "SELECT 1 FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? "
                + "AND COLUMN_NAME = ?";
        try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
            stmt.setString(1, tabela);
            stmt.setString(2, coluna);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
