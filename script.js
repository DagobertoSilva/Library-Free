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