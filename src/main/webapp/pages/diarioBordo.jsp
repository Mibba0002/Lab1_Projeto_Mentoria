<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,java.util.Map,java.time.format.DateTimeFormatter,model.*,util.HtmlUtil" %>
<%
Object usuario = request.getAttribute("usuario");
boolean ehMentor = usuario instanceof Mentor;
String nomeUsuario = ehMentor ? ((Mentor) usuario).getNome() : ((Mentorado) usuario).getNome();
ArrayList<Mentoria> mentorias = (ArrayList<Mentoria>) request.getAttribute("mentorias");
ArrayList<DiarioBordo> registros = (ArrayList<DiarioBordo>) request.getAttribute("registros");
Map<Integer, String> participantes = (Map<Integer, String>) request.getAttribute("participantes");
Map<Integer, String> autores = (Map<Integer, String>) request.getAttribute("autores");
int selecionada = (Integer) request.getAttribute("mentoriaSelecionada");
String c = request.getContextPath();
DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
Mentoria mentoriaAtual = null;
for (Mentoria item : mentorias) {
    if (item.getIdMentoria() == selecionada) {
        mentoriaAtual = item;
        break;
    }
}
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Diário de Bordo - Miracle Mentorias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/diarioBordoMentorado.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body>
    <div class="sidebar">
        <div class="logo"><i class="bi bi-calendar-event-fill"></i><br>Mentoria</div>
        <a href="<%= c %>/dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <% if (ehMentor) { %>
        <a href="<%= c %>/mentorias/solicitacoes"><i class="bi bi-inbox-fill"></i> Solicitações Recebidas</a>
        <a href="<%= c %>/mentorias/ativas"><i class="bi bi-people-fill"></i> Mentorias Ativas</a>
        <% } else { %>
        <a href="<%= c %>/mentores"><i class="bi bi-search"></i> Encontrar Mentores</a>
        <a href="<%= c %>/mentorias/minhas"><i class="bi bi-people-fill"></i> Minhas Mentorias</a>
        <% } %>
        <a class="active" href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks<%= ehMentor ? " Recebidos" : "" %></a>
        <a href="<%= c %>/perfil/<%= ehMentor ? "mentor" : "mentorado" %>"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-journal-richtext"></i></span>
                <div>
                    <h2>Diário de Bordo</h2>
                    <p class="text-muted"><%= ehMentor ? "Registre a evolução e os próximos passos de cada mentoria." : "Compartilhe reflexões e acompanhe os registros da sua mentoria." %></p>
                </div>
            </div>
            <div class="user user-role">
                <i class="bi bi-person-circle"></i>
                <span><%= HtmlUtil.escapar(nomeUsuario) %></span>
                <span class="role-pill"><%= ehMentor ? "Mentor" : "Mentorado" %></span>
            </div>
        </div>

        <% if ("sucesso".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-success soft-alert"><i class="bi bi-check-circle-fill me-2"></i>Registro salvo no diário.</div>
        <% } else if ("erro".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-danger soft-alert"><i class="bi bi-exclamation-triangle-fill me-2"></i>Não foi possível salvar o registro.</div>
        <% } %>

        <section class="box-select selection-strip">
            <form action="<%= c %>/diario" method="get" class="row g-2 align-items-end">
                <div class="col-md-9">
                    <label class="form-label fw-semibold" for="selectMentoria">Selecione a mentoria compartilhada</label>
                    <select class="form-select" id="selectMentoria" name="mentoria" required <%= mentorias.isEmpty() ? "disabled" : "" %>>
                        <option value="">Selecione uma mentoria...</option>
                        <% for (Mentoria m : mentorias) { %>
                        <option value="<%= m.getIdMentoria() %>" <%= selecionada == m.getIdMentoria() ? "selected" : "" %>>
                            Mentoria #<%= m.getIdMentoria() %> — <%= HtmlUtil.escapar(participantes.get(m.getIdMentoria())) %> (<%= HtmlUtil.escapar(m.getStatus()) %>)
                        </option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-outline-success w-100" <%= mentorias.isEmpty() ? "disabled" : "" %>><i class="bi bi-journal-arrow-down"></i> Abrir diário</button>
                </div>
            </form>
        </section>

        <% if (selecionada > 0 && mentoriaAtual != null) { %>
        <div class="diary-summary">
            <div class="diary-stat">
                <small>Mentoria selecionada</small>
                <strong>#<%= selecionada %> — <%= HtmlUtil.escapar(participantes.get(selecionada)) %></strong>
            </div>
            <div class="diary-stat">
                <small>Status da mentoria</small>
                <strong><i class="bi bi-circle-fill text-success me-1" style="font-size:8px"></i><%= HtmlUtil.escapar(mentoriaAtual.getStatus()) %></strong>
            </div>
            <div class="diary-stat">
                <small>Registros compartilhados</small>
                <strong><%= registros.size() %> registro(s)</strong>
            </div>
        </div>

        <div class="row g-3 align-items-start">
            <div class="col-lg-5">
                <section class="box-form diary-composer">
                    <div class="section-title">
                        <div>
                            <h5><i class="bi bi-journal-plus text-success me-1"></i><%= ehMentor ? "Novo registro" : "Nova reflexão" %></h5>
                            <small>Visível apenas para os participantes desta mentoria.</small>
                        </div>
                    </div>
                    <form action="<%= c %>/diario" method="post">
                        <input type="hidden" name="idMentoria" value="<%= selecionada %>">
                        <label class="form-label fw-semibold" for="conteudoRegistro"><%= ehMentor ? "Evolução, decisões e próximos passos" : "Aprendizados, dúvidas e próximos passos" %></label>
                        <textarea class="form-control" id="conteudoRegistro" name="conteudo" maxlength="5000" placeholder="<%= ehMentor ? "Registre os temas abordados, a evolução observada e o que será trabalhado no próximo encontro..." : "Conte o que você aprendeu, as dúvidas que permaneceram e o que pretende desenvolver..." %>" required></textarea>
                        <div class="d-flex flex-wrap gap-1 mt-2 mb-3">
                            <button type="button" class="prompt-chip" data-prompt="Temas abordados: ">Temas abordados</button>
                            <button type="button" class="prompt-chip" data-prompt="Aprendizados: ">Aprendizados</button>
                            <button type="button" class="prompt-chip" data-prompt="Próximos passos: ">Próximos passos</button>
                        </div>
                        <div class="d-flex justify-content-between align-items-center gap-2">
                            <small class="text-muted"><i class="bi bi-shield-check"></i> Registro compartilhado com <%= HtmlUtil.escapar(participantes.get(selecionada)) %></small>
                            <button class="btn btn-success"><i class="bi bi-save2"></i> Salvar</button>
                        </div>
                    </form>
                </section>
            </div>

            <div class="col-lg-7">
                <section class="box">
                    <div class="section-title">
                        <div>
                            <h5>Histórico de registros</h5>
                            <small>Registros do mentor e do mentorado em ordem cronológica.</small>
                        </div>
                    </div>
                    <% if (registros.isEmpty()) { %>
                    <div class="empty-state">
                        <i class="bi bi-journal-x"></i>
                        <h6>O diário ainda está vazio</h6>
                        <p class="mb-0">Escreva o primeiro registro desta mentoria.</p>
                    </div>
                    <% } else { %>
                    <div class="diary-timeline">
                        <% for (DiarioBordo r : registros) {
                            boolean autorMentorado = "Mentorado".equalsIgnoreCase(r.getTipoAutor());
                        %>
                        <article class="diary-entry <%= autorMentorado ? "mentorado" : "mentor" %>">
                            <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
                                <div class="entry-author">
                                    <span class="entry-avatar"><i class="bi <%= autorMentorado ? "bi-mortarboard-fill" : "bi-person-workspace" %>"></i></span>
                                    <div>
                                        <strong><%= HtmlUtil.escapar(autores.get(r.getIdDiario())) %></strong>
                                        <div><span class="tag-autor <%= autorMentorado ? "mentorado" : "" %>"><%= HtmlUtil.escapar(r.getTipoAutor()) %></span></div>
                                    </div>
                                </div>
                                <small class="text-muted"><i class="bi bi-clock-history"></i> <%= r.getDataRegistro() == null ? "" : r.getDataRegistro().format(formato) %></small>
                            </div>
                            <p class="entry-content"><%= HtmlUtil.escapar(r.getConteudo()) %></p>
                        </article>
                        <% } %>
                    </div>
                    <% } %>
                </section>
            </div>
        </div>
        <% } else { %>
        <div class="box empty-state">
            <i class="bi bi-journal-bookmark"></i>
            <h5>Nenhum diário disponível</h5>
            <p class="mb-0">Você precisa ter uma mentoria ativa ou finalizada para compartilhar registros.</p>
        </div>
        <% } %>
    </main>

    <script>
    document.addEventListener('DOMContentLoaded', function () {
        const campo = document.getElementById('conteudoRegistro');
        document.querySelectorAll('.prompt-chip').forEach(function (botao) {
            botao.addEventListener('click', function () {
                if (!campo) return;
                const prefixo = botao.dataset.prompt;
                campo.value += (campo.value && !campo.value.endsWith('\n') ? '\n\n' : '') + prefixo;
                campo.focus();
                campo.setSelectionRange(campo.value.length, campo.value.length);
            });
        });
    });
    </script>
</body>
</html>
