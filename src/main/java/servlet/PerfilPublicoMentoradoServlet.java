package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.InteresseEmController;
import controller.MentoradoController;
import controller.MentoriaController;
import model.Mentor;
import model.Mentorado;

@WebServlet("/mentorado/perfil")
public class PerfilPublicoMentoradoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MentoradoController mentoradoController = new MentoradoController();
    private final MentoriaController mentoriaController = new MentoriaController();
    private final InteresseEmController interesseController =
            new InteresseEmController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null ? null : sessao.getAttribute("usuarioLogado");
        if (!(usuario instanceof Mentor mentor)) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }
        String cpf = request.getParameter("cpf");
        boolean relacionado = mentoriaController.buscarPorMentor(
                mentor.getCpfMentor()).stream()
                .anyMatch(m -> cpf != null && cpf.equals(m.getCpfMentorado()));
        Mentorado mentorado = relacionado
                ? mentoradoController.buscarPorCpf(cpf) : null;
        if (mentorado == null) {
            response.sendRedirect(request.getContextPath()
                    + "/mentorias/solicitacoes");
            return;
        }
        request.setAttribute("mentor", mentor);
        request.setAttribute("mentorado", mentorado);
        request.setAttribute("interesses", interesseController
                .buscarPorMentorado(mentorado.getCpfMentorado()));
        request.getRequestDispatcher("/pages/perfilMentorado.jsp")
                .forward(request, response);
    }
}
