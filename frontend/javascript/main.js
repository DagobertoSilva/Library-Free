async function setLogout() {
    const exit = document.querySelector(".exit");

    if (exit) {
        exit.addEventListener("click", handleLogout);
    }

    function handleLogout(event) {
        event.preventDefault();

        localStorage.removeItem("token");
        localStorage.removeItem("usuarioNome");
        
        window.location.href = "./login.html";
    }
}

setLogout();