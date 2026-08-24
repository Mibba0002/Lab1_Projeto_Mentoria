<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,model.*,util.HtmlUtil" %>
<%
Mentor mentor = (Mentor) request.getAttribute("mentor");
Mentorado mentorado = (Mentorado) request.getAttribute("mentorado");
ArrayList<InteresseEm> interesses = (ArrayList<InteresseEm>) request.getAttribute("interesses");
String c = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de <%= HtmlUtil.escapar(mentorado.getNome()) %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/perfilMentorado.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="<%= c %>/mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a href="<%= c %>/mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks Recebidos</a>
        <a href="<%= c %>/perfil/mentor"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-person-vcard-fill"></i></span>
                <div>
                    <a href="<%= c %>/mentorias/ativas" class="text-success small"><i class="bi bi-arrow-left"></i> Voltar às mentorias</a>
                    <h2>Perfil do Mentorado</h2>
                    <p class="text-muted">Informações compartilhadas pelo participante da mentoria.</p>
                </div>
            </div>
            <div class="user user-role"><i class="bi bi-person-circle"></i><span><%= HtmlUtil.escapar(mentor.getNome()) %></span><span class="role-pill">Mentor</span></div>
        </div>

        <section class="profile-hero mb-3">
            <div class="profile-hero-content">
                <span class="profile-avatar"><i class="bi bi-mortarboard-fill"></i></span>
                <div>
                    <span class="badge bg-light text-success mb-2">Mentorado</span>
                    <h2 class="mb-1"><%= HtmlUtil.escapar(mentorado.getNome()) %></h2>
                    <p class="mb-0"><i class="bi bi-geo-alt-fill"></i> <%= HtmlUtil.escapar(mentorado.getCidade()) %>/<%= HtmlUtil.escapar(mentorado.getEstado()) %> &nbsp;·&nbsp; <i class="bi bi-mortarboard"></i> <%= HtmlUtil.escapar(mentorado.getFormacao()) %></p>
                </div>
            </div>
        </section>

        <div class="row g-3">
            <div class="col-lg-7">
                <section class="box info-panel">
                    <div class="section-title"><div><h5>Sobre o mentorado</h5><small>Objetivos e expectativas informados no cadastro.</small></div></div>
                    <div class="info-line">
                        <i class="bi bi-bullseye"></i>
                        <div><small>Objetivos profissionais</small><strong><%= HtmlUtil.escapar(mentorado.getObjetivosProfissionais()) %></strong></div>
                    </div>
                    <div class="info-line">
                        <i class="bi bi-question-circle"></i>
                        <div><small>Principais dúvidas</small><strong><%= HtmlUtil.escapar(mentorado.getPrincipaisDuvidas()) %></strong></div>
                    </div>
                    <div class="info-line">
                        <i class="bi bi-stars"></i>
                        <div><small>Expectativas para a mentoria</small><strong><%= HtmlUtil.escapar(mentorado.getExpectativas()) %></strong></div>
                    </div>
                </section>
            </div>

            <div class="col-lg-5">
                <section class="box info-panel">
                    <div class="section-title"><div><h5>Áreas de interesse</h5><small>Temas que o mentorado deseja desenvolver.</small></div></div>
                    <% if (interesses.isEmpty()) { %>
                    <div class="empty-state py-4"><i class="bi bi-tags"></i><p class="mb-0">Nenhuma área cadastrada.</p></div>
                    <% } else { %>
                    <div class="d-flex flex-column gap-2">
                        <% for (InteresseEm interesse : interesses) { %>
                        <div class="d-flex justify-content-between align-items-center gap-2 p-3 rounded-3" style="background:#f7faf7">
                            <span><i class="bi bi-bookmark-star-fill text-success me-2"></i><strong><%= HtmlUtil.escapar(interesse.getAreaInteresse()) %></strong></span>
                            <span class="badge-area"><%= HtmlUtil.escapar(interesse.getNivelExperiencia()) %></span>
                        </div>
                        <% } %>
                    </div>
                    <% } %>
                </section>
            </div>
        </div>
    </main>
</body>
</html>
