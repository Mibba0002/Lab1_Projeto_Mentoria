package servlet;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AdminController;
import model.Admin;

@WebServlet("/admin/administradores")
public class AdminAdministradoresServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Set<String> NIVEIS = Set.of(
            "SUPER_ADMIN", "ADMIN", "MODERADOR");
    private final AdminController adminController = new AdminController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null ? null
                : sessao.getAttribute("usuarioLogado");
        if (!(usuario instanceof Admin atual)
                || !"SUPER_ADMIN".equalsIgnoreCase(atual.getNivelAcesso())) {
            redirecionar(request, response, "sem_permissao");
            return;
        }

        String acao = limpar(request.getParameter("acao"));
        String resultado = "erro_admin";
        if ("cadastrar".equals(acao)) {
            String nivel = limpar(request.getParameter("nivelAcesso"))
                    .toUpperCase();
            String senha = limpar(request.getParameter("senha"));
            if (NIVEIS.contains(nivel) && senha.length() >= 6) {
                Admin novo = new Admin();
                novo.setNome(limpar(request.getParameter("nome")));
                novo.setEmail(limpar(request.getParameter("email")));
                novo.setSenha(senha);
                novo.setStatus("Ativo");
                novo.setNivelAcesso(nivel);
                resultado = adminController.cadastrarAdmin(novo)
                        ? "admin_cadastrado" : "admin_duplicado";
            }
        } else if ("excluir".equals(acao)) {
            int id = inteiro(request.getParameter("idAdmin"));
            Admin alvo = adminController.buscarPorId(id);
            boolean ultimoSuper = alvo != null
                    && "SUPER_ADMIN".equalsIgnoreCase(alvo.getNivelAcesso())
                    && adminController.contarPorNivel("SUPER_ADMIN") <= 1;
            if (id == atual.getIdAdmin()) {
                resultado = "proprio_admin";
            } else if (ultimoSuper) {
                resultado = "ultimo_super";
            } else if (alvo != null && adminController.excluirAdmin(id)) {
                resultado = "admin_excluido";
            }
        }
        redirecionar(request, response, resultado);
    }

    private void redirecionar(HttpServletRequest request,
                              HttpServletResponse response,
                              String resultado) throws IOException {
        response.sendRedirect(request.getContextPath()
                + "/pages/configuracoesAdmin.jsp?resultado="
                + resultado + "#tabAdmins");
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private int inteiro(String valor) {
        try { return Integer.parseInt(valor); }
        catch (Exception e) { return 0; }
    }
}
