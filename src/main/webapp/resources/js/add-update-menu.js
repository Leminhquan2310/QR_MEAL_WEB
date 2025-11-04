// Lưu toàn bộ products từ server
let allProducts = [];

document.addEventListener("DOMContentLoaded", () => {
    fetch(`/menu?action=get-products`)
        .then(res => res.json())
        .then(data => allProducts = data)
        .catch(err => console.log(err.message));
});

// Select Product
const tagSelectorProducts = new MultiSelectTag('products', {
    required: true,
    placeholder: 'Search tags',
    onChange: function (selected) {
        console.log("Selected: ", selected)
    },
});
