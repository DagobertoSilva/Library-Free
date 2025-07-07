export function createPaginatedTable(config){
    let dataList = [];
    let currentPage = 1;
    let lastPage = 1;
    let dataPerPage = 8;
    
    const tableBody = document.querySelector(".table-body");
    const prevButton = document.querySelector("#btn-previous");
    const pageIndex = document.querySelector("#page-index")
    const nextButton = document.querySelector("#btn-next");
    const searchForm = document.querySelector("#search-form");
    const searchInput = document.querySelector("#search-input");
    const searchButton = document.querySelector("#search-button");
    
    async function init(){
        await updateDataList(null);
        updatePageStates();
        setupPagination();
        setupSearch();
    }
    init();

    async function updateDataList(updatedList){
        console.log(updatedList);
        if(updatedList === null){
            const response = await fetch(config.source)
            dataList = await response.json();
        }else{
            dataList = updatedList;
        }
        currentPage = 1;
        lastPage = Math.ceil(dataList.length / dataPerPage);
        paginateData();
    }

    function updatePageStates(){
        pageIndex.textContent = currentPage;
        prevButton.disabled = currentPage === 1;
        nextButton.disabled = currentPage === lastPage || lastPage === 0;
    }

    function setupPagination(){
        prevButton.addEventListener("click", () => {
            if(currentPage > 1){
                currentPage--;
                updatePageStates();
                paginateData();
            }
        });
        nextButton.addEventListener("click", () => {
            if(currentPage < lastPage){
                currentPage++;
                updatePageStates();
                paginateData();
            }
        });
    }

    function paginateData(){
        const startIndex = (currentPage - 1) * dataPerPage;
        const endIndex = startIndex + dataPerPage;
        const paginatedData = dataList.slice(startIndex, endIndex);
        renderTableData(paginatedData);
    }

    function renderTableData(paginatedData){
        tableBody.innerHTML = "";
        paginatedData.forEach(data => {
            const tableRow = document.createElement('tr');
            tableRow.innerHTML = config.renderRow(data);
            tableBody.appendChild(tableRow);
        });
    }

    function setupSearch(){
        let isSearchActive = false;
        const search = `
            <svg width="16" height="16" viewBox="0 0 16 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M7.33333 12.6667C10.2789 12.6667 12.6667 10.2789 12.6667 7.33333C12.6667 4.38781 10.2789 2 7.33333 2C4.38781 2 2 4.38781 2 7.33333C2 10.2789 4.38781 12.6667 7.33333 12.6667Z" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                <path d="M14.0001 14L11.1001 11.1" stroke="black" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>    
            <p>Buscar</p>
        `
        const clear = `
            <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#black"><path d="m256-200-56-56 224-224-224-224 56-56 224 224 224-224 56 56-224 224 224 224-56 56-224-224-224 224Z"/></svg>   
            <p>Limpar</p>
        `
        searchButton.innerHTML = search;
        searchForm.addEventListener("submit", handleSubmit);
        function handleSubmit(event){
            event.preventDefault();
            if(isSearchActive){
                isSearchActive = false;
                searchInput.value = "";
                searchButton.innerHTML = search;
                updateDataList(null);
            }else{
                if(searchInput.value){
                    isSearchActive = true;
                    const filteredData = config.filterData(dataList, searchInput.value);
                    updateDataList(filteredData);
                    searchButton.innerHTML = clear;
                }else{
                    return;
                }
            }
        }
    }
}