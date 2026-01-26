import { createPaginatedTable } from '../modules/table-manager.js';
import { BookService, CursoService, TurmaService, AlunoService, EmprestimoService } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', () => {
    
    // --- Lógica do Filtro (NOVO) ---
    let currentFilterType = 'titulo'; // Padrão
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

    // --- Elementos do Modal Customizado ---
    const customModal = document.getElementById('customModal');
    const modalBookTitle = document.getElementById('modalBookTitle');
    const btnCloseX = document.getElementById('closeModalBtn');
    const btnCancel = document.getElementById('btnCancelCustom');
    const btnConfirm = document.getElementById('btnConfirmCustom');
    
    // --- Elementos do Modal de Empréstimo ---
    const lendModal = document.getElementById('lendModal');
    const lendBookTitle = document.getElementById('lendBookTitle');
    const btnCloseLendX = document.getElementById('closeLendModalBtn');
    const lendCursoSelect = document.getElementById('lend-curso-select');
    const lendTurmaSelect = document.getElementById('lend-turma-select');
    const lendAlunoSelect = document.getElementById('lend-aluno-select');
    const btnCancelLend = document.getElementById('btnCancelLend');
    const btnConfirmLend = document.getElementById('btnConfirmLend');
    
    let idToDelete = null;
    let idToLend = null;

    // --- Funções de Controle do Modal ---
    function openModal(id, title) {
        idToDelete = id;
        if(modalBookTitle) modalBookTitle.textContent = title || "Livro sem título";
        if(customModal) customModal.style.display = 'flex';
    }

    function closeModal() {
        idToDelete = null;
        if(customModal) customModal.style.display = 'none';
    }

    // --- Configuração da Tabela ---
    const tableConfig = {
        onLoad: (page) => BookService.getAll(page),
        
        // ALTERADO: Lógica de decisão baseada no filtro selecionado
        onFilter: (query, page) => {
            switch (currentFilterType) {
                case 'autor':
                    return BookService.getByAuthor(query, page);
                case 'genero':
                    return BookService.getByGenre(query, page);
                case 'titulo':
                default:
                    return BookService.getByTitle(query, page);
            }
        },
        
        renderRow: (book) => {
            const isbnDisplay = book.isbn ? `<span class="font-monospace text-muted">${book.isbn}</span>` : '-';
            const autoresDisplay = (book.autores && book.autores.length) ? book.autores.map(a => a.nome).join(', ') : 'Nenhum';
            const generosDisplay = (book.generos && book.generos.length) ? book.generos.map(g => `<span class="badge bg-light text-dark border me-1">${g.nome}</span>`).join('') : '-';
            
            const total = book.quantidadeTotal || 0;
            const disponivel = book.quantidadeDisponivel || 0;
            const qtyBadgeClass = disponivel > 0 ? 'bg-success-subtle text-success-emphasis' : 'bg-danger-subtle text-danger-emphasis';

            return `
                <td class="align-middle">${isbnDisplay}</td>
                <td class="align-middle"><div class="fw-bold text-dark">${book.titulo}</div></td>
                <td class="align-middle text-secondary">${autoresDisplay}</td>
                <td class="align-middle">${generosDisplay}</td>
                <td class="align-middle text-center">
                    <span class="badge ${qtyBadgeClass} rounded-pill px-3 py-2 border border-opacity-10">${disponivel} / ${total}</span>
                </td>
                <td class="align-middle text-end">${renderActions(book)}</td>
            `;
        }
    };

    function renderActions(book) {
        const editIcon = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>`;
        const deleteIcon = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>`;
        const lendIcon = `<svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"></path><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"></path></svg>`;
        
        const isDisabled = book.quantidadeDisponivel <= 0 ? 'disabled' : '';

        return `
            <div class="d-flex gap-2 justify-content-end">
                <button class="btn btn-action btn-action-edit edit" data-id="${book.id}" title="Editar">
                    ${editIcon}
                </button>
                <button class="btn btn-action lend" data-id="${book.id}" data-title="${book.titulo}" title="Emprestar" ${isDisabled} style="background-color: #d4edda; color: #155724; ${isDisabled ? 'opacity: 0.5; cursor: not-allowed;' : ''}">
                    ${lendIcon}
                </button>
                <button class="btn btn-action btn-action-delete delete" data-id="${book.id}" data-title="${book.titulo}" title="Excluir">
                    ${deleteIcon}
                </button>
            </div>
        `;
    }

    // --- Listeners Gerais ---
    const btnAdd = document.getElementById('btn-add');
    if (btnAdd) btnAdd.addEventListener('click', () => { window.location.href = './register-book.html'; });

    // --- Listener da Tabela (Delegação) ---
    const tableBody = document.querySelector('.table-body');
    if (tableBody) {
        tableBody.addEventListener('click', (e) => {
            const btn = e.target.closest('button');
            if (!btn) return;

            const id = btn.dataset.id;

            if (btn.classList.contains('edit')) {
                window.location.href = `./register-book.html?id=${id}`;
            }

            if (btn.classList.contains('delete')) {
                const title = btn.dataset.title || 'este livro'; 
                openModal(id, title);
            }

            if (btn.classList.contains('lend')) {
                const title = btn.dataset.title || 'este livro';
                openLendModal(id, title);
            }
        });
    }

    // --- Funções de Controle do Modal de Empréstimo ---
    function openLendModal(id, title) {
        idToLend = id;
        if(lendBookTitle) lendBookTitle.textContent = title || "Livro sem título";
        if(lendModal) lendModal.style.display = 'flex';
        
        // Reset dos selects
        lendCursoSelect.value = '';
        lendTurmaSelect.value = '';
        lendTurmaSelect.disabled = true;
        lendAlunoSelect.value = '';
        lendAlunoSelect.disabled = true;
        
        // Carrega cursos
        loadCursosForLend();
    }

    function closeLendModal() {
        idToLend = null;
        if(lendModal) lendModal.style.display = 'none';
    }

    async function loadCursosForLend() {
        try {
            const { url, options } = CursoService.getAll();
            const { data: cursos, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);

            lendCursoSelect.innerHTML = '<option value="">Selecione um curso</option>';
            cursos.forEach(curso => {
                const option = document.createElement('option');
                option.value = curso.id;
                option.textContent = curso.nome;
                lendCursoSelect.appendChild(option);
            });
        } catch (err) {
            console.error('Erro ao carregar cursos:', err);
            alert('Erro ao carregar cursos para empréstimo.');
        }
    }

    async function loadTurmasForLend(cursoId) {
        if (!cursoId) {
            lendTurmaSelect.disabled = true;
            lendTurmaSelect.innerHTML = '<option value="">Selecione uma turma</option>';
            lendAlunoSelect.disabled = true;
            lendAlunoSelect.innerHTML = '<option value="">Selecione um aluno</option>';
            return;
        }

        try {
            const { url, options } = TurmaService.getByCursoId(cursoId);
            const { data: turmas, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);

            lendTurmaSelect.innerHTML = '<option value="">Selecione uma turma</option>';
            turmas.forEach(turma => {
                const option = document.createElement('option');
                option.value = turma.id;
                const display = turma.serie ? `${turma.serie}º ${turma.nome || 'série'}` : turma.nome;
                option.textContent = display;
                lendTurmaSelect.appendChild(option);
            });

            lendTurmaSelect.disabled = false;
            lendAlunoSelect.disabled = true;
            lendAlunoSelect.innerHTML = '<option value="">Selecione um aluno</option>';
        } catch (err) {
            console.error('Erro ao carregar turmas:', err);
            alert('Erro ao carregar turmas para empréstimo.');
        }
    }

    async function loadAlunosForLend(turmaId) {
        if (!turmaId) {
            lendAlunoSelect.disabled = true;
            lendAlunoSelect.innerHTML = '<option value="">Selecione um aluno</option>';
            return;
        }

        try {
            const { url, options } = AlunoService.getByTurmaId(turmaId);
            const { data: response, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);

            // Extrai o array de alunos (pode ser response.content se paginado, ou response direto)
            const alunos = Array.isArray(response) ? response : (response.content || []);

            lendAlunoSelect.innerHTML = '<option value="">Selecione um aluno</option>';
            alunos.forEach(aluno => {
                const option = document.createElement('option');
                option.value = aluno.id;
                option.textContent = `${aluno.nome} (${aluno.matricula})`;
                lendAlunoSelect.appendChild(option);
            });

            lendAlunoSelect.disabled = false;
        } catch (err) {
            console.error('Erro ao carregar alunos:', err);
            alert('Erro ao carregar alunos para empréstimo.');
        }
    }

    // --- Listeners do Modal de Empréstimo ---
    if (lendCursoSelect) {
        lendCursoSelect.addEventListener('change', (e) => {
            loadTurmasForLend(e.target.value);
        });
    }

    if (lendTurmaSelect) {
        lendTurmaSelect.addEventListener('change', (e) => {
            loadAlunosForLend(e.target.value);
        });
    }

    if (btnCloseLendX) btnCloseLendX.addEventListener('click', closeLendModal);
    if (btnCancelLend) btnCancelLend.addEventListener('click', closeLendModal);
    if (lendModal) {
        lendModal.addEventListener('click', (e) => {
            if (e.target === lendModal) closeLendModal();
        });
    }

    if (btnConfirmLend) {
        btnConfirmLend.addEventListener('click', async () => {
            const alunoId = lendAlunoSelect.value;

            if (!alunoId) {
                alert('Por favor, selecione um aluno.');
                return;
            }

            if (idToLend) {
                try {
                    const emprestimoData = {
                        livroId: idToLend,
                        alunoId: parseInt(alunoId)
                    };

                    const { url, options } = EmprestimoService.create(emprestimoData);
                    const { error } = await fetchData(url, options);

                    if (!error) {
                        closeLendModal();
                        alert('Empréstimo realizado com sucesso!');
                        createPaginatedTable(tableConfig);
                    } else {
                        alert('Erro ao realizar empréstimo: ' + (error.message || 'Erro desconhecido'));
                    }
                } catch (e) {
                    console.error(e);
                    alert("Erro de conexão ao tentar fazer empréstimo.");
                }
            }
        });
    }

    // --- Listeners do Modal de Delete ---
    if (btnCloseX) btnCloseX.addEventListener('click', closeModal);
    if (btnCancel) btnCancel.addEventListener('click', closeModal);
    if (customModal) {
        customModal.addEventListener('click', (e) => {
            if (e.target === customModal) closeModal();
        });
    }

    if (btnConfirm) {
        btnConfirm.addEventListener('click', async () => {
            if (idToDelete) {
                try {
                    const { url, options } = BookService.delete(idToDelete);
                    const { error } = await fetchData(url, options);
                    
                    if (!error) {
                        closeModal();
                        createPaginatedTable(tableConfig);
                    } else {
                        alert('Erro ao excluir: ' + (error.message || 'Erro desconhecido'));
                        closeModal();
                    }
                } catch (e) {
                    console.error(e);
                    alert("Erro de conexão ao tentar excluir.");
                    closeModal();
                }
            }
        });
    }

    // Inicializa
    createPaginatedTable(tableConfig);
});