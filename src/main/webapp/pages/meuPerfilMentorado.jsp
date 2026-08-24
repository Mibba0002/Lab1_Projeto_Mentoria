<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.AreaAtuacao" %>
<%@ page import="model.InteresseEm" %>
<%@ page import="model.Mentorado" %>
<%@ page import="util.HtmlUtil" %>
<%
Mentorado mentorado = (Mentorado) request.getAttribute("mentorado");
if (mentorado == null) {
    response.sendRedirect(request.getContextPath() + "/perfil/mentorado");
    return;
}
ArrayList<AreaAtuacao> areas = (ArrayList<AreaAtuacao>) request.getAttribute("areas");
ArrayList<InteresseEm> interesses = (ArrayList<InteresseEm>) request.getAttribute("interesses");
String resultado = request.getParameter("resultado");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meu Perfil - Mentorado</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="../assets/css/global.css">
    <link rel="stylesheet" href="../assets/css/meuPerfilMentorado.css">
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
        <a href="../perfil/mentorado" class="active"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="../logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div>
                <h2>Meu Perfil</h2>
                <p class="text-muted">Dados carregados diretamente do banco.</p>
            </div>
            <div class="user"><i class="bi bi-person-circle"></i> <%= HtmlUtil.escapar(mentorado.getNome()) %></div>
        </div>

        <% if ("sucesso".equals(resultado)) { %>
        <div class="alert alert-success">Alteração salva com sucesso.</div>
        <% } else if ("erro".equals(resultado)) { %>
        <div class="alert alert-danger">Não foi possível salvar. Confira os dados informados.</div>
        <% } %>

        <section class="box-form mb-3">
            <div class="d-flex justify-content-between flex-wrap gap-2">
                <div>
                    <h5><i class="bi bi-person-vcard-fill"></i> Dados do mentorado</h5>
                    <small class="text-muted">CPF: <%= HtmlUtil.escapar(mentorado.getCpfMentorado()) %></small>
                </div>
                <span class="badge bg-secondary align-self-start"><%= HtmlUtil.escapar(mentorado.getStatus()) %></span>
            </div>

            <form action="../perfil/mentorado" method="post" class="mt-3">
                <input type="hidden" name="acao" value="salvar">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Nome completo</label>
                        <input class="form-control" name="nome" required value="<%= HtmlUtil.escapar(mentorado.getNome()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">E-mail</label>
                        <input type="email" class="form-control" name="email" required value="<%= HtmlUtil.escapar(mentorado.getEmail()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Telefone</label>
                        <input type="tel" class="form-control" name="telefone" required value="<%= HtmlUtil.escapar(mentorado.getTelefone()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Formação</label>
                        <input class="form-control" name="formacao" required value="<%= HtmlUtil.escapar(mentorado.getFormacao()) %>">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Estado</label>
                        <input class="form-control text-uppercase" name="estado" maxlength="2" required value="<%= HtmlUtil.escapar(mentorado.getEstado()) %>">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Cidade</label>
                        <input class="form-control" name="cidade" required value="<%= HtmlUtil.escapar(mentorado.getCidade()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Objetivos profissionais</label>
                        <textarea class="form-control" name="objetivos" rows="3" required><%= HtmlUtil.escapar(mentorado.getObjetivosProfissionais()) %></textarea>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Principais dúvidas</label>
                        <textarea class="form-control" name="duvidas" rows="3" required><%= HtmlUtil.escapar(mentorado.getPrincipaisDuvidas()) %></textarea>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Expectativas</label>
                        <textarea class="form-control" name="expectativas" rows="3" required><%= HtmlUtil.escapar(mentorado.getExpectativas()) %></textarea>
                    </div>
                    <div class="col-12 text-end">
                        <button class="btn btn-success"><i class="bi bi-save2"></i> Salvar alterações</button>
                    </div>
                </div>
            </form>
        </section>

        <section class="box-form mb-3">
            <h5><i class="bi bi-tags-fill"></i> Áreas de interesse</h5>
            <div class="d-flex flex-wrap gap-2 my-3">
                <% if (interesses.isEmpty()) { %>
                <span class="text-muted">Nenhuma área de interesse cadastrada.</span>
                <% } %>
                <% for (InteresseEm interesse : interesses) { %>
                <form action="../perfil/mentorado" method="post" class="d-inline-flex">
                    <input type="hidden" name="acao" value="removerInteresse">
                    <input type="hidden" name="idArea" value="<%= interesse.getIdAreaAtuacao() %>">
                    <button class="btn btn-outline-success btn-sm" title="Remover interesse">
                        <%= HtmlUtil.escapar(interesse.getAreaInteresse()) %>
                        - <%= HtmlUtil.escapar(interesse.getNivelExperiencia()) %> <i class="bi bi-x"></i>
                    </button>
                </form>
                <% } %>
            </div>
            <form action="../perfil/mentorado" method="post" class="row g-2">
                <input type="hidden" name="acao" value="adicionarInteresse">
                <div class="col-md-7">
                    <select class="form-select" name="idArea" required>
                        <option value="">Selecione uma nova área</option>
                        <% for (AreaAtuacao area : areas) { %>
                        <option value="<%= area.getIdAreaAtuacao() %>"><%= HtmlUtil.escapar(area.getNomeArea()) %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <select class="form-select" name="nivelExperiencia" required>
                        <option value="Iniciante">Iniciante</option>
                        <option value="Intermediário">Intermediário</option>
                        <option value="Avançado">Avançado</option>
                    </select>
                </div>
                <div class="col-md-2"><button class="btn btn-success w-100">Adicionar</button></div>
            </form>
        </section>

        <section class="box-form">
            <h5><i class="bi bi-shield-lock-fill"></i> Alterar senha</h5>
            <form action="../perfil/mentorado" method="post" class="row g-2 mt-2">
                <input type="hidden" name="acao" value="senha">
                <div class="col-md-5"><input type="password" minlength="6" class="form-control" name="novaSenha" placeholder="Nova senha" required></div>
                <div class="col-md-5"><input type="password" minlength="6" class="form-control" name="confirmarSenha" placeholder="Confirmar senha" required></div>
                <div class="col-md-2"><button class="btn btn-outline-success w-100">Atualizar</button></div>
            </form>
        </section>
    </main>
</body>
</html>
