package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AreaAtuacaoController;
import controller.InteresseEmController;
import controller.MentoradoController;
import model.AreaAtuacao;
import model.InteresseEm;
import model.Mentorado;

@WebServlet("/cadastro-mentorado")
public class CadastroMentoradoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MentoradoController mentoradoController =
            new MentoradoController();
    private final AreaAtuacaoController areaController =
            new AreaAtuacaoController();
    private final InteresseEmController interesseController =
            new InteresseEmController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String[] areasSelecionadas = parametros(
                request, "areasInteresse[]", "areasInteresse");
        if (areasSelecionadas == null || areasSelecionadas.length == 0) {
            redirecionar(request, response,
                    "/pages/cadastroMentorado.jsp?erro=dados");
            return;
        }

        String expectativas = request.getParameter("expectativas");
        String duvidas = request.getParameter("duvidas");

        Mentorado mentorado = new Mentorado();
        mentorado.setCpfMentorado(request.getParameter("cpf"));
        mentorado.setNome(request.getParameter("nome"));
        mentorado.setEmail(request.getParameter("email"));
        mentorado.setSenha(request.getParameter("senha"));
        mentorado.setTelefone(request.getParameter("telefone"));
        mentorado.setFormacao(request.getParameter("formacao"));
        mentorado.setObjetivosProfissionais(request.getParameter("objetivos"));
        mentorado.setPrincipaisDuvidas(
                vazio(duvidas) ? expectativas : duvidas);
        mentorado.setExpectativas(expectativas);
        mentorado.setCidade(request.getParameter("cidade"));
        mentorado.setEstado(request.getParameter("estado"));
        mentorado.setStatus("Ativo");

        if (!mentoradoController.cadastrarMentorado(mentorado)) {
            redirecionar(request, response,
                    "/pages/cadastroMentorado.jsp?erro=cadastro");
            return;
        }

        cadastrarInteresses(
                areasSelecionadas,
                mentorado.getCpfMentorado(),
                request.getParameter("nivelExperiencia"));

        redirecionar(request, response,
                "/pages/login.html?cadastro=sucesso");
    }

    private void cadastrarInteresses(String[] idsAreas,
                                     String cpfMentorado,
                                     String nivelExperiencia) {
        if (idsAreas == null) {
            return;
        }

        for (String idArea : idsAreas) {
            AreaAtuacao area = areaController.buscarPorId(
                    inteiroOuZero(idArea));
            if (area == null || area.getIdAreaAtuacao() <= 0) {
                continue;
            }

            InteresseEm interesse = new InteresseEm();
            interesse.setIdAreaAtuacao(area.getIdAreaAtuacao());
            interesse.setCpfMentorado(cpfMentorado);
            interesse.setAreaInteresse(area.getNomeArea());
            interesse.setNivelExperiencia(nivelExperiencia);
            interesseController.cadastrarInteresse(interesse);
        }
    }

    private int inteiroOuZero(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String[] parametros(HttpServletRequest request,
                                String nomePrincipal,
                                String nomeAlternativo) {
        String[] valores = request.getParameterValues(nomePrincipal);
        return valores == null
                ? request.getParameterValues(nomeAlternativo) : valores;
    }

    private boolean vazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private void redirecionar(HttpServletRequest request,
                              HttpServletResponse response,
                              String caminho) throws IOException {
        response.sendRedirect(request.getContextPath() + caminho);
    }
}
