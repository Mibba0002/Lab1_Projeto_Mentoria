package controller;

import java.util.ArrayList;

import dao.EncontroDao;
import model.Encontro;

public class EncontroController {

    private EncontroDao encontroDAO;

    public EncontroController() {
        encontroDAO = new EncontroDao();
    }

    // ==========================================
    // CADASTRAR ENCONTRO
    // ==========================================

    public boolean cadastrarEncontro(Encontro encontro) {

        if (encontro == null) {
            System.out.println("Encontro inválido.");
            return false;
        }

        if (encontro.getIdMentoria() <= 0) {
            System.out.println("Mentoria inválida.");
            return false;
        }

        if (encontro.getData() == null) {
            System.out.println("Data do encontro não informada.");
            return false;
        }

        if (encontro.getHorario() == null) {
            System.out.println("Horário do encontro não informado.");
            return false;
        }

        return encontroDAO.cadastrar(encontro);
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public Encontro buscarPorId(int idEncontro) {

        return encontroDAO.buscarPorId(idEncontro);
    }


    // ==========================================
    // BUSCAR POR MENTORIA
    // ==========================================

    public ArrayList<Encontro> buscarPorMentoria(int idMentoria) {

        return encontroDAO.buscarPorMentoria(idMentoria);
    }


    // ==========================================
    // LISTAR TODOS
    // ==========================================

    public ArrayList<Encontro> listarEncontros() {

        return encontroDAO.listar();
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizarEncontro(Encontro encontro) {

        if (encontro == null) {
            System.out.println("Encontro inválido.");
            return false;
        }

        if (encontro.getIdEncontro() <= 0) {
            System.out.println("ID do encontro inválido.");
            return false;
        }

        if (encontro.getData() == null) {
            System.out.println("Data do encontro não informada.");
            return false;
        }

        if (encontro.getHorario() == null) {
            System.out.println("Horário do encontro não informado.");
            return false;
        }

        return encontroDAO.atualizar(encontro);
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluirEncontro(int idEncontro) {

        if (idEncontro <= 0) {
            System.out.println("ID do encontro inválido.");
            return false;
        }

        return encontroDAO.excluir(idEncontro);
    }
}