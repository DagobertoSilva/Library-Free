import { AuthService } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

function setLogin() {
    const form = document.querySelector(".form-login");
    const emailInput = document.querySelector("#email");
    const senhaInput = document.querySelector("#senha");
    const errorText = document.querySelector(".error");
    const submitButton = form.querySelector('button[type="submit"]');

    autoLogin();

    form.addEventListener("submit", handleSubmit);

    async function autoLogin() {
        const token = localStorage.getItem("token");
        if (!token) return;

        const { url, options } = AuthService.me(token);
        
        const { data, error } = await fetchData(url, options);

        if (error) {
            localStorage.clear();
        } else {
            if(data.perfil) {
                localStorage.setItem("token", data.token);
                localStorage.setItem("usuarioNome", data.perfil.nome);
            }
            window.location.href = "./books.html";
        }
    }

    async function handleSubmit(event) {
        event.preventDefault();

        submitButton.disabled = true;
        submitButton.textContent = "Entrando...";
        errorText.textContent = "";

        const body = {
            email: emailInput.value,
            senha: senhaInput.value
        };

        const { url, options } = AuthService.login(body);
        
        const { data, error } = await fetchData(url, options);

        submitButton.disabled = false;
        submitButton.textContent = "Entrar";

        if (error) {
            errorText.textContent = error.message;
            return;
        }

        if (data) {
            localStorage.setItem("token", data.token);
            localStorage.setItem("usuarioNome", data.perfil.nome);
            
            window.location.href = "./books.html";
        }
    }
}

setLogin();