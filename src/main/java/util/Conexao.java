package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static final String URL_PADRAO =
            "jdbc:mysql://localhost:3306/mydb?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Bahia"
            + "&useUnicode=true&characterEncoding=UTF-8";

    private static final Properties PROPRIEDADES = carregarPropriedades();

    private static final String URL = obterConfiguracao(
            "db.url", "DB_URL", URL_PADRAO);

    private static final String USER = obterConfiguracao(
            "db.user", "DB_USER", "root");

    private static final String PASSWORD = obterConfiguracao(
            "db.password", "DB_PASSWORD", "");

    public static Connection conectar() throws SQLException {
        carregarDriver();
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean testarConexao() {
        try (Connection conexao = conectar()) {
            return conexao != null && !conexao.isClosed();
        } catch (SQLException e) {
            System.err.println("Falha na conexão com o MySQL: "
                    + e.getMessage());
            return false;
        }
    }

    private static void carregarDriver() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL Connector/J não foi encontrado no projeto.", e);
        }
    }

    private static Properties carregarPropriedades() {
        Properties propriedades = new Properties();

        try (InputStream arquivo = Conexao.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (arquivo != null) {
                propriedades.load(arquivo);
            }
        } catch (IOException e) {
            System.err.println("Não foi possível ler db.properties: "
                    + e.getMessage());
        }

        return propriedades;
    }

    private static String obterConfiguracao(
            String propriedade,
            String variavelAmbiente,
            String valorPadrao) {

        String valorSistema = System.getProperty(propriedade);
        if (valorSistema != null && !valorSistema.isBlank()) {
            return valorSistema;
        }

        String valorAmbiente = System.getenv(variavelAmbiente);
        if (valorAmbiente != null && !valorAmbiente.isBlank()) {
            return valorAmbiente;
        }

        return PROPRIEDADES.getProperty(propriedade, valorPadrao);
    }
}
