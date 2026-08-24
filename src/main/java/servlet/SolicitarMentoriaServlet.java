package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.EspecializacaoEmController;
import controller.MentorController;
import controller.MentoriaController;
import dao.MentoriaDao;
import model.EspecializacaoEm;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/mentorias/solicitar")
public class SolicitarMentoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MentorController mentorController = new MentorController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();
    private final MentoriaController mentoriaController =
            new MentoriaController();
    private final MentoriaDao mentoriaDao = new MentoriaDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Mentorado mentorado = mentoradoLogado(request, response);
        if (mentorado == null) {
            return;
        }

        Mentor mentor = mentorController.buscarPorCpf(
                request.getParameter("mentor"));
        if (mentor == null || !"Ativo".equalsIgnoreCase(mentor.getStatus())) {
            response.sendRedirect(request.getContextPath() + "/mentores");
            return;
        }

        request.setAttribute("mentorado", mentorado);
        request.setAttribute("mentor", mentor);
        request.setAttribute("especializacoes",
                especializacaoController.buscarPorMentor(
                        mentor.getCpfMentor()));
        request.getRequestDispatcher("/pages/solicitarMentoria.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Mentorado mentorado = mentoradoLogado(request, response);
        if (mentorado == null) {
            return;
        }

        String cpfMentor = request.getParameter("cpfMentor");
        Mentor mentor = mentorController.buscarPorCpf(cpfMentor);
        int idEspecializacao = inteiro(
                request.getParameter("idEspecializacao"));
        EspecializacaoEm especializacao =
                especializacaoController.buscarPorId(idEspecializacao);

        boolean dadosValidos = mentor != null
                && "Ativo".equalsIgnoreCase(mentor.getStatus())
                && especializacao != null
                && cpfMentor.equals(especializacao.getCpfMentor());

        if (!dadosValidos) {
            response.sendRedirect(request.getContextPath()
                    + "/mentores?resultado=erro");
            return;
        }

        if (mentoriaDao.existePendente(
                cpfMentor, mentorado.getCpfMentorado())) {
            response.sendRedirect(request.getContextPath()
                    + "/mentorias/minhas?resultado=duplicada");
            return;
        }

        Mentoria mentoria = new Mentoria();
        mentoria.setCpfMentor(cpfMentor);
        mentoria.setCpfMentorado(mentorado.getCpfMentorado());
        mentoria.setIdEspecializacao(idEspecializacao);
        mentoria.setStatus("Pendente");
        mentoria.setDataInicio(LocalDate.now());
        mentoria.setObjetivosDefinidos(montarObjetivos(request));
        mentoria.setDepoimentos(null);

        boolean sucesso = mentoriaController.cadastrarMentoria(mentoria);
        response.sendRedirect(request.getContextPath()
                + "/mentorias/minhas?resultado="
                + (sucesso ? "solicitada" : "erro"));
    }

    private String montarObjetivos(HttpServletRequest request) {
        return "Objetivos: " + limpar(request.getParameter("objetivos"))
                + "\nExpectativas: "
                + limpar(request.getParameter("expectativas"))
                + "\nNível: "
                + limpar(request.getParameter("nivelExperiencia"))
                + "\nFormato preferido: "
                + limpar(request.getParameter("formatoPreferido"));
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

    private String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private int inteiro(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
