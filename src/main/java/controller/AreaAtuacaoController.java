package controller;

import java.util.ArrayList;

import dao.AreaAtuacaoDao;
import model.AreaAtuacao;

public class AreaAtuacaoController {

    private AreaAtuacaoDao areaAtuacaoDAO;

    public AreaAtuacaoController() {
        areaAtuacaoDAO = new AreaAtuacaoDao();
    }

    // ==========================================
    // CADASTRAR
    // ==========================================

    public boolean cadastrarArea(AreaAtuacao area) {

        if (area == null) {
            System.out.println("Área de atuação inválida.");
            return false;
        }

        if (area.getNomeArea() == null ||
            area.getNomeArea().trim().isEmpty()) {

            System.out.println("Nome da área não informado.");
            return false;
        }

        // Verifica se já existe
        if (areaAtuacaoDAO.existe(area.getNomeArea())) {
            System.out.println("Esta área de atuação já está cadastrada.");
            return false;
        }

        return areaAtuacaoDAO.cadastrar(area);
    }


    // ==========================================
    // BUSCAR POR ID
    // ==========================================

    public AreaAtuacao buscarPorId(int id) {

        return areaAtuacaoDAO.buscarPorId(id);
    }


    // ==========================================
    // LISTAR
    // ==========================================

    public ArrayList<AreaAtuacao> listarAreas() {

        return areaAtuacaoDAO.listar();
    }


    // ==========================================
    // ATUALIZAR
    // ==========================================

    public boolean atualizarArea(AreaAtuacao area) {

        if (area == null) {
            System.out.println("Área de atuação inválida.");
            return false;
        }

        if (area.getIdAreaAtuacao() <= 0) {
            System.out.println("ID da área inválido.");
            return false;
        }

        if (area.getNomeArea() == null ||
            area.getNomeArea().trim().isEmpty()) {

            System.out.println("Nome da área não informado.");
            return false;
        }

        return areaAtuacaoDAO.atualizar(area);
    }


    // ==========================================
    // EXCLUIR
    // ==========================================

    public boolean excluirArea(int id) {

        if (id <= 0) {
            System.out.println("ID da área inválido.");
            return false;
        }

        return areaAtuacaoDAO.excluir(id);
    }
}