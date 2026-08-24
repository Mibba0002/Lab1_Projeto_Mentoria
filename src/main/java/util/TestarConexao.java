package util;

public class TestarConexao {

    public static void main(String[] args) {
        if (Conexao.testarConexao()) {
            System.out.println("Conexão com o banco mydb realizada com sucesso.");
        } else {
            System.out.println("Não foi possível conectar ao banco mydb.");
        }
    }
}
