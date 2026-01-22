export async function fetchData(url, options) {
    // --- MOCK DATA SYSTEM ---
    // Usado quando a API falha ou retorna 401/403, para permitir testes de UI.
    const MOCK_BOOKS = [
        { id: 1, isbn: "978-85-359-0277-8", titulo: "Dom Casmurro", genero: "Romance", emprestado: false },
        { id: 2, isbn: "978-85-7657-200-8", titulo: "Duna", genero: "Ficção Científica", emprestado: true },
        { id: 3, isbn: "978-85-325-3078-3", titulo: "Harry Potter e a Pedra Filosofal", genero: "Fantasia", emprestado: false },
        { id: 4, isbn: "978-85-950-8153-6", titulo: "Clean Code", genero: "Tecnologia", emprestado: false }
    ];

    const MOCK_USERS = [
        { id: 1, matricula: "2024001", nome: "Ana Silva", cpf: "123.456.789-00", ativo: true },
        { id: 2, matricula: "2024002", nome: "Carlos Souza", cpf: "987.654.321-01", ativo: false },
        { id: 3, matricula: "2024003", nome: "Beatriz Oliveira", cpf: "456.789.123-44", ativo: true }
    ];

    const MOCK_LOANS = [
        { livroTitulo: "Dom Casmurro", alunoNome: "Ana Silva", dataEmprestimo: "2024-01-10", prazoDevolucao: "2024-01-17" },
        { livroTitulo: "Duna", alunoNome: "Carlos Souza", dataEmprestimo: "2024-01-12", prazoDevolucao: "2024-01-19" }
    ];

    function getMockData(url, method) {
        console.warn(`[MOCK MODE] Retornando dados falsos para: ${url} (${method})`);

        // Se não for GET, retorna sucesso vazio (para simular POST/PUT/DELETE funcionando)
        if (method && method !== 'GET') {
            return { data: {}, error: null };
        }

        if (url.includes('/livros')) return { data: MOCK_BOOKS, error: null };
        if (url.includes('/alunos')) return { data: MOCK_USERS, error: null };
        if (url.includes('/emprestimos/recentes')) return { data: MOCK_LOANS, error: null };

        return { data: [], error: null };
    }
    // ------------------------

    try {
        const response = await fetch(url, options);

        // Se der erro de auth (401/403), usa o Mock
        if (response.status === 401 || response.status === 403) {
            return getMockData(url, options?.method || 'GET');
        }

        if (!response.ok) {
            let errorJson = null;
            try {
                errorJson = await response.json();
            } catch (e) {
                // Se falhar o parse do erro JSON, tenta retornar Mock se for 404 (opcional) ou erro genérico
                throw new Error(`Erro ${response.status}: ${response.statusText}`);
            }
            throw new Error(errorJson.message || `Erro ${response.status}: ${response.statusText}`);
        }

        const contentType = response.headers.get('content-type');
        if (response.status !== 204 && contentType && contentType.includes('application/json')) {
            const json = await response.json();
            return { data: json, error: null };
        } else if (response.status === 204) {
            return { data: null, error: null };
        } else {
            console.warn(`Requisição ${url} retornou 200 OK, mas sem content-type JSON ou 204 No Content.`);
            return { data: null, error: null };
        }

    } catch (error) {
        // Se der erro de rede (Ex: backend desligado), usa o Mock
        console.warn("Falha na conexão com API. Usando Mock Data.");
        return getMockData(url, options?.method || 'GET');
    }
}