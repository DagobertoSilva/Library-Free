import { AuthorService, GenreService, PublisherService, BookService } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

class DropdownManager {
    constructor({ containerId, service, type, label }) {
        this.container = document.getElementById(containerId);
        if (!this.container) return;

        this.service = service;
        this.type = type;
        this.label = label;
        
        this.listContainer = this.container.querySelector('.list-container');
        this.dropdownToggle = this.container.querySelector('.dropdown-toggle');
        this.selectedContainer = this.container.querySelector('.selected-container') || this.container.querySelector('.selected-value');
        this.placeholder = this.container.querySelector('.placeholder-text');
        this.btnCreate = this.container.querySelector('.btn-create-new');
        
        this.items = [];
        this.selectedIds = new Set();

        this.init();
    }

    async init() {
        await this.loadData();
        this.renderList();
        this.attachEvents();
    }

    async loadData() {
        try {
            const { url, options } = this.service.getAll();
            const { data, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);
            
            this.items = data || [];
        } catch (err) {
            console.error(`Erro ao carregar ${this.label}:`, err);
            this.listContainer.innerHTML = `<div class="p-2 text-danger small">Erro ao carregar lista.</div>`;
        }
    }

    renderList() {
        if (this.items.length === 0) {
            this.listContainer.innerHTML = `<div class="p-2 text-muted small">Nenhum registro encontrado.</div>`;
            return;
        }

        this.listContainer.innerHTML = this.items.map(item => {
            const isChecked = this.selectedIds.has(item.id.toString());
            const inputType = this.type === 'multi' ? 'checkbox' : 'radio';
            const groupName = `group-${this.label.toLowerCase()}`;

            return `
            <li>
                <label class="dropdown-item d-flex gap-2 align-items-center" style="cursor: pointer;">
                    <input class="form-check-input mt-0" 
                           type="${inputType}" 
                           name="${groupName}"
                           value="${item.id}" 
                           ${isChecked ? 'checked' : ''}>
                    <span>${item.nome}</span>
                </label>
            </li>`;
        }).join('');
    }

    attachEvents() {
        this.listContainer.addEventListener('change', (e) => {
            if (e.target.tagName === 'INPUT') {
                const id = e.target.value;

                if (this.type === 'single') {
                    this.selectedIds.clear();
                    this.selectedIds.add(id);
                } else {
                    if (e.target.checked) this.selectedIds.add(id);
                    else this.selectedIds.delete(id);
                }
                this.updateDisplay();
            }
        });

        if (this.btnCreate) {
            this.btnCreate.addEventListener('click', (e) => this.handleCreateNew(e));
        }
    }

    async handleCreateNew(e) {
        e.preventDefault();
        e.stopPropagation();

        const nome = prompt(`Digite o nome do novo ${this.label}:`);
        if (!nome || !nome.trim()) return;

        try {
            const { url, options } = this.service.create(nome);
            const { data, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);

            this.items.push(data);
            
            if (this.type === 'single') this.selectedIds.clear();
            this.selectedIds.add(data.id.toString());

            this.renderList();
            this.updateDisplay();

        } catch (err) {
            alert(`Erro ao criar: ${err.message}`);
        }
    }

    updateDisplay() {
        const hasSelection = this.selectedIds.size > 0;
        if (this.placeholder) this.placeholder.style.display = hasSelection ? 'none' : 'block';

        if (!hasSelection) {
            if (this.type === 'single') this.selectedContainer.classList.add('d-none');
            else this.selectedContainer.innerHTML = '';
            return;
        }

        if (this.type === 'multi') {
            this.selectedContainer.innerHTML = '';
            this.selectedIds.forEach(id => {
                const item = this.items.find(i => i.id == id);
                if (item) {
                    this.selectedContainer.innerHTML += `
                        <span class="badge bg-secondary border text-white selected-tag">
                            ${item.nome}
                        </span>`;
                }
            });
        } else {
            const id = [...this.selectedIds][0];
            const item = this.items.find(i => i.id == id);
            if (item) {
                this.selectedContainer.textContent = item.nome;
                this.selectedContainer.classList.remove('d-none');
            }
        }
    }

    getValue() {
        if (this.type === 'multi') return Array.from(this.selectedIds);
        return [...this.selectedIds][0] || null;
    }

    // --- NOVO MÉTODO PARA EDIÇÃO ---
    setSelected(data) {
        this.selectedIds.clear();
        
        if (!data) return;

        // Se for array (ex: lista de autores)
        if (Array.isArray(data)) {
            data.forEach(item => {
                const id = item.id ? item.id : item; 
                this.selectedIds.add(id.toString());
            });
        } 
        // Se for objeto único (ex: editora)
        else if (typeof data === 'object' && data.id) {
            this.selectedIds.add(data.id.toString());
        }
        // Se for ID primitivo
        else {
            this.selectedIds.add(data.toString());
        }

        this.renderList(); 
        this.updateDisplay();
    }
}

// ------------------------------------------------------------------

document.addEventListener('DOMContentLoaded', async () => {
    
    // 1. Inicializa os Dropdowns
    const authorManager = new DropdownManager({
        containerId: 'dropdown-autores',
        service: AuthorService,
        type: 'multi',
        label: 'Autor'
    });

    const genreManager = new DropdownManager({
        containerId: 'dropdown-generos',
        service: GenreService,
        type: 'multi',
        label: 'Gênero'
    });

    const publisherManager = new DropdownManager({
        containerId: 'dropdown-editora',
        service: PublisherService,
        type: 'single',
        label: 'Editora'
    });

    const form = document.getElementById('bookRegisterForm');
    
    // 2. Verifica se é Edição (Busca ID na URL)
    const urlParams = new URLSearchParams(window.location.search);
    const bookId = urlParams.get('id');

    if (bookId) {
        // --- MODO EDIÇÃO ---
        
        // 2.1. Ajuste Visual (Título e Botão)
        const pageTitle = document.querySelector('.title'); 
        if (pageTitle) pageTitle.textContent = 'Editar Livro';

        const btnSubmit = form.querySelector('button[type="submit"]');
        if (btnSubmit) btnSubmit.textContent = 'Salvar Alterações';

        // 2.2. Busca e Preenchimento de Dados
        try {
            const { url, options } = BookService.getById(bookId);
            const { data: book, error } = await fetchData(url, options);

            if (error) throw new Error(error.message);

            // Preenche Inputs normais
            if (form.elements['titulo']) form.elements['titulo'].value = book.titulo || '';
            if (form.elements['isbn']) form.elements['isbn'].value = book.isbn || '';
            if (form.elements['quantidadeTotal']) form.elements['quantidadeTotal'].value = book.quantidadeTotal || 0;
            if (form.elements['anoPublicacao']) form.elements['anoPublicacao'].value = book.anoPublicacao || '';

            // Preenche os Dropdowns
            if (book.autores) authorManager.setSelected(book.autores);
            if (book.generos) genreManager.setSelected(book.generos);
            if (book.editora) publisherManager.setSelected(book.editora);

        } catch (err) {
            console.error(err);
            alert('Erro ao carregar dados do livro para edição.');
            window.location.href = './books.html';
        }
    }

    // 3. Listener do Submit (Criação ou Edição)
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = new FormData(form);
        const autoresIds = authorManager.getValue();
        const generosIds = genreManager.getValue();
        const editoraId = publisherManager.getValue();

        const getIntOrNull = (value) => value ? parseInt(value) : null;
        const getStringOrNull = (value) => value && value.trim() !== "" ? value.trim() : null;

        const bookData = {
            titulo: formData.get('titulo'),
            quantidadeTotal: parseInt(formData.get('quantidadeTotal')),
            isbn: getStringOrNull(formData.get('isbn')),
            anoPublicacao: getIntOrNull(formData.get('anoPublicacao')),
            editoraId: editoraId ? parseInt(editoraId) : null,
            autoresIds: autoresIds.map(id => parseInt(id)),
            generosIds: generosIds.map(id => parseInt(id))
        };

        try {
            let result;
            
            if (bookId) {
                // --- ATUALIZAR (PUT) ---
                const { url, options } = BookService.update(bookId, bookData);
                result = await fetchData(url, options);
            } else {
                // --- CRIAR (POST) ---
                const { url, options } = BookService.create(bookData);
                result = await fetchData(url, options);
            }

            if (result.error) {
                alert(`Erro ao salvar: ${result.error.message || 'Verifique os dados.'}`);
            } else {
                alert(bookId ? 'Livro atualizado com sucesso!' : 'Livro cadastrado com sucesso!');
                window.location.href = './books.html';
            }

        } catch (err) {
            console.error(err);
            alert("Ocorreu um erro inesperado.");
        }
    });
});