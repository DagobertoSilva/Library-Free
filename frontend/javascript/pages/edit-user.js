import { GET_ALUNO_BY_ID, EDIT_ALUNO } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', async () => {
    const userEditForm = document.getElementById('userEditForm');
    const nameInput = document.getElementById('name');
    const registrationInput = document.getElementById('registration');
    const cpfInput = document.getElementById('cpf');
    const statusInput = document.getElementById('status-select');

    let currentUserId = null; 

    const params = new URLSearchParams(window.location.search);
    const userId = params.get('id');

    if (userId) {
        currentUserId = userId;
        const { url, options } = GET_ALUNO_BY_ID(userId);
        const { data: userData, error: userError } = await fetchData(url, options);

        if (userError) {
            console.error("Erro ao carregar detalhes do aluno:", userError);
            alert('Erro ao carregar os dados do aluno. Tente novamente.');
            window.location.href = './users.html';
            return;
        }

        if (userData) {
            nameInput.value = userData.nome || '';
            registrationInput.value = userData.matricula || '';
            cpfInput.value = userData.cpf || '';
            statusInput.value = userData.ativo.toString(); 
        } else {
            alert('Aluno não encontrado.');
            window.location.href = './users.html';
        }
    } else {
        alert('ID do aluno não fornecido na URL.');
        window.location.href = './users.html';
    }

    userEditForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const updatedUserData = {
            nome: nameInput.value,
            matricula: registrationInput.value,
            cpf: cpfInput.value,
            ativo: statusInput.value === 'true', 
        };

        if (nameInput.value.trim() === '') {
            alert('O nome do aluno é obrigatório.');
            return;
        }

        const { url, options } = EDIT_ALUNO(currentUserId, updatedUserData);
        const { error: editError } = await fetchData(url, options); 

        if (editError) {
            console.error("Erro ao salvar edição do aluno:", editError);
            alert(`Erro ao salvar edição: CPF ou Matrícula editada já existente`);
        } else {
            alert('Aluno atualizado com sucesso!');
            window.location.href = './users.html';
        }
    });
});