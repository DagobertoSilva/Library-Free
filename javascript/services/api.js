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