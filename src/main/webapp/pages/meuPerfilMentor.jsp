<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.AreaAtuacao" %>
<%@ page import="model.EspecializacaoEm" %>
<%@ page import="model.Mentor" %>
<%@ page import="util.HtmlUtil" %>
<%
Mentor mentor = (Mentor) request.getAttribute("mentor");
if (mentor == null) {
    response.sendRedirect(request.getContextPath() + "/perfil/mentor");
    return;
}
ArrayList<AreaAtuacao> areas = (ArrayList<AreaAtuacao>) request.getAttribute("areas");
ArrayList<EspecializacaoEm> especializacoes = (ArrayList<EspecializacaoEm>) request.getAttribute("especializacoes");
String resultado = request.getParameter("resultado");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meu Perfil - Mentor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="../assets/css/global.css">
    <link rel="stylesheet" href="../assets/css/meuPerfilMentor.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="../dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="../mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a href="../mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <a href="../diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="../agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="../avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks Recebidos</a>
        <a href="../perfil/mentor" class="active"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="../logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div>
                <h2>Meu Perfil</h2>
                <p class="text-muted">Dados carregados diretamente do banco.</p>
            </div>
            <div class="user"><i class="bi bi-person-circle"></i> <%= HtmlUtil.escapar(mentor.getNome()) %></div>
        </div>

        <% if ("sucesso".equals(resultado)) { %>
        <div class="alert alert-success">Alteração salva com sucesso.</div>
        <% } else if ("erro".equals(resultado)) { %>
        <div class="alert alert-danger">Não foi possível salvar. Confira os dados informados.</div>
        <% } %>

        <section class="box-form mb-3">
            <div class="d-flex justify-content-between flex-wrap gap-2">
                <div>
                    <h5><i class="bi bi-person-vcard-fill"></i> Dados do mentor</h5>
                    <small class="text-muted">CPF: <%= HtmlUtil.escapar(mentor.getCpfMentor()) %></small>
                </div>
                <div>
                    <span class="badge bg-secondary"><%= HtmlUtil.escapar(mentor.getStatus()) %></span>
                    <% if (mentor.isVerificado()) { %>
                    <span class="badge bg-success">Verificado</span>
                    <% } else { %>
                    <span class="badge bg-warning text-dark">Aguardando verificação</span>
                    <% } %>
                </div>
            </div>

            <form action="../perfil/mentor" method="post" class="mt-3">
                <input type="hidden" name="acao" value="salvar">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Nome completo</label>
                        <input class="form-control" name="nome" required
                               value="<%= HtmlUtil.escapar(mentor.getNome()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">E-mail</label>
                        <input type="email" class="form-control" name="email" required
                               value="<%= HtmlUtil.escapar(mentor.getEmail()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Telefone</label>
                        <input type="tel" class="form-control" name="telefone" required
                               value="<%= HtmlUtil.escapar(mentor.getTelefone()) %>">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Formato da mentoria</label>
                        <select class="form-select" name="formato" required>
                            <% for (String formato : new String[]{"Online", "Presencial", "Híbrida"}) { %>
                            <option value="<%= formato %>" <%= formato.equals(mentor.getFormatoMentoria()) ? "selected" : "" %>><%= formato %></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Disponibilidade</label>
                        <input class="form-control" name="disponibilidade" required
                               value="<%= HtmlUtil.escapar(mentor.getDisponibilidade()) %>">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Estado</label>
                        <input class="form-control text-uppercase" name="estado" maxlength="2" required
                               value="<%= HtmlUtil.escapar(mentor.getEstado()) %>">
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Cidade</label>
                        <input class="form-control" name="cidade" required
                               value="<%= HtmlUtil.escapar(mentor.getCidade()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Link do portfólio</label>
                        <input type="url" class="form-control" name="portfolio"
                               value="<%= HtmlUtil.escapar(mentor.getLinkPortifolio()) %>">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Rede profissional</label>
                        <input type="url" class="form-control" name="redesProfissionais"
                               value="<%= HtmlUtil.escapar(mentor.getRedesProfissionais()) %>">
                    </div>
                    <div class="col-12">
                        <label class="form-label">Mini biografia</label>
                        <textarea class="form-control" name="biografia" rows="4" required><%= HtmlUtil.escapar(mentor.getMiniBiografia()) %></textarea>
                    </div>
                    <div class="col-12 text-end">
                        <button class="btn btn-success"><i class="bi bi-save2"></i> Salvar alterações</button>
                    </div>
                </div>
            </form>
        </section>

        <section class="box-form mb-3">
            <h5><i class="bi bi-award-fill"></i> Especializações</h5>
            <div class="d-flex flex-wrap gap-2 my-3">
                <% if (especializacoes.isEmpty()) { %>
                <span class="text-muted">Nenhuma especialização cadastrada.</span>
                <% } %>
                <% for (EspecializacaoEm especializacao : especializacoes) { %>
                <form action="../perfil/mentor" method="post" class="d-inline-flex">
                    <input type="hidden" name="acao" value="removerEspecializacao">
                    <input type="hidden" name="idEspecializacao" value="<%= especializacao.getIdEspecializacao() %>">
                    <button class="btn btn-outline-success btn-sm" title="Remover especialização">
                        <%= HtmlUtil.escapar(especializacao.getEspecializacao()) %>
                        (<%= especializacao.getTempoExperiencia() %> ano(s)) <i class="bi bi-x"></i>
                    </button>
                </form>
                <% } %>
            </div>
            <form action="../perfil/mentor" method="post" class="row g-2">
                <input type="hidden" name="acao" value="adicionarEspecializacao">
                <div class="col-md-7">
                    <select class="form-select" name="idArea" required>
                        <option value="">Selecione uma nova área</option>
                        <% for (AreaAtuacao area : areas) { %>
                        <option value="<%= area.getIdAreaAtuacao() %>"><%= HtmlUtil.escapar(area.getNomeArea()) %></option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <input type="number" min="0" class="form-control" name="tempoExperiencia" placeholder="Anos" required>
                </div>
                <div class="col-md-2">
                    <button class="btn btn-success w-100">Adicionar</button>
                </div>
            </form>
        </section>

        <section class="box-form">
            <h5><i class="bi bi-shield-lock-fill"></i> Alterar senha</h5>
            <form action="../perfil/mentor" method="post" class="row g-2 mt-2">
                <input type="hidden" name="acao" value="senha">
                <div class="col-md-5"><input type="password" minlength="6" class="form-control" name="novaSenha" placeholder="Nova senha" required></div>
                <div class="col-md-5"><input type="password" minlength="6" class="form-control" name="confirmarSenha" placeholder="Confirmar senha" required></div>
                <div class="col-md-2"><button class="btn btn-outline-success w-100">Atualizar</button></div>
            </form>
        </section>
    </main>
</body>
</html>
