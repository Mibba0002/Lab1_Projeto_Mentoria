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

import controller.EspecializacaoEmController;
import controller.EncontroController;
import controller.MentorController;
import controller.MentoriaController;
import controller.AvaliacaoController;
import model.EspecializacaoEm;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/mentorias/minhas")
public class MinhasMentoriasServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MentoriaController mentoriaController =
            new MentoriaController();
    private final MentorController mentorController = new MentorController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();
    private final EncontroController encontroController =
            new EncontroController();
    private final AvaliacaoController avaliacaoController =
            new AvaliacaoController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null
                ? null : sessao.getAttribute("usuarioLogado");
        if (!(usuario instanceof Mentorado mentorado)) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }

        ArrayList<Mentoria> mentorias =
                mentoriaController.buscarPorMentorado(
                        mentorado.getCpfMentorado());
        Map<Integer, Mentor> mentores = new LinkedHashMap<>();
        Map<Integer, EspecializacaoEm> especializacoes =
                new LinkedHashMap<>();
        Map<Integer, Integer> encontrosPorMentoria = new LinkedHashMap<>();
        Map<Integer, Boolean> avaliacoesPorMentoria = new LinkedHashMap<>();

        for (Mentoria mentoria : mentorias) {
            mentores.put(mentoria.getIdMentoria(),
                    mentorController.buscarPorCpf(mentoria.getCpfMentor()));
            especializacoes.put(mentoria.getIdMentoria(),
                    especializacaoController.buscarPorId(
                            mentoria.getIdEspecializacao()));
            encontrosPorMentoria.put(mentoria.getIdMentoria(),
                    encontroController.buscarPorMentoria(
                            mentoria.getIdMentoria()).size());
            avaliacoesPorMentoria.put(mentoria.getIdMentoria(),
                    avaliacaoController.existePorMentoria(
                            mentoria.getIdMentoria()));
        }

        request.setAttribute("mentorado", mentorado);
        request.setAttribute("mentorias", mentorias);
        request.setAttribute("mentoresPorMentoria", mentores);
        request.setAttribute("especializacoesPorMentoria", especializacoes);
        request.setAttribute("encontrosPorMentoria", encontrosPorMentoria);
        request.setAttribute("avaliacoesPorMentoria", avaliacoesPorMentoria);
        request.getRequestDispatcher("/pages/minhasMentorias.jsp")
                .forward(request, response);
    }
}
