import { LOGIN } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

function setLogin(){
    const form = document.querySelector(".form-login");
    const emailInput = document.querySelector("#email");
    const senhaInput = document.querySelector("#senha");
    const errorText = document.querySelector(".error");
    form.addEventListener("submit", handleSubmit);
    async function handleSubmit(event){
        event.preventDefault();
        let body = {
            email: emailInput.value,
            senha: senhaInput.value
        }
        const {url, options} = LOGIN(body);
        const {data, error} = await fetchData(url,options);
        if(error){
            errorText.textContent = error;
        }else{
            localStorage.setItem("usuarioNome", data.nome);
            window.location.href = "./books.html";
        }
    }
}
setLogin();