package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AvaliacaoController;
import controller.MentorController;
import controller.MentoriaController;
import model.Avaliacao;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/avaliacoes")
public class AvaliacaoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AvaliacaoController avaliacaoController =
            new AvaliacaoController();
    private final MentoriaController mentoriaController =
            new MentoriaController();
    private final MentorController mentorController = new MentorController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Object usuario = usuario(request);
        if (usuario instanceof Mentor mentor) {
            ArrayList<Avaliacao> avaliacoes = avaliacaoController
                    .listarPorMentor(mentor.getCpfMentor());
            request.setAttribute("usuario", mentor);
            request.setAttribute("avaliacoes", avaliacoes);
            request.setAttribute("media", avaliacaoController
                    .mediaPorMentor(mentor.getCpfMentor()));
            request.setAttribute("total", avaliacoes.size());
        } else if (usuario instanceof Mentorado mentorado) {
            ArrayList<Mentoria> finalizadas = mentoriaController
                    .buscarPorMentorado(mentorado.getCpfMentorado());
            finalizadas.removeIf(m -> !"Finalizada"
                    .equalsIgnoreCase(m.getStatus()));
            Map<Integer, Mentor> mentores = new LinkedHashMap<>();
            Map<Integer, Boolean> avaliadas = new LinkedHashMap<>();
            for (Mentoria mentoria : finalizadas) {
                mentores.put(mentoria.getIdMentoria(), mentorController
                        .buscarPorCpf(mentoria.getCpfMentor()));
                avaliadas.put(mentoria.getIdMentoria(), avaliacaoController
                        .existePorMentoria(mentoria.getIdMentoria()));
            }
            request.setAttribute("usuario", mentorado);
            request.setAttribute("mentorias", finalizadas);
            request.setAttribute("mentores", mentores);
            request.setAttribute("avaliadas", avaliadas);
            request.setAttribute("avaliacoes", avaliacaoController
                    .listarPorMentorado(mentorado.getCpfMentorado()));
            request.setAttribute("mentoriaSelecionada",
                    inteiro(request.getParameter("mentoria")));
        } else {
            login(request, response);
            return;
        }
        request.getRequestDispatcher("/pages/avaliacoes.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Object usuario = usuario(request);
        if (!(usuario instanceof Mentorado mentorado)) {
            login(request, response);
            return;
        }
        boolean sucesso = avaliacaoController.avaliar(
                inteiro(request.getParameter("idMentoria")),
                mentorado.getCpfMentorado(),
                inteiro(request.getParameter("nota")),
                request.getParameter("comentario"));
        response.sendRedirect(request.getContextPath()
                + "/avaliacoes?resultado="
                + (sucesso ? "sucesso" : "erro"));
    }

    private Object usuario(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : sessao.getAttribute("usuarioLogado");
    }

    private int inteiro(String valor) {
        try { return Integer.parseInt(valor); }
        catch (Exception e) { return 0; }
    }

    private void login(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath()
                + "/pages/login.html?erro=credenciais");
    }
}
