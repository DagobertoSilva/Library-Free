const API_URL = 'http://localhost:8080/libraryfree';

export function LOGIN(body){
    return{
        url: `${API_URL}/auth/login`,
        options:{
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(body),
            credentials: 'include'
        }
    }
}

export function LOGOUT() {
    return {
        url: `${API_URL}/auth/logout`,
        options: {
            method: 'POST',
            credentials: 'include'
        }
    };
}

export function GET_LIVROS(){
    return{
        url: `${API_URL}/livros`,
        options: {
            credentials: 'include'
        }
    }
}

export function FILTER_LIVROS(titulo){
    const encodedTitulo = encodeURIComponent(titulo);
    return{
        url: `${API_URL}/livros/search?titulo=${encodedTitulo}`,
        options: {
            credentials: 'include'
        }
    }
}

export function GET_LIVRO_BY_ID(livroId) {
    return {
        url: `${API_URL}/livros/${livroId}`,
        options: {
            credentials: 'include'
        }
    };
}

export function CREATE_LIVRO(body) {
    return {
        url: `${API_URL}/livros`,
        options: {
            method: 'POST',
            headers: { 
                "Content-Type": "application/json" 
            },
            body: JSON.stringify(body),
            credentials: 'include'
        }
    };
}

export function EDIT_LIVRO(livroId, body) {
    return {
        url: `${API_URL}/livros/${livroId}`,
        options: {
            method: 'PUT',
            headers: { 
                "Content-Type": "application/json" 
            },
            body: JSON.stringify(body),
            credentials: 'include'
        }
    };
}

export function DELETE_LIVRO(livroId) {
    return {
        url: `${API_URL}/livros/${livroId}`,
        options: {
            method: 'DELETE',
            credentials: 'include'
        }
    };
}

export function GET_ALUNOS(){
    return{
        url: `${API_URL}/alunos`,
        options: {
            credentials: 'include'
        }
    }
}

export function FILTER_ALUNOS(nome){
    const encodedNome = encodeURIComponent(nome);
    return{
        url: `${API_URL}/alunos/search?nome=${encodedNome}`,
        options: {
            credentials: 'include'
        }
    }
}

export function GET_ALUNO_BY_ID(alunoId) {
    return {
        url: `${API_URL}/alunos/${alunoId}`,
        options: {
            credentials: 'include'
        }
    };
}

export function GET_ALUNO_BY_MATRICULA(alunoMatricula) {
    return {
        url: `${API_URL}/alunos/matricula/${alunoMatricula}`,
        options: {
            credentials: 'include'
        }
    };
}

export function CREATE_ALUNO(body) {
    return {
        url: `${API_URL}/alunos`,
        options: {
            method: 'POST',
            headers: { 
                "Content-Type": "application/json" 
            },
            body: JSON.stringify(body),
            credentials: 'include'
        }
    };
}

export function EDIT_ALUNO(alunoId, body) {
    return {
        url: `${API_URL}/alunos/${alunoId}`,
        options: {
            method: 'PUT',
            headers: { 
                "Content-Type": "application/json" 
            },
            body: JSON.stringify(body),
            credentials: 'include'
        }
    };
}

export function DELETE_ALUNO(alunoId) {
    return {
        url: `${API_URL}/alunos/${alunoId}`,
        options: {
            method: 'DELETE',
            credentials: 'include'
        }
    };
}

export function GET_EMPRESTIMOS() {
    return {
        url: `${API_URL}/emprestimos`,
        options: {
            credentials: 'include'
        }
    };
}

export function GET_EMPRESTIMO_BY_ID(emprestimoId) {
    return {
        url: `${API_URL}/emprestimos/${emprestimoId}`,
        options: {
            credentials: 'include'
        }
    };
}

export function CREATE_EMPRESTIMO(body) {
    return {
        url: `${API_URL}/emprestimos`,
        options: {
            method: 'POST',
            headers: { 
                "Content-Type": "application/json" 
            },
            body: JSON.stringify(body),
            credentials: 'include'
        }
    };
}

// Essa é a função de devolver um empréstimo, ela recebe como parâmetro o id do livro.
export function EDIT_EMPRESTIMO(livroId) {
    return {
        url: `${API_URL}/emprestimos/${livroId}`,
        options: {
            method: 'PUT',
            credentials: 'include'
        }
    };
}