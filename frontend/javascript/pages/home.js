import { GET_LATEST_EMPRESTIMOS } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

async function displayLatestLoans() {
    const container = document.getElementById('latest-loans-container');
    if (!container) return;
    container.innerHTML = '<p>Carregando empréstimos...</p>';
    const { url, options } = GET_LATEST_EMPRESTIMOS();
    const { data: latestLoans, error } = await fetchData(url, options);
    if (error || !latestLoans) {
        container.innerHTML = '<p>Erro ao carregar os empréstimos.</p>';
        return;
    }
    const cardsHtml = latestLoans.map(loan => {
        const emprestimoDate = new Date(loan.dataEmprestimo).toLocaleDateString('pt-BR');
        const devolucaoDate = new Date(loan.prazoDevolucao).toLocaleDateString('pt-BR');
        return `
            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                <div class="card h-100 loan-card">
                    <img src="../img/book.svg" class="card-img-top" alt="Capa do livro ${loan.livroTitulo}">
                    <div class="card-body">
                        <h5 class="card-title">${loan.livroTitulo}</h5>
                        <p class="card-text"><small class="text-body-secondary">${emprestimoDate} - ${devolucaoDate}</small></p>
                        <p class="card-text">Empréstimo feito a ${loan.alunoNome}</p>
                    </div>
                </div>
            </div>
        `;
    }).join('');
    container.innerHTML = cardsHtml;
}
document.addEventListener('DOMContentLoaded', displayLatestLoans);

document.addEventListener('DOMContentLoaded', () => {
    const nomeDoUsuario = localStorage.getItem('usuarioNome');
    const tituloBoasVindas = document.querySelector('h1.title');
    if (nomeDoUsuario && tituloBoasVindas) {
        tituloBoasVindas.textContent = `Seja Bem-vindo, ${nomeDoUsuario}`;
    }
});
