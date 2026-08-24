<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList,java.util.Map,java.time.format.DateTimeFormatter,model.*,util.HtmlUtil" %>
<%
Object usuario = request.getAttribute("usuario");
boolean ehMentor = usuario instanceof Mentor;
String nomeUsuario = ehMentor ? ((Mentor) usuario).getNome() : ((Mentorado) usuario).getNome();
ArrayList<Mentoria> mentorias = (ArrayList<Mentoria>) request.getAttribute("mentorias");
ArrayList<Encontro> encontros = (ArrayList<Encontro>) request.getAttribute("encontros");
Map<Integer, String> participantes = (Map<Integer, String>) request.getAttribute("participantes");
int selecionada = (Integer) request.getAttribute("mentoriaSelecionada");
String c = request.getContextPath();
DateTimeFormatter horaBr = DateTimeFormatter.ofPattern("HH:mm");
boolean temMentoriaAtiva = false;
for (Mentoria m : mentorias) {
    if ("Ativa".equalsIgnoreCase(m.getStatus())) temMentoriaAtiva = true;
}
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agenda de Encontros - Miracle Mentorias</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/global.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/<%= ehMentor ? "agendaEncontrosMentor" : "agendaEncontrosMentorado" %>.css">
    <link rel="stylesheet" href="<%= c %>/assets/css/painelFuncional.css">
</head>
<body class="agenda-page">
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
        <a href="<%= c %>/diario"><i class="bi bi-journal-text"></i> Diário de Bordo</a>
        <a class="active" href="<%= c %>/agenda"><i class="bi bi-calendar-check"></i> Agenda de Encontros</a>
        <a href="<%= c %>/avaliacoes"><i class="bi bi-star-fill"></i> Feedbacks<%= ehMentor ? " Recebidos" : "" %></a>
        <a href="<%= c %>/perfil/<%= ehMentor ? "mentor" : "mentorado" %>"><i class="bi bi-person-circle"></i> Meu Perfil</a>
        <a href="<%= c %>/logout"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </div>

    <main class="content">
        <div class="header">
            <div class="page-heading">
                <span class="page-heading-icon"><i class="bi bi-calendar2-week"></i></span>
                <div>
                    <h2>Agenda de Encontros</h2>
                    <p class="text-muted"><%= ehMentor ? "Organize e acompanhe os encontros das suas mentorias." : "Acompanhe os encontros marcados pelos seus mentores." %></p>
                </div>
            </div>
            <div class="d-flex align-items-center gap-2 flex-wrap">
                <div class="user user-role">
                    <i class="bi bi-person-circle"></i>
                    <span><%= HtmlUtil.escapar(nomeUsuario) %></span>
                    <span class="role-pill"><%= ehMentor ? "Mentor" : "Mentorado" %></span>
                </div>
                <% if (ehMentor && temMentoriaAtiva) { %>
                <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#modalNovoEncontro">
                    <i class="bi bi-calendar-plus"></i> Novo encontro
                </button>
                <% } %>
            </div>
        </div>

        <% if ("sucesso".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-success soft-alert"><i class="bi bi-check-circle-fill me-2"></i>Encontro agendado com sucesso.</div>
        <% } else if ("erro".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-danger soft-alert"><i class="bi bi-exclamation-triangle-fill me-2"></i>Confira a mentoria, a data e o horário informados.</div>
        <% } else if ("status_sucesso".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-success soft-alert"><i class="bi bi-check-circle-fill me-2"></i>Situação do encontro atualizada com sucesso.</div>
        <% } else if ("status_erro".equals(request.getParameter("resultado"))) { %>
        <div class="alert alert-danger soft-alert"><i class="bi bi-exclamation-triangle-fill me-2"></i>Não foi possível atualizar. Um encontro já concluído não pode ser alterado.</div>
        <% } %>

        <div class="box selection-strip">
            <form method="get" action="<%= c %>/agenda" class="row g-2 align-items-end">
                <div class="col-md-9">
                    <label class="form-label fw-semibold" for="filtroMentoria">Visualizar agenda da mentoria</label>
                    <select class="form-select" id="filtroMentoria" name="mentoria">
                        <option value="0">Todas as mentorias</option>
                        <% for (Mentoria m : mentorias) { %>
                        <option value="<%= m.getIdMentoria() %>" <%= selecionada == m.getIdMentoria() ? "selected" : "" %>>
                            Mentoria #<%= m.getIdMentoria() %> — <%= HtmlUtil.escapar(participantes.get(m.getIdMentoria())) %>
                        </option>
                        <% } %>
                    </select>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-outline-success w-100"><i class="bi bi-funnel"></i> Aplicar filtro</button>
                </div>
            </form>
        </div>

        <% if (mentorias.isEmpty()) { %>
        <div class="box empty-state">
            <i class="bi bi-calendar2-x"></i>
            <h5>Nenhuma mentoria disponível</h5>
            <p class="mb-0">Quando uma mentoria estiver ativa ou finalizada, seus encontros aparecerão aqui.</p>
        </div>
        <% } else { %>
        <div class="row agenda-layout">
            <div class="col-xl-7">
                <section class="box-calendario agenda-calendar">
                    <div class="calendario-nav">
                        <button type="button" id="btnMesAnterior" aria-label="Mês anterior"><i class="bi bi-chevron-left"></i></button>
                        <h5 id="mesAnoAtual"></h5>
                        <button type="button" id="btnMesProximo" aria-label="Próximo mês"><i class="bi bi-chevron-right"></i></button>
                    </div>
                    <div class="grid-dias" id="gridDias"></div>
                    <div class="legenda-status">
                        <div class="item-legenda"><span class="flag-status flag-realizado"></span><span>Realizado</span></div>
                        <div class="item-legenda"><span class="flag-status flag-agendado flag-pendente"></span><span>Agendado</span></div>
                        <div class="item-legenda"><span class="flag-status flag-nao-realizado"></span><span>Não realizado</span></div>
                    </div>
                    <p class="calendar-hint mb-0"><i class="bi bi-cursor-fill"></i> Clique em uma data marcada para filtrar os encontros.</p>
                </section>
            </div>

            <div class="col-xl-5">
                <section class="box events-panel">
                    <div class="section-title">
                        <div>
                            <h5>Encontros</h5>
                            <small id="eventosResumo"><%= encontros.size() %> encontro(s) encontrado(s)</small>
                        </div>
                        <button type="button" class="btn btn-sm btn-outline-success d-none" id="limparDia">Mostrar todos</button>
                    </div>
                    <div class="events-scroll" id="listaEncontros">
                        <% for (Encontro e : encontros) {
                            String status = e.getStatus() == null || e.getStatus().isBlank() ? "Agendado" : e.getStatus();
                            String statusNormalizado = status.toLowerCase();
                            String statusCss = statusNormalizado.contains("não") || statusNormalizado.contains("nao")
                                    ? "nao-realizado" : statusNormalizado.contains("realizado") ? "realizado" : "agendado";
                            String tipo = e.getTipoEncontro() == null ? "" : e.getTipoEncontro();
                            boolean online = "online".equalsIgnoreCase(tipo);
                        %>
                        <article class="event-card" data-date="<%= e.getData() %>" data-status="<%= statusCss %>">
                            <div class="data-box">
                                <span class="dia-num"><%= e.getData() == null ? "--" : String.format("%02d", e.getData().getDayOfMonth()) %></span>
                                <span class="mes-abrev"><%= e.getData() == null ? "" : e.getData().getMonth().toString().substring(0, 3) %></span>
                            </div>
                            <div class="flex-grow-1">
                                <div class="d-flex justify-content-between align-items-start gap-2">
                                    <div>
                                        <h6><%= HtmlUtil.escapar(participantes.get(e.getIdMentoria())) %></h6>
                                        <div class="event-meta">
                                            <span><i class="bi bi-clock"></i> <%= e.getHorario() == null ? "--:--" : e.getHorario().format(horaBr) %></span>
                                            <span><i class="bi bi-people"></i> Mentoria #<%= e.getIdMentoria() %></span>
                                        </div>
                                    </div>
                                    <span class="status-pill status-<%= statusCss %>"><%= HtmlUtil.escapar(status) %></span>
                                </div>
                                <p><%= HtmlUtil.escapar(e.getDescricao()) %></p>
                                <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                                    <span class="type-pill <%= online ? "type-online" : "type-presencial" %>">
                                        <i class="bi <%= online ? "bi-camera-video" : "bi-geo-alt" %> me-1"></i><%= HtmlUtil.escapar(tipo) %>
                                    </span>
                                    <% if (online && e.getLinkReuniao() != null && !e.getLinkReuniao().isBlank()) { %>
                                    <a target="_blank" rel="noopener" href="<%= HtmlUtil.escapar(e.getLinkReuniao()) %>" class="btn btn-sm btn-success"><i class="bi bi-box-arrow-up-right"></i> Acessar</a>
                                    <% } else if (!online && e.getLocalEncontro() != null && !e.getLocalEncontro().isBlank()) { %>
                                    <small class="text-muted"><i class="bi bi-pin-map text-success"></i> <%= HtmlUtil.escapar(e.getLocalEncontro()) %></small>
                                    <% } %>
                                </div>
                                <% if ("nao-realizado".equals(statusCss)
                                        && e.getMotivoNaoRealizacao() != null
                                        && !e.getMotivoNaoRealizacao().isBlank()) { %>
                                <div class="meeting-reason">
                                    <i class="bi bi-info-circle"></i>
                                    <span><strong>Motivo:</strong> <%= HtmlUtil.escapar(e.getMotivoNaoRealizacao()) %></span>
                                </div>
                                <% } %>
                                <% if (ehMentor && "agendado".equals(statusCss)) { %>
                                <div class="meeting-result-actions">
                                    <span>Este encontro aconteceu?</span>
                                    <form method="post" action="<%= c %>/agenda/status" onsubmit="return confirm('Confirmar que este encontro aconteceu?');">
                                        <input type="hidden" name="idEncontro" value="<%= e.getIdEncontro() %>">
                                        <input type="hidden" name="idMentoria" value="<%= e.getIdMentoria() %>">
                                        <input type="hidden" name="status" value="Realizado">
                                        <button type="submit" class="btn btn-sm btn-success"><i class="bi bi-check2-circle"></i> Aconteceu</button>
                                    </form>
                                    <button type="button" class="btn btn-sm btn-outline-danger btn-nao-realizado"
                                            data-bs-toggle="modal" data-bs-target="#modalNaoRealizado"
                                            data-encontro="<%= e.getIdEncontro() %>"
                                            data-mentoria="<%= e.getIdMentoria() %>">
                                        <i class="bi bi-x-circle"></i> Não aconteceu
                                    </button>
                                </div>
                                <% } %>
                            </div>
                        </article>
                        <% } %>
                        <% if (encontros.isEmpty()) { %>
                        <div class="agenda-empty" id="agendaVazia">
                            <i class="bi bi-calendar2-heart"></i>
                            <strong>Nenhum encontro agendado</strong>
                            <p class="mb-0 mt-1"><%= ehMentor ? "Use o botão “Novo encontro” para começar." : "Seu mentor ainda não marcou um encontro." %></p>
                        </div>
                        <% } %>
                        <div class="agenda-empty d-none" id="diaSemEncontro">
                            <i class="bi bi-calendar2-x"></i>
                            <strong>Sem encontros nesta data</strong>
                            <p class="mb-0 mt-1">Escolha outra data ou mostre todos.</p>
                        </div>
                    </div>
                </section>
            </div>
        </div>
        <% } %>
    </main>

    <% if (ehMentor && temMentoriaAtiva) { %>
    <div class="modal fade" id="modalNovoEncontro" tabindex="-1" aria-labelledby="tituloModalNovoEncontro" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <div>
                        <h5 class="modal-title text-success" id="tituloModalNovoEncontro"><i class="bi bi-calendar-plus"></i> Agendar novo encontro</h5>
                        <small class="text-muted">O encontro ficará visível imediatamente para o mentorado.</small>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
                </div>
                <form method="post" action="<%= c %>/agenda" id="formNovoEncontro">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="mentoriaEncontro" class="form-label fw-semibold">Mentoria</label>
                            <select class="form-select" id="mentoriaEncontro" name="idMentoria" required>
                                <option value="">Selecione a mentoria</option>
                                <% for (Mentoria m : mentorias) { if (!"Ativa".equalsIgnoreCase(m.getStatus())) continue; %>
                                <option value="<%= m.getIdMentoria() %>" <%= selecionada == m.getIdMentoria() ? "selected" : "" %>>
                                    #<%= m.getIdMentoria() %> — <%= HtmlUtil.escapar(participantes.get(m.getIdMentoria())) %>
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="dataEncontro" class="form-label fw-semibold">Data</label>
                                <input type="date" class="form-control" id="dataEncontro" name="data" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="horarioEncontro" class="form-label fw-semibold">Horário</label>
                                <input type="time" class="form-control" id="horarioEncontro" name="horario" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-5 mb-3">
                                <label for="tipoEncontro" class="form-label fw-semibold">Tipo de encontro</label>
                                <select class="form-select" id="tipoEncontro" name="tipo" required>
                                    <option value="Online">Online</option>
                                    <option value="Presencial">Presencial</option>
                                </select>
                            </div>
                            <div class="col-md-7 mb-3" id="campoLinkEncontro">
                                <label for="linkEncontro" class="form-label fw-semibold">Link da reunião</label>
                                <input type="url" class="form-control" id="linkEncontro" name="link" placeholder="https://meet.google.com/...">
                            </div>
                            <div class="col-md-7 mb-3 d-none" id="campoLocalEncontro">
                                <label for="localEncontro" class="form-label fw-semibold">Local do encontro</label>
                                <input type="text" class="form-control" id="localEncontro" name="local" placeholder="Informe o endereço ou local">
                            </div>
                        </div>
                        <div>
                            <label for="descricaoEncontro" class="form-label fw-semibold">Descrição ou pauta</label>
                            <textarea class="form-control campo-descricao" id="descricaoEncontro" name="descricao" maxlength="5000" placeholder="Assuntos que serão tratados no encontro..." required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer border-0 pt-0">
                        <button type="button" class="btn btn-outline-success" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-success"><i class="bi bi-check-lg"></i> Confirmar encontro</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <% } %>

    <% if (ehMentor) { %>
    <div class="modal fade" id="modalNaoRealizado" tabindex="-1" aria-labelledby="tituloModalNaoRealizado" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <div>
                        <h5 class="modal-title text-danger" id="tituloModalNaoRealizado"><i class="bi bi-calendar-x"></i> Encontro não realizado</h5>
                        <small class="text-muted">Informe o motivo. Ele também ficará visível para o mentorado.</small>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Fechar"></button>
                </div>
                <form method="post" action="<%= c %>/agenda/status" id="formNaoRealizado">
                    <div class="modal-body">
                        <input type="hidden" name="idEncontro" id="idEncontroResultado">
                        <input type="hidden" name="idMentoria" id="idMentoriaResultado">
                        <input type="hidden" name="status" value="Não realizado">
                        <label for="motivoNaoRealizacao" class="form-label fw-semibold">Motivo</label>
                        <textarea class="form-control campo-descricao" id="motivoNaoRealizacao" name="motivo"
                                  maxlength="1000" rows="4" required
                                  placeholder="Ex.: o mentorado informou que não poderia comparecer."></textarea>
                    </div>
                    <div class="modal-footer border-0 pt-0">
                        <button type="button" class="btn btn-outline-success" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-danger"><i class="bi bi-check-lg"></i> Registrar motivo</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <% } %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    document.addEventListener('DOMContentLoaded', function () {
        const grid = document.getElementById('gridDias');
        const tituloMes = document.getElementById('mesAnoAtual');
        const cards = Array.from(document.querySelectorAll('.event-card'));
        const resumo = document.getElementById('eventosResumo');
        const limparDia = document.getElementById('limparDia');
        const diaSemEncontro = document.getElementById('diaSemEncontro');
        const nomesMeses = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
        const cabecalho = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
        const statusPorData = {};

        cards.forEach(function (card) {
            const data = card.dataset.date;
            const status = card.dataset.status || 'agendado';
            if (!statusPorData[data] || status === 'nao-realizado' || status === 'agendado') statusPorData[data] = status;
        });

        let referencia = cards.length > 0 ? new Date(cards[0].dataset.date + 'T12:00:00') : new Date();

        function isoDate(ano, mes, dia) {
            return ano + '-' + String(mes + 1).padStart(2, '0') + '-' + String(dia).padStart(2, '0');
        }

        function selecionarData(dataIso, botao) {
            let visiveis = 0;
            cards.forEach(function (card) {
                const mostrar = card.dataset.date === dataIso;
                card.classList.toggle('is-hidden', !mostrar);
                if (mostrar) visiveis++;
            });
            document.querySelectorAll('.dia.selecionado').forEach(function (dia) { dia.classList.remove('selecionado'); });
            botao.classList.add('selecionado');
            if (resumo) resumo.textContent = visiveis + ' encontro(s) em ' + dataIso.split('-').reverse().join('/');
            if (limparDia) limparDia.classList.remove('d-none');
            if (diaSemEncontro) diaSemEncontro.classList.toggle('d-none', visiveis !== 0);
        }

        function mostrarTodos() {
            cards.forEach(function (card) { card.classList.remove('is-hidden'); });
            document.querySelectorAll('.dia.selecionado').forEach(function (dia) { dia.classList.remove('selecionado'); });
            if (resumo) resumo.textContent = cards.length + ' encontro(s) encontrado(s)';
            if (limparDia) limparDia.classList.add('d-none');
            if (diaSemEncontro) diaSemEncontro.classList.add('d-none');
        }

        function renderizarCalendario() {
            if (!grid || !tituloMes) return;
            grid.innerHTML = '';
            cabecalho.forEach(function (nome) {
                const item = document.createElement('div');
                item.className = 'dia-semana';
                item.textContent = nome;
                grid.appendChild(item);
            });
            const ano = referencia.getFullYear();
            const mes = referencia.getMonth();
            const primeiroDia = new Date(ano, mes, 1).getDay();
            const diasNoMes = new Date(ano, mes + 1, 0).getDate();
            const diasMesAnterior = new Date(ano, mes, 0).getDate();
            tituloMes.textContent = nomesMeses[mes] + ' de ' + ano;

            for (let i = primeiroDia - 1; i >= 0; i--) {
                const vazio = document.createElement('span');
                vazio.className = 'dia outro-mes';
                vazio.textContent = diasMesAnterior - i;
                grid.appendChild(vazio);
            }
            const hoje = new Date();
            for (let dia = 1; dia <= diasNoMes; dia++) {
                const dataIso = isoDate(ano, mes, dia);
                const botao = document.createElement('button');
                botao.type = 'button';
                botao.className = 'dia border-0';
                botao.textContent = dia;
                if (ano === hoje.getFullYear() && mes === hoje.getMonth() && dia === hoje.getDate()) botao.classList.add('hoje');
                if (statusPorData[dataIso]) botao.classList.add('encontro-' + statusPorData[dataIso], 'tem-encontro');
                botao.addEventListener('click', function () { selecionarData(dataIso, botao); });
                grid.appendChild(botao);
            }
            const restantes = (7 - ((primeiroDia + diasNoMes) % 7)) % 7;
            for (let dia = 1; dia <= restantes; dia++) {
                const vazio = document.createElement('span');
                vazio.className = 'dia outro-mes';
                vazio.textContent = dia;
                grid.appendChild(vazio);
            }
        }

        const anterior = document.getElementById('btnMesAnterior');
        const proximo = document.getElementById('btnMesProximo');
        if (anterior) anterior.addEventListener('click', function () { referencia.setMonth(referencia.getMonth() - 1); mostrarTodos(); renderizarCalendario(); });
        if (proximo) proximo.addEventListener('click', function () { referencia.setMonth(referencia.getMonth() + 1); mostrarTodos(); renderizarCalendario(); });
        if (limparDia) limparDia.addEventListener('click', mostrarTodos);
        renderizarCalendario();

        const formulario = document.getElementById('formNovoEncontro');
        if (formulario) {
            const data = document.getElementById('dataEncontro');
            const horario = document.getElementById('horarioEncontro');
            const tipo = document.getElementById('tipoEncontro');
            const campoLink = document.getElementById('campoLinkEncontro');
            const campoLocal = document.getElementById('campoLocalEncontro');
            const link = document.getElementById('linkEncontro');
            const local = document.getElementById('localEncontro');
            data.min = new Date().toLocaleDateString('en-CA');

            function atualizarTipo() {
                const presencial = tipo.value === 'Presencial';
                campoLink.classList.toggle('d-none', presencial);
                campoLocal.classList.toggle('d-none', !presencial);
                link.required = !presencial;
                local.required = presencial;
                if (presencial) link.value = ''; else local.value = '';
            }
            tipo.addEventListener('change', atualizarTipo);
            atualizarTipo();
            formulario.addEventListener('submit', function (evento) {
                horario.setCustomValidity('');
                if (data.value && horario.value && new Date(data.value + 'T' + horario.value) <= new Date()) {
                    evento.preventDefault();
                    horario.setCustomValidity('Escolha uma data e um horário posteriores ao momento atual.');
                    formulario.reportValidity();
                }
            });
        }

        document.querySelectorAll('.btn-nao-realizado').forEach(function (botao) {
            botao.addEventListener('click', function () {
                document.getElementById('idEncontroResultado').value = botao.dataset.encontro;
                document.getElementById('idMentoriaResultado').value = botao.dataset.mentoria;
                document.getElementById('motivoNaoRealizacao').value = '';
            });
        });
    });
    </script>
</body>
</html>
