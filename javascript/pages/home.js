import { GET_EMPRESTIMOS } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

async function displayLatestLoans() {
    const container = document.getElementById('latest-loans-container');
    if (!container) return;
    container.innerHTML = '<p>Carregando empréstimos...</p>';
    const { url, options } = GET_EMPRESTIMOS();
    const { data: allLoans, error } = await fetchData(url, options);
    console.log(allLoans);
    if (error) {
        container.innerHTML = '<p>Erro ao carregar os empréstimos.</p>';
        return;
    }

    
}


document.addEventListener('DOMContentLoaded', displayLatestLoans);
