<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.AreaAtuacaoDao" %>
<%@ page import="model.AreaAtuacao" %>
<%@ page import="util.HtmlUtil" %>
<%
ArrayList<AreaAtuacao> areasDisponiveis = new AreaAtuacaoDao().listar();
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cadastro de Mentorado</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
    <link rel="stylesheet" href="../assets/css/cadastroMentorado.css">

    <style>
        .select-tags-wrapper {
            position: relative;
        }

        .select-tags-display {
            min-height: 38px;
            padding: 4px 36px 4px 12px;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            background-color: #fff;
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            gap: 4px;
            cursor: pointer;
            position: relative;
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%23343a40' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='m2 5 6 6 6-6'/%3e%3c/svg%3e");
            background-repeat: no-repeat;
            background-position: right 0.75rem center;
            background-size: 16px 12px;
            transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
        }

        .select-tags-display:focus-within,
        .select-tags-display.active {
            border-color: #2e7d32;
            box-shadow: 0 0 0 0.25rem rgba(46, 125, 50, 0.25);
            outline: 0;
        }

        .select-tag-badge {
            background-color: #e8f5e9;
            color: #2e7d32;
            font-size: 0.775rem;
            font-weight: 500;
            padding: 2px 6px;
            border-radius: 4px;
            display: inline-flex;
            align-items: center;
            gap: 3px;
            line-height: 1.2;
        }

        .select-tag-badge i {
            cursor: pointer;
            font-size: 0.85rem;
        }

        .select-tags-placeholder {
            color: #6c757d;
            font-size: 0.875rem;
            user-select: none;
        }

        .select-tags-dropdown {
            position: absolute;
            top: calc(100% + 4px);
            left: 0;
            right: 0;
            background: #fff;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            z-index: 1050;
            max-height: 200px;
            overflow-y: auto;
            padding: 4px;
            display: none;
        }

        .select-tags-dropdown.show {
            display: block;
        }

        .select-option-item {
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 0.875rem;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: space-between;
            transition: background 0.15s;
        }

        .select-option-item:hover {
            background-color: #f8f9fa;
        }

        .select-option-item.selected {
            background-color: #e8f5e9;
            color: #2e7d32;
            font-weight: 600;
        }
    </style>
</head>

<body>

    <aside class="sidebar">
        <div class="logo">
            <i class="bi bi-calendar-event-fill"></i>
            <br>
            Mentoria
        </div>

        <a href="../dashboard"><i class="bi bi-speedometer2"></i> Dashboard</a>
        <a href="Cadastrode_Mentor.jsp"><i class="bi bi-person-workspace"></i> Mentores</a>
        <a href="cadastroMentorado.jsp" class="active"><i class="bi bi-mortarboard-fill"></i> Mentorados</a>
        <a href="login.html"><i class="bi bi-box-arrow-left"></i> Sair</a>
    </aside>

    <main class="content">
        <section class="formulario">
            <div class="text-center cabecalho-formulario">
                <div class="perfil">
                    <i class="bi bi-mortarboard-fill"></i>
                </div>
                <h2 class="text-success">Cadastro de Mentorado</h2>
                <p class="text-muted">Encontre um mentor para acelerar sua carreira na área de eventos.</p>
                <span class="badge-etapa">Etapa 1 de 1</span>
            </div>

            <hr>

            <% if ("cadastro".equals(request.getParameter("erro"))) { %>
            <div class="alert alert-danger">CPF ou e-mail já cadastrado, ou não foi possível salvar.</div>
            <% } else if ("dados".equals(request.getParameter("erro"))) { %>
            <div class="alert alert-warning">Selecione pelo menos uma área de interesse.</div>
            <% } %>

            <form id="formMentorado" action="<%= request.getContextPath() %>/cadastro-mentorado" method="post">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label for="nomeMentorado" class="form-label fw-semibold">Nome Completo</label>
                        <input type="text" class="form-control" id="nomeMentorado" name="nome" placeholder="Digite seu nome" required>
                    </div>

                    <div class="col-md-6">
                        <label for="emailMentorado" class="form-label fw-semibold">Email</label>
                        <input type="email" class="form-control" id="emailMentorado" name="email" placeholder="Digite seu email" required>
                    </div>

                    <div class="col-md-6">
                        <label for="cpfMentorado" class="form-label fw-semibold">CPF</label>
                        <input type="text" class="form-control" id="cpfMentorado" name="cpf" maxlength="14" placeholder="000.000.000-00" required>
                    </div>

                    <div class="col-md-6">
                        <label for="senhaMentorado" class="form-label fw-semibold">Senha</label>
                        <input type="password" class="form-control" id="senhaMentorado" name="senha" minlength="6" placeholder="Mínimo de 6 caracteres" required>
                    </div>

                    <div class="col-md-3">
                        <label for="telefoneMentorado" class="form-label fw-semibold">Telefone</label>
                        <input type="tel" class="form-control" id="telefoneMentorado" name="telefone" placeholder="(00) 00000-0000" required>
                    </div>

                    <div class="col-md-3">
                        <label for="formacaoMentorado" class="form-label fw-semibold">Formação</label>
                        <input type="text" class="form-control" id="formacaoMentorado" name="formacao" placeholder="Ex.: Administração" required>
                    </div>

                    <div class="col-md-2">
                        <label for="estadoMentorado" class="form-label fw-semibold">Estado</label>
                        <select class="form-select" id="estadoMentorado" name="estado" required>
                            <option value="" selected disabled>UF</option>
                            <option value="AC">AC</option><option value="AL">AL</option><option value="AP">AP</option>
                            <option value="AM">AM</option><option value="BA">BA</option><option value="CE">CE</option>
                            <option value="DF">DF</option><option value="ES">ES</option><option value="GO">GO</option>
                            <option value="MA">MA</option><option value="MT">MT</option><option value="MS">MS</option>
                            <option value="MG">MG</option><option value="PA">PA</option><option value="PB">PB</option>
                            <option value="PR">PR</option><option value="PE">PE</option><option value="PI">PI</option>
                            <option value="RJ">RJ</option><option value="RN">RN</option><option value="RS">RS</option>
                            <option value="RO">RO</option><option value="RR">RR</option><option value="SC">SC</option>
                            <option value="SP">SP</option><option value="SE">SE</option><option value="TO">TO</option>
                        </select>
                    </div>

                    <div class="col-md-4">
                        <label for="cidadeMentorado" class="form-label fw-semibold">Cidade</label>
                        <input type="text" class="form-control" id="cidadeMentorado" name="cidade"
                               maxlength="100" placeholder="Digite sua cidade" required>
                    </div>

                    <div class="col-md-6">
                        <label for="nivelExperiencia" class="form-label fw-semibold">Nível de Experiência</label>
                        <select class="form-select" id="nivelExperiencia" name="nivelExperiencia" required>
                            <option value="" selected disabled>Selecione seu nível</option>
                            <option value="Iniciante">Iniciante</option>
                            <option value="Intermediário">Intermediário</option>
                            <option value="Avançado">Avançado</option>
                        </select>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Áreas de Interesse</label>
                        <div class="select-tags-wrapper" id="componenteAreasInteresse">
                            <div class="select-tags-display" tabindex="0">
                                <span class="select-tags-placeholder">Selecione uma ou mais áreas...</span>
                            </div>
                            <div class="select-tags-dropdown">
                                <% for (AreaAtuacao area : areasDisponiveis) { %>
                                <div class="select-option-item"
                                     data-value="<%= area.getIdAreaAtuacao() %>"
                                     data-label="<%= HtmlUtil.escapar(area.getNomeArea()) %>"><%= HtmlUtil.escapar(area.getNomeArea()) %></div>
                                <% } %>
                            </div>
                            <div class="hidden-inputs-container"></div>
                        </div>
                    </div>

                    <div class="col-md-6">
                        <label for="objetivosMentorado" class="form-label fw-semibold">Objetivos Profissionais</label>
                        <textarea class="form-control" id="objetivosMentorado" name="objetivos" placeholder="Descreva seus objetivos na área de eventos..." rows="3" required></textarea>
                    </div>

                    <div class="col-md-6">
                        <label for="duvidasMentorado" class="form-label fw-semibold">Principais Dúvidas</label>
                        <textarea class="form-control" id="duvidasMentorado" name="duvidas" placeholder="Quais são suas principais dúvidas profissionais?" rows="3" required></textarea>
                    </div>

                    <div class="col-md-6">
                        <label for="expectativasMentorado" class="form-label fw-semibold">Expectativas</label>
                        <textarea class="form-control" id="expectativasMentorado" name="expectativas" placeholder="O que você espera aprender com a mentoria?" rows="3" required></textarea>
                    </div>

                    <div class="col-12 mt-2">
                        <% if (areasDisponiveis.isEmpty()) { %>
                        <div class="alert alert-warning">O administrador ainda não cadastrou áreas de atuação.</div>
                        <% } %>
                        <button type="submit" class="btn btn-success w-100 py-2 fw-semibold">
                            <i class="bi bi-person-plus-fill me-1"></i> Cadastrar Mentorado
                        </button>
                    </div>
                </div>
            </form>
        </section>
    </main>

    <script>
        function inicializarDropdownTags(wrapperId, inputName) {
            const wrapper = document.getElementById(wrapperId);
            const display = wrapper.querySelector('.select-tags-display');
            const dropdown = wrapper.querySelector('.select-tags-dropdown');
            const placeholder = wrapper.querySelector('.select-tags-placeholder');
            const hiddenContainer = wrapper.querySelector('.hidden-inputs-container');
            const options = wrapper.querySelectorAll('.select-option-item');

            let selecionados = new Map();

            display.addEventListener('click', (e) => {
                if (!e.target.classList.contains('bi-x')) {
                    dropdown.classList.toggle('show');
                    display.classList.toggle('active');
                }
            });

            document.addEventListener('click', (e) => {
                if (!wrapper.contains(e.target)) {
                    dropdown.classList.remove('show');
                    display.classList.remove('active');
                }
            });

            options.forEach(opt => {
                opt.addEventListener('click', () => {
                    const val = opt.dataset.value;
                    const label = opt.dataset.label || val;
                    if (selecionados.has(val)) {
                        selecionados.delete(val);
                        opt.classList.remove('selected');
                    } else {
                        selecionados.set(val, label);
                        opt.classList.add('selected');
                    }
                    atualizarVisualizacao();
                });
            });

            function atualizarVisualizacao() {
                display.querySelectorAll('.select-tag-badge').forEach(b => b.remove());
                hiddenContainer.innerHTML = '';

                if (selecionados.size === 0) {
                    placeholder.style.display = 'inline';
                } else {
                    placeholder.style.display = 'none';
                    selecionados.forEach((label, val) => {
                        const badge = document.createElement('span');
                        badge.className = 'select-tag-badge';
                        badge.appendChild(document.createTextNode(label + ' '));
                        const remover = document.createElement('i');
                        remover.className = 'bi bi-x';
                        remover.dataset.val = val;
                        badge.appendChild(remover);
                        remover.addEventListener('click', (e) => {
                            e.stopPropagation();
                            selecionados.delete(val);
                            const opcao = wrapper.querySelector(
                                '.select-option-item[data-value="' + val + '"]'
                            );
                            if (opcao) opcao.classList.remove('selected');
                            atualizarVisualizacao();
                        });
                        display.appendChild(badge);

                        const hidden = document.createElement('input');
                        hidden.type = 'hidden';
                        hidden.name = inputName + '[]';
                        hidden.value = val;
                        hiddenContainer.appendChild(hidden);
                    });
                }
            }
        }

        document.addEventListener('DOMContentLoaded', () => {
            inicializarDropdownTags('componenteAreasInteresse', 'areasInteresse');
        });
    </script>
</body>

</html>
