<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.AreaAtuacao" %>
<%@ page import="model.EspecializacaoEm" %>
<%@ page import="model.Mentor" %>
<%@ page import="model.Mentorado" %>
<%@ page import="util.HtmlUtil" %>
<%
ArrayList<Mentor> mentores = (ArrayList<Mentor>) request.getAttribute("mentores");
ArrayList<AreaAtuacao> areas = (ArrayList<AreaAtuacao>) request.getAttribute("areas");
Map<String, ArrayList<EspecializacaoEm>> especializacoesPorCpf =
        (Map<String, ArrayList<EspecializacaoEm>>) request.getAttribute("especializacoesPorCpf");
Mentorado mentorado = (Mentorado) request.getAttribute("mentorado");
String filtroNome = (String) request.getAttribute("filtroNome");
String filtroFormato = (String) request.getAttribute("filtroFormato");
Integer filtroArea = (Integer) request.getAttribute("filtroArea");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Encontrar Mentores</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/global.css">
    <link rel="stylesheet" href="assets/css/encontrarMentores.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="mentores" class="active"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a href="mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <a href="diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="perfil/mentorado"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div>
                <h2>Encontrar Mentores</h2>
                <p class="text-muted">Mentores ativos cadastrados no banco de dados.</p>
            </div>
            <div class="user"><i class="bi bi-person-circle"></i> <%= HtmlUtil.escapar(mentorado.getNome()) %></div>
        </div>

        <div class="box-filtro">
            <form class="row g-3 align-items-end" action="mentores" method="get">
                <div class="col-md-4">
                    <label class="form-label fw-semibold">Buscar por nome</label>
                    <input type="text" class="form-control" name="nome"
                           value="<%= HtmlUtil.escapar(filtroNome) %>" placeholder="Digite o nome do mentor">
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-semibold">Área de atuação</label>
                    <select class="form-select" name="area">
                        <option value="">Todas as áreas</option>
                        <% for (AreaAtuacao area : areas) { %>
                        <option value="<%= area.getIdAreaAtuacao() %>"
                                <%= filtroArea != null && filtroArea == area.getIdAreaAtuacao() ? "selected" : "" %>>
                            <%= HtmlUtil.escapar(area.getNomeArea()) %>
                        </option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label fw-semibold">Formato</label>
                    <select class="form-select" name="formato">
                        <option value="">Todos os formatos</option>
                        <% for (String formato : new String[]{"Online", "Presencial", "Híbrida"}) { %>
                        <option value="<%= formato %>" <%= formato.equals(filtroFormato) ? "selected" : "" %>><%= formato %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-2"><button class="btn btn-success w-100"><i class="bi bi-search"></i> Buscar</button></div>
            </form>
        </div>

        <div class="row g-3 mt-1" id="listaMentores">
            <% if (mentores.isEmpty()) { %>
            <div class="col-12"><div class="alert alert-info">Nenhum mentor ativo corresponde aos filtros.</div></div>
            <% } %>
            <% for (Mentor mentor : mentores) {
                ArrayList<EspecializacaoEm> especializacoes =
                        especializacoesPorCpf.get(mentor.getCpfMentor());
                int maiorExperiencia = 0;
                if (especializacoes != null) {
                    for (EspecializacaoEm item : especializacoes) {
                        maiorExperiencia = Math.max(maiorExperiencia, item.getTempoExperiencia());
                    }
                }
            %>
            <div class="col-md-6 col-lg-4">
                <div class="card-mentor h-100 d-flex flex-column">
                    <div class="text-center">
                        <div class="avatar-mentor"><i class="bi bi-person-workspace"></i></div>
                        <a href="mentor/perfil?cpf=<%= mentor.getCpfMentor() %>"><h5 class="mb-1"><%= HtmlUtil.escapar(mentor.getNome()) %></h5></a>
                        <% if (mentor.isVerificado()) { %>
                        <span class="badge bg-success mb-2"><i class="bi bi-patch-check-fill"></i> Verificado</span>
                        <% } else { %>
                        <span class="badge bg-warning text-dark mb-2">Verificação pendente</span>
                        <% } %>
                    </div>

                    <div class="d-flex flex-wrap justify-content-center gap-1 mb-2">
                        <% if (especializacoes == null || especializacoes.isEmpty()) { %>
                        <span class="badge-area">Sem especialização</span>
                        <% } else { for (EspecializacaoEm item : especializacoes) { %>
                        <span class="badge-area"><%= HtmlUtil.escapar(item.getEspecializacao()) %></span>
                        <% }} %>
                    </div>

                    <p class="bio-mentor mt-2"><%= HtmlUtil.escapar(mentor.getMiniBiografia()) %></p>
                    <div class="info-mini mb-3">
                        <div><i class="bi bi-geo-alt"></i> <%= HtmlUtil.escapar(mentor.getCidade()) %>/<%= HtmlUtil.escapar(mentor.getEstado()) %></div>
                        <div><i class="bi bi-laptop"></i> <%= HtmlUtil.escapar(mentor.getFormatoMentoria()) %></div>
                        <div><i class="bi bi-clock"></i> <%= HtmlUtil.escapar(mentor.getDisponibilidade()) %></div>
                        <div><i class="bi bi-briefcase"></i> Até <%= maiorExperiencia %> ano(s)</div>
                    </div>
                    <a href="mentorias/solicitar?mentor=<%= mentor.getCpfMentor() %>" class="btn btn-success w-100 mt-auto">
                        <i class="bi bi-send-plus-fill"></i> Solicitar Mentoria
                    </a>
                </div>
            </div>
            <% } %>
        </div>
    </main>
</body>
</html>
