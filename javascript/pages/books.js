import { createPaginatedTable } from '../modules/table-manager.js';
import { GET_LIVROS, FILTER_LIVROS, CREATE_EMPRESTIMO, GET_ALUNO_BY_MATRICULA, EDIT_EMPRESTIMO, DELETE_LIVRO } from '../services/api.js';
import { fetchData } from '../services/fetch.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reserveBookModal = document.getElementById('reserveBookModal');
    const closeButtons = document.querySelectorAll('.close-button');
    const bookTitleModal = document.querySelector('.book-title-modal');
    const reserveForm = document.getElementById('reserveForm');
    const deliveryDateInput = document.getElementById('deliveryDate');
    const addButton = document.getElementById('btn-add');
    console.log(addButton);
    let selectedBook = null;

    const bookConfig = {
        source: GET_LIVROS(),
        renderRow: (book) => {
            const lendIcon = `
            <button class="lendIcon" data-book-id="${book.id}" data-book-isbn="${book.isbn}" data-book-title="${book.titulo}">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M17.5 12.5V15.8333C17.5 16.2754 17.3244 16.6993 17.0118 17.0118C16.6993 17.3244 16.2754 17.5 15.8333 17.5H4.16667C3.72464 17.5 3.30072 17.3244 2.98816 17.0118C2.67559 16.6993 2.5 16.2754 2.5 15.8333V12.5M14.1667 6.66667L10 2.5M10 2.5L5.83333 6.66667M10 2.5V12.5" stroke="#02542D" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
            `
            const returnIcon = `
            <button class="returnIcon" data-book-id="${book.id}" data-book-isbn="${book.isbn}" data-book-title="${book.titulo}">
                <svg width="20" height="21" viewBox="0 0 20 21" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M17.5 12.8333V16.1666C17.5 16.6087 17.3244 17.0326 17.0118 17.3452C16.6993 17.6577 16.2754 17.8333 15.8333 17.8333H4.16667C3.72464 17.8333 3.30072 17.6577 2.98816 17.3452C2.67559 17.0326 2.5 16.6087 2.5 16.1666V12.8333M5.83333 8.66665L10 12.8333M10 12.8333L14.1667 8.66665M10 12.8333V2.83331" stroke="#303030" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
            `
            const editIcon = `
            <button class="editIcon" data-book-id="${book.id}">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <g clip-path="url(#clip0_151_188)">
                        <path d="M9.16675 3.33332H3.33341C2.89139 3.33332 2.46746 3.50891 2.1549 3.82147C1.84234 4.13403 1.66675 4.55796 1.66675 4.99999V16.6667C1.66675 17.1087 1.84234 17.5326 2.1549 17.8452C2.46746 18.1577 2.89139 18.3333 3.33341 18.3333H15.0001C15.4421 18.3333 15.866 18.1577 16.1786 17.8452C16.4912 17.5326 16.6667 17.1087 16.6667 16.6667V10.8333M15.4167 2.08332C15.7483 1.7518 16.1979 1.56555 16.6667 1.56555C17.1356 1.56555 17.5852 1.7518 17.9167 2.08332C18.2483 2.41484 18.4345 2.86448 18.4345 3.33332C18.4345 3.80216 18.2483 4.2518 17.9167 4.58332L10.0001 12.5L6.66675 13.3333L7.50008 9.99999L15.4167 2.08332Z" stroke="#522504" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </g>
                    <defs>
                        <clipPath id="clip0_151_188">
                            <rect width="20" height="20" fill="white"/>
                        </clipPath>
                    </defs>
                </svg>
            </button>
            `
            const deleteIcon = `
            <button class="deleteIcon" data-book-id="${book.id}" data-book-title="${book.titulo}">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M2.5 5.00002H4.16667M4.16667 5.00002H17.5M4.16667 5.00002L4.16667 16.6667C4.16667 17.1087 4.34226 17.5326 4.65482 17.8452C4.96738 18.1578 5.39131 18.3334 5.83333 18.3334H14.1667C14.6087 18.3334 15.0326 18.1578 15.3452 17.8452C15.6577 17.5326 15.8333 17.1087 15.8333 16.6667V5.00002M6.66667 5.00002V3.33335C6.66667 2.89133 6.84226 2.4674 7.15482 2.15484C7.46738 1.84228 7.89131 1.66669 8.33333 1.66669H11.6667C12.1087 1.66669 12.5326 1.84228 12.8452 2.15484C13.1577 2.4674 13.3333 2.89133 13.3333 3.33335V5.00002M8.33333 9.16669V14.1667M11.6667 9.16669V14.1667" stroke="#900B09" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
            `;

            const bookFlag = book.emprestado ? "Ocupado" : "Livre"

            return `
                <td>${book.isbn}</td>
                <td>${book.titulo}</td>
                <td>${book.genero}</td>
                <td><span class="${bookFlag.toLowerCase()}">${bookFlag}</span></td>
                <td class="table-body-actions">
                    ${book.emprestado ? returnIcon : lendIcon}
                    ${editIcon}
                    ${deleteIcon}
                </td>
            `
        },
        filterData: (titulo) => {
            return FILTER_LIVROS(titulo);
        }
    }
    createPaginatedTable(bookConfig);

    document.addEventListener('click', async (event) => { 
        if (event.target.closest('.lendIcon')) {
            const button = event.target.closest('.lendIcon');
            selectedBook = {
                id: button.dataset.bookId, 
                isbn: button.dataset.bookIsbn,
                titulo: button.dataset.bookTitle
            };
            bookTitleModal.textContent = selectedBook.titulo;
            reserveBookModal.style.display = 'flex';
        }

        if (event.target.closest('.editIcon')) {
            const button = event.target.closest('.editIcon');
            const bookId = button.dataset.bookId; 
            window.location.href = `./edit-book.html?id=${bookId}`; 
        }

        if (event.target.closest('.returnIcon')) {
            const button = event.target.closest('.returnIcon');
            const bookIdToReturn = button.dataset.bookId; 
            const bookTitleToReturn = button.dataset.bookTitle;

            const confirmReturn = confirm(`Deseja realmente devolver o livro "${bookTitleToReturn}"?`);

            if (confirmReturn) {
                const { url, options } = EDIT_EMPRESTIMO(bookIdToReturn); 
                const { error } = await fetchData(url, options); 

                if (error) {
                    console.error("Erro ao devolver livro:", error);
                    alert(`Erro ao devolver o livro "${bookTitleToReturn}": ${error.message || 'Ocorreu um erro ao tentar devolver o livro.'}`);
                } else {
                    alert(`Livro "${bookTitleToReturn}" devolvido com sucesso!`);
                    createPaginatedTable(bookConfig); 
                }
            }
        }
    });

    closeButtons.forEach(button => {
        button.addEventListener('click', () => {
            reserveBookModal.style.display = 'none';
            reserveForm.reset();
            selectedBook = null;
        });
    });

    window.addEventListener('click', (event) => {
        if (event.target === reserveBookModal) {
            reserveBookModal.style.display = 'none';
            reserveForm.reset();
            selectedBook = null;
        }
    });

    reserveForm.addEventListener('submit', async (event) => { 
        event.preventDefault();

        const userMatriculaOuNome = document.getElementById('userMatricula').value.trim();
        const deliveryDateString = document.getElementById('deliveryDate').value;

        if (!userMatriculaOuNome || !deliveryDateString) {
            alert('Por favor, preencha todos os campos!');
            return;
        }

        const deliveryDate = new Date(deliveryDateString);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (deliveryDate < today) {
            alert('A data de entrega não pode ser uma data passada. Por favor, selecione uma data futura ou a data de hoje.');
            return;
        }
        else if (deliveryDate > new Date(today.getFullYear(), today.getMonth(), today.getDate() + 7)) {
            alert('O empréstimo não pode ultrapassar o total 7 dias. Tente novamente.');
            return;
        }

        const { url: alunoUrl, options: alunoOptions } = GET_ALUNO_BY_MATRICULA(userMatriculaOuNome);
        const { data: alunoData, error: alunoError } = await fetchData(alunoUrl, alunoOptions);

        if (alunoError) {
            console.error("Erro ao buscar aluno:", alunoError);
            alert('Erro ao verificar usuário. Por favor, tente novamente ou verifique a conexão.');
            return;
        }
       
        if (!alunoData || !alunoData.id) {
            alert('Usuário não encontrado. Por favor, verifique a matrícula.');
            return;
        }

        const foundUser = alunoData;

        if (foundUser.ativo !== true) {
            alert(`Usuário "${foundUser.nome || userMatriculaOuNome}" está inativo ou inválido. Não é possível realizar a reserva.`);
            return;
        }

        const emprestimoBody = {
            alunoId: foundUser.id, 
            livroId: selectedBook.id, 
            dataEmprestimo: new Date().toISOString().split('T')[0], 
            prazoDevolucao: deliveryDateString 
        };

        const { url: emprestimoUrl, options: emprestimoOptions } = CREATE_EMPRESTIMO(emprestimoBody);
        const { data: emprestimoData, error: emprestimoError } = await fetchData(emprestimoUrl, emprestimoOptions);

        if (emprestimoError) {
            console.error("Erro ao criar empréstimo:", emprestimoError);
            alert(`Erro ao realizar reserva: ${emprestimoError.message || 'Verifique se o livro já está emprestado ou o usuário tem reservas pendentes.'}`);
            return;
        }

        alert(`Livro "${selectedBook.titulo}" reservado com sucesso para "${foundUser.nome}"!`);

        reserveBookModal.style.display = 'none';
        reserveForm.reset();
        selectedBook = null;

        createPaginatedTable(bookConfig); 
    });
    
    document.getElementById('cancel-reserve-button').addEventListener('click', () => {
        reserveBookModal.style.display = 'none';
        reserveForm.reset();
        selectedBook = null;
    });

    addButton.addEventListener('click', () => {
        window.location.href = "./register-book.html";
    });

    // Lógica para o modal de deleção de livro

    const confirmModal = document.getElementById('confirmModal');
    const confirmModalTitle = document.getElementById('confirmModalTitle');
    const confirmModalBody = document.getElementById('confirmModalBody');
    const confirmModalConfirmBtn = document.getElementById('confirmModalConfirm');
    const confirmModalCancelBtn = document.getElementById('confirmModalCancel');
    let confirmAction = null;

    /**
     * @param {string} title
     * @param {string} body
     * @param {Function} onConfirm
     */

    function openConfirmModal(title, body, onConfirm) {
        confirmModalTitle.textContent = title;
        confirmModalBody.textContent = body;
        confirmAction = onConfirm;
        confirmModal.style.display = 'flex';
    }

    function closeConfirmModal() {
        confirmModal.style.display = 'none';
        confirmAction = null;
    }

    confirmModalConfirmBtn.addEventListener('click', () => {
        if (confirmAction) {
            confirmAction();
        }
        closeConfirmModal();
    });

    confirmModalCancelBtn.addEventListener('click', closeConfirmModal);
    confirmModal.querySelector('.close-button').addEventListener('click', closeConfirmModal);
    window.addEventListener('click', (event) => {
        if (event.target === confirmModal) {
            closeConfirmModal();
        }
    });

    document.addEventListener('click', async (event) => { 
        if (event.target.closest('.deleteIcon')) {
            const button = event.target.closest('.deleteIcon');
            const bookId = button.dataset.bookId;
            const bookTitle = button.dataset.bookTitle;

            const deleteAction = async () => {
                console.log(`Deletando livro com ID: ${bookId}`);
                const { url, options } = DELETE_LIVRO(bookId);
                const { error } = await fetchData(url, options);
                if (error) { 
                    alert("Erro ao deletar livro.")
                 }
                else { 
                    alert(`Livro "${bookTitle}" deletado com sucesso!`)
                    createPaginatedTable(bookConfig);                
                }
                
            };
            openConfirmModal(
                'Deseja apagar esse Livro?',
                bookTitle,
                deleteAction
            );
        }
    });
});