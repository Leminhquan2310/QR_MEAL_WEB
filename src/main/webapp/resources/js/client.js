// Render lại toàn bộ cart dựa vào JSON từ server
function renderCart(cart) {
    const modalBody = document.getElementById("modal-cart");
    let container = document.getElementById("cart-container");
    const cartEmpty = document.getElementById("cart-empty");
    const cartCountEls = document.querySelectorAll(".cart-count");

    // Nếu chưa có container mà có sản phẩm, tạo mới
    if (!container && cart.items && cart.items.length > 0) {
        container = document.createElement("div");
        container.id = "cart-container";
        modalBody.innerHTML = ""; // xóa nội dung trống
        modalBody.appendChild(container);
    }

    // Nếu không có sản phẩm
    if (!cart.items || cart.items.length === 0) {
        if (container) container.innerHTML = "";
        document.getElementById("btn-checkout").classList.add("disabled")
        if (cartEmpty) {
            cartEmpty.classList.remove("d-none");
            cartEmpty.textContent = "Giỏ hàng của bạn đang trống.";
        } else {
            // Nếu bị xóa mất #cart-empty, tạo lại
            const emptyMsg = document.createElement("p");
            emptyMsg.id = "cart-empty";
            emptyMsg.className = "text-center text-muted my-4";
            emptyMsg.textContent = "Giỏ hàng của bạn đang trống.";
            modalBody.innerHTML = "";
            modalBody.appendChild(emptyMsg);
        }
        cartCountEls.forEach(el => (el.textContent = 0));
        return;
    } else {
        if (cartEmpty) cartEmpty.classList.add("d-none");
        container.classList.remove("d-none");
        document.getElementById("btn-checkout").classList.remove("disabled");
    }


    // Render sản phẩm
    let html = "";
    cart.items.forEach(item => {
        html += `
        <div class="cart-item position-relative d-flex align-items-center justify-content-between p-2 border-bottom">
        <!-- Nút xóa tinh tế -->
            <button type="button" class="btn-remove" data-id="${item.id}" title="Xóa sản phẩm">
                  <i class="bi bi-x-circle-fill"></i>
            </button>
            <!-- Ảnh -->
            <img src="${item.image}" alt="${item.name}" class="rounded"
                 style="width:60px; height:60px; object-fit:cover;">

            <!-- Tên + đơn giá -->
            <div class="flex-grow-1 ms-3">
                <div class="fw-semibold">${item.name}</div>
                <div class="text-muted small">Đơn giá: ${item.price.toLocaleString("vi-VN")} ₫</div>
            </div>

            <!-- Thành tiền + số lượng -->
            <div class="text-danger fw-bold ms-3">
                <div class="d-flex justify-content-end">
                    ${(item.price * item.quantity).toLocaleString("vi-VN")} ₫
                </div>
                <div class="input-group input-group-sm mt-1 quantity-group" style="max-width:120px;">
                    <button class="btn btn-outline-secondary btn-decrease" data-id="${item.id}">-</button>
                    <input type="text" class="form-control text-center quantity-input" 
                           value="${item.quantity}" data-id="${item.id}" readonly>
                    <button class="btn btn-outline-secondary btn-increase" data-id="${item.id}">+</button>
                </div>
            </div>
        </div>`;
    });

    // Tổng cộng
    html += `
        <div class="text-end mt-3">
            <h6 class="fw-bold">
                Tổng cộng: 
                <span class="text-danger">
                    ${cart.totalAmount.toLocaleString("vi-VN")} ₫
                </span>
            </h6>
        </div>`;

    // Cập nhật giao diện
    container.innerHTML = html;

    // Cập nhật icon giỏ hàng
    cartCountEls.forEach(el => (el.textContent = cart.totalQuantity));

    // Gắn lại event
    initCartEvents();
}


// Xử lý sự kiện cho các nút trong cart
function initCartEvents() {
    document.querySelectorAll('.btn-remove').forEach(btn => {
        btn.addEventListener('click', () => removeFromCart(btn.dataset.id));
    });

    document.querySelectorAll('.btn-decrease').forEach(btn => {
        btn.addEventListener('click', () => updateQuantity(btn.dataset.id, -1));
    });

    document.querySelectorAll('.btn-increase').forEach(btn => {
        btn.addEventListener('click', () => updateQuantity(btn.dataset.id, 1));
    });
}

// Thêm sản phẩm vào cart
document.querySelectorAll('.btn-add-cart').forEach(btn => {
    btn.addEventListener('click', () => {
        const productId = btn.dataset.id;
        const productCard = btn.closest('.product-card');
        const img = productCard.querySelector('.product-img');
        let cartIcon = document.getElementById('cart-icon');
        if (window.getComputedStyle(cartIcon).display == "none") {
            cartIcon = document.getElementById("cart-icon-mobile");
        }

        // 🪄 Tạo ảnh bay
        const flyImg = img.cloneNode(true);
        const rect = img.getBoundingClientRect();
        const cartRect = cartIcon.getBoundingClientRect();

        flyImg.classList.add('fly-img');
        flyImg.style.left = rect.left + 'px';
        flyImg.style.top = rect.top + 'px';
        flyImg.style.width = rect.width + 'px';
        flyImg.style.height = rect.height + 'px';
        document.body.appendChild(flyImg);

        // 🧭 Bắt đầu di chuyển đến giỏ hàng
        setTimeout(() => {
            flyImg.style.left = (cartRect.left + cartRect.width / 2) + 'px';
            flyImg.style.top = (cartRect.top + cartRect.height / 2) + 'px';
            flyImg.style.width = '20px';
            flyImg.style.height = '20px';
            flyImg.style.opacity = '0.5';
        }, 50);

        // 🧹 Xóa ảnh sau khi bay xong
        setTimeout(() => {
            flyImg.remove();

            // Gọi AJAX thêm giỏ hàng
            fetch('/cart', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: `action=add&id=${productId}`
            })
                .then(res => res.json())
                .then(cart => {
                    renderCart(cart);
                    // ✨ Hiệu ứng nháy nhẹ giỏ hàng
                    cartIcon.classList.add('cart-bounce');
                    setTimeout(() => cartIcon.classList.remove('cart-bounce'), 500);
                });
        }, 800);

    });
});

// Xóa sản phẩm
function removeFromCart(productId) {
    fetch('/cart', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `action=remove&id=${productId}`
    })
        .then(res => res.json())
        .then(cart => renderCart(cart));
}

// Cập nhật số lượng
function updateQuantity(productId, delta) {
    fetch('/cart', {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `action=update&id=${productId}&delta=${delta}`
    })
        .then(res => res.json())
        .then(cart => renderCart(cart));
}

// Khởi tạo event khi load
document.addEventListener('DOMContentLoaded', () => {
    initCartEvents();
});
