package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.EspecializacaoEmController;
import controller.MentorController;
import controller.AvaliacaoController;
import model.Mentor;
import model.Mentorado;

@WebServlet("/mentor/perfil")
public class PerfilPublicoMentorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MentorController mentorController = new MentorController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();
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

        Mentor mentor = mentorController.buscarPorCpf(
                request.getParameter("cpf"));
        if (mentor == null || !"Ativo".equalsIgnoreCase(mentor.getStatus())) {
            response.sendRedirect(request.getContextPath()
                    + "/mentores?resultado=indisponivel");
            return;
        }

        request.setAttribute("mentorado", mentorado);
        request.setAttribute("mentor", mentor);
        request.setAttribute("especializacoes",
                especializacaoController.buscarPorMentor(
                        mentor.getCpfMentor()));
        request.setAttribute("avaliacoes",
                avaliacaoController.listarPorMentor(mentor.getCpfMentor()));
        request.setAttribute("mediaAvaliacao",
                avaliacaoController.mediaPorMentor(mentor.getCpfMentor()));
        request.setAttribute("totalAvaliacoes",
                avaliacaoController.contarPorMentor(mentor.getCpfMentor()));
        request.getRequestDispatcher("/pages/perfilMentor.jsp")
                .forward(request, response);
    }
}
