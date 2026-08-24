package servlet;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Admin;
import model.Mentor;
import model.Mentorado;

@WebFilter("/pages/*")
public class AutenticacaoFilter implements Filter {

    private static final Set<String> PUBLICAS = Set.of(
            "login.html", "escolhaCadastro.html",
            "Cadastrode_Mentor.jsp", "cadastroMentorado.jsp",
            "recuperarSenha.html");

    private static final Set<String> ADMIN = Set.of(
            "dashboardAdmin.html", "gerenciarMentores.jsp",
            "gerenciarMentorados.jsp", "gerenciarMentorias.html",
            "relatoriosAdmin.html", "feedbacksAdmin.html",
            "configuracoesAdmin.jsp", "gerenciarMentores.html",
            "gerenciarMentorados.html");

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        HttpServletResponse resposta = (HttpServletResponse) response;
        String pagina = http.getRequestURI().substring(
                http.getRequestURI().lastIndexOf('/') + 1);

        if (PUBLICAS.contains(pagina)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession sessao = http.getSession(false);
        Object usuario = sessao == null
                ? null : sessao.getAttribute("usuarioLogado");
        if (usuario == null) {
            resposta.sendRedirect(http.getContextPath()
                    + "/pages/login.html?erro=credenciais");
            return;
        }

        if (ADMIN.contains(pagina) && !(usuario instanceof Admin)) {
            redirecionarInicio(http, resposta, usuario);
            return;
        }

        if (!ADMIN.contains(pagina)
                && pagina.contains("Mentorado")
                && !(usuario instanceof Mentorado)
                && !pagina.equals("perfilMentorado.html")) {
            redirecionarInicio(http, resposta, usuario);
            return;
        }

        if ((pagina.equals("dashboardMentor.html")
                || pagina.equals("agendaEncontrosMentor.html")
                || pagina.equals("diarioBordoMentor.html")
                || pagina.equals("feedbacksRecebidos.html")
                || pagina.equals("mentoriasAtivas.html"))
                && !(usuario instanceof Mentor)) {
            redirecionarInicio(http, resposta, usuario);
            return;
        }

        chain.doFilter(request, response);
    }

    private void redirecionarInicio(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object usuario) throws IOException {
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    @Override
    public void destroy() {
    }
}
