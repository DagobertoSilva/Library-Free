import { CREATE_ALUNO, GET_LIVRO_BY_ID } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', () => {
    async function initRegisterUserPage() {
        const { url, options } = GET_LIVRO_BY_ID(1);
        const { error: authError } = await fetchData(url, options);
        if (authError) {
            return;
        }
       
        const userRegisterForm = document.getElementById('userRegisterForm');
        const nomeInput = document.getElementById('name');
        const matriculaInput = document.getElementById('registration');
        const cpfInput = document.getElementById('cpf');
        userRegisterForm.addEventListener('submit', userHandleSubmit);

        async function userHandleSubmit(event) {
            event.preventDefault();
            const newUserData = {
                nome: nomeInput.value,
                matricula: matriculaInput.value,
                cpf: cpfInput.value,
                ativo: true
            };

            const { url, options } = CREATE_ALUNO(newUserData);
            const { error } = await fetchData(url, options);

            if (error) {
                alert("Matrícula ou CPF já existente");
            } else {
                alert("Aluno cadastrado com sucesso.");
                setTimeout(() => {
                    window.location.href = "./users.html";
                }, 100);
            }
        }
    }
    initRegisterUserPage();
});