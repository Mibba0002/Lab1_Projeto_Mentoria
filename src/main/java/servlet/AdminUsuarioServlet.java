package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AdminController;
import model.Admin;

@WebServlet("/admin/usuarios")
public class AdminUsuarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminController adminController = new AdminController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null
                ? null
                : sessao.getAttribute("usuarioLogado");

        if (!(usuario instanceof Admin admin)) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/pages/login.html?erro=credenciais");
            return;
        }

        String tipo = request.getParameter("tipo");
        String acao = request.getParameter("acao");
        String cpf = request.getParameter("cpf");
        boolean sucesso = false;

        boolean moderador = "MODERADOR".equalsIgnoreCase(
                admin.getNivelAcesso());
        if (moderador && !("verificar".equals(acao)
                || "bloquear".equals(acao))) {
            redirecionar(request, response, tipo, false);
            return;
        }

        if ("mentor".equals(tipo)) {
            sucesso = executarAcaoMentor(acao, cpf, admin.getIdAdmin());
        } else if ("mentorado".equals(tipo)) {
            sucesso = executarAcaoMentorado(acao, cpf);
        }

        redirecionar(request, response, tipo, sucesso);
    }

    private boolean executarAcaoMentor(
            String acao, String cpf, int idAdmin) {

        return switch (acao == null ? "" : acao) {
            case "verificar" ->
                    adminController.verificarMentor(cpf, idAdmin);
            case "ativar" ->
                    adminController.ativarMentor(cpf);
            case "bloquear" ->
                    adminController.bloquearMentor(cpf);
            default -> false;
        };
    }

    private boolean executarAcaoMentorado(String acao, String cpf) {
        return switch (acao == null ? "" : acao) {
            case "ativar" ->
                    adminController.ativarMentorado(cpf);
            case "bloquear" ->
                    adminController.bloquearMentorado(cpf);
            default -> false;
        };
    }

    private void redirecionar(HttpServletRequest request,
                              HttpServletResponse response,
                              String tipo, boolean sucesso) throws IOException {
        String pagina = "mentor".equals(tipo)
                ? "/pages/gerenciarMentores.jsp"
                : "/pages/gerenciarMentorados.jsp";
        response.sendRedirect(request.getContextPath() + pagina
                + (sucesso ? "?resultado=sucesso" : "?resultado=erro"));
    }
}
