package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AdminController;
import controller.MentorController;
import controller.MentoradoController;
import model.Admin;
import model.Mentor;
import model.Mentorado;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminController adminController = new AdminController();
    private final MentorController mentorController = new MentorController();
    private final MentoradoController mentoradoController =
            new MentoradoController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        HttpSession sessao = request.getSession(true);

        Admin admin = adminController.login(email, senha);
        if (admin != null) {
            sessao.setAttribute("usuarioLogado", admin);
            sessao.setAttribute("tipoUsuario", "admin");
            redirecionar(request, response, "/dashboard");
            return;
        }

        Mentor mentor = mentorController.login(email, senha);
        if (mentor != null) {
            sessao.setAttribute("usuarioLogado", mentor);
            sessao.setAttribute("tipoUsuario", "mentor");
            redirecionar(request, response, "/dashboard");
            return;
        }

        Mentorado mentorado = mentoradoController.login(email, senha);
        if (mentorado != null) {
            sessao.setAttribute("usuarioLogado", mentorado);
            sessao.setAttribute("tipoUsuario", "mentorado");
            redirecionar(request, response, "/dashboard");
            return;
        }

        redirecionar(request, response,
                "/pages/login.html?erro=credenciais");
    }

    private void redirecionar(HttpServletRequest request,
                              HttpServletResponse response,
                              String caminho) throws IOException {
        response.sendRedirect(request.getContextPath() + caminho);
    }
}
