package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AreaAtuacaoController;
import controller.EspecializacaoEmController;
import controller.MentorController;
import model.AreaAtuacao;
import model.EspecializacaoEm;
import model.Mentor;

@WebServlet("/perfil/mentor")
public class PerfilMentorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MentorController mentorController = new MentorController();
    private final AreaAtuacaoController areaController =
            new AreaAtuacaoController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Mentor logado = mentorLogado(request, response);
        if (logado == null) {
            return;
        }

        Mentor atualizado = mentorController.buscarPorCpf(
                logado.getCpfMentor());
        if (atualizado == null) {
            sair(request, response);
            return;
        }

        request.getSession().setAttribute("usuarioLogado", atualizado);
        request.setAttribute("mentor", atualizado);
        request.setAttribute("areas", areaController.listarAreas());
        request.setAttribute("especializacoes",
                especializacaoController.buscarPorMentor(
                        atualizado.getCpfMentor()));
        request.getRequestDispatcher("/pages/meuPerfilMentor.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        Mentor logado = mentorLogado(request, response);
        if (logado == null) {
            return;
        }

        Mentor mentor = mentorController.buscarPorCpf(logado.getCpfMentor());
        if (mentor == null) {
            sair(request, response);
            return;
        }

        String acao = request.getParameter("acao");
        boolean sucesso = switch (acao == null ? "" : acao) {
            case "salvar" -> salvarDados(request, mentor);
            case "senha" -> alterarSenha(request, mentor);
            case "adicionarEspecializacao" ->
                    adicionarEspecializacao(request, mentor);
            case "removerEspecializacao" ->
                    removerEspecializacao(request, mentor);
            default -> false;
        };

        if (sucesso && ("salvar".equals(acao) || "senha".equals(acao))) {
            request.getSession().setAttribute("usuarioLogado",
                    mentorController.buscarPorCpf(mentor.getCpfMentor()));
        }

        response.sendRedirect(request.getContextPath()
                + "/perfil/mentor?resultado="
                + (sucesso ? "sucesso" : "erro"));
    }

    private boolean salvarDados(HttpServletRequest request, Mentor mentor) {
        String nome = limpar(request.getParameter("nome"));
        String email = limpar(request.getParameter("email"));
        String telefone = limpar(request.getParameter("telefone"));
        String cidade = limpar(request.getParameter("cidade"));
        String estado = limpar(request.getParameter("estado")).toUpperCase();
        String biografia = limpar(request.getParameter("biografia"));
        String formato = limpar(request.getParameter("formato"));
        String disponibilidade = limpar(
                request.getParameter("disponibilidade"));

        if (vazio(nome) || vazio(email) || !email.contains("@")
                || vazio(telefone) || vazio(cidade) || estado.length() != 2
                || vazio(biografia) || vazio(formato)
                || vazio(disponibilidade)) {
            return false;
        }

        mentor.setNome(nome);
        mentor.setEmail(email);
        mentor.setTelefone(telefone);
        mentor.setFormatoMentoria(limpar(
                formato));
        mentor.setDisponibilidade(disponibilidade);
        mentor.setLinkPortifolio(limpar(
                request.getParameter("portfolio")));
        mentor.setRedesProfissionais(limpar(
                request.getParameter("redesProfissionais")));
        mentor.setMiniBiografia(biografia);
        mentor.setCidade(cidade);
        mentor.setEstado(estado);
        return mentorController.atualizarMentor(mentor);
    }

    private boolean alterarSenha(HttpServletRequest request, Mentor mentor) {
        String nova = request.getParameter("novaSenha");
        String confirmacao = request.getParameter("confirmarSenha");
        if (nova == null || nova.length() < 6 || !nova.equals(confirmacao)) {
            return false;
        }
        mentor.setSenha(nova);
        return mentorController.atualizarMentor(mentor);
    }

    private boolean adicionarEspecializacao(HttpServletRequest request,
                                             Mentor mentor) {
        int idArea = inteiro(request.getParameter("idArea"));
        AreaAtuacao area = areaController.buscarPorId(idArea);
        if (area == null) {
            return false;
        }

        ArrayList<EspecializacaoEm> atuais =
                especializacaoController.buscarPorMentor(
                        mentor.getCpfMentor());
        for (EspecializacaoEm atual : atuais) {
            if (atual.getIdAreaAtuacao() == idArea) {
                return false;
            }
        }

        EspecializacaoEm especializacao = new EspecializacaoEm();
        especializacao.setIdAreaAtuacao(idArea);
        especializacao.setCpfMentor(mentor.getCpfMentor());
        especializacao.setEspecializacao(area.getNomeArea());
        especializacao.setTempoExperiencia(
                inteiro(request.getParameter("tempoExperiencia")));
        return especializacaoController.cadastrarEspecializacao(
                especializacao);
    }

    private boolean removerEspecializacao(HttpServletRequest request,
                                           Mentor mentor) {
        int id = inteiro(request.getParameter("idEspecializacao"));
        EspecializacaoEm especializacao =
                especializacaoController.buscarPorId(id);
        return especializacao != null
                && mentor.getCpfMentor().equals(
                        especializacao.getCpfMentor())
                && especializacaoController.excluirEspecializacao(id);
    }

    private Mentor mentorLogado(HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null
                ? null : sessao.getAttribute("usuarioLogado");
        if (usuario instanceof Mentor mentor) {
            return mentor;
        }
        response.sendRedirect(request.getContextPath()
                + "/pages/login.html?erro=credenciais");
        return null;
    }

    private void sair(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
        response.sendRedirect(request.getContextPath()
                + "/pages/login.html?erro=credenciais");
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }

    private int inteiro(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
