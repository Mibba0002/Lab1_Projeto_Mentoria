package servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.EncontroController;
import controller.MentorController;
import controller.MentoradoController;
import controller.MentoriaController;
import model.Encontro;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/agenda")
public class AgendaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MentoriaController mentoriaController = new MentoriaController();
    private final EncontroController encontroController = new EncontroController();
    private final MentorController mentorController = new MentorController();
    private final MentoradoController mentoradoController = new MentoradoController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Object usuario = usuario(request);
        if (!(usuario instanceof Mentor) && !(usuario instanceof Mentorado)) {
            login(request, response);
            return;
        }
        ArrayList<Mentoria> mentorias = mentoriasDisponiveis(usuario);

        int selecionada = inteiro(request.getParameter("mentoria"));
        if (selecionada > 0 && !pertence(selecionada, mentorias)) selecionada = 0;
        ArrayList<Encontro> encontros = new ArrayList<>();
        Map<Integer, String> participantes = new LinkedHashMap<>();
        for (Mentoria mentoria : mentorias) {
            participantes.put(mentoria.getIdMentoria(), contraparte(mentoria, usuario));
            if (selecionada == 0 || selecionada == mentoria.getIdMentoria()) {
                encontros.addAll(encontroController.buscarPorMentoria(
                        mentoria.getIdMentoria()));
            }
        }
        encontros.sort((a, b) -> {
            int data = a.getData().compareTo(b.getData());
            return data != 0 ? data : a.getHorario().compareTo(b.getHorario());
        });
        request.setAttribute("usuario", usuario);
        request.setAttribute("mentorias", mentorias);
        request.setAttribute("mentoriaSelecionada", selecionada);
        request.setAttribute("encontros", encontros);
        request.setAttribute("participantes", participantes);
        request.getRequestDispatcher("/pages/agenda.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Object usuario = usuario(request);
        if (!(usuario instanceof Mentor mentor)) {
            login(request, response);
            return;
        }
        int idMentoria = inteiro(request.getParameter("idMentoria"));
        Mentoria mentoria = mentoriaController.buscarPorId(idMentoria);
        boolean autorizada = mentoria != null
                && mentor.getCpfMentor().equals(mentoria.getCpfMentor())
                && "Ativa".equalsIgnoreCase(mentoria.getStatus());
        boolean sucesso = false;
        if (autorizada) {
            try {
                LocalDate data = LocalDate.parse(request.getParameter("data"));
                LocalTime horario = LocalTime.parse(request.getParameter("horario"));
                if (LocalDateTime.of(data, horario).isAfter(LocalDateTime.now())) {
                    Encontro encontro = new Encontro();
                    encontro.setIdMentoria(idMentoria);
                    encontro.setData(data);
                    encontro.setHorario(horario);
                    encontro.setTipoEncontro(limpar(request.getParameter("tipo")));
                    encontro.setDescricao(limpar(request.getParameter("descricao")));
                    encontro.setLinkReuniao(limpar(request.getParameter("link")));
                    encontro.setLocalEncontro(limpar(request.getParameter("local")));
                    encontro.setStatus("Agendado");
                    sucesso = encontroController.cadastrarEncontro(encontro);
                }
            } catch (RuntimeException e) {
                sucesso = false;
            }
        }
        response.sendRedirect(request.getContextPath() + "/agenda?mentoria="
                + idMentoria + "&resultado=" + (sucesso ? "sucesso" : "erro"));
    }

    private Object usuario(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : sessao.getAttribute("usuarioLogado");
    }
    private ArrayList<Mentoria> mentoriasDisponiveis(Object usuario) {
        ArrayList<Mentoria> todas = usuario instanceof Mentor mentor
                ? mentoriaController.buscarPorMentor(mentor.getCpfMentor())
                : usuario instanceof Mentorado mentorado
                    ? mentoriaController.buscarPorMentorado(mentorado.getCpfMentorado())
                    : new ArrayList<>();
        todas.removeIf(m -> !("Ativa".equalsIgnoreCase(m.getStatus())
                || "Finalizada".equalsIgnoreCase(m.getStatus())));
        return todas;
    }
    private boolean pertence(int id, ArrayList<Mentoria> mentorias) {
        return mentorias.stream().anyMatch(m -> m.getIdMentoria() == id);
    }
    private String contraparte(Mentoria m, Object usuario) {
        if (usuario instanceof Mentor) {
            Mentorado item = mentoradoController.buscarPorCpf(m.getCpfMentorado());
            return item == null ? "Mentorado" : item.getNome();
        }
        Mentor item = mentorController.buscarPorCpf(m.getCpfMentor());
        return item == null ? "Mentor" : item.getNome();
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
