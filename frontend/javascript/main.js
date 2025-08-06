import { LOGOUT } from "./services/api.js";

async function setLogout() {
    const exit = document.querySelector(".exit");
    if(exit){
        exit.addEventListener("click", handleLogout);
    }
    async function handleLogout(event){
        event.preventDefault();
        const { url, options } = LOGOUT();
        try {
            await fetch(url, options);
        } finally {
            localStorage.removeItem("usuarioNome");
            window.location.href = "./login.html";
        }
    }
}
setLogout();