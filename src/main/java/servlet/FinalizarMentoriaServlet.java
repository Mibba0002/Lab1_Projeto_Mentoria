package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.MentoriaController;
import model.Mentor;

@WebServlet("/mentorias/finalizar")
public class FinalizarMentoriaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final MentoriaController mentoriaController = new MentoriaController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null ? null
                : sessao.getAttribute("usuarioLogado");
        if (!(usuario instanceof Mentor mentor)) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }
        int id = inteiro(request.getParameter("idMentoria"));
        boolean sucesso = mentoriaController.finalizarMentoria(
                id, mentor.getCpfMentor());
        response.sendRedirect(request.getContextPath()
                + "/mentorias/ativas?resultado="
                + (sucesso ? "finalizada" : "erro"));
    }

    private int inteiro(String valor) {
        try { return Integer.parseInt(valor); }
        catch (Exception e) { return 0; }
    }
}
