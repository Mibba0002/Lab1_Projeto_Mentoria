<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.AreaAtuacaoDao" %>
<%@ page import="dao.AdminDao" %>
<%@ page import="model.Admin" %>
<%@ page import="model.AreaAtuacao" %>
<%@ page import="util.HtmlUtil" %>
<%
Object usuario = session.getAttribute("usuarioLogado");
if (!(usuario instanceof Admin)) {
    response.sendRedirect("login.html?erro=credenciais");
    return;
}
Admin admin = (Admin) usuario;
ArrayList<AreaAtuacao> areas = new dao.AreaAtuacaoDao().listar();
ArrayList<Admin> administradores = new dao.AdminDao().listar();
boolean superAdmin = "SUPER_ADMIN".equalsIgnoreCase(admin.getNivelAcesso());
boolean podeGerenciarAreas = !"MODERADOR".equalsIgnoreCase(admin.getNivelAcesso());
String resultado = request.getParameter("resultado");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Configurações - Administrador</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet"
href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">

<link rel="stylesheet" href="../assets/css/global.css">
<link rel="stylesheet" href="../assets/css/configuracoesAdmin.css">

</head>

<body>

<div class="sidebar">

<div class="logo">
<i class="bi bi-calendar-event-fill"></i>
<br>
Mentoria
</div>

<a href="../dashboard" title="Dashboard Geral">
<i class="bi bi-speedometer2"></i>
Dashboard Geral
</a>

<a href="gerenciarMentores.jsp" title="Gerenciar Mentores">
<i class="bi bi-person-workspace"></i>
Gerenciar Mentores
</a>

<a href="gerenciarMentorados.jsp" title="Gerenciar Mentorados">
<i class="bi bi-mortarboard-fill"></i>
Gerenciar Mentorados
</a>

<a href="gerenciarMentorias.html" title="Gerenciar Mentorias">
<i class="bi bi-people-fill"></i>
Gerenciar Mentorias
</a>

<a href="relatoriosAdmin.html" title="Relatórios">
<i class="bi bi-bar-chart-fill"></i>
Relatórios
</a>

<a href="feedbacksAdmin.html" title="Feedbacks">
<i class="bi bi-star-fill"></i>
Feedbacks
</a>

<a href="configuracoesAdmin.jsp" title="Configurações" class="active">
<i class="bi bi-gear-fill"></i>
Configurações
</a>

<a href="../logout" title="Sair">
<i class="bi bi-box-arrow-left"></i>
Sair
</a>

</div>

<div class="content">

<div class="header">

<div>
<h2>Configurações</h2>
<p class="text-muted">
Gerencie as configurações gerais da plataforma.
</p>
</div>

<div class="user">
<i class="bi bi-person-circle"></i>
<span id="nomeUsuarioLogado"><%= HtmlUtil.escapar(admin.getNome()) %></span>
<span class="badge-admin"><%= HtmlUtil.escapar(admin.getNivelAcesso()) %></span>
</div>

</div>

<div class="row">

<!-- NAV LATERAL -->

<div class="col-lg-3 mb-3">

<div class="nav-config nav flex-column">

<a class="nav-link active" data-bs-toggle="tab" href="#tabGeral">
<i class="bi bi-sliders"></i>
Geral
</a>

<a class="nav-link" data-bs-toggle="tab" href="#tabAreas">
<i class="bi bi-tags-fill"></i>
Áreas de Atuação
</a>

<a class="nav-link" data-bs-toggle="tab" href="#tabNotificacoes">
<i class="bi bi-bell-fill"></i>
Notificações
</a>

<a class="nav-link" data-bs-toggle="tab" href="#tabAdmins">
<i class="bi bi-person-badge-fill"></i>
Administradores
</a>

<a class="nav-link" data-bs-toggle="tab" href="#tabSeguranca">
<i class="bi bi-shield-lock-fill"></i>
Segurança
</a>

</div>

</div>

<!-- CONTEÚDO -->

<div class="col-lg-9">

<div class="tab-content">

<!-- GERAL -->

<div class="tab-pane fade show active" id="tabGeral">

<div class="box-form">

<h5><i class="bi bi-sliders"></i> Configurações Gerais</h5>

<form>

<div class="row">

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Nome da Plataforma</label>
<input type="text" class="form-control" id="nomePlataforma">
</div>

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Email de Contato</label>
<input type="email" class="form-control" id="emailContato">
</div>

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Máximo de Mentorias Ativas por Mentor</label>
<input type="number" class="form-control" id="maxMentorias">
</div>

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Duração Padrão da Mentoria (meses)</label>
<input type="number" class="form-control" id="duracaoPadrao">
</div>

</div>

<button class="btn btn-success mt-2">
<i class="bi bi-save2"></i>
Salvar Alterações
</button>

</form>

</div>

</div>

<!-- ÁREAS -->

<div class="tab-pane fade" id="tabAreas">

<div class="box-form">

<h5><i class="bi bi-tags-fill"></i> Áreas de Atuação Disponíveis</h5>

<% if ("area_cadastrada".equals(resultado)) { %>
<div class="alert alert-success">Área cadastrada com sucesso.</div>
<% } else if ("area_excluida".equals(resultado)) { %>
<div class="alert alert-success">Área excluída com sucesso.</div>
<% } else if ("area_duplicada".equals(resultado)) { %>
<div class="alert alert-warning">Essa área já existe ou o nome está vazio.</div>
<% } else if ("area_em_uso".equals(resultado)) { %>
<div class="alert alert-warning">A área não pode ser excluída porque já está sendo usada por um usuário.</div>
<% } else if ("sem_permissao_area".equals(resultado)) { %>
<div class="alert alert-danger">Seu nível não permite cadastrar ou excluir áreas.</div>
<% } else if ("erro".equals(resultado)) { %>
<div class="alert alert-danger">Não foi possível concluir a operação.</div>
<% } %>

<div id="listaAreas">
<% if (areas.isEmpty()) { %>
<div class="item-config">
<span class="text-muted">Nenhuma área cadastrada.</span>
</div>
<% } %>
<% for (AreaAtuacao area : areas) { %>
<div class="item-config">
<span><%= HtmlUtil.escapar(area.getNomeArea()) %></span>
<% if (podeGerenciarAreas) { %><form action="../admin/areas" method="post" class="m-0">
<input type="hidden" name="acao" value="excluir">
<input type="hidden" name="idArea" value="<%= area.getIdAreaAtuacao() %>">
<button type="submit" class="btn btn-sm btn-outline-danger"
        onclick="return confirm('Excluir esta área?');">
<i class="bi bi-trash"></i>
</button>
</form>
<% } %>
</div>
<% } %>
</div>

<% if (podeGerenciarAreas) { %><form action="../admin/areas" method="post" class="row mt-3">
<input type="hidden" name="acao" value="cadastrar">

<div class="col-md-9 mb-3">
<input type="text" class="form-control" name="nomeArea"
       maxlength="100" placeholder="Nova área de atuação" required>
</div>

<div class="col-md-3 mb-3">
<button type="submit" class="btn btn-success w-100">
<i class="bi bi-plus-lg"></i>
Adicionar
</button>
</div>

</form>
<% } else { %><div class="alert alert-info mt-3 mb-0">MODERADOR pode verificar e bloquear usuários, mas não altera as áreas disponíveis.</div><% } %>

</div>

</div>

<!-- NOTIFICAÇÕES -->

<div class="tab-pane fade" id="tabNotificacoes">

<div class="box-form">

<h5><i class="bi bi-bell-fill"></i> Notificações do Sistema</h5>

<div class="item-config">
<div>
<h6>Novo cadastro de mentor</h6>
<small>Notificar administradores por email quando um novo mentor se cadastrar</small>
</div>
<div class="form-check form-switch">
<input class="form-check-input" type="checkbox" checked>
</div>
</div>

<div class="item-config">
<div>
<h6>Nova solicitação de mentoria</h6>
<small>Notificar o mentor por email ao receber uma nova solicitação</small>
</div>
<div class="form-check form-switch">
<input class="form-check-input" type="checkbox" checked>
</div>
</div>

<div class="item-config">
<div>
<h6>Feedback denunciado</h6>
<small>Notificar administradores quando um feedback for denunciado</small>
</div>
<div class="form-check form-switch">
<input class="form-check-input" type="checkbox" checked>
</div>
</div>

<div class="item-config">
<div>
<h6>Lembrete de encontro</h6>
<small>Enviar lembrete 24h antes de cada encontro agendado</small>
</div>
<div class="form-check form-switch">
<input class="form-check-input" type="checkbox">
</div>
</div>

</div>

</div>

<!-- ADMINISTRADORES -->

<div class="tab-pane fade" id="tabAdmins">

<div class="box-form">

<div class="d-flex justify-content-between align-items-center mb-3">
<h5 class="mb-0"><i class="bi bi-person-badge-fill"></i> Administradores</h5>
<% if (superAdmin) { %>
<button class="btn btn-success btn-sm" type="button" data-bs-toggle="collapse" data-bs-target="#formNovoAdmin">
<i class="bi bi-plus-lg"></i>
Novo Admin
</button>
<% } %>
</div>

<% if ("admin_cadastrado".equals(resultado)) { %><div class="alert alert-success">Administrador cadastrado com sucesso.</div><% } %>
<% if ("admin_excluido".equals(resultado)) { %><div class="alert alert-success">Administrador removido com sucesso.</div><% } %>
<% if ("admin_duplicado".equals(resultado)) { %><div class="alert alert-warning">Confira os dados. O e-mail pode já estar cadastrado.</div><% } %>
<% if ("proprio_admin".equals(resultado)) { %><div class="alert alert-warning">Você não pode remover a própria conta.</div><% } %>
<% if ("ultimo_super".equals(resultado)) { %><div class="alert alert-warning">O último SUPER_ADMIN não pode ser removido.</div><% } %>
<% if ("sem_permissao".equals(resultado)) { %><div class="alert alert-danger">Somente um SUPER_ADMIN pode gerenciar administradores.</div><% } %>
<% if ("erro_admin".equals(resultado)) { %><div class="alert alert-danger">Não foi possível concluir a operação.</div><% } %>

<% if (superAdmin) { %>
<div class="collapse mb-4" id="formNovoAdmin">
<form action="../admin/administradores" method="post" class="p-3 rounded-3 border bg-light">
<input type="hidden" name="acao" value="cadastrar">
<div class="row g-2">
<div class="col-md-6"><label class="form-label fw-semibold">Nome</label><input class="form-control" name="nome" maxlength="100" required></div>
<div class="col-md-6"><label class="form-label fw-semibold">E-mail</label><input class="form-control" type="email" name="email" maxlength="100" required></div>
<div class="col-md-6"><label class="form-label fw-semibold">Senha inicial</label><input class="form-control" type="password" name="senha" minlength="6" maxlength="100" required></div>
<div class="col-md-6"><label class="form-label fw-semibold">Nível de acesso</label><select class="form-select" name="nivelAcesso" required><option value="MODERADOR">MODERADOR</option><option value="ADMIN">ADMIN</option><option value="SUPER_ADMIN">SUPER_ADMIN</option></select></div>
<div class="col-12"><button class="btn btn-success"><i class="bi bi-person-plus-fill"></i> Cadastrar administrador</button></div>
</div>
</form>
</div>
<% } else { %>
<div class="alert alert-info"><i class="bi bi-info-circle me-2"></i>Seu nível permite visualizar os administradores. Cadastros e remoções são exclusivos do SUPER_ADMIN.</div>
<% } %>

<table class="tabela-admins">

<thead>
<tr>
<th>Nome</th>
<th>Email</th>
<th>Nível</th>
<th></th>
</tr>
</thead>

<tbody id="tabelaAdmins">

<% for (Admin item : administradores) {
String nomeItem = item.getNome() == null ? "A" : item.getNome().trim();
String inicial = nomeItem.isEmpty() ? "A" : nomeItem.substring(0, 1).toUpperCase();
%>
<tr>
<td><div class="d-flex align-items-center gap-2"><div class="avatar-sm"><%= inicial %></div><%= HtmlUtil.escapar(item.getNome()) %><% if (item.getIdAdmin() == admin.getIdAdmin()) { %><span class="badge bg-light text-success">Você</span><% } %></div></td>
<td><%= HtmlUtil.escapar(item.getEmail()) %></td>
<td><span class="badge bg-success"><%= HtmlUtil.escapar(item.getNivelAcesso()) %></span></td>
<td>
<% if (superAdmin && item.getIdAdmin() != admin.getIdAdmin()) { %>
<form action="../admin/administradores" method="post" class="m-0"><input type="hidden" name="acao" value="excluir"><input type="hidden" name="idAdmin" value="<%= item.getIdAdmin() %>"><button class="btn btn-sm btn-outline-danger" onclick="return confirm('Remover este administrador?');" title="Remover"><i class="bi bi-trash"></i></button></form>
<% } %>
</td>
</tr>
<% } %>

</tbody>

</table>

</div>

</div>

<!-- SEGURANÇA -->

<div class="tab-pane fade" id="tabSeguranca">

<div class="box-form">

<h5><i class="bi bi-shield-lock-fill"></i> Segurança</h5>

<form>

<div class="row">

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Senha Atual</label>
<input type="password" class="form-control" placeholder="Digite sua senha atual">
</div>

<div class="col-md-6 mb-3"></div>

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Nova Senha</label>
<input type="password" class="form-control" placeholder="Digite a nova senha">
</div>

<div class="col-md-6 mb-3">
<label class="form-label fw-semibold">Confirmar Nova Senha</label>
<input type="password" class="form-control" placeholder="Confirme a nova senha">
</div>

</div>

<div class="item-config">
<div>
<h6>Autenticação em Duas Etapas</h6>
<small>Adicione uma camada extra de segurança ao seu login</small>
</div>
<div class="form-check form-switch">
<input class="form-check-input" type="checkbox">
</div>
</div>

<button class="btn btn-success mt-3">
<i class="bi bi-save2"></i>
Salvar Alterações
</button>

</form>

</div>

</div>

</div>

</div>

</div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.addEventListener("DOMContentLoaded", function () {
    var alvo = window.location.hash;
    if (!alvo && new URLSearchParams(window.location.search).has("resultado")) {
        alvo = "#tabAreas";
    }
    if (alvo) {
        var linkAba = document.querySelector('a[href="' + alvo + '"]');
        if (linkAba) {
            bootstrap.Tab.getOrCreateInstance(linkAba).show();
        }
    }
});
</script>

</body>

</html>
