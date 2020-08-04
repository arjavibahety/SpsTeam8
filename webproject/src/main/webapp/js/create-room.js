$(document).ready(function() {
    getCategories();
});

function getCategories() {
    let categoryBox = document.getElementById("category");
    let categories = ["Chinese", "Thai", "Indian", "Western", "Halal"]
    for (let category of categories) {
        categoryBox.innerHTML += `<option value="${category.toUpperCase()}">${category}</option>`
    }
}
