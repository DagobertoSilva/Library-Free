import { createPaginatedTable } from '../modules/table-manager.js';
import { EmprestimoService } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', () => {
    
    // --- Lógica do Filtro ---
    let currentFilterType = 'tituloLivro'; // Padrão
    const btnFilterType = document.getElementById('btn-filter-type');
    const filterOptions = document.getElementById('filter-options');
    const searchInput = document.getElementById('search-input');

    if (filterOptions) {
        filterOptions.addEventListener('click', (e) => {
            if (e.target.classList.contains('dropdown-item')) {
                e.preventDefault();
                
                // Atualiza visualmente
                document.querySelectorAll('.dropdown-item').forEach(el => el.classList.remove('active'));
                e.target.classList.add('active');
                
                // Atualiza estado e texto do botão
                currentFilterType = e.target.dataset.type;
                btnFilterType.textContent = e.target.textContent;
                
                // Limpa o input e foca para nova busca
                searchInput.value = '';
                searchInput.focus();
            }
        });
    }
    
    // --- Elementos do Modal de Devolução ---
    const returnModal = document.getElementById('returnModal');
    const returnModalBody = document.getElementById('returnModalBody');
    const btnCloseReturnX = document.getElementById('closeReturnModalBtn');
    const btnCancelReturn = document.getElementById('btnCancelReturn');
    const btnConfirmReturn = document.getElementById('btnConfirmReturn');
    
    let idToReturn = null;

    // --- Funções de Formatação ---
    function formatDate(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR');
    }

    function formatDateTime(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleDateString('pt-BR') + ' ' + date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    }

    // --- Configuração da Tabela ---
    const tableConfig = {
        onLoad: (page) => EmprestimoService.getAll(page, 8),
        
        // Lógica de decisão baseada no filtro selecionado
        onFilter: (query, page) => {
            switch (currentFilterType) {
                case 'nomeAluno':
                    return EmprestimoService.getByNomeAluno(query, page, 8);
                case 'tituloLivro':
                default:
                    return EmprestimoService.getByTituloLivro(query, page, 8);
            }
        },
        
        renderRow: (emprestimo) => {
            const isReturned = emprestimo.dataDevolucao !== null;
            const statusBadgeClass = isReturned ? 'badge-status returned' : 'badge-status pending';
            const statusText = isReturned ? 'Devolvido' : 'Em Aberto';
            
            const returnIcon = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path><polyline points="9 22 9 12 15 12 15 22"></polyline></svg>`;

            const actionButton = isReturned 
                ? ''
                : `<button class="btn btn-action btn-action-return return" data-id="${emprestimo.id}" data-titulo="${emprestimo.tituloLivro}" data-aluno="${emprestimo.nomeAluno}" title="Devolver">
                    ${returnIcon}
                </button>`;

            return `
                <td class="align-middle"><div class="fw-bold text-dark">${emprestimo.tituloLivro}</div></td>
                <td class="align-middle text-secondary">${emprestimo.nomeAluno}</td>
                <td class="align-middle text-secondary">${formatDateTime(emprestimo.dataEmprestimo)}</td>
                <td class="align-middle text-secondary">${formatDate(emprestimo.prazoDevolucao)}</td>
                <td class="align-middle">
                    <span class="badge ${statusBadgeClass} rounded-pill px-3 py-2">${statusText}</span>
                </td>
                <td class="align-middle text-center">${actionButton}</td>
            `;
        }
    };

    // --- Funções de Controle do Modal de Devolução ---
    function openReturnModal(id, titulo, aluno) {
        idToReturn = id;
        if(returnModalBody) returnModalBody.textContent = `${titulo} - ${aluno}`;
        if(returnModal) returnModal.style.display = 'flex';
    }

    function closeReturnModal() {
        idToReturn = null;
        if(returnModal) returnModal.style.display = 'none';
    }

    // --- Listener da Tabela (Delegação) ---
    const tableBody = document.querySelector('.table-body');
    if (tableBody) {
        tableBody.addEventListener('click', (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;

            if (btn.classList.contains('return')) {
                const id = btn.dataset.id;
                const titulo = btn.dataset.titulo;
                const aluno = btn.dataset.aluno;
                openReturnModal(id, titulo, aluno);
            }
        });
    }

    // --- Listeners do Modal de Devolução ---
    if (btnCloseReturnX) btnCloseReturnX.addEventListener('click', closeReturnModal);
    if (btnCancelReturn) btnCancelReturn.addEventListener('click', closeReturnModal);
    if (returnModal) {
        returnModal.addEventListener('click', (e) => {
            if (e.target === returnModal) closeReturnModal();
        });
    }

    if (btnConfirmReturn) {
        btnConfirmReturn.addEventListener('click', async () => {
            if (idToReturn) {
                try {
                    const { url, options } = EmprestimoService.devolver(idToReturn);
                    const { error } = await fetchData(url, options);

                    if (!error) {
                        closeReturnModal();
                        alert('Livro devolvido com sucesso!');
                        createPaginatedTable(tableConfig);
                    } else {
                        alert('Erro ao devolver: ' + (error.message || 'Erro desconhecido'));
                        closeReturnModal();
                    }
                } catch (e) {
                    console.error(e);
                    alert("Erro de conexão ao tentar devolver.");
                    closeReturnModal();
                }
            }
        });
    }

    // Inicializa
    createPaginatedTable(tableConfig);
});
