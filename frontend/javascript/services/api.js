const API_URL = 'http://localhost:8080/api/v1';

// --- HELPERS ---

const getAuthHeader = () => {
    const token = localStorage.getItem('token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
};

// Factory para CRUDs simples (DRY - Don't Repeat Yourself)
const createSimpleCrudService = (endpoint) => ({
    getAll: () => ({
        url: `${API_URL}/${endpoint}`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),
    create: (nome) => ({
        url: `${API_URL}/${endpoint}`,
        options: {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() },
            body: JSON.stringify({ nome })
        }
    })
});

// --- SERVICES ---

export const AuthService = {
    login: (body) => ({
        url: `${API_URL}/auth/login`,
        options: {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        }
    }),
    me: (token) => ({
        // Se o token for passado explicitamente, usa ele. Senão, tenta pegar do localStorage via getAuthHeader
        url: `${API_URL}/auth/me`,
        options: {
            method: 'GET',
            headers: token ? { Authorization: 'Bearer ' + token } : getAuthHeader(),
        }
    })
};

export const BookService = {
    getAll: (page = 0, size = 8) => ({
        url: `${API_URL}/livros?page=${page}&size=${size}&sort=titulo,asc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    getById: (id) => ({
        url: `${API_URL}/livros/${id}`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    getByTitle: (term, page = 0, size = 8) => ({
        url: `${API_URL}/livros/titulo?termo=${encodeURIComponent(term)}&page=${page}&size=${size}&sort=titulo,asc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    getByAuthor: (term, page = 0, size = 8) => ({
        url: `${API_URL}/livros/autor?termo=${encodeURIComponent(term)}&page=${page}&size=${size}&sort=titulo,asc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    getByGenre: (term, page = 0, size = 8) => ({
        url: `${API_URL}/livros/genero?termo=${encodeURIComponent(term)}&page=${page}&size=${size}&sort=titulo,asc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    // Adicionado pois será usado no register-book.js
    create: (bookData) => ({
        url: `${API_URL}/livros`,
        options: {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() },
            body: JSON.stringify(bookData)
        }
    }),

    // Adicionado para suportar o botão de edição
    update: (id, bookData) => ({
        url: `${API_URL}/livros/${id}`,
        options: {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() },
            body: JSON.stringify(bookData)
        }
    }),

    // Adicionado para suportar o botão de exclusão
    delete: (id) => ({
        url: `${API_URL}/livros/${id}`,
        options: {
            method: 'DELETE',
            headers: { ...getAuthHeader() }
        }
    })
};

export const AlunoService = {
    getAll: (page = 0, size = 8, cursoId = null, turmaId = null) => {
        const params = new URLSearchParams();
        params.append('page', page);
        params.append('size', size);
        params.append('sort', 'nome');
        if (cursoId) params.append('cursoId', cursoId);
        if (turmaId) params.append('turmaId', turmaId);

        return {
            url: `${API_URL}/alunos?${params.toString()}`,
            options: {
                method: 'GET',
                headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
            }
        };
    },

    getById: (id) => ({
        url: `${API_URL}/alunos/${id}`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    create: (alunoData) => ({
        url: `${API_URL}/alunos`,
        options: {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() },
            body: JSON.stringify(alunoData)
        }
    }),

    update: (id, alunoData) => ({
        url: `${API_URL}/alunos/${id}`,
        options: {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() },
            body: JSON.stringify(alunoData)
        }
    }),

    delete: (id) => ({
        url: `${API_URL}/alunos/${id}`,
        options: {
            method: 'DELETE',
            headers: { ...getAuthHeader() }
        }
    })
};

export const CursoService = {
    getAll: () => ({
        url: `${API_URL}/escolar/cursos`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    })
};

export const TurmaService = {
    getByCursoId: (cursoId) => ({
        url: `${API_URL}/escolar/turmas?cursoId=${cursoId}`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    })
};

export const EmprestimoService = {
    create: (emprestimoData) => ({
        url: `${API_URL}/emprestimos`,
        options: {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() },
            body: JSON.stringify(emprestimoData)
        }
    }),

    getAll: (page = 0, size = 8) => ({
        url: `${API_URL}/emprestimos?page=${page}&size=${size}&sort=dataEmprestimo,desc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    getByTituloLivro: (termo, page = 0, size = 8) => ({
        url: `${API_URL}/emprestimos?tituloLivro=${encodeURIComponent(termo)}&page=${page}&size=${size}&sort=dataEmprestimo,desc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    getByNomeAluno: (termo, page = 0, size = 8) => ({
        url: `${API_URL}/emprestimos?nomeAluno=${encodeURIComponent(termo)}&page=${page}&size=${size}&sort=dataEmprestimo,desc`,
        options: {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    }),

    devolver: (emprestimoId) => ({
        url: `${API_URL}/emprestimos/${emprestimoId}/devolucao`,
        options: {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
        }
    })
};

// Adicionar método para buscar alunos por turma
AlunoService.getByTurmaId = (turmaId) => ({
    url: `${API_URL}/alunos?turmaId=${turmaId}`,
    options: {
        method: 'GET',
        headers: { 'Content-Type': 'application/json', ...getAuthHeader() }
    }
});

export const AuthorService = createSimpleCrudService('autores');
export const GenreService = createSimpleCrudService('generos');
export const PublisherService = createSimpleCrudService('editoras');