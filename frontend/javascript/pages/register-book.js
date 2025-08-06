import { GET_LIVRO_BY_ID, CREATE_LIVRO } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', () => {
    async function initRegisterPage() {
        const { url, options } = GET_LIVRO_BY_ID(1);
        const { error: authError } = await fetchData(url, options);

        if (authError) {
            return;
        }
        
        const bookRegisterForm = document.getElementById('bookRegisterForm');
        const tituloInput = document.getElementById('title');
        const isbnInput = document.getElementById('isbn');
        const autorInput = document.getElementById('author');
        const generoInput = document.getElementById('genre');
        const edicaoInput = document.getElementById('edition');
        const dataPublicacaoInput = document.getElementById('date');
        const editoraInput = document.getElementById('publisher');

        bookRegisterForm.addEventListener('submit', bookHandleSubmit);
        async function bookHandleSubmit(event) {
            event.preventDefault();
            const updatedBookData = {
                titulo: tituloInput.value,
                isbn: isbnInput.value,
                autor: autorInput.value,
                genero: generoInput.value,
                edicao: edicaoInput.value,
                publicacao: dataPublicacaoInput.value,
                editora: editoraInput.value,
                emprestado: false
            };
            
            const { url, options } = CREATE_LIVRO(updatedBookData);
            const { error } = await fetchData(url, options);

            if (error) {
                alert("ISBN já existente");
            } else {
                alert("Livro cadastrado com sucesso.");
                setTimeout(() => {
                    window.location.href = "./books.html";
                }, 100);
            }
        }
    }
    initRegisterPage();
});