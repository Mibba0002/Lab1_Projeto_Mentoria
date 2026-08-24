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

import controller.AdminController;
import controller.EncontroController;
import controller.MentorController;
import controller.MentoradoController;
import controller.MentoriaController;
import model.Admin;
import model.Encontro;
import model.Mentor;
import model.Mentorado;
import model.Mentoria;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final AdminController adminController = new AdminController();
    private final MentorController mentorController = new MentorController();
    private final MentoradoController mentoradoController =
            new MentoradoController();
    private final MentoriaController mentoriaController =
            new MentoriaController();
    private final EncontroController encontroController =
            new EncontroController();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null ? null
                : sessao.getAttribute("usuarioLogado");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }

        if (usuario instanceof Admin admin) {
            preencherAdmin(request, admin);
        } else if (usuario instanceof Mentor mentor) {
            preencherMentor(request, mentor);
        } else if (usuario instanceof Mentorado mentorado) {
            preencherMentorado(request, mentorado);
        } else {
            response.sendRedirect(request.getContextPath() + "/logout");
            return;
        }
        request.getRequestDispatcher("/pages/dashboard.jsp")
                .forward(request, response);
    }

    private void preencherAdmin(HttpServletRequest request, Admin admin) {
        ArrayList<Mentoria> mentorias = mentoriaController.listarMentorias();
        request.setAttribute("tipo", "admin");
        request.setAttribute("nome", admin.getNome());
        request.setAttribute("rotulo1", "Mentores cadastrados");
        request.setAttribute("valor1", mentorController.listarMentores().size());
        request.setAttribute("rotulo2", "Mentorados cadastrados");
        request.setAttribute("valor2", mentoradoController.listarMentorados().size());
        request.setAttribute("rotulo3", "Mentorias ativas");
        request.setAttribute("valor3", contarStatus(mentorias, "Ativa"));
        request.setAttribute("rotulo4", "Solicitações pendentes");
        request.setAttribute("valor4", contarStatus(mentorias, "Pendente"));
    }

    private void preencherMentor(HttpServletRequest request, Mentor mentor) {
        ArrayList<Mentoria> mentorias = mentoriaController.buscarPorMentor(
                mentor.getCpfMentor());
        request.setAttribute("tipo", "mentor");
        request.setAttribute("nome", mentor.getNome());
        request.setAttribute("rotulo1", "Solicitações pendentes");
        request.setAttribute("valor1", contarStatus(mentorias, "Pendente"));
        request.setAttribute("rotulo2", "Mentorias ativas");
        request.setAttribute("valor2", contarStatus(mentorias, "Ativa"));
        request.setAttribute("rotulo3", "Próximos encontros");
        request.setAttribute("valor3", contarProximosEncontros(mentorias));
        request.setAttribute("rotulo4", "Mentorias finalizadas");
        request.setAttribute("valor4", contarStatus(mentorias, "Finalizada"));
    }

    private void preencherMentorado(HttpServletRequest request,
                                    Mentorado mentorado) {
        ArrayList<Mentoria> mentorias = mentoriaController.buscarPorMentorado(
                mentorado.getCpfMentorado());
        request.setAttribute("tipo", "mentorado");
        request.setAttribute("nome", mentorado.getNome());
        request.setAttribute("rotulo1", "Mentorias ativas");
        request.setAttribute("valor1", contarStatus(mentorias, "Ativa"));
        request.setAttribute("rotulo2", "Solicitações pendentes");
        request.setAttribute("valor2", contarStatus(mentorias, "Pendente"));
        request.setAttribute("rotulo3", "Próximos encontros");
        request.setAttribute("valor3", contarProximosEncontros(mentorias));
        request.setAttribute("rotulo4", "Mentorias finalizadas");
        request.setAttribute("valor4", contarStatus(mentorias, "Finalizada"));
    }

    private int contarStatus(ArrayList<Mentoria> mentorias, String status) {
        int total = 0;
        for (Mentoria mentoria : mentorias) {
            if (status.equalsIgnoreCase(mentoria.getStatus())) total++;
        }
        return total;
    }

    private int contarProximosEncontros(ArrayList<Mentoria> mentorias) {
        int total = 0;
        LocalDate hoje = LocalDate.now();
        for (Mentoria mentoria : mentorias) {
            for (Encontro encontro : encontroController.buscarPorMentoria(
                    mentoria.getIdMentoria())) {
                if (encontro.getData() != null
                        && !encontro.getData().isBefore(hoje)
                        && "Agendado".equalsIgnoreCase(encontro.getStatus())) {
                    total++;
                }
            }
        }
        return total;
    }
}
