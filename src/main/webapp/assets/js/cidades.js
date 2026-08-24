document.addEventListener("DOMContentLoaded", function () {
    configurarSelecaoDeCidades("estadoMentor", "cidadeMentor");
    configurarSelecaoDeCidades("estadoMentorado", "cidadeMentorado");
});

function configurarSelecaoDeCidades(estadoId, cidadeId) {
    var estadoSelect = document.getElementById(estadoId);
    var cidadeSelect = document.getElementById(cidadeId);

    /*
     * Como o mesmo JavaScript será usado em páginas diferentes,
     * encerramos a função caso os campos não existam na página atual.
     */
    if (!estadoSelect || !cidadeSelect) {
        return;
    }

    estadoSelect.addEventListener("change", function () {
        var ufSelecionada = estadoSelect.value;

        if (!ufSelecionada) {
            limparCidades(cidadeSelect);
            return;
        }

        carregarCidades(ufSelecionada, cidadeSelect);
    });

    /*
     * Também carrega as cidades caso a página seja aberta
     * com um estado já selecionado, como numa tela de edição.
     */
    if (estadoSelect.value) {
        carregarCidades(estadoSelect.value, cidadeSelect);
    }
}

function carregarCidades(uf, cidadeSelect) {
    cidadeSelect.disabled = true;
    cidadeSelect.innerHTML =
        '<option value="">Carregando cidades...</option>';

    var url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/"
        + encodeURIComponent(uf)
        + "/municipios?orderBy=nome";

    fetch(url)
        .then(function (resposta) {
            if (!resposta.ok) {
                throw new Error(
                    "Erro ao consultar as cidades: " + resposta.status
                );
            }

            return resposta.json();
        })
        .then(function (cidades) {
            cidadeSelect.innerHTML =
                '<option value="">Selecione a cidade</option>';

            cidades.forEach(function (cidade) {
                var opcao = document.createElement("option");

                /*
                 * O nome será enviado posteriormente ao Servlet
                 * pelo parâmetro chamado "cidade".
                 */
                opcao.value = cidade.nome;
                opcao.textContent = cidade.nome;

                cidadeSelect.appendChild(opcao);
            });

            cidadeSelect.disabled = false;
        })
        .catch(function (erro) {
            console.error(erro);

            cidadeSelect.innerHTML =
                '<option value="">Não foi possível carregar as cidades</option>';

            cidadeSelect.disabled = true;
        });
}

function limparCidades(cidadeSelect) {
    cidadeSelect.innerHTML =
        '<option value="">Selecione primeiro o estado</option>';

    cidadeSelect.disabled = true;
}
