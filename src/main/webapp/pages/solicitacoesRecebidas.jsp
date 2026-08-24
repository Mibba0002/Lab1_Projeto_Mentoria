<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.EspecializacaoEm" %>
<%@ page import="model.Mentor" %>
<%@ page import="model.Mentorado" %>
<%@ page import="model.Mentoria" %>
<%@ page import="util.HtmlUtil" %>
<%
Mentor mentor = (Mentor) request.getAttribute("mentor");
ArrayList<Mentoria> mentorias = (ArrayList<Mentoria>) request.getAttribute("mentorias");
Map<Integer, Mentorado> mentorados = (Map<Integer, Mentorado>) request.getAttribute("mentoradosPorMentoria");
Map<Integer, EspecializacaoEm> especializacoes = (Map<Integer, EspecializacaoEm>) request.getAttribute("especializacoesPorMentoria");
String resultado = request.getParameter("resultado");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitações Recebidas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="../assets/css/global.css">
    <link rel="stylesheet" href="../assets/css/solicitacoesRecebidas.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="../dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="../mentorias/solicitacoes" class="active"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a href="../mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <a href="../diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="../agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="../avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks Recebidos</a>
        <a href="../perfil/mentor"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="../logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>
    <main class="content">
        <div class="header">
            <div><h2>Solicitações Recebidas</h2><p class="text-muted">Solicitações enviadas pelos mentorados.</p></div>
            <div class="user"><i class="bi bi-person-circle"></i> <%= HtmlUtil.escapar(mentor.getNome()) %></div>
        </div>
        <% if ("sucesso".equals(resultado)) { %><div class="alert alert-success">Solicitação respondida.</div><% } %>
        <% if ("erro".equals(resultado)) { %><div class="alert alert-danger">A solicitação já foi respondida ou não pôde ser alterada.</div><% } %>

        <div class="box">
            <% boolean encontrou = false; %>
            <% for (Mentoria mentoria : mentorias) {
                if (!"Pendente".equals(mentoria.getStatus())) continue;
                encontrou = true;
                Mentorado itemMentorado = mentorados.get(mentoria.getIdMentoria());
                EspecializacaoEm especializacao = especializacoes.get(mentoria.getIdMentoria());
            %>
            <div class="card mb-3 border-0 shadow-sm">
                <div class="card-body">
                    <div class="d-flex justify-content-between flex-wrap gap-3">
                        <div>
                            <h5><% if (itemMentorado == null) { %>Mentorado<% } else { %><a href="../mentorado/perfil?cpf=<%= itemMentorado.getCpfMentorado() %>"><%= HtmlUtil.escapar(itemMentorado.getNome()) %></a><% } %></h5>
                            <% if (itemMentorado != null) { %><p class="text-muted mb-1"><%= HtmlUtil.escapar(itemMentorado.getFormacao()) %> — <%= HtmlUtil.escapar(itemMentorado.getCidade()) %>/<%= HtmlUtil.escapar(itemMentorado.getEstado()) %></p><% } %>
                            <span class="badge bg-success"><%= especializacao == null ? "Área não encontrada" : HtmlUtil.escapar(especializacao.getEspecializacao()) %></span>
                            <p class="mt-3 mb-0" style="white-space:pre-line"><%= HtmlUtil.escapar(mentoria.getObjetivosDefinidos()) %></p>
                        </div>
                        <div class="d-flex gap-2 align-items-start">
                            <form action="../mentorias/solicitacoes" method="post">
                                <input type="hidden" name="idMentoria" value="<%= mentoria.getIdMentoria() %>">
                                <input type="hidden" name="acao" value="aceitar">
                                <button class="btn btn-success">Aceitar</button>
                            </form>
                            <form action="../mentorias/solicitacoes" method="post">
                                <input type="hidden" name="idMentoria" value="<%= mentoria.getIdMentoria() %>">
                                <input type="hidden" name="acao" value="recusar">
                                <button class="btn btn-outline-danger">Recusar</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <% } %>
            <% if (!encontrou) { %><div class="alert alert-info mb-0">Nenhuma solicitação pendente.</div><% } %>
        </div>
    </main>
</body>
</html>
