import { GET_LIVRO_BY_ID, EDIT_LIVRO } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', async () => {
    const bookEditForm = document.getElementById('bookEditForm');
    const tituloInput = document.getElementById('titulo');
    const isbnInput = document.getElementById('isbn');
    const autorInput = document.getElementById('autor');
    const generoInput = document.getElementById('genero');
    const edicaoInput = document.getElementById('edicao');
    const dataPublicacaoInput = document.getElementById('dataPublicacao');
    const editoraInput = document.getElementById('editora');

    let currentBookId = null; 
    let emprestadoStatus = false;

    function formatDateForInput(dateString) {
        if (!dateString) return '';
        return dateString; 
    }

    const params = new URLSearchParams(window.location.search);
    const bookId = params.get('id'); 

    if (bookId) {
        currentBookId = bookId;
        const { url, options } = GET_LIVRO_BY_ID(bookId);
        const { data: bookData, error: bookError } = await fetchData(url, options);

        if (bookError) {
            console.error("Erro ao carregar detalhes do livro:", bookError);
            alert('Erro ao carregar os dados do livro. Tente novamente ou verifique a conexão.');
            window.history.back();
            return;
        }


        if (bookData) {
            tituloInput.value = bookData.titulo || '';
            isbnInput.value = bookData.isbn || '';
            autorInput.value = bookData.autor || '';
            generoInput.value = bookData.genero || '';
            edicaoInput.value = bookData.edicao || '';
            dataPublicacaoInput.value = formatDateForInput(bookData.publicacao);
            editoraInput.value = bookData.editora || '';
            emprestadoStatus = bookData.emprestado ?? false;
        } else {
            alert('Livro não encontrado no backend.');
            window.history.back();
        }
    } else {
        alert('ID do livro não fornecido na URL. Esta página é para edição.');
        window.history.back(); 
    }

    bookEditForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const updatedBookData = {
            titulo: tituloInput.value,
            isbn: isbnInput.value,
            autor: autorInput.value,
            genero: generoInput.value,
            edicao: edicaoInput.value,
            publicacao: dataPublicacaoInput.value,
            editora: editoraInput.value,
            emprestado: emprestadoStatus
        };

        if (tituloInput.value.trim() === '') {
            alert('O título do livro é obrigatório.');
            return;
        }

        

        const { url, options } = EDIT_LIVRO(currentBookId, updatedBookData);
        const { error: editError } = await fetchData(url, options); 

        if (editError) {
            console.error("Erro ao salvar edição do livro:", editError);
            alert(`Erro ao salvar edição: ${editError.message || 'Ocorreu um erro ao atualizar o livro.'}`);
        } else {
            alert('Livro atualizado com sucesso!');
            window.history.back(); 
        }
    });
});