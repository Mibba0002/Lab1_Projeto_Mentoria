<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,java.util.Map,java.time.format.DateTimeFormatter,model.*,util.HtmlUtil" %>
<%
Object usuario = request.getAttribute("usuario");
boolean ehMentor = usuario instanceof Mentor;
String nomeUsuario = ehMentor ? ((Mentor) usuario).getNome() : ((Mentorado) usuario).getNome();
ArrayList<Avaliacao> avaliacoes = (ArrayList<Avaliacao>) request.getAttribute("avaliacoes");
String c = request.getContextPath();
DateTimeFormatter dataBr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= ehMentor ? "Feedbacks Recebidos" : "Avaliações" %> - Miracle Mentorias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <% if (ehMentor) { %>
        <a href="<%= c %>/mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a href="<%= c %>/mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <% } else { %>
        <a href="<%= c %>/mentores"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a href="<%= c %>/mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <% } %>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a class="active" href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> <%= ehMentor ? "Feedbacks Recebidos" : "Feedbacks" %></a>
        <a href="<%= c %>/perfil/<%= ehMentor ? "mentor" : "mentorado" %>"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-star-fill"></i></span>
                <div><h2><%= ehMentor ? "Feedbacks Recebidos" : "Avaliações" %></h2>
                <p class="text-muted"><%= ehMentor ? "Confira as avaliações das mentorias finalizadas." : "Avalie seus mentores após a conclusão da mentoria." %></p></div>
            </div>
            <div class="user user-role"><i class="bi bi-person-circle"></i><span><%= HtmlUtil.escapar(nomeUsuario) %></span><span class="role-pill"><%= ehMentor ? "Mentor" : "Mentorado" %></span></div>
        </div>

        <% if ("sucesso".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-success soft-alert">Avaliação enviada com sucesso.</div>
        <% } else if ("erro".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-danger soft-alert">Não foi possível avaliar. Confira se a mentoria foi finalizada e ainda não foi avaliada.</div>
        <% } %>

        <% if (ehMentor) {
            double media = (Double) request.getAttribute("media");
            int total = (Integer) request.getAttribute("total");
        %>
        <div class="row g-3 mb-3">
            <div class="col-md-4"><section class="box text-center h-100"><small class="text-muted">Média geral</small><h1 class="text-success my-2"><%= String.format(java.util.Locale.US, "%.1f", media) %></h1><div class="rating-stars"><% for (int i=1;i<=5;i++) { %><i class="bi <%= i <= Math.round(media) ? "bi-star-fill" : "bi-star" %>"></i><% } %></div></section></div>
            <div class="col-md-8"><section class="box h-100 d-flex flex-column justify-content-center"><h5><%= total %> avaliação(ões)</h5><p class="text-muted mb-0">A média é calculada automaticamente com todas as mentorias avaliadas.</p></section></div>
        </div>
        <% } else {
            ArrayList<Mentoria> mentorias = (ArrayList<Mentoria>) request.getAttribute("mentorias");
            Map<Integer,Mentor> mentores = (Map<Integer,Mentor>) request.getAttribute("mentores");
            Map<Integer,Boolean> avaliadas = (Map<Integer,Boolean>) request.getAttribute("avaliadas");
            int selecionada = (Integer) request.getAttribute("mentoriaSelecionada");
            boolean temPendente = false;
            for (Mentoria m : mentorias) if (!Boolean.TRUE.equals(avaliadas.get(m.getIdMentoria()))) temPendente = true;
        %>
        <section class="box mb-3">
            <div class="section-title"><div><h5>Mentorias aguardando avaliação</h5><small>Cada mentoria pode receber uma única avaliação.</small></div></div>
            <% if (!temPendente) { %>
            <div class="empty-state py-4"><i class="bi bi-check-circle"></i><p class="mb-0">Nenhuma mentoria aguardando avaliação.</p></div>
            <% } else { %>
            <div class="row g-3">
            <% for (Mentoria m : mentorias) { if (Boolean.TRUE.equals(avaliadas.get(m.getIdMentoria()))) continue; Mentor item = mentores.get(m.getIdMentoria()); %>
                <div class="col-lg-6"><div class="evaluation-card <%= selecionada == m.getIdMentoria() ? "evaluation-highlight" : "" %>">
                    <div><small class="text-muted">Mentoria #<%= m.getIdMentoria() %></small><h6 class="mb-1"><%= item == null ? "Mentor" : HtmlUtil.escapar(item.getNome()) %></h6><small>Finalizada em <%= m.getDataFim() == null ? "data não informada" : m.getDataFim().format(dataBr) %></small></div>
                    <button class="btn btn-success btn-sm" data-bs-toggle="modal" data-bs-target="#modalAvaliar" data-id="<%= m.getIdMentoria() %>" data-nome="<%= item == null ? "Mentor" : HtmlUtil.escapar(item.getNome()) %>"><i class="bi bi-star"></i> Avaliar</button>
                </div></div>
            <% } %>
            </div>
            <% } %>
        </section>
        <% } %>

        <section class="box">
            <div class="section-title"><div><h5><%= ehMentor ? "Depoimentos" : "Avaliações enviadas" %></h5><small>Histórico registrado no banco de dados.</small></div></div>
            <% if (avaliacoes.isEmpty()) { %>
            <div class="empty-state py-4"><i class="bi bi-chat-square-heart"></i><p class="mb-0">Ainda não existem avaliações.</p></div>
            <% } else { %>
            <div class="row g-3">
                <% for (Avaliacao a : avaliacoes) { %>
                <div class="col-md-6"><article class="review-card">
                    <div class="d-flex justify-content-between gap-2"><div><strong><%= HtmlUtil.escapar(ehMentor ? a.getNomeMentorado() : a.getNomeMentor()) %></strong><small class="d-block text-muted">Mentoria #<%= a.getIdMentoria() %> · <%= a.getDataAvaliacao().toLocalDate().format(dataBr) %></small></div><span class="rating-stars"><% for (int i=1;i<=5;i++) { %><i class="bi <%= i <= a.getNota() ? "bi-star-fill" : "bi-star" %>"></i><% } %></span></div>
                    <p class="mb-0 mt-3"><%= a.getComentario() == null || a.getComentario().isBlank() ? "Sem comentário." : HtmlUtil.escapar(a.getComentario()) %></p>
                </article></div>
                <% } %>
            </div>
            <% } %>
        </section>
    </main>

    <% if (!ehMentor) { %>
    <div class="modal fade" id="modalAvaliar" tabindex="-1" aria-hidden="true"><div class="modal-dialog modal-dialog-centered"><div class="modal-content">
        <div class="modal-header"><div><h5 class="modal-title text-success">Avaliar mentor</h5><small class="text-muted" id="nomeMentorAvaliacao"></small></div><button class="btn-close" data-bs-dismiss="modal"></button></div>
        <form method="post" action="<%= c %>/avaliacoes"><div class="modal-body"><input type="hidden" name="idMentoria" id="idMentoriaAvaliacao">
            <label class="form-label fw-semibold">Nota</label><select class="form-select mb-3" name="nota" required><option value="">Selecione</option><option value="5">5 - Excelente</option><option value="4">4 - Muito bom</option><option value="3">3 - Bom</option><option value="2">2 - Regular</option><option value="1">1 - Ruim</option></select>
            <label class="form-label fw-semibold">Comentário</label><textarea class="form-control" name="comentario" maxlength="2000" rows="5" placeholder="Conte como foi sua experiência..."></textarea>
        </div><div class="modal-footer"><button type="button" class="btn btn-outline-success" data-bs-dismiss="modal">Cancelar</button><button class="btn btn-success"><i class="bi bi-send-check"></i> Enviar avaliação</button></div></form>
    </div></div></div>
    <% } %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    document.querySelectorAll('[data-bs-target="#modalAvaliar"]').forEach(function (botao) {
        botao.addEventListener('click', function () {
            document.getElementById('idMentoriaAvaliacao').value = botao.dataset.id;
            document.getElementById('nomeMentorAvaliacao').textContent = botao.dataset.nome + ' · Mentoria #' + botao.dataset.id;
        });
    });
    </script>
</body>
</html>
