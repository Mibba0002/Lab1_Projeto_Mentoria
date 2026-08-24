package controller;

import java.util.ArrayList;

import dao.AvaliacaoDao;
import model.Avaliacao;

public class AvaliacaoController {
    private final AvaliacaoDao avaliacaoDao = new AvaliacaoDao();

    public boolean avaliar(int idMentoria, String cpfMentorado,
                           int nota, String comentario) {
        if (idMentoria <= 0 || cpfMentorado == null
                || cpfMentorado.isBlank() || nota < 1 || nota > 5) {
            return false;
        }
        String texto = comentario == null ? "" : comentario.trim();
        if (texto.length() > 2000) return false;
        return avaliacaoDao.cadastrarParaMentoriaFinalizada(
                idMentoria, cpfMentorado, nota, texto);
    }

    public boolean existePorMentoria(int idMentoria) {
        return idMentoria > 0 && avaliacaoDao.existePorMentoria(idMentoria);
    }

    public ArrayList<Avaliacao> listarPorMentor(String cpf) {
        return avaliacaoDao.listarPorMentor(cpf);
    }

    public ArrayList<Avaliacao> listarPorMentorado(String cpf) {
        return avaliacaoDao.listarPorMentorado(cpf);
    }

    public double mediaPorMentor(String cpf) {
        return avaliacaoDao.mediaPorMentor(cpf);
    }

    public int contarPorMentor(String cpf) {
        return avaliacaoDao.contarPorMentor(cpf);
    }
}
