<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.MentoradoDao" %>
<%@ page import="model.Admin" %>
<%@ page import="model.Mentorado" %>
<%
    Object usuario = session.getAttribute("usuarioLogado");
    if (!(usuario instanceof Admin)) {
        response.sendRedirect("login.html?erro=credenciais");
        return;
    }
    Admin admin = (Admin) usuario;
    boolean moderador = "MODERADOR".equalsIgnoreCase(admin.getNivelAcesso());
    ArrayList<Mentorado> mentorados = new dao.MentoradoDao().listar();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gerenciar Mentorados - Administrador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="../assets/css/global.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="../dashboard"><i class="bi bi-speedometer2"></i> Dashboard Geral</a>
        <a href="gerenciarMentores.jsp"><i class="bi bi-person-workspace"></i> Gerenciar Mentores</a>
        <a href="gerenciarMentorados.jsp" class="active"><i class="bi bi-mortarboard-fill"></i> Gerenciar Mentorados</a>
        <a href="gerenciarMentorias.html"><i class="bi bi-people-fill"></i> Gerenciar Mentorias</a>
        <a href="relatoriosAdmin.html"><i class="bi bi-bar-chart-fill"></i> Relatórios</a>
        <a href="feedbacksAdmin.html"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="configuracoesAdmin.jsp"><i class="bi bi-gear-fill"></i> Configurações</a>
        <a href="../logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <div class="content">
        <div class="header">
            <div>
                <h2>Gerenciar Mentorados</h2>
                <p class="text-muted">Mentorados cadastrados no banco de dados.</p>
            </div>
            <div class="user">
                <i class="bi bi-person-circle"></i>
                <span><%= admin.getNome() %></span>
                <span class="badge-admin"><%= admin.getNivelAcesso() %></span>
            </div>
        </div>

        <% if ("sucesso".equals(request.getParameter("resultado"))) { %>
            <div class="alert alert-success">Ação realizada com sucesso.</div>
        <% } else if ("erro".equals(request.getParameter("resultado"))) { %>
            <div class="alert alert-danger">Não foi possível realizar a ação.</div>
        <% } %>

        <div class="box">
            <h5 class="mb-3"><%= mentorados.size() %> mentorado(s) cadastrado(s)</h5>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th>Nome</th><th>Email</th><th>Formação</th>
                            <th>Localização</th><th>Status</th><th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                    <% for (Mentorado mentorado : mentorados) { %>
                        <tr>
                            <td><%= mentorado.getNome() %></td>
                            <td><%= mentorado.getEmail() %></td>
                            <td><%= mentorado.getFormacao() %></td>
                            <td><%= mentorado.getCidade() %>/<%= mentorado.getEstado() %></td>
                            <td><span class="badge bg-secondary"><%= mentorado.getStatus() %></span></td>
                            <td class="d-flex gap-1">
                                <% if ("Ativo".equalsIgnoreCase(mentorado.getStatus())) { %>
                                    <form action="../admin/usuarios" method="post">
                                        <input type="hidden" name="tipo" value="mentorado">
                                        <input type="hidden" name="acao" value="bloquear">
                                        <input type="hidden" name="cpf" value="<%= mentorado.getCpfMentorado() %>">
                                        <button class="btn btn-outline-danger btn-sm" type="submit">Bloquear</button>
                                    </form>
                                <% } else if (!moderador && "Bloqueado".equalsIgnoreCase(mentorado.getStatus())) { %>
                                    <form action="../admin/usuarios" method="post">
                                        <input type="hidden" name="tipo" value="mentorado">
                                        <input type="hidden" name="acao" value="ativar">
                                        <input type="hidden" name="cpf" value="<%= mentorado.getCpfMentorado() %>">
                                        <button class="btn btn-outline-success btn-sm" type="submit">Ativar</button>
                                    </form>
                                <% } %>
                            </td>
                        </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
