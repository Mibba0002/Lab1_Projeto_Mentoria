package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AreaAtuacaoController;
import controller.InteresseEmController;
import controller.MentoradoController;
import model.AreaAtuacao;
import model.InteresseEm;
import model.Mentorado;

@WebServlet("/perfil/mentorado")
public class PerfilMentoradoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MentoradoController mentoradoController =
            new MentoradoController();
    private final AreaAtuacaoController areaController =
            new AreaAtuacaoController();
    private final InteresseEmController interesseController =
            new InteresseEmController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Mentorado logado = mentoradoLogado(request, response);
        if (logado == null) {
            return;
        }

        Mentorado atualizado = mentoradoController.buscarPorCpf(
                logado.getCpfMentorado());
        if (atualizado == null) {
            sair(request, response);
            return;
        }

        request.getSession().setAttribute("usuarioLogado", atualizado);
        request.setAttribute("mentorado", atualizado);
        request.setAttribute("areas", areaController.listarAreas());
        request.setAttribute("interesses",
                interesseController.buscarPorMentorado(
                        atualizado.getCpfMentorado()));
        request.getRequestDispatcher("/pages/meuPerfilMentorado.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        Mentorado logado = mentoradoLogado(request, response);
        if (logado == null) {
            return;
        }

        Mentorado mentorado = mentoradoController.buscarPorCpf(
                logado.getCpfMentorado());
        if (mentorado == null) {
            sair(request, response);
            return;
        }

        String acao = request.getParameter("acao");
        boolean sucesso = switch (acao == null ? "" : acao) {
            case "salvar" -> salvarDados(request, mentorado);
            case "senha" -> alterarSenha(request, mentorado);
            case "adicionarInteresse" ->
                    adicionarInteresse(request, mentorado);
            case "removerInteresse" ->
                    removerInteresse(request, mentorado);
            default -> false;
        };

        if (sucesso && ("salvar".equals(acao) || "senha".equals(acao))) {
            request.getSession().setAttribute("usuarioLogado",
                    mentoradoController.buscarPorCpf(
                            mentorado.getCpfMentorado()));
        }

        response.sendRedirect(request.getContextPath()
                + "/perfil/mentorado?resultado="
                + (sucesso ? "sucesso" : "erro"));
    }

    private boolean salvarDados(HttpServletRequest request,
                                Mentorado mentorado) {
        String nome = limpar(request.getParameter("nome"));
        String email = limpar(request.getParameter("email"));
        String telefone = limpar(request.getParameter("telefone"));
        String formacao = limpar(request.getParameter("formacao"));
        String objetivos = limpar(request.getParameter("objetivos"));
        String duvidas = limpar(request.getParameter("duvidas"));
        String expectativas = limpar(request.getParameter("expectativas"));
        String cidade = limpar(request.getParameter("cidade"));
        String estado = limpar(request.getParameter("estado")).toUpperCase();

        if (vazio(nome) || vazio(email) || !email.contains("@")
                || vazio(telefone) || vazio(formacao) || vazio(objetivos)
                || vazio(duvidas) || vazio(expectativas) || vazio(cidade)
                || estado.length() != 2) {
            return false;
        }

        mentorado.setNome(nome);
        mentorado.setEmail(email);
        mentorado.setTelefone(telefone);
        mentorado.setFormacao(formacao);
        mentorado.setObjetivosProfissionais(objetivos);
        mentorado.setPrincipaisDuvidas(duvidas);
        mentorado.setExpectativas(expectativas);
        mentorado.setCidade(cidade);
        mentorado.setEstado(estado);
        return mentoradoController.atualizarMentorado(mentorado);
    }

    private boolean alterarSenha(HttpServletRequest request,
                                 Mentorado mentorado) {
        String nova = request.getParameter("novaSenha");
        String confirmacao = request.getParameter("confirmarSenha");
        if (nova == null || nova.length() < 6 || !nova.equals(confirmacao)) {
            return false;
        }
        mentorado.setSenha(nova);
        return mentoradoController.atualizarMentorado(mentorado);
    }

    private boolean adicionarInteresse(HttpServletRequest request,
                                       Mentorado mentorado) {
        int idArea = inteiro(request.getParameter("idArea"));
        AreaAtuacao area = areaController.buscarPorId(idArea);
        if (area == null) {
            return false;
        }

        InteresseEm interesse = new InteresseEm();
        interesse.setIdAreaAtuacao(idArea);
        interesse.setCpfMentorado(mentorado.getCpfMentorado());
        interesse.setAreaInteresse(area.getNomeArea());
        interesse.setNivelExperiencia(limpar(
                request.getParameter("nivelExperiencia")));
        return interesseController.cadastrarInteresse(interesse);
    }

    private boolean removerInteresse(HttpServletRequest request,
                                     Mentorado mentorado) {
        int idArea = inteiro(request.getParameter("idArea"));
        return interesseController.excluirInteresse(
                idArea, mentorado.getCpfMentorado());
    }

    private Mentorado mentoradoLogado(HttpServletRequest request,
                                      HttpServletResponse response)
            throws IOException {
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null
                ? null : sessao.getAttribute("usuarioLogado");
        if (usuario instanceof Mentorado mentorado) {
            return mentorado;
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
