import { fetchData } from '../services/fetch.js';

export async function createPaginatedTable(config) {
    const state = {
        currentPage: 0,
        totalPages: 0,
        currentQuery: null,
    };

    const dom = {
        tableBody: document.querySelector(".table-body"),
        prevButton: document.querySelector("#btn-previous"),
        nextButton: document.querySelector("#btn-next"),
        pageIndex: document.querySelector("#page-index"),
        searchForm: document.querySelector("#search-form"),
        searchInput: document.querySelector("#search-input"),
    };

    async function loadData(page = 0, query = null) {
        state.currentPage = page;
        state.currentQuery = query;

        // Decisão de qual serviço chamar (busca ou listagem)
        const request = query 
            ? config.onFilter(query, page) 
            : config.onLoad(page);

        const { data, error } = await fetchData(request.url, request.options);

        if (error) {
            alert("Erro ao carregar dados: " + (error.message || "Erro desconhecido"));
            return;
        }

        // No Spring, os dados vêm dentro de 'content'
        state.totalPages = data.totalPages;
        render(data.content || []);
        updateUI();
    }

    function render(items) {
        dom.tableBody.innerHTML = items.length > 0 
            ? items.map(item => `<tr>${config.renderRow(item)}</tr>`).join('')
            : `<tr><td colspan="6" style="text-align:center; padding: 2rem; color: #6c757d;">Nenhum registro encontrado.</td></tr>`; 
    }

    function updateUI() {
        dom.pageIndex.textContent = state.currentPage + 1;
        dom.prevButton.disabled = state.currentPage === 0;
        dom.nextButton.disabled = state.currentPage >= state.totalPages - 1 || state.totalPages === 0;
    }

    // Eventos de Navegação
    dom.prevButton.onclick = () => loadData(state.currentPage - 1, state.currentQuery);
    dom.nextButton.onclick = () => loadData(state.currentPage + 1, state.currentQuery);

    // Evento de Busca
    if (dom.searchForm) {
        dom.searchForm.onsubmit = (e) => {
            e.preventDefault();
            const term = dom.searchInput.value.trim();
            loadData(0, term || null);
        };
    }

    // Inicialização
    await loadData(0);
}