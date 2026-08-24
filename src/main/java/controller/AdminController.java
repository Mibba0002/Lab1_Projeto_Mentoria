package controller;

import java.util.ArrayList;

import dao.AdminDao;
import dao.MentorDao;
import dao.MentoradoDao;
import model.Admin;

public class AdminController {

	private AdminDao adminDao;
	private MentorDao mentorDao;
	private MentoradoDao mentoradoDao;

	public AdminController() {
		adminDao = new AdminDao();
		mentorDao = new MentorDao();
		mentoradoDao = new MentoradoDao();
	}

	// CADASTRAR ADMINISTRADOR
	public boolean cadastrarAdmin(Admin admin) {

		if (!dadosValidos(admin)) {
			return false;
		}

		// Valores padrão definidos no DER.
		if (textoVazio(admin.getStatus())) {
			admin.setStatus("Ativo");
		}

		if (textoVazio(admin.getNivelAcesso())) {
			admin.setNivelAcesso("Admin");
		}

		if (adminDao.emailExiste(admin.getEmail())) {
			System.out.println("E-mail de administrador já cadastrado.");
			return false;
		}

		return adminDao.cadastrar(admin);
	}

	// LISTAR ADMINISTRADORES
	public ArrayList<Admin> listarAdmins() {
		return adminDao.listar();
	}

	// BUSCAR ADMINISTRADOR PELO ID
	public Admin buscarPorId(int idAdmin) {

		if (idAdmin <= 0) {
			System.out.println("ID do administrador inválido.");
			return null;
		}

		return adminDao.buscarPorId(idAdmin);
	}

	public Admin buscarPorEmail(String email) {
		if (textoVazio(email)) {
			return null;
		}
		return adminDao.buscarPorEmail(email);
	}

	public Admin login(String email, String senha) {
		if (textoVazio(email) || textoVazio(senha)) {
			return null;
		}
		return adminDao.login(email, senha);
	}

	// ATUALIZAR ADMINISTRADOR
	public boolean atualizarAdmin(Admin admin) {

		if (admin == null) {
			System.out.println("Administrador inválido.");
			return false;
		}

		if (admin.getIdAdmin() <= 0) {
			System.out.println("ID do administrador inválido.");
			return false;
		}

		if (!dadosValidos(admin)) {
			return false;
		}

		if (textoVazio(admin.getStatus())) {
			admin.setStatus("Ativo");
		}

		if (textoVazio(admin.getNivelAcesso())) {
			admin.setNivelAcesso("Admin");
		}

		return adminDao.atualizar(admin);
	}

	// EXCLUIR ADMINISTRADOR
	public boolean excluirAdmin(int idAdmin) {

		if (idAdmin <= 0) {
			System.out.println("ID do administrador inválido.");
			return false;
		}

		return adminDao.excluir(idAdmin);
	}

	public int contarPorNivel(String nivelAcesso) {
		return textoVazio(nivelAcesso) ? 0
				: adminDao.contarPorNivel(nivelAcesso);
	}

	public boolean verificarMentor(String cpfMentor, int idAdmin) {
		if (textoVazio(cpfMentor) || idAdmin <= 0) {
			return false;
		}
		return mentorDao.verificarMentor(cpfMentor, idAdmin);
	}

	public boolean alterarStatusMentor(String cpfMentor, String status) {
		if (textoVazio(cpfMentor) || textoVazio(status)) {
			return false;
		}
		return mentorDao.alterarStatus(cpfMentor, status);
	}

	public boolean alterarStatusMentorado(
			String cpfMentorado, String status) {
		if (textoVazio(cpfMentorado) || textoVazio(status)) {
			return false;
		}
		return mentoradoDao.alterarStatus(cpfMentorado, status);
	}

	public boolean bloquearMentor(String cpfMentor) {
		return !textoVazio(cpfMentor)
				&& mentorDao.alterarStatusSeAtual(
						cpfMentor, "Ativo", "Bloqueado");
	}

	public boolean ativarMentor(String cpfMentor) {
		return !textoVazio(cpfMentor)
				&& mentorDao.alterarStatusSeAtual(
						cpfMentor, "Bloqueado", "Ativo");
	}

	public boolean bloquearMentorado(String cpfMentorado) {
		return !textoVazio(cpfMentorado)
				&& mentoradoDao.alterarStatusSeAtual(
						cpfMentorado, "Ativo", "Bloqueado");
	}

	public boolean ativarMentorado(String cpfMentorado) {
		return !textoVazio(cpfMentorado)
				&& mentoradoDao.alterarStatusSeAtual(
						cpfMentorado, "Bloqueado", "Ativo");
	}

	public boolean excluirMentor(String cpfMentor) {
		return !textoVazio(cpfMentor) && mentorDao.excluir(cpfMentor);
	}

	public boolean excluirMentorado(String cpfMentorado) {
		return !textoVazio(cpfMentorado)
				&& mentoradoDao.excluir(cpfMentorado);
	}

	// VALIDAÇÕES INTERNAS
	private boolean dadosValidos(Admin admin) {

		if (admin == null) {
			System.out.println("Administrador inválido.");
			return false;
		}

		if (textoVazio(admin.getNome())) {
			System.out.println("Nome do administrador não informado.");
			return false;
		}

		if (textoVazio(admin.getEmail())) {
			System.out.println("E-mail do administrador não informado.");
			return false;
		}

		if (!admin.getEmail().contains("@")) {
			System.out.println("E-mail do administrador inválido.");
			return false;
		}

		if (textoVazio(admin.getSenha())) {
			System.out.println("Senha do administrador não informada.");
			return false;
		}

		return true;
	}

	private boolean textoVazio(String texto) {
		return texto == null || texto.trim().isEmpty();
	}
}
