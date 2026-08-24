package controller;

import java.util.ArrayList;

import dao.EspecializacaoEmDao;
import model.EspecializacaoEm;

public class EspecializacaoEmController {

    private EspecializacaoEmDao especializacaoDao;

    public EspecializacaoEmController() {
        especializacaoDao = new EspecializacaoEmDao();
    }

    public boolean cadastrarEspecializacao(EspecializacaoEm especializacao) {
        if (especializacao == null
                || especializacao.getIdAreaAtuacao() <= 0
                || textoVazio(especializacao.getCpfMentor())
                || textoVazio(especializacao.getEspecializacao())
                || especializacao.getTempoExperiencia() < 0) {
            System.out.println("Dados da especialização inválidos.");
            return false;
        }

        return especializacaoDao.cadastrar(especializacao);
    }

    public EspecializacaoEm buscarPorId(int idEspecializacao) {
        return idEspecializacao > 0
                ? especializacaoDao.buscarPorId(idEspecializacao)
                : null;
    }

    public ArrayList<EspecializacaoEm> buscarPorMentor(String cpfMentor) {
        return textoVazio(cpfMentor)
                ? new ArrayList<>()
                : especializacaoDao.buscarPorMentor(cpfMentor);
    }

    public ArrayList<EspecializacaoEm> listarEspecializacoes() {
        return especializacaoDao.listar();
    }

    public boolean atualizarEspecializacao(
            EspecializacaoEm especializacao) {

        return especializacao != null
                && especializacao.getIdEspecializacao() > 0
                && cadastrarDadosValidos(especializacao)
                && especializacaoDao.atualizar(especializacao);
    }

    public boolean excluirEspecializacao(int idEspecializacao) {
        return idEspecializacao > 0
                && especializacaoDao.excluir(idEspecializacao);
    }

    private boolean cadastrarDadosValidos(EspecializacaoEm especializacao) {
        return especializacao.getIdAreaAtuacao() > 0
                && !textoVazio(especializacao.getCpfMentor())
                && !textoVazio(especializacao.getEspecializacao())
                && especializacao.getTempoExperiencia() >= 0;
    }

    private boolean textoVazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
