<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="util.HtmlUtil" %>
<%
String tipo = (String) request.getAttribute("tipo");
String nome = (String) request.getAttribute("nome");
String c = request.getContextPath();
boolean admin = "admin".equals(tipo);
boolean mentor = "mentor".equals(tipo);
String papel = admin ? "Administrador" : mentor ? "Mentor" : "Mentorado";
String titulo = admin ? "Dashboard Geral" : mentor ? "Dashboard do Mentor" : "Dashboard do Mentorado";
String subtitulo = admin ? "Visão geral da plataforma e dos usuários cadastrados."
        : mentor ? "Acompanhe suas solicitações, mentorias e próximos encontros."
        : "Acompanhe sua jornada, seus encontros e solicitações de mentoria.";
String[] icones = admin
        ? new String[]{"bi-person-workspace", "bi-mortarboard-fill", "bi-people-fill", "bi-hourglass-split"}
        : mentor
            ? new String[]{"bi-inbox-fill", "bi-people-fill", "bi-calendar2-check", "bi-check-circle-fill"}
            : new String[]{"bi-people-fill", "bi-hourglass-split", "bi-calendar2-check", "bi-check-circle-fill"};
String[] rotulos = new String[4];
int[] valores = new int[4];
int maxValor = 1;
for (int i = 0; i < 4; i++) {
    rotulos[i] = String.valueOf(request.getAttribute("rotulo" + (i + 1)));
    Object valor = request.getAttribute("valor" + (i + 1));
    valores[i] = valor instanceof Number ? ((Number) valor).intValue() : 0;
    maxValor = Math.max(maxValor, valores[i]);
}
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= titulo %> - Miracle Mentorias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/<%= admin ? "dashboardAdmin" : mentor ? "dashboardMentor" : "dashboardMentorado" %>.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <% if (admin) { %>
        <a class="active" href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard Geral</a>
        <a href="<%= c %>/pages/gerenciarMentores.jsp"><i class="bi bi-person-workspace"></i> Gerenciar Mentores</a>
        <a href="<%= c %>/pages/gerenciarMentorados.jsp"><i class="bi bi-mortarboard-fill"></i> Gerenciar Mentorados</a>
        <a href="<%= c %>/pages/gerenciarMentorias.html"><i class="bi bi-people-fill"></i> Gerenciar Mentorias</a>
        <a href="<%= c %>/pages/relatoriosAdmin.html"><i class="bi bi-bar-chart-fill"></i> Relatórios</a>
        <a href="<%= c %>/pages/feedbacksAdmin.html"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="<%= c %>/pages/configuracoesAdmin.jsp"><i class="bi bi-gear-fill"></i> Configurações</a>
        <% } else if (mentor) { %>
        <a class="active" href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="<%= c %>/mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a href="<%= c %>/mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks Recebidos</a>
        <a href="<%= c %>/perfil/mentor"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <% } else { %>
        <a class="active" href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="<%= c %>/mentores"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a href="<%= c %>/mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="<%= c %>/perfil/mentorado"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <% } %>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-grid-1x2-fill"></i></span>
                <div>
                    <h2><%= titulo %></h2>
                    <p class="text-muted"><%= subtitulo %></p>
                </div>
            </div>
            <div class="user user-role">
                <i class="bi bi-person-circle"></i>
                <span><%= HtmlUtil.escapar(nome) %></span>
                <span class="role-pill"><%= papel %></span>
            </div>
        </div>

        <div class="row g-3">
            <% for (int i = 0; i < 4; i++) { %>
            <div class="col-sm-6 col-xl-3">
                <div class="card-dashboard dashboard-card c<%= i + 1 %>">
                    <span class="stat-icon"><i class="bi <%= icones[i] %>"></i></span>
                    <h6><%= HtmlUtil.escapar(rotulos[i]) %></h6>
                    <h2><%= valores[i] %></h2>
                    <p>Informação atualizada do banco</p>
                </div>
            </div>
            <% } %>
        </div>

        <div class="row g-3 mt-1">
            <div class="col-xl-8">
                <section class="box h-100">
                    <div class="section-title">
                        <div>
                            <h5>Acessos rápidos</h5>
                            <small>Continue de onde parou.</small>
                        </div>
                    </div>
                    <div class="quick-grid">
                        <% if (admin) { %>
                        <a class="quick-action" href="<%= c %>/pages/gerenciarMentores.jsp"><i class="bi bi-person-workspace"></i><span><strong>Mentores</strong><small>Verificar e gerenciar contas</small></span></a>
                        <a class="quick-action" href="<%= c %>/pages/gerenciarMentorados.jsp"><i class="bi bi-mortarboard-fill"></i><span><strong>Mentorados</strong><small>Acompanhar usuários</small></span></a>
                        <a class="quick-action" href="<%= c %>/pages/configuracoesAdmin.jsp#tabAreas"><i class="bi bi-tags-fill"></i><span><strong>Áreas de atuação</strong><small>Cadastrar novas áreas</small></span></a>
                        <% } else if (mentor) { %>
                        <a class="quick-action" href="<%= c %>/mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i><span><strong>Solicitações</strong><small>Aceitar ou recusar pedidos</small></span></a>
                        <a class="quick-action" href="<%= c %>/mentorias/ativas"><i class="bi bi-people-fill"></i><span><strong>Mentorias ativas</strong><small>Acompanhar mentorados</small></span></a>
                        <a class="quick-action" href="<%= c %>/agenda"><i class="bi bi-calendar-plus"></i><span><strong>Agenda</strong><small>Marcar um novo encontro</small></span></a>
                        <% } else { %>
                        <a class="quick-action" href="<%= c %>/mentores"><i class="bi bi-search"></i><span><strong>Encontrar mentores</strong><small>Pesquisar por área</small></span></a>
                        <a class="quick-action" href="<%= c %>/mentorias/minhas"><i class="bi bi-people-fill"></i><span><strong>Minhas mentorias</strong><small>Acompanhar solicitações</small></span></a>
                        <a class="quick-action" href="<%= c %>/agenda"><i class="bi bi-calendar2-check"></i><span><strong>Agenda</strong><small>Ver próximos encontros</small></span></a>
                        <% } %>
                    </div>
                </section>
            </div>

            <div class="col-xl-4">
                <section class="box h-100">
                    <div class="section-title">
                        <div>
                            <h5>Visão geral</h5>
                            <small>Comparação dos indicadores atuais.</small>
                        </div>
                    </div>
                    <% for (int i = 0; i < 4; i++) {
                        int percentual = (int) Math.round(valores[i] * 100.0 / maxValor);
                    %>
                    <div class="overview-item">
                        <div class="overview-label"><span><%= HtmlUtil.escapar(rotulos[i]) %></span><strong><%= valores[i] %></strong></div>
                        <div class="overview-track"><div class="overview-fill" style="width:<%= percentual %>%"></div></div>
                    </div>
                    <% } %>
                </section>
            </div>
        </div>

        <div class="row g-3 mt-1">
            <div class="col-12">
                <section class="welcome-panel">
                    <% if (admin) { %>
                    <h4>Administração da plataforma</h4>
                    <p>Cadastre áreas, verifique novos mentores e acompanhe o crescimento da rede em um só lugar.</p>
                    <a class="btn" href="<%= c %>/pages/configuracoesAdmin.jsp"><i class="bi bi-gear-fill"></i> Abrir configurações</a>
                    <% } else if (mentor) { %>
                    <h4>Continue acompanhando suas mentorias</h4>
                    <p>Registre os avanços no diário de bordo e mantenha os próximos encontros organizados.</p>
                    <a class="btn" href="<%= c %>/diario"><i class="bi bi-journal-plus"></i> Abrir diário de bordo</a>
                    <% } else { %>
                    <h4>Pronto para o próximo passo?</h4>
                    <p>Encontre profissionais da sua área de interesse e acompanhe todo o desenvolvimento da mentoria.</p>
                    <a class="btn" href="<%= c %>/mentores"><i class="bi bi-search"></i> Encontrar mentores</a>
                    <% } %>
                </section>
            </div>
        </div>
    </main>
</body>
</html>
