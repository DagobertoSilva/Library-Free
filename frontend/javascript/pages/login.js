import { LOGIN } from '../services/api.js';

function setLogin() {
    const form = document.querySelector(".form-login");
    const emailInput = document.querySelector("#email");
    const senhaInput = document.querySelector("#senha");
    const errorText = document.querySelector(".error");
    const submitButton = form.querySelector('button[type="submit"]');

    form.addEventListener("submit", handleSubmit);

    async function handleSubmit(event) {
        event.preventDefault();
        submitButton.disabled = true;
        submitButton.textContent = "Entrando...";
        errorText.textContent = "";

        try {
            const body = {
                email: emailInput.value,
                senha: senhaInput.value
            };
            const { url, options } = LOGIN(body);
            const response = await fetch(url, options);
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.message || 'Ocorreu um erro no login.'); 
            }
            errorText.textContent = "";
            localStorage.setItem("usuarioNome", data.nome);
            window.location.href = "./home.html";
        } catch (error) {
            console.error("Erro no login:", error);
            errorText.textContent = error.message;
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Entrar";
        }
    }
}
setLogin();