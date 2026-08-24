package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.AreaAtuacaoController;
import controller.EspecializacaoEmController;
import controller.MentorController;
import model.AreaAtuacao;
import model.EspecializacaoEm;
import model.Mentor;

@WebServlet("/cadastro-mentor")
public class CadastroMentorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final MentorController mentorController = new MentorController();
    private final AreaAtuacaoController areaController =
            new AreaAtuacaoController();
    private final EspecializacaoEmController especializacaoController =
            new EspecializacaoEmController();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String[] areasSelecionadas = parametros(
                request, "areas[]", "areas");
        String[] disponibilidades = parametros(
                request, "disponibilidades[]", "disponibilidades");
        if (areasSelecionadas == null || areasSelecionadas.length == 0
                || disponibilidades == null
                || disponibilidades.length == 0) {
            redirecionar(request, response,
                    "/pages/Cadastrode_Mentor.jsp?erro=dados");
            return;
        }

        Mentor mentor = new Mentor();
        mentor.setCpfMentor(request.getParameter("cpf"));
        mentor.setNome(request.getParameter("nome"));
        mentor.setEmail(request.getParameter("email"));
        mentor.setSenha(request.getParameter("senha"));
        mentor.setTelefone(request.getParameter("telefone"));
        mentor.setMiniBiografia(request.getParameter("biografia"));
        mentor.setDisponibilidade(
                juntar(disponibilidades));
        mentor.setFormatoMentoria(request.getParameter("formato"));
        mentor.setLinkPortifolio(valorOuVazio(
                request.getParameter("portfolio")));
        mentor.setRedesProfissionais(valorOuVazio(
                request.getParameter("portfolio")));
        mentor.setStatus("Ativo");
        mentor.setCidade(request.getParameter("cidade"));
        mentor.setEstado(request.getParameter("estado"));
        mentor.setVerificado(false);

        if (!mentorController.cadastrarMentor(mentor)) {
            redirecionar(request, response,
                    "/pages/Cadastrode_Mentor.jsp?erro=cadastro");
            return;
        }

        int tempoExperiencia = inteiroOuZero(
                request.getParameter("tempoExperiencia"));
        cadastrarEspecializacoes(
                areasSelecionadas,
                mentor.getCpfMentor(), tempoExperiencia);

        redirecionar(request, response,
                "/pages/login.html?cadastro=sucesso");
    }

    private void cadastrarEspecializacoes(String[] idsAreas,
                                          String cpfMentor,
                                          int tempoExperiencia) {
        if (idsAreas == null) {
            return;
        }

        for (String idArea : idsAreas) {
            AreaAtuacao area = areaController.buscarPorId(
                    inteiroOuZero(idArea));
            if (area == null || area.getIdAreaAtuacao() <= 0) {
                continue;
            }

            EspecializacaoEm especializacao = new EspecializacaoEm();
            especializacao.setIdAreaAtuacao(area.getIdAreaAtuacao());
            especializacao.setCpfMentor(cpfMentor);
            especializacao.setEspecializacao(area.getNomeArea());
            especializacao.setTempoExperiencia(tempoExperiencia);
            especializacaoController.cadastrarEspecializacao(especializacao);
        }
    }

    private String juntar(String[] valores) {
        return valores == null ? "" : String.join(", ", valores);
    }

    private String valorOuVazio(String valor) {
        return valor == null ? "" : valor.trim();
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

    private void redirecionar(HttpServletRequest request,
                              HttpServletResponse response,
                              String caminho) throws IOException {
        response.sendRedirect(request.getContextPath() + caminho);
    }
}
