package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Mentor;
import util.Conexao;

public class MentorDao {

    private static final Logger LOGGER = Logger.getLogger(MentorDao.class.getName());

    // Cadastrar
    public boolean cadastrar(Mentor mentor) {

        String sql = "INSERT INTO Mentor (cpf_mentor, nome, email, senha, mini_biografia, disponibilidade, formato_mentoria, link_portifolio, redes_profissionais, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentor.getCpfMentor());
            stmt.setString(2, mentor.getNome());
            stmt.setString(3, mentor.getEmail());
            stmt.setString(4, mentor.getSenha());
            stmt.setString(5, mentor.getMiniBiografia());
            stmt.setString(6, mentor.getDisponibilidade());
            stmt.setString(7, mentor.getFormatoMentoria());
            stmt.setString(8, mentor.getLinkPortifolio());
            stmt.setString(9, mentor.getRedesProfissionais());
            stmt.setString(10, mentor.getStatus());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao cadastrar mentor: " + mentor.getCpfMentor(), e);
            return false;
        }
    }

    // Listar
    public ArrayList<Mentor> listar() {

        ArrayList<Mentor> lista = new ArrayList<>();

        String sql = "SELECT cpf_mentor, nome, email, senha, mini_biografia, disponibilidade, " +
                     "formato_mentoria, link_portifolio, redes_profissionais, status FROM Mentor";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Mentor mentor = new Mentor();

                mentor.setCpfMentor(rs.getString("cpf_mentor"));
                mentor.setNome(rs.getString("nome"));
                mentor.setEmail(rs.getString("email"));
                mentor.setSenha(rs.getString("senha"));
                mentor.setMiniBiografia(rs.getString("mini_biografia"));
                mentor.setDisponibilidade(rs.getString("disponibilidade"));
                mentor.setFormatoMentoria(rs.getString("formato_mentoria"));
                mentor.setLinkPortifolio(rs.getString("link_portifolio"));
                mentor.setRedesProfissionais(rs.getString("redes_profissionais"));
                mentor.setStatus(rs.getString("status"));

                lista.add(mentor);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao listar mentores", e);
        }

        return lista;
    }

    // Excluir
    public boolean excluir(String cpfMentor) {

        String sql = "DELETE FROM Mentor WHERE cpf_mentor = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpfMentor);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao excluir mentor: " + cpfMentor, e);
            return false;
        }
    }

    // Atualizar
    public boolean atualizar(Mentor mentor) {

        String sql = "UPDATE Mentor SET nome=?, email=?, senha=?, mini_biografia=?, disponibilidade=?, formato_mentoria=?, link_portifolio=?, redes_profissionais=?, status=? WHERE cpf_mentor=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mentor.getNome());
            stmt.setString(2, mentor.getEmail());
            stmt.setString(3, mentor.getSenha());
            stmt.setString(4, mentor.getMiniBiografia());
            stmt.setString(5, mentor.getDisponibilidade());
            stmt.setString(6, mentor.getFormatoMentoria());
            stmt.setString(7, mentor.getLinkPortifolio());
            stmt.setString(8, mentor.getRedesProfissionais());
            stmt.setString(9, mentor.getStatus());
            stmt.setString(10, mentor.getCpfMentor());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar mentor: " + mentor.getCpfMentor(), e);
            return false;
        }
    }

	public boolean cpfExiste(String cpfMentor) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean emailExiste(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	public Mentor buscarPorCpf(String cpf) {
		// TODO Auto-generated method stub
		return null;
	}

	public Mentor login(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	
}