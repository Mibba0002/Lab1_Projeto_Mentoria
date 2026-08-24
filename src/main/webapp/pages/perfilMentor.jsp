<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,java.time.format.DateTimeFormatter,model.Avaliacao,model.EspecializacaoEm,model.Mentor,model.Mentorado,util.HtmlUtil" %>
<%
Mentor mentor = (Mentor) request.getAttribute("mentor");
Mentorado mentorado = (Mentorado) request.getAttribute("mentorado");
ArrayList<EspecializacaoEm> especializacoes = (ArrayList<EspecializacaoEm>) request.getAttribute("especializacoes");
ArrayList<Avaliacao> avaliacoes = (ArrayList<Avaliacao>) request.getAttribute("avaliacoes");
double mediaAvaliacao = (Double) request.getAttribute("mediaAvaliacao");
int totalAvaliacoes = (Integer) request.getAttribute("totalAvaliacoes");
DateTimeFormatter dataBr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String c = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil de <%= HtmlUtil.escapar(mentor.getNome()) %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/perfilMentor.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="<%= c %>/mentores" class="active"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a href="<%= c %>/mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="<%= c %>/perfil/mentorado"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-person-vcard-fill"></i></span>
                <div>
                    <a href="<%= c %>/mentores" class="text-success small"><i class="bi bi-arrow-left"></i> Voltar para a busca</a>
                    <h2>Perfil do Mentor</h2>
                    <p class="text-muted">Conheça o profissional antes de solicitar a mentoria.</p>
                </div>
            </div>
            <div class="user user-role"><i class="bi bi-person-circle"></i><span><%= HtmlUtil.escapar(mentorado.getNome()) %></span><span class="role-pill">Mentorado</span></div>
        </div>

        <section class="profile-hero mb-3">
            <div class="profile-hero-content justify-content-between flex-wrap">
                <div class="d-flex align-items-center gap-3 flex-wrap">
                    <span class="profile-avatar"><i class="bi bi-person-workspace"></i></span>
                    <div>
                        <div class="d-flex align-items-center flex-wrap gap-2 mb-1">
                            <h2 class="mb-0"><%= HtmlUtil.escapar(mentor.getNome()) %></h2>
                            <% if (mentor.isVerificado()) { %><span class="badge bg-light text-success"><i class="bi bi-patch-check-fill"></i> Verificado</span><% } %>
                        </div>
                        <p class="mb-1"><i class="bi bi-geo-alt-fill"></i> <%= HtmlUtil.escapar(mentor.getCidade()) %>/<%= HtmlUtil.escapar(mentor.getEstado()) %></p>
                        <p class="mb-0"><i class="bi bi-camera-video-fill"></i> Mentoria <%= HtmlUtil.escapar(mentor.getFormatoMentoria()) %></p>
                    </div>
                </div>
                <% if (especializacoes.isEmpty()) { %>
                <button class="btn btn-light text-success" disabled><i class="bi bi-exclamation-circle"></i> Sem especializações</button>
                <% } else { %>
                <a href="<%= c %>/mentorias/solicitar?mentor=<%= mentor.getCpfMentor() %>" class="btn btn-light text-success"><i class="bi bi-send-plus-fill"></i> Solicitar mentoria</a>
                <% } %>
            </div>
        </section>

        <div class="row g-3">
            <div class="col-lg-8">
                <section class="box mb-3">
                    <div class="section-title"><div><h5>Sobre o mentor</h5><small>Apresentação profissional cadastrada pelo próprio mentor.</small></div></div>
                    <% if (mentor.getMiniBiografia() == null || mentor.getMiniBiografia().isBlank()) { %>
                    <p class="text-muted mb-0">O mentor ainda não escreveu uma biografia.</p>
                    <% } else { %>
                    <p class="mb-0" style="white-space:pre-line;line-height:1.65"><%= HtmlUtil.escapar(mentor.getMiniBiografia()) %></p>
                    <% } %>
                </section>

                <section class="box mt-3">
                    <div class="section-title"><div><h5>Avaliações</h5><small>Experiências registradas após mentorias finalizadas.</small></div></div>
                    <% if (avaliacoes.isEmpty()) { %>
                    <div class="empty-state py-4"><i class="bi bi-chat-square-heart"></i><p class="mb-0">Este mentor ainda não recebeu avaliações.</p></div>
                    <% } else { %>
                    <div class="row g-2">
                        <% for (Avaliacao avaliacao : avaliacoes) { %>
                        <div class="col-md-6"><article class="review-card h-100">
                            <div class="d-flex justify-content-between gap-2"><div><strong><%= HtmlUtil.escapar(avaliacao.getNomeMentorado()) %></strong><small class="d-block text-muted"><%= avaliacao.getDataAvaliacao().toLocalDate().format(dataBr) %></small></div><span class="rating-stars"><% for (int i=1;i<=5;i++) { %><i class="bi <%= i <= avaliacao.getNota() ? "bi-star-fill" : "bi-star" %>"></i><% } %></span></div>
                            <p class="mb-0 mt-2"><%= avaliacao.getComentario() == null || avaliacao.getComentario().isBlank() ? "Sem comentário." : HtmlUtil.escapar(avaliacao.getComentario()) %></p>
                        </article></div>
                        <% } %>
                    </div>
                    <% } %>
                </section>

                <section class="box">
                    <div class="section-title"><div><h5>Especializações</h5><small>Áreas de atuação e tempo de experiência.</small></div></div>
                    <% if (especializacoes.isEmpty()) { %>
                    <div class="empty-state py-4"><i class="bi bi-award"></i><p class="mb-0">Nenhuma especialização informada.</p></div>
                    <% } else { %>
                    <div class="row g-2">
                        <% for (EspecializacaoEm item : especializacoes) { %>
                        <div class="col-md-6">
                            <div class="d-flex align-items-center gap-3 p-3 rounded-3 h-100" style="background:#f7faf7">
                                <span class="avatar-sm"><i class="bi bi-award-fill"></i></span>
                                <div><strong class="d-block"><%= HtmlUtil.escapar(item.getEspecializacao()) %></strong><small class="text-muted"><%= item.getTempoExperiencia() %> ano(s) de experiência</small></div>
                            </div>
                        </div>
                        <% } %>
                    </div>
                    <% } %>
                </section>
            </div>

            <div class="col-lg-4">
                <section class="box info-panel">
                    <div class="section-title"><div><h5>Informações da mentoria</h5><small>Disponibilidade e canais profissionais.</small></div></div>
                    <div class="info-line"><i class="bi bi-clock-fill"></i><div><small>Disponibilidade</small><strong><%= HtmlUtil.escapar(mentor.getDisponibilidade()) %></strong></div></div>
                    <div class="info-line"><i class="bi bi-camera-video-fill"></i><div><small>Formato</small><strong><%= HtmlUtil.escapar(mentor.getFormatoMentoria()) %></strong></div></div>
                    <div class="info-line"><i class="bi bi-star-fill"></i><div><small>Avaliação</small><strong><%= totalAvaliacoes == 0 ? "Aguardando avaliações" : String.format(java.util.Locale.US, "%.1f de 5 · %d avaliação(ões)", mediaAvaliacao, totalAvaliacoes) %></strong></div></div>
                    <% if (mentor.getLinkPortifolio() != null && !mentor.getLinkPortifolio().isBlank()) { %>
                    <a class="btn btn-outline-success w-100 mt-3" target="_blank" rel="noopener" href="<%= HtmlUtil.escapar(mentor.getLinkPortifolio()) %>"><i class="bi bi-link-45deg"></i> Ver portfólio</a>
                    <% } %>
                    <% if (mentor.getRedesProfissionais() != null && !mentor.getRedesProfissionais().isBlank()) { %>
                    <a class="btn btn-outline-success w-100 mt-2" target="_blank" rel="noopener" href="<%= HtmlUtil.escapar(mentor.getRedesProfissionais()) %>"><i class="bi bi-globe2"></i> Rede profissional</a>
                    <% } %>
                </section>
            </div>
        </div>
    </main>
</body>
</html>
