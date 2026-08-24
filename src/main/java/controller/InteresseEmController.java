package controller;

import java.util.ArrayList;

import dao.InteresseEmDao;
import model.InteresseEm;

public class InteresseEmController {

    private InteresseEmDao interesseEmDAO;

    public InteresseEmController() {
        interesseEmDAO = new InteresseEmDao();
    }

    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrarInteresse(InteresseEm interesse) {

        if (interesse == null) {
            System.out.println("Interesse inválido.");
            return false;
        }

        if (interesse.getIdAreaAtuacao() <= 0) {
            System.out.println("Área de atuação inválida.");
            return false;
        }

        if (interesse.getCpfMentorado() == null ||
            interesse.getCpfMentorado().trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");
            return false;
        }

        if (interesse.getNivelExperiencia() == null ||
            interesse.getNivelExperiencia().trim().isEmpty()) {

            System.out.println("Nível de experiência não informado.");
            return false;
        }

        if (interesse.getAreaInteresse() == null
                || interesse.getAreaInteresse().trim().isEmpty()) {
            System.out.println("Área de interesse não informada.");
            return false;
        }

        // Verifica se o interesse já existe
        if (interesseEmDAO.existe(
                interesse.getIdAreaAtuacao(),
                interesse.getCpfMentorado())) {

            System.out.println("Este interesse já está cadastrado.");
            return false;
        }

        return interesseEmDAO.cadastrar(interesse);
    }


    // ==========================================
    // BUSCAR POR MENTORADO
    // ==========================================

    public ArrayList<InteresseEm> buscarPorMentorado(
            String cpfMentorado) {

        if (cpfMentorado == null ||
            cpfMentorado.trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");

            return new ArrayList<>();
        }

        return interesseEmDAO.buscarPorMentorado(cpfMentorado);
    }


    // ==========================================
    // LISTAR
    // ==========================================

    public ArrayList<InteresseEm> listarInteresses() {

        return interesseEmDAO.listar();
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizarInteresse(InteresseEm interesse) {

        if (interesse == null) {
            System.out.println("Interesse inválido.");
            return false;
        }

        if (interesse.getIdAreaAtuacao() <= 0) {
            System.out.println("Área de atuação inválida.");
            return false;
        }

        if (interesse.getCpfMentorado() == null ||
            interesse.getCpfMentorado().trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");
            return false;
        }

        if (interesse.getNivelExperiencia() == null ||
            interesse.getNivelExperiencia().trim().isEmpty()) {

            System.out.println("Nível de experiência não informado.");
            return false;
        }

        if (interesse.getAreaInteresse() == null
                || interesse.getAreaInteresse().trim().isEmpty()) {
            System.out.println("Área de interesse não informada.");
            return false;
        }

        return interesseEmDAO.atualizar(interesse);
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluirInteresse(
            int idAreaAtuacao,
            String cpfMentorado) {

        if (idAreaAtuacao <= 0) {
            System.out.println("Área de atuação inválida.");
            return false;
        }

        if (cpfMentorado == null ||
            cpfMentorado.trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");
            return false;
        }

        return interesseEmDAO.excluir(
                idAreaAtuacao,
                cpfMentorado
        );
    }
}
