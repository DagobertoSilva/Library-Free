import { createPaginatedTable } from '../modules/table-manager.js';
import { AlunoService, CursoService, TurmaService } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', async () => {
    // --- Estado dos Filtros ---
    let currentCursoId = null;
    let currentTurmaId = null;
    let tableInstance = null;

    // --- Elementos do Modal ---
    const confirmModal = document.getElementById('confirmModal');
    const confirmModalBody = document.getElementById('confirmModalBody');
    const confirmModalConfirmBtn = document.getElementById('confirmModalConfirm');
    const confirmModalCancelBtn = document.getElementById('confirmModalCancel');
    const closeModalBtn = document.getElementById('closeModalBtn');
    
    // --- Elementos do DOM ---
    const courseSelect = document.getElementById('course-select');
    const classSelect = document.getElementById('class-select');
    const btnAdd = document.getElementById('btn-add');
    
    let confirmAction = null;

    // --- Função para abrir/fechar modal de confirmação ---
    function openConfirmModal(alunoName, onConfirm) {
        confirmModalBody.textContent = alunoName;
        confirmAction = onConfirm;
        confirmModal.style.display = 'flex';
    }

    function closeConfirmModal() {
        confirmModal.style.display = 'none';
        confirmAction = null;
    }

    confirmModalConfirmBtn.addEventListener('click', () => {
        if (confirmAction) {
            confirmAction();
        }
        closeConfirmModal();
    });
    
    confirmModalCancelBtn.addEventListener('click', closeConfirmModal);
    closeModalBtn.addEventListener('click', closeConfirmModal);
    confirmModal.addEventListener('click', (event) => {
        if (event.target === confirmModal) {
            closeConfirmModal();
        }
    });

    // --- Configuração da Tabela ---
    const tableConfig = {
        onLoad: (page) => AlunoService.getAll(page, 8, currentCursoId, currentTurmaId),
        onFilter: (query, page) => AlunoService.getAll(page, 8, currentCursoId, currentTurmaId),
        
        renderRow: (aluno) => {
            const turmaDisplay = `${aluno.cursoNome} - ${aluno.serie}º série`;
            const editIcon = `
                <button class="editIcon" data-aluno-id="${aluno.id}" title="Editar">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <g clip-path="url(#clip0_151_188)">
                            <path d="M9.16675 3.33332H3.33341C2.89139 3.33332 2.46746 3.50891 2.1549 3.82147C1.84234 4.13403 1.66675 4.55796 1.66675 4.99999V16.6667C1.66675 17.1087 1.84234 17.5326 2.1549 17.8452C2.46746 18.1577 2.89139 18.3333 3.33341 18.3333H15.0001C15.4421 18.3333 15.866 18.1577 16.1786 17.8452C16.4912 17.5326 16.6667 17.1087 16.6667 16.6667V10.8333M15.4167 2.08332C15.7483 1.7518 16.1979 1.56555 16.6667 1.56555C17.1356 1.56555 17.5852 1.7518 17.9167 2.08332C18.2483 2.41484 18.4345 2.86448 18.4345 3.33332C18.4345 3.80216 18.2483 4.2518 17.9167 4.58332L10.0001 12.5L6.66675 13.3333L7.50008 9.99999L15.4167 2.08332Z" stroke="#522504" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </g>
                    </svg>
                </button>`;
            const deleteIcon = `
                <button class="deleteIcon" data-aluno-id="${aluno.id}" data-aluno-name="${aluno.nome}" title="Excluir">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M2.5 5.00002H4.16667M4.16667 5.00002H17.5M4.16667 5.00002L4.16667 16.6667C4.16667 17.1087 4.34226 17.5326 4.65482 17.8452C4.96738 18.1578 5.39131 18.3334 5.83333 18.3334H14.1667C14.6087 18.3334 15.0326 18.1578 15.3452 17.8452C15.6577 17.5326 15.8333 17.1087 15.8333 16.6667V5.00002M6.66667 5.00002V3.33335C6.66667 2.89133 6.84226 2.4674 7.15482 2.15484C7.46738 1.84228 7.89131 1.66669 8.33333 1.66669H11.6667C12.1087 1.66669 12.5326 1.84228 12.8452 2.15484C13.1577 2.4674 13.3333 2.89133 13.3333 3.33335V5.00002M8.33333 9.16669V14.1667M11.6667 9.16669V14.1667" stroke="#900B09" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
                </button>`;

            return `
                <td>${aluno.matricula}</td>
                <td>${aluno.nome}</td>
                <td>${aluno.cursoNome}</td>
                <td>${turmaDisplay}</td>
                <td class="table-body-actions">
                    ${editIcon}
                    ${deleteIcon}
                </td>
            `;
        }
    };

    // --- Carrega Cursos ao inicializar ---
    try {
        const { url: cursosUrl, options: cursosOptions } = CursoService.getAll();
        const { data: cursos, error: cursosError } = await fetchData(cursosUrl, cursosOptions);

        if (cursosError) throw new Error(cursosError.message);

        cursos.forEach(curso => {
            const option = document.createElement('option');
            option.value = curso.id;
            option.textContent = curso.nome;
            courseSelect.appendChild(option);
        });
    } catch (err) {
        console.error('Erro ao carregar cursos:', err);
    }

    // --- Eventos dos Filtros ---
    courseSelect.addEventListener('change', async () => {
        currentCursoId = courseSelect.value ? parseInt(courseSelect.value) : null;
        currentTurmaId = null; // Reset turma ao trocar curso
        classSelect.value = '';
        classSelect.disabled = !currentCursoId;
        
        // Carregar turmas do curso selecionado
        if (currentCursoId) {
            try {
                const { url: turmasUrl, options: turmasOptions } = TurmaService.getByCursoId(currentCursoId);
                const { data: turmas, error: turmasError } = await fetchData(turmasUrl, turmasOptions);

                if (turmasError) throw new Error(turmasError.message);

                classSelect.innerHTML = '<option value="">Selecione uma turma</option>';
                turmas.forEach(turma => {
                    const option = document.createElement('option');
                    option.value = turma.id;
                    const display = `${turma.serie}º série${turma.nome ? ' - ' + turma.nome : ''}`;
                    option.textContent = display;
                    classSelect.appendChild(option);
                });
            } catch (err) {
                console.error('Erro ao carregar turmas:', err);
                classSelect.innerHTML = '<option value="">Erro ao carregar turmas</option>';
            }
        } else {
            classSelect.innerHTML = '<option value="">Selecione uma turma</option>';
        }
        
        // Recarregar tabela com novo filtro
        tableInstance = await createPaginatedTable(tableConfig);
    });

    classSelect.addEventListener('change', async () => {
        currentTurmaId = classSelect.value ? parseInt(classSelect.value) : null;
        // Recarregar tabela com novo filtro
        tableInstance = await createPaginatedTable(tableConfig);
    });

    // --- Listener de Ações da Tabela ---
    document.addEventListener('click', (e) => {
        const editBtn = e.target.closest('.editIcon');
        const deleteBtn = e.target.closest('.deleteIcon');

        if (editBtn) {
            const alunoId = editBtn.dataset.alunoId;
            window.location.href = `./register-user.html?id=${alunoId}`;
        }

        if (deleteBtn) {
            const alunoId = deleteBtn.dataset.alunoId;
            const alunoName = deleteBtn.dataset.alunoName;
            openConfirmModal(
                alunoName,
                async () => {
                    try {
                        const { url, options } = AlunoService.delete(alunoId);
                        const { error } = await fetchData(url, options);
                        
                        if (!error) {
                            tableInstance = await createPaginatedTable(tableConfig);
                        } else {
                            alert('Erro ao excluir: ' + (error.message || 'Erro desconhecido'));
                        }
                    } catch (e) {
                        console.error(e);
                        alert("Erro de conexão ao tentar excluir.");
                    }
                }
            );
        }
    });

    // --- Listener do Botão "Novo Aluno" ---
    if (btnAdd) {
        btnAdd.addEventListener('click', () => {
            window.location.href = './register-user.html';
        });
    }

    // --- Inicializar Tabela ---
    tableInstance = await createPaginatedTable(tableConfig);
});