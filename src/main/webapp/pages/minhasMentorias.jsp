<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,java.util.Map,java.time.format.DateTimeFormatter,model.*,util.HtmlUtil" %>
<%
Mentorado mentorado = (Mentorado) request.getAttribute("mentorado");
ArrayList<Mentoria> mentorias = (ArrayList<Mentoria>) request.getAttribute("mentorias");
Map<Integer, Mentor> mentores = (Map<Integer, Mentor>) request.getAttribute("mentoresPorMentoria");
Map<Integer, EspecializacaoEm> especializacoes = (Map<Integer, EspecializacaoEm>) request.getAttribute("especializacoesPorMentoria");
Map<Integer, Integer> encontrosPorMentoria = (Map<Integer, Integer>) request.getAttribute("encontrosPorMentoria");
Map<Integer, Boolean> avaliacoesPorMentoria = (Map<Integer, Boolean>) request.getAttribute("avaliacoesPorMentoria");
String resultado = request.getParameter("resultado");
String c = request.getContextPath();
DateTimeFormatter dataBr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
int ativas = 0, pendentes = 0, historico = 0;
for (Mentoria m : mentorias) {
    if ("Ativa".equalsIgnoreCase(m.getStatus())) ativas++;
    else if ("Pendente".equalsIgnoreCase(m.getStatus())) pendentes++;
    else historico++;
}
String[] abasId = {"ativas", "pendentes", "historico"};
String[] abasTitulo = {"Em andamento", "Pendentes", "Histórico"};
String[] abasIcone = {"bi-arrow-repeat", "bi-clock-fill", "bi-clock-history"};
int[] abasContagem = {ativas, pendentes, historico};
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Minhas Mentorias - Miracle Mentorias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="<%= c %>/mentores"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a class="active" href="<%= c %>/mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="<%= c %>/perfil/mentorado"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-people-fill"></i></span>
                <div>
                    <h2>Minhas Mentorias</h2>
                    <p class="text-muted">Acompanhe solicitações, encontros e registros de cada mentoria.</p>
                </div>
            </div>
            <div class="user user-role"><i class="bi bi-person-circle"></i><span><%= HtmlUtil.escapar(mentorado.getNome()) %></span><span class="role-pill">Mentorado</span></div>
        </div>

        <% if ("solicitada".equals(resultado)) { %><div class="alert alert-success soft-alert"><i class="bi bi-check-circle-fill me-2"></i>Solicitação enviada ao mentor.</div><% } %>
        <% if ("duplicada".equals(resultado)) { %><div class="alert alert-warning soft-alert"><i class="bi bi-info-circle-fill me-2"></i>Já existe uma solicitação pendente para esse mentor.</div><% } %>
        <% if ("erro".equals(resultado)) { %><div class="alert alert-danger soft-alert"><i class="bi bi-exclamation-triangle-fill me-2"></i>Não foi possível enviar a solicitação.</div><% } %>

        <% if (mentorias.isEmpty()) { %>
        <div class="box empty-state">
            <i class="bi bi-people"></i>
            <h5>Você ainda não solicitou uma mentoria</h5>
            <p>Encontre um mentor compatível com seus objetivos profissionais.</p>
            <a class="btn btn-success" href="<%= c %>/mentores"><i class="bi bi-search"></i> Encontrar mentores</a>
        </div>
        <% } else { %>
        <ul class="nav nav-tabs" id="abasMentorias" role="tablist">
            <% for (int i = 0; i < abasId.length; i++) { %>
            <li class="nav-item" role="presentation">
                <button class="nav-link <%= i == 0 ? "active" : "" %>" data-bs-toggle="tab" data-bs-target="#<%= abasId[i] %>" type="button" role="tab">
                    <i class="bi <%= abasIcone[i] %>"></i> <%= abasTitulo[i] %> (<%= abasContagem[i] %>)
                </button>
            </li>
            <% } %>
        </ul>

        <div class="tab-content mt-3">
            <% for (int aba = 0; aba < abasId.length; aba++) { %>
            <div class="tab-pane fade <%= aba == 0 ? "show active" : "" %>" id="<%= abasId[aba] %>" role="tabpanel">
                <div class="row mentorship-grid">
                    <% boolean encontrou = false;
                    for (Mentoria m : mentorias) {
                        boolean pertence = aba == 0 ? "Ativa".equalsIgnoreCase(m.getStatus())
                                : aba == 1 ? "Pendente".equalsIgnoreCase(m.getStatus())
                                : !("Ativa".equalsIgnoreCase(m.getStatus()) || "Pendente".equalsIgnoreCase(m.getStatus()));
                        if (!pertence) continue;
                        encontrou = true;
                        Mentor itemMentor = mentores.get(m.getIdMentoria());
                        EspecializacaoEm esp = especializacoes.get(m.getIdMentoria());
                        String nomeMentor = itemMentor == null ? "Mentor indisponível" : itemMentor.getNome();
                        boolean ativa = "Ativa".equalsIgnoreCase(m.getStatus());
                        boolean pendente = "Pendente".equalsIgnoreCase(m.getStatus());
                        boolean finalizada = "Finalizada".equalsIgnoreCase(m.getStatus());
                        String statusClasse = ativa ? "status-andamento" : pendente ? "status-pendente" : finalizada ? "status-concluida" : "badge bg-secondary";
                    %>
                    <div class="col-md-6 col-xl-4">
                        <article class="mentorship-card">
                            <div class="d-flex justify-content-between align-items-start gap-3">
                                <div class="d-flex align-items-center gap-3">
                                    <div class="avatar-lg"><i class="bi bi-person-workspace"></i></div>
                                    <div>
                                        <small class="text-muted">Mentoria #<%= m.getIdMentoria() %></small>
                                        <h5 class="mb-1">
                                            <% if (itemMentor == null) { %><%= HtmlUtil.escapar(nomeMentor) %><% } else { %>
                                            <a class="text-success" href="<%= c %>/mentor/perfil?cpf=<%= itemMentor.getCpfMentor() %>"><%= HtmlUtil.escapar(nomeMentor) %></a>
                                            <% } %>
                                        </h5>
                                    </div>
                                </div>
                                <span class="<%= statusClasse %>"><%= HtmlUtil.escapar(m.getStatus()) %></span>
                            </div>
                            <hr>
                            <div class="d-flex flex-wrap gap-2 mb-3">
                                <span class="badge-area"><i class="bi bi-award"></i> <%= esp == null ? "Área não informada" : HtmlUtil.escapar(esp.getEspecializacao()) %></span>
                                <span class="badge-area"><i class="bi bi-calendar3"></i> Data de Início: <%= m.getDataInicio() == null ? "Aguardando aceitação" : m.getDataInicio().format(dataBr) %></span>
                            </div>
                            <p class="card-body-copy mb-0" style="white-space:pre-line"><strong>Objetivos:</strong><br><%= HtmlUtil.escapar(m.getObjetivosDefinidos()) %></p>
                            <div class="card-actions">
                                <% if (itemMentor != null) { %><a class="btn btn-sm btn-outline-success" href="<%= c %>/mentor/perfil?cpf=<%= itemMentor.getCpfMentor() %>"><i class="bi bi-person-vcard"></i> Perfil</a><% } %>
                                <% if (ativa) { %>
                                <a class="btn btn-sm btn-outline-success" href="<%= c %>/agenda?mentoria=<%= m.getIdMentoria() %>"><i class="bi bi-calendar2-check"></i> Agenda (<%= encontrosPorMentoria.get(m.getIdMentoria()) == null ? 0 : encontrosPorMentoria.get(m.getIdMentoria()) %>)</a>
                                <a class="btn btn-sm btn-success" href="<%= c %>/diario?mentoria=<%= m.getIdMentoria() %>"><i class="bi bi-journal-text"></i> Diário</a>
                                <% } else if (finalizada) { %>
                                <a class="btn btn-sm btn-success" href="<%= c %>/diario?mentoria=<%= m.getIdMentoria() %>"><i class="bi bi-journal-text"></i> Consultar diário</a>
                                <% if (Boolean.TRUE.equals(avaliacoesPorMentoria.get(m.getIdMentoria()))) { %>
                                <span class="badge bg-success align-self-center"><i class="bi bi-star-fill"></i> Avaliação enviada</span>
                                <% } else { %>
                                <a class="btn btn-sm btn-warning" href="<%= c %>/avaliacoes?mentoria=<%= m.getIdMentoria() %>"><i class="bi bi-star-fill"></i> Avaliar mentor</a>
                                <% } %>
                                <% } else if (pendente) { %>
                                <span class="text-muted small align-self-center"><i class="bi bi-hourglass-split"></i> Aguardando resposta do mentor</span>
                                <% } %>
                            </div>
                        </article>
                    </div>
                    <% } %>
                    <% if (!encontrou) { %>
                    <div class="col-12"><div class="box empty-state"><i class="bi bi-folder2-open"></i><h6>Nenhuma mentoria nesta categoria</h6></div></div>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
        <% } %>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
