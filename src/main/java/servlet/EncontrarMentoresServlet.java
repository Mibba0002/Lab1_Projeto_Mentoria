package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AreaAtuacaoController;
import controller.EspecializacaoEmController;
import dao.MentorDao;
import model.EspecializacaoEm;
import model.Mentor;
import model.Mentorado;

@WebServlet("/mentores")
public class EncontrarMentoresServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final MentorDao mentorDao = new MentorDao();
    private final AreaAtuacaoController areaController =
            new AreaAtuacaoController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();

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

        String nome = limpar(request.getParameter("nome"));
        String formato = limpar(request.getParameter("formato"));
        int idArea = inteiro(request.getParameter("area"));

        ArrayList<Mentor> encontrados = new ArrayList<>();
        Map<String, ArrayList<EspecializacaoEm>> especializacoes =
                new LinkedHashMap<>();

        for (Mentor mentor : mentorDao.listarAtivos()) {
            ArrayList<EspecializacaoEm> areasMentor =
                    especializacaoController.buscarPorMentor(
                            mentor.getCpfMentor());
            especializacoes.put(mentor.getCpfMentor(), areasMentor);

            boolean nomeCombina = nome.isEmpty()
                    || minusculo(mentor.getNome()).contains(minusculo(nome));
            boolean formatoCombina = formato.isEmpty()
                    || formato.equalsIgnoreCase(mentor.getFormatoMentoria());
            boolean areaCombina = idArea <= 0
                    || areasMentor.stream().anyMatch(
                            item -> item.getIdAreaAtuacao() == idArea);

            if (nomeCombina && formatoCombina && areaCombina) {
                encontrados.add(mentor);
            }
        }

        request.setAttribute("mentorado", mentorado);
        request.setAttribute("mentores", encontrados);
        request.setAttribute("especializacoesPorCpf", especializacoes);
        request.setAttribute("areas", areaController.listarAreas());
        request.setAttribute("filtroNome", nome);
        request.setAttribute("filtroFormato", formato);
        request.setAttribute("filtroArea", idArea);
        request.getRequestDispatcher("/pages/encontrarMentores.jsp")
                .forward(request, response);
    }

    private String minusculo(String valor) {
        return valor == null ? "" : valor.toLowerCase(
                Locale.forLanguageTag("pt-BR"));
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
