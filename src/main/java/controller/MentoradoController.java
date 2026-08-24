package controller;

import java.util.ArrayList;

import dao.MentoradoDao;
import model.Mentorado;

public class MentoradoController {

    private MentoradoDao mentoradoDao;

    public MentoradoController() {
        mentoradoDao = new MentoradoDao();
    }

    // CADASTRAR MENTORADO
    public boolean cadastrarMentorado(Mentorado mentorado) {

        if (mentorado == null
                || textoVazio(mentorado.getCpfMentorado())
                || textoVazio(mentorado.getNome())
                || textoVazio(mentorado.getEmail())
                || textoVazio(mentorado.getSenha())
                || textoVazio(mentorado.getTelefone())
                || textoVazio(mentorado.getCidade())
                || textoVazio(mentorado.getEstado())) {
            System.out.println("Dados obrigatórios do mentorado não informados.");
            return false;
        }

        if (mentoradoDao.cpfExiste(mentorado.getCpfMentorado())) {
            System.out.println("CPF já cadastrado.");
            return false;
        }

        if (mentoradoDao.emailExiste(mentorado.getEmail())) {
            System.out.println("E-mail já cadastrado.");
            return false;
        }

        if (textoVazio(mentorado.getStatus())) {
            mentorado.setStatus("Ativo");
        }

        return mentoradoDao.cadastrar(mentorado);
    }

    // LISTAR MENTORADOS
    public ArrayList<Mentorado> listarMentorados() {
        return mentoradoDao.listar();
    }

    public Mentorado buscarPorCpf(String cpf) {
        return mentoradoDao.buscarPorCpf(cpf);
    }

    public Mentorado buscarPorEmail(String email) {
        return mentoradoDao.buscarPorEmail(email);
    }

    public Mentorado login(String email, String senha) {
        return mentoradoDao.login(email, senha);
    }

    // ATUALIZAR MENTORADO
    public boolean atualizarMentorado(Mentorado mentorado) {
        return mentoradoDao.atualizar(mentorado);
    }

    // EXCLUIR MENTORADO
    public boolean excluirMentorado(String cpf) {
        return mentoradoDao.excluir(cpf);
    }

    public boolean alterarStatus(String cpf, String status) {
        return mentoradoDao.alterarStatus(cpf, status);
    }

    private boolean textoVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
