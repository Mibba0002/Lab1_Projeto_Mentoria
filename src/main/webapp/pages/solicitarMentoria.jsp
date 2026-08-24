<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.EspecializacaoEm" %>
<%@ page import="model.Mentor" %>
<%@ page import="model.Mentorado" %>
<%@ page import="util.HtmlUtil" %>
<%
Mentor mentor = (Mentor) request.getAttribute("mentor");
Mentorado mentorado = (Mentorado) request.getAttribute("mentorado");
ArrayList<EspecializacaoEm> especializacoes =
        (ArrayList<EspecializacaoEm>) request.getAttribute("especializacoes");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitar Mentoria</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="../assets/css/global.css">
    <link rel="stylesheet" href="../assets/css/solicitarMentoria.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="../dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="../mentores"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a href="../mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <a href="../diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="../agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="../avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks</a>
        <a href="../perfil/mentorado"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="../logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div><h2>Solicitar Mentoria</h2><p class="text-muted">Envie sua solicitação para o mentor escolhido.</p></div>
            <div class="user"><i class="bi bi-person-circle"></i> <%= HtmlUtil.escapar(mentorado.getNome()) %></div>
        </div>

        <div class="box-mentor-escolhido">
            <div class="avatar-mentor"><i class="bi bi-person-workspace"></i></div>
            <div class="flex-grow-1">
                <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                    <div>
                        <h5 class="mb-1"><%= HtmlUtil.escapar(mentor.getNome()) %></h5>
                        <span class="badge-area"><%= HtmlUtil.escapar(mentor.getFormatoMentoria()) %></span>
                        <% if (mentor.isVerificado()) { %><span class="badge bg-success">Verificado</span><% } %>
                    </div>
                    <a href="../mentores" class="btn btn-outline-success btn-sm"><i class="bi bi-arrow-left"></i> Trocar mentor</a>
                </div>
                <p class="text-muted mt-2 mb-0"><%= HtmlUtil.escapar(mentor.getMiniBiografia()) %></p>
            </div>
        </div>

        <div class="formulario">
            <% if (especializacoes.isEmpty()) { %>
            <div class="alert alert-warning">Este mentor ainda não possui especializações e não pode receber solicitações.</div>
            <% } else { %>
            <form action="../mentorias/solicitar" method="post">
                <input type="hidden" name="cpfMentor" value="<%= mentor.getCpfMentor() %>">
                <div class="row">
                    <div class="col-12 mb-3">
                        <label class="form-label fw-semibold">Área da mentoria</label>
                        <select class="form-select" name="idEspecializacao" required>
                            <option value="">Selecione</option>
                            <% for (EspecializacaoEm item : especializacoes) { %>
                            <option value="<%= item.getIdEspecializacao() %>"><%= HtmlUtil.escapar(item.getEspecializacao()) %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-semibold">Nível de experiência</label>
                        <select class="form-select" name="nivelExperiencia" required>
                            <option>Iniciante</option><option>Intermediário</option><option>Avançado</option>
                        </select>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-semibold">Formato preferido</label>
                        <select class="form-select" name="formatoPreferido" required>
                            <option>Online</option><option>Presencial</option><option>Híbrida</option>
                        </select>
                    </div>
                    <div class="col-12 mb-3">
                        <label class="form-label fw-semibold">Objetivos profissionais</label>
                        <textarea class="form-control" name="objetivos" rows="3" required><%= HtmlUtil.escapar(mentorado.getObjetivosProfissionais()) %></textarea>
                    </div>
                    <div class="col-12 mb-3">
                        <label class="form-label fw-semibold">Dúvidas ou expectativas</label>
                        <textarea class="form-control" name="expectativas" rows="3" required><%= HtmlUtil.escapar(mentorado.getExpectativas()) %></textarea>
                    </div>
                    <div class="col-12"><button class="btn btn-success w-100"><i class="bi bi-send-plus-fill"></i> Enviar solicitação</button></div>
                </div>
            </form>
            <% } %>
        </div>
    </main>
</body>
</html>
