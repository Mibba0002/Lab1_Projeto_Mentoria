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

import controller.MentorController;
import controller.MentoradoController;
import controller.MentoriaController;
import dao.DiarioBordoDao;
import model.DiarioBordo;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/diario")
public class DiarioBordoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MentoriaController mentoriaController = new MentoriaController();
    private final MentorController mentorController = new MentorController();
    private final MentoradoController mentoradoController = new MentoradoController();
    private final DiarioBordoDao diarioDao = new DiarioBordoDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Object usuario = usuario(request);
        if (!(usuario instanceof Mentor) && !(usuario instanceof Mentorado)) {
            login(request, response);
            return;
        }
        ArrayList<Mentoria> mentorias = mentoriasCompartilhadas(usuario);
        int selecionada = inteiro(request.getParameter("mentoria"));
        if (selecionada == 0 && !mentorias.isEmpty()) {
            selecionada = mentorias.get(0).getIdMentoria();
        }
        if (!pertence(selecionada, mentorias)) selecionada = 0;

        Map<Integer, String> participantes = new LinkedHashMap<>();
        for (Mentoria mentoria : mentorias) {
            participantes.put(mentoria.getIdMentoria(),
                    contraparte(mentoria, usuario));
        }
        ArrayList<DiarioBordo> registros = selecionada == 0
                ? new ArrayList<>() : diarioDao.buscarPorMentoria(selecionada);
        Map<Integer, String> autores = new LinkedHashMap<>();
        for (DiarioBordo registro : registros) {
            autores.put(registro.getIdDiario(), nomeAutor(registro));
        }
        request.setAttribute("usuario", usuario);
        request.setAttribute("mentorias", mentorias);
        request.setAttribute("mentoriaSelecionada", selecionada);
        request.setAttribute("participantes", participantes);
        request.setAttribute("registros", registros);
        request.setAttribute("autores", autores);
        request.getRequestDispatcher("/pages/diarioBordo.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Object usuario = usuario(request);
        ArrayList<Mentoria> mentorias = mentoriasCompartilhadas(usuario);
        int idMentoria = inteiro(request.getParameter("idMentoria"));
        String conteudo = limpar(request.getParameter("conteudo"));
        boolean sucesso = false;
        if (pertence(idMentoria, mentorias)
                && !conteudo.isEmpty() && conteudo.length() <= 5000) {
            DiarioBordo registro = new DiarioBordo();
            registro.setIdMentoria(idMentoria);
            registro.setConteudo(conteudo);
            if (usuario instanceof Mentor mentor) {
                registro.setTipoAutor("Mentor");
                registro.setCpfAutor(mentor.getCpfMentor());
            } else if (usuario instanceof Mentorado mentorado) {
                registro.setTipoAutor("Mentorado");
                registro.setCpfAutor(mentorado.getCpfMentorado());
            }
            sucesso = registro.getCpfAutor() != null
                    && diarioDao.cadastrar(registro);
        }
        response.sendRedirect(request.getContextPath() + "/diario?mentoria="
                + idMentoria + "&resultado=" + (sucesso ? "sucesso" : "erro"));
    }

    private Object usuario(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : sessao.getAttribute("usuarioLogado");
    }
    private ArrayList<Mentoria> mentoriasCompartilhadas(Object usuario) {
        ArrayList<Mentoria> lista = usuario instanceof Mentor mentor
                ? mentoriaController.buscarPorMentor(mentor.getCpfMentor())
                : usuario instanceof Mentorado mentorado
                    ? mentoriaController.buscarPorMentorado(mentorado.getCpfMentorado())
                    : new ArrayList<>();
        lista.removeIf(m -> !("Ativa".equalsIgnoreCase(m.getStatus())
                || "Finalizada".equalsIgnoreCase(m.getStatus())));
        return lista;
    }
    private boolean pertence(int id, ArrayList<Mentoria> mentorias) {
        return id > 0 && mentorias.stream()
                .anyMatch(m -> m.getIdMentoria() == id);
    }
    private String contraparte(Mentoria m, Object usuario) {
        if (usuario instanceof Mentor) {
            Mentorado item = mentoradoController.buscarPorCpf(m.getCpfMentorado());
            return item == null ? "Mentorado" : item.getNome();
        }
        Mentor item = mentorController.buscarPorCpf(m.getCpfMentor());
        return item == null ? "Mentor" : item.getNome();
    }
    private String nomeAutor(DiarioBordo registro) {
        if ("Mentor".equals(registro.getTipoAutor())) {
            Mentor item = mentorController.buscarPorCpf(registro.getCpfAutor());
            return item == null ? "Mentor" : item.getNome();
        }
        Mentorado item = mentoradoController.buscarPorCpf(registro.getCpfAutor());
        return item == null ? "Mentorado" : item.getNome();
    }
    private int inteiro(String valor) {
        try { return Integer.parseInt(valor); } catch (Exception e) { return 0; }
    }
    private String limpar(String valor) { return valor == null ? "" : valor.trim(); }
    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect(request.getContextPath()
                + "/pages/login.html?erro=credenciais");
    }
}
