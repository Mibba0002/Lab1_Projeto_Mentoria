<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,java.util.Map,java.time.format.DateTimeFormatter,model.*,util.HtmlUtil" %>
<%
Mentor mentor = (Mentor) request.getAttribute("mentor");
ArrayList<Mentoria> mentorias = (ArrayList<Mentoria>) request.getAttribute("mentorias");
Map<Integer, Mentorado> mentorados = (Map<Integer, Mentorado>) request.getAttribute("mentorados");
Map<Integer, EspecializacaoEm> especializacoes = (Map<Integer, EspecializacaoEm>) request.getAttribute("especializacoes");
String c = request.getContextPath();
DateTimeFormatter dataBr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mentorias Ativas - Miracle Mentorias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="<%= c %>/mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a class="active" href="<%= c %>/mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks Recebidos</a>
        <a href="<%= c %>/perfil/mentor"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-people-fill"></i></span>
                <div>
                    <h2>Mentorias Ativas</h2>
                    <p class="text-muted">Acompanhe seus mentorados e acesse rapidamente a agenda e o diário.</p>
                </div>
            </div>
            <div class="user user-role"><i class="bi bi-person-circle"></i><span><%= HtmlUtil.escapar(mentor.getNome()) %></span><span class="role-pill">Mentor</span></div>
        </div>

        <% if ("finalizada".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-success soft-alert"><i class="bi bi-check-circle-fill me-2"></i>Mentoria finalizada. O mentorado já pode enviar a avaliação.</div>
        <% } else if ("erro".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-danger soft-alert"><i class="bi bi-exclamation-triangle-fill me-2"></i>Não foi possível finalizar essa mentoria.</div>
        <% } %>

        <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-3">
            <div>
                <h5 class="mb-1"><%= mentorias.size() %> mentoria(s) em andamento</h5>
                <small class="text-muted">Somente mentorias aceitas aparecem nesta página.</small>
            </div>
            <a href="<%= c %>/agenda" class="btn btn-outline-success"><i class="bi bi-calendar2-week"></i> Ver agenda completa</a>
        </div>

        <% if (mentorias.isEmpty()) { %>
        <div class="box empty-state">
            <i class="bi bi-people"></i>
            <h5>Nenhuma mentoria ativa</h5>
            <p class="mb-0">As solicitações aceitas aparecerão aqui automaticamente.</p>
            <a class="btn btn-success mt-3" href="<%= c %>/mentorias/solicitacoes">Ver solicitações recebidas</a>
        </div>
        <% } else { %>
        <div class="row mentorship-grid">
            <% for (Mentoria m : mentorias) {
                Mentorado aluno = mentorados.get(m.getIdMentoria());
                EspecializacaoEm esp = especializacoes.get(m.getIdMentoria());
                String nomeAluno = aluno == null ? "Mentorado" : aluno.getNome();
            %>
            <div class="col-md-6 col-xl-4">
                <article class="mentorship-card">
                    <div class="d-flex justify-content-between align-items-start gap-3">
                        <div class="d-flex align-items-center gap-3">
                            <div class="avatar-lg"><i class="bi bi-mortarboard-fill"></i></div>
                            <div>
                                <small class="text-muted">Mentoria #<%= m.getIdMentoria() %></small>
                                <h5 class="mb-1">
                                    <% if (aluno == null) { %><%= HtmlUtil.escapar(nomeAluno) %><% } else { %>
                                    <a class="text-success" href="<%= c %>/mentorado/perfil?cpf=<%= aluno.getCpfMentorado() %>"><%= HtmlUtil.escapar(nomeAluno) %></a>
                                    <% } %>
                                </h5>
                            </div>
                        </div>
                        <span class="status-andamento">Em andamento</span>
                    </div>
                    <hr>
                    <div class="d-flex flex-wrap gap-2 mb-3">
                        <span class="badge-area"><i class="bi bi-award"></i> <%= esp == null ? "Área não informada" : HtmlUtil.escapar(esp.getEspecializacao()) %></span>
                        <span class="badge-area"><i class="bi bi-calendar3"></i> Início: <%= m.getDataInicio() == null ? "Não informado" : m.getDataInicio().format(dataBr) %></span>
                    </div>
                    <p class="card-body-copy mb-0" style="white-space:pre-line"><strong>Objetivos:</strong><br><%= HtmlUtil.escapar(m.getObjetivosDefinidos()) %></p>
                    <div class="card-actions">
                        <% if (aluno != null) { %><a class="btn btn-sm btn-outline-success" href="<%= c %>/mentorado/perfil?cpf=<%= aluno.getCpfMentorado() %>"><i class="bi bi-person-vcard"></i> Perfil</a><% } %>
                        <a class="btn btn-sm btn-outline-success" href="<%= c %>/agenda?mentoria=<%= m.getIdMentoria() %>"><i class="bi bi-calendar-plus"></i> Agenda</a>
                        <a class="btn btn-sm btn-success" href="<%= c %>/diario?mentoria=<%= m.getIdMentoria() %>"><i class="bi bi-journal-text"></i> Diário</a>
                        <form method="post" action="<%= c %>/mentorias/finalizar" class="m-0">
                            <input type="hidden" name="idMentoria" value="<%= m.getIdMentoria() %>">
                            <button class="btn btn-sm btn-outline-danger" onclick="return confirm('Finalizar esta mentoria? Novos encontros não poderão ser marcados.');"><i class="bi bi-check2-circle"></i> Finalizar</button>
                        </form>
                    </div>
                </article>
            </div>
            <% } %>
        </div>
        <% } %>
    </main>
</body>
</html>
