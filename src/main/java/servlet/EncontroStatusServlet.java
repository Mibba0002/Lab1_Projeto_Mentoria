package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.EncontroController;
import model.Mentor;

@WebServlet("/agenda/status")
public class EncontroStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final EncontroController encontroController =
            new EncontroController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null ? null
                : sessao.getAttribute("usuarioLogado");
        if (!(usuario instanceof Mentor mentor)) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }

        int idEncontro = inteiro(request.getParameter("idEncontro"));
        int idMentoria = inteiro(request.getParameter("idMentoria"));
        boolean sucesso = encontroController.registrarResultado(
                idEncontro,
                mentor.getCpfMentor(),
                request.getParameter("status"),
                request.getParameter("motivo"));

        response.sendRedirect(request.getContextPath()
                + "/agenda?mentoria=" + idMentoria
                + "&resultado="
                + (sucesso ? "status_sucesso" : "status_erro"));
    }

    private int inteiro(String valor) {
        try { return Integer.parseInt(valor); }
        catch (Exception e) { return 0; }
    }
}
