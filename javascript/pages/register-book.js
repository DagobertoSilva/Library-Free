import {  CREATE_LIVRO } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

const bookRegisterForm = document.getElementById('bookRegisterForm');
const tituloInput = document.getElementById('title');
const isbnInput = document.getElementById('isbn');
const autorInput = document.getElementById('author');
const generoInput = document.getElementById('genre');
const edicaoInput = document.getElementById('edition');
const dataPublicacaoInput = document.getElementById('date');
const editoraInput = document.getElementById('publisher');
console.log(bookRegisterForm)

bookRegisterForm.addEventListener('submit', handleSubmit)
async function handleSubmit(event){
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
    const {url, options} = CREATE_LIVRO(updatedBookData);
    const {data, error} = await fetchData(url, options);
    if(error){
        alert("ISBN já existente")
        error = null;
    }else{
        alert("Livro cadastrado com sucesso.");
        setTimeout(() => {
        window.location.href = "./books.html";
        }, 100);
    }
}