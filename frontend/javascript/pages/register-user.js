import { AlunoService, CursoService, TurmaService } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

class TurmaDropdownManager {
    constructor(containerId) {
        this.container = document.getElementById(containerId);
        if (!this.container) return;

        this.listContainer = this.container.querySelector('.list-container');
        this.selectedValue = this.container.querySelector('.selected-value');
        this.placeholder = this.container.querySelector('.placeholder-text');
        
        this.turmas = [];
        this.selectedId = null;
    }

    async loadTurmasByCurso(cursoId) {
        if (!cursoId) {
            this.turmas = [];
            this.selectedId = null;
            this.renderList();
            return;
        }

        try {
            const { url, options } = TurmaService.getByCursoId(cursoId);
            const { data, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);
            
            this.turmas = data || [];
            this.selectedId = null;
            this.renderList();
        } catch (err) {
            console.error('Erro ao carregar turmas:', err);
            this.listContainer.innerHTML = `<div class="p-2 text-danger small">Erro ao carregar turmas.</div>`;
        }
    }

    renderList() {
        if (this.turmas.length === 0) {
            this.listContainer.innerHTML = `<div class="p-2 text-muted small">Nenhuma turma disponível.</div>`;
            return;
        }

        this.listContainer.innerHTML = this.turmas.map(turma => {
            const isChecked = this.selectedId === turma.id.toString();
            const serieDisplay = turma.serie ? `${turma.serie}º série` : '';
            const nomeDisplay = turma.nome ? turma.nome : '';
            const display = `${serieDisplay} ${nomeDisplay}`.trim();

            return `
            <li>
                <label class="dropdown-item d-flex gap-2 align-items-center" style="cursor: pointer;">
                    <input class="form-check-input mt-0" 
                           type="radio" 
                           name="turma"
                           value="${turma.id}" 
                           ${isChecked ? 'checked' : ''}>
                    <span>${display}</span>
                </label>
            </li>`;
        }).join('');

        this.attachChangeListener();
    }

    attachChangeListener() {
        this.listContainer.addEventListener('change', (e) => {
            if (e.target.name === 'turma') {
                this.selectedId = e.target.value;
                this.updateDisplay();
            }
        });
    }

    updateDisplay() {
        const hasSelection = this.selectedId !== null;
        if (this.placeholder) this.placeholder.style.display = hasSelection ? 'none' : 'block';

        if (hasSelection) {
            const turma = this.turmas.find(t => t.id.toString() === this.selectedId);
            if (turma) {
                const serieDisplay = turma.serie ? `${turma.serie}º série` : '';
                const nomeDisplay = turma.nome ? turma.nome : '';
                const display = `${serieDisplay} ${nomeDisplay}`.trim();
                this.selectedValue.textContent = display;
                this.selectedValue.classList.remove('d-none');
            }
        } else {
            this.selectedValue.classList.add('d-none');
        }
    }

    getValue() {
        return this.selectedId ? parseInt(this.selectedId) : null;
    }

    setSelected(turmaId) {
        this.selectedId = turmaId ? turmaId.toString() : null;
        this.updateDisplay();
        this.renderList();  // Re-renderiza para aplicar a seleção no radio button
    }
}

// ------------------------------------------------------------------

document.addEventListener('DOMContentLoaded', async () => {
    
    // Inicializa o Dropdown de Turma
    const turmaDropdown = new TurmaDropdownManager('dropdown-turma');
    
    const cursoSelect = document.getElementById('curso-select');
    const nomeInput = document.getElementById('nome');
    const matriculaInput = document.getElementById('matricula');
    const form = document.getElementById('userRegisterForm');
    
    // Verifica se é Edição (Busca ID na URL)
    const urlParams = new URLSearchParams(window.location.search);
    const alunoId = urlParams.get('id');

    // 1. Carrega os Cursos
    try {
        const { url, options } = CursoService.getAll();
        const { data: cursos, error } = await fetchData(url, options);

        if (error) throw new Error(error.message);

        cursos.forEach(curso => {
            const option = document.createElement('option');
            option.value = curso.id;
            option.textContent = curso.nome;
            cursoSelect.appendChild(option);
        });
    } catch (err) {
        console.error('Erro ao carregar cursos:', err);
        alert('Erro ao carregar cursos. Recarregue a página.');
    }

    // 2. Se for edição, carrega e preenche os dados do aluno
    if (alunoId) {
        // --- MODO EDIÇÃO ---
        
        // 2.1. Ajuste Visual (Título e Botão)
        const pageTitle = document.querySelector('.title');
        if (pageTitle) pageTitle.textContent = 'Editar Aluno';

        const btnSubmit = form.querySelector('button[type="submit"]');
        if (btnSubmit) btnSubmit.textContent = 'Salvar Alterações';

        // 2.2. Busca e Preenchimento de Dados
        try {
            const { url, options } = AlunoService.getById(alunoId);
            const { data: aluno, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);

            // Preenche Inputs normais
            nomeInput.value = aluno.nome || '';
            matriculaInput.value = aluno.matricula || '';

            // Seleciona o curso e carrega as turmas
            cursoSelect.value = aluno.cursoId || '';
            if (aluno.cursoId) {
                await turmaDropdown.loadTurmasByCurso(aluno.cursoId);
                // Seleciona a turma do aluno
                turmaDropdown.setSelected(aluno.turmaId);
            }

        } catch (err) {
            console.error(err);
            alert('Erro ao carregar dados do aluno para edição.');
            window.location.href = './users.html';
        }
    }

    // 3. Ao selecionar curso, carrega as turmas
    cursoSelect.addEventListener('change', async () => {
        const selectedCursoId = cursoSelect.value ? parseInt(cursoSelect.value) : null;
        await turmaDropdown.loadTurmasByCurso(selectedCursoId);
    });

    // 4. Listener do Submit (Criação ou Edição)
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const nome = nomeInput.value.trim();
        const matricula = matriculaInput.value.trim();
        const turmaId = turmaDropdown.getValue();

        if (!nome || !matricula || !turmaId) {
            alert('Preencha todos os campos obrigatórios.');
            return;
        }

        const alunoData = {
            nome,
            matricula,
            turmaId
        };

        try {
            let result;

            if (alunoId) {
                // --- ATUALIZAR (PUT) ---
                const { url, options } = AlunoService.update(alunoId, alunoData);
                result = await fetchData(url, options);
            } else {
                // --- CRIAR (POST) ---
                const { url, options } = AlunoService.create(alunoData);
                result = await fetchData(url, options);
            }

            if (result.error) {
                alert(`Erro ao salvar: ${result.error.message || 'Verifique os dados.'}`);
            } else {
                alert(alunoId ? 'Aluno atualizado com sucesso!' : 'Aluno cadastrado com sucesso!');
                window.location.href = './users.html';
            }

        } catch (err) {
            console.error(err);
            alert("Ocorreu um erro inesperado.");
        }
    });
});
