package controller;

import java.util.ArrayList;

import dao.MentoriaDao;
import model.Mentoria;

public class MentoriaController {

    private MentoriaDao mentoriaDAO;

    public MentoriaController() {
        mentoriaDAO = new MentoriaDao();
    }


    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrarMentoria(Mentoria mentoria) {

        if (mentoria == null) {
            System.out.println("Mentoria inválida.");
            return false;
        }

        if (mentoria.getCpfMentor() == null ||
            mentoria.getCpfMentor().trim().isEmpty()) {

            System.out.println("CPF do mentor não informado.");
            return false;
        }

        if (mentoria.getCpfMentorado() == null ||
            mentoria.getCpfMentorado().trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");
            return false;
        }

        if (mentoria.getIdEspecializacao() <= 0) {
            System.out.println("Especialização inválida.");
            return false;
        }

        if (mentoria.getDataInicio() == null) {
            System.out.println("Data de início não informada.");
            return false;
        }

        return mentoriaDAO.cadastrar(mentoria);
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Mentoria buscarPorId(int idMentoria) {

        if (idMentoria <= 0) {
            System.out.println("ID da mentoria inválido.");
            return null;
        }

        return mentoriaDAO.buscarPorId(idMentoria);
    }


    // ==========================================
    // BUSCAR POR MENTOR
    // ==========================================

    public ArrayList<Mentoria> buscarPorMentor(String cpfMentor) {

        if (cpfMentor == null ||
            cpfMentor.trim().isEmpty()) {

            System.out.println("CPF do mentor não informado.");

            return new ArrayList<>();
        }

        return mentoriaDAO.buscarPorMentor(cpfMentor);
    }


    // ==========================================
    // BUSCAR POR MENTORADO
    // ==========================================

    public ArrayList<Mentoria> buscarPorMentorado(
            String cpfMentorado) {

        if (cpfMentorado == null ||
            cpfMentorado.trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");

            return new ArrayList<>();
        }

        return mentoriaDAO.buscarPorMentorado(cpfMentorado);
    }


    // ==========================================
    // LISTAR
    // ==========================================

    public ArrayList<Mentoria> listarMentorias() {

        return mentoriaDAO.listar();
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizarMentoria(Mentoria mentoria) {

        if (mentoria == null) {
            System.out.println("Mentoria inválida.");
            return false;
        }

        if (mentoria.getIdMentoria() <= 0) {
            System.out.println("ID da mentoria inválido.");
            return false;
        }

        if (mentoria.getCpfMentor() == null ||
            mentoria.getCpfMentor().trim().isEmpty()) {

            System.out.println("CPF do mentor não informado.");
            return false;
        }

        if (mentoria.getCpfMentorado() == null ||
            mentoria.getCpfMentorado().trim().isEmpty()) {

            System.out.println("CPF do mentorado não informado.");
            return false;
        }

        return mentoriaDAO.atualizar(mentoria);
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluirMentoria(int idMentoria) {

        if (idMentoria <= 0) {
            System.out.println("ID da mentoria inválido.");
            return false;
        }

        return mentoriaDAO.excluir(idMentoria);
    }

    public boolean finalizarMentoria(int idMentoria, String cpfMentor) {
        if (idMentoria <= 0 || cpfMentor == null
                || cpfMentor.trim().isEmpty()) {
            return false;
        }
        return mentoriaDAO.finalizarSeAtiva(idMentoria, cpfMentor);
    }
}
