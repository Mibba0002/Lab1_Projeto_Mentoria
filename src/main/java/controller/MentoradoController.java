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
        return mentoradoDao.cadastrar(mentorado);
    }

    // LISTAR MENTORADOS
    public ArrayList<Mentorado> listarMentorados() {
        return mentoradoDao.listar();
    }

    // ATUALIZAR MENTORADO
    public boolean atualizarMentorado(Mentorado mentorado) {
        return mentoradoDao.atualizar(mentorado);
    }

    // EXCLUIR MENTORADO
    public boolean excluirMentorado(String cpf) {
        return mentoradoDao.excluir(cpf);
    }
}