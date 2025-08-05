import { CREATE_ALUNO } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

const userRegisterForm = document.getElementById('userRegisterForm');
const nomeInput = document.getElementById('name');
const matriculaInput = document.getElementById('registration');
const cpfInput = document.getElementById('cpf');
userRegisterForm.addEventListener('submit', userHandleSubmit)
async function userHandleSubmit(event){
    event.preventDefault();
    const updatedUserData = {
        nome: nomeInput.value,
        matricula: matriculaInput.value,
        cpf: cpfInput.value,
        ativo: true
    };
    const {url, options} = CREATE_ALUNO(updatedUserData);
    const { error } = await fetchData(url, options);
    if(error){
        alert("Matrícula ou CPF já existente")
    }else{
        alert("Aluno cadastrado com sucesso.");
        setTimeout(() => {
        window.location.href = "./users.html";
        }, 100);
    }
}