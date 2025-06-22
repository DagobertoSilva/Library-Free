let allBooks = [];
let currentPage = 1;
let booksPerPage = 8;

const prevButton = document.querySelector("#btn-previous");
const pageIndex = document.querySelector("#page-index")
const nextButton = document.querySelector("#btn-next");

async function init(){
    response = await fetch("./books.json");
    allBooks = await response.json();
    pageIndex.textContent = currentPage;
    lastPage = Math.ceil(allBooks.length / booksPerPage);
    prevButton.addEventListener('click', previousPage)
    nextButton.addEventListener('click', nextPage)
    displayPage();
}
init();

function previousPage() {
    if (currentPage > 1) {
        currentPage--;
        displayPage();
    }
}

function nextPage() {
    if (currentPage < lastPage) {
        currentPage++;
        displayPage();
    }
}

function updateButtonState() {
    prevButton.disabled = currentPage === 1;
    pageIndex.textContent = currentPage;
    nextButton.disabled = currentPage === lastPage;
}

function displayPage(){
    const startIndex = (currentPage - 1) * booksPerPage;
    const endIndex = startIndex + booksPerPage;
    const paginatedBooks = allBooks.slice(startIndex, endIndex);
    renderBooks(paginatedBooks);
    updateButtonState();
}

async function renderBooks(books){
    const tbody = document.querySelector(".book-table-body");
    tbody.innerHTML = "";
    books.forEach(book => {
        const row = document.createElement('tr');
        const lendIcon = `
            <button class="lendIcon">
                <svg width="36" height="36" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M18 0.5C27.665 0.5 35.5 8.33502 35.5 18C35.5 27.665 27.665 35.5 18 35.5C8.33502 35.5 0.5 27.665 0.5 18C0.5 8.33502 8.33502 0.5 18 0.5Z" fill="#14AE5C"/>
                    <path d="M18 0.5C27.665 0.5 35.5 8.33502 35.5 18C35.5 27.665 27.665 35.5 18 35.5C8.33502 35.5 0.5 27.665 0.5 18C0.5 8.33502 8.33502 0.5 18 0.5Z" stroke="#02542D" stroke-linecap="round"/>
                    <path d="M25.5 20.5V23.8333C25.5 24.2754 25.3244 24.6993 25.0118 25.0118C24.6993 25.3244 24.2754 25.5 23.8333 25.5H12.1667C11.7246 25.5 11.3007 25.3244 10.9882 25.0118C10.6756 24.6993 10.5 24.2754 10.5 23.8333V20.5M22.1667 14.6667L18 10.5M18 10.5L13.8333 14.6667M18 10.5V20.5" stroke="#02542D" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        `
        const returnIcon = `
            <button class="returnIcon">
                <svg width="36" height="37" viewBox="0 0 36 37" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M18 1.16666C27.665 1.16666 35.5 9.00167 35.5 18.6667C35.5 28.3316 27.665 36.1667 18 36.1667C8.33502 36.1667 0.5 28.3316 0.5 18.6667C0.5 9.00167 8.33502 1.16666 18 1.16666Z" fill="#CDCDCD"/>
                    <path d="M18 1.16666C27.665 1.16666 35.5 9.00167 35.5 18.6667C35.5 28.3316 27.665 36.1667 18 36.1667C8.33502 36.1667 0.5 28.3316 0.5 18.6667C0.5 9.00167 8.33502 1.16666 18 1.16666Z" stroke="#303030" stroke-linecap="round"/>
                    <path d="M25.5 21.1667V24.5C25.5 24.942 25.3244 25.3659 25.0118 25.6785C24.6993 25.9911 24.2754 26.1667 23.8333 26.1667H12.1667C11.7246 26.1667 11.3007 25.9911 10.9882 25.6785C10.6756 25.3659 10.5 24.942 10.5 24.5V21.1667M13.8333 17L18 21.1667M18 21.1667L22.1667 17M18 21.1667V11.1667" stroke="#303030" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        `
        row.innerHTML = `
        <td>${book.isbn}</td>
        <td>${book.titulo}</td>
        <td>${book.genero}</td>
        <td><span class="${book.status.toLowerCase()}">${book.status}</span></td>
        <td class="book-table-body-actions">
            ${book.status.toLowerCase() === "livre" ? lendIcon : returnIcon}
            <button class="editIcon">
                <svg width="36" height="36" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M18 0.5C27.665 0.5 35.5 8.33502 35.5 18C35.5 27.665 27.665 35.5 18 35.5C8.33502 35.5 0.5 27.665 0.5 18C0.5 8.33502 8.33502 0.5 18 0.5Z" fill="#E8B931"/>
                    <path d="M18 0.5C27.665 0.5 35.5 8.33502 35.5 18C35.5 27.665 27.665 35.5 18 35.5C8.33502 35.5 0.5 27.665 0.5 18C0.5 8.33502 8.33502 0.5 18 0.5Z" stroke="#522504" stroke-linecap="round"/>
                    <g clip-path="url(#clip0_6_9583)">
                        <path d="M17.1667 11.3333H11.3334C10.8914 11.3333 10.4675 11.5089 10.1549 11.8215C9.84234 12.134 9.66675 12.558 9.66675 13V24.6667C9.66675 25.1087 9.84234 25.5326 10.1549 25.8452C10.4675 26.1577 10.8914 26.3333 11.3334 26.3333H23.0001C23.4421 26.3333 23.866 26.1577 24.1786 25.8452C24.4912 25.5326 24.6667 25.1087 24.6667 24.6667V18.8333M23.4167 10.0833C23.7483 9.7518 24.1979 9.56555 24.6667 9.56555C25.1356 9.56555 25.5852 9.7518 25.9167 10.0833C26.2483 10.4148 26.4345 10.8645 26.4345 11.3333C26.4345 11.8022 26.2483 12.2518 25.9167 12.5833L18.0001 20.5L14.6667 21.3333L15.5001 18L23.4167 10.0833Z" stroke="#522504" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </g>
                    <defs>
                        <clipPath id="clip0_6_9583">
                            <rect width="20" height="20" fill="white" transform="translate(8 8)"/>
                        </clipPath>
                    </defs>
                </svg>
            </button>
            <button class="deleteIcon">
                <svg width="36" height="36" viewBox="0 0 36 36" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M18 0.5C27.665 0.5 35.5 8.33502 35.5 18C35.5 27.665 27.665 35.5 18 35.5C8.33502 35.5 0.5 27.665 0.5 18C0.5 8.33502 8.33502 0.5 18 0.5Z" fill="#EC221F"/>
                    <path d="M18 0.5C27.665 0.5 35.5 8.33502 35.5 18C35.5 27.665 27.665 35.5 18 35.5C8.33502 35.5 0.5 27.665 0.5 18C0.5 8.33502 8.33502 0.5 18 0.5Z" stroke="#900B09" stroke-linecap="round"/>
                    <path d="M10.5 13H12.1667M12.1667 13H25.5M12.1667 13L12.1667 24.6667C12.1667 25.1087 12.3423 25.5326 12.6548 25.8452C12.9674 26.1578 13.3913 26.3334 13.8333 26.3334H22.1667C22.6087 26.3334 23.0326 26.1578 23.3452 25.8452C23.6577 25.5326 23.8333 25.1087 23.8333 24.6667V13M14.6667 13V11.3334C14.6667 10.8913 14.8423 10.4674 15.1548 10.1548C15.4674 9.84228 15.8913 9.66669 16.3333 9.66669H19.6667C20.1087 9.66669 20.5326 9.84228 20.8452 10.1548C21.1577 10.4674 21.3333 10.8913 21.3333 11.3334V13M16.3333 17.1667V22.1667M19.6667 17.1667V22.1667" stroke="#900B09" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
            </button>
        </td>
        `
        tbody.appendChild(row);
    });
}

function activeClass() {
    const currentPage = window.location.pathname;
    const menuItems = document.querySelectorAll(".menu-list .menu-item");
    menuItems.forEach(item => {
        const itemHref = item.getAttribute("href");
        if (currentPage.includes(itemHref) && itemHref !== "/") {
            item.classList.add("active");
        }
    });
}
activeClass();

