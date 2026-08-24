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
import controller.MentoradoController;
import controller.MentoriaController;
import model.EspecializacaoEm;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/mentorias/ativas")
public class MentoriasAtivasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MentoriaController mentoriaController = new MentoriaController();
    private final MentoradoController mentoradoController = new MentoradoController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();

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
        ArrayList<Mentoria> mentorias = mentoriaController.buscarPorMentor(
                mentor.getCpfMentor());
        mentorias.removeIf(m -> !"Ativa".equalsIgnoreCase(m.getStatus()));
        Map<Integer, Mentorado> mentorados = new LinkedHashMap<>();
        Map<Integer, EspecializacaoEm> especializacoes = new LinkedHashMap<>();
        for (Mentoria mentoria : mentorias) {
            mentorados.put(mentoria.getIdMentoria(),
                    mentoradoController.buscarPorCpf(mentoria.getCpfMentorado()));
            especializacoes.put(mentoria.getIdMentoria(),
                    especializacaoController.buscarPorId(
                            mentoria.getIdEspecializacao()));
        }
        request.setAttribute("mentor", mentor);
        request.setAttribute("mentorias", mentorias);
        request.setAttribute("mentorados", mentorados);
        request.setAttribute("especializacoes", especializacoes);
        request.getRequestDispatcher("/pages/mentoriasAtivas.jsp")
                .forward(request, response);
    }
}
