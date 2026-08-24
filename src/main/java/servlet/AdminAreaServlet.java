package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.AreaAtuacaoController;
import model.Admin;
import model.AreaAtuacao;

@WebServlet("/admin/areas")
public class AdminAreaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final AreaAtuacaoController areaController =
            new AreaAtuacaoController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession sessao = request.getSession(false);
        Object usuario = sessao == null
                ? null : sessao.getAttribute("usuarioLogado");

        if (!(usuario instanceof Admin admin)) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }
        if ("MODERADOR".equalsIgnoreCase(admin.getNivelAcesso())) {
            response.sendRedirect(request.getContextPath()
                    + "/pages/configuracoesAdmin.jsp?resultado="
                    + "sem_permissao_area#tabAreas");
            return;
        }

        String acao = request.getParameter("acao");
        String resultado = "erro";

        if ("cadastrar".equals(acao)) {
            String nome = limpar(request.getParameter("nomeArea"));
            AreaAtuacao area = new AreaAtuacao();
            area.setNomeArea(nome);
            resultado = areaController.cadastrarArea(area)
                    ? "area_cadastrada" : "area_duplicada";
        } else if ("excluir".equals(acao)) {
            int id = inteiro(request.getParameter("idArea"));
            if (areaController.areaEmUso(id)) {
                resultado = "area_em_uso";
            } else if (areaController.excluirArea(id)) {
                resultado = "area_excluida";
            }
        }

        response.sendRedirect(request.getContextPath()
                + "/pages/configuracoesAdmin.jsp?resultado="
                + resultado + "#tabAreas");
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
