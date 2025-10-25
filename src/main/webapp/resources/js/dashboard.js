document.addEventListener('DOMContentLoaded', () => {
    const tableModal = document.getElementById('tableModal');
    // Khởi tạo Lottie animation
    const lottieAnim = lottie.loadAnimation({
        container: document.getElementById('lottieLoading'), // DOM element
        renderer: 'svg',
        loop: true,
        autoplay: true,
        path: 'resources/animations/loading.json' // đường dẫn tới file JSON loading
    });

    tableModal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        const tableId = button.getAttribute('data-id');
        const modalBody = document.querySelector('#orderDetailsBody');
        const spinner = document.getElementById('loadingSpinner');
        const container = document.getElementById('orderDetailsContainer');
        const totalSpan = document.getElementById('orderTotal');


        // Lấy dữ liệu từ data-attributes
        const name = button.getAttribute('data-name');
        const status = button.getAttribute('data-status');
        // set màu header
        const header = document.querySelector(".modal-header");
        header.classList.add(`bg-${status === "1" ? "success" : "warning"}`);

        // Reset UI
        modalBody.innerHTML = '';
        container.style.display = 'none';
        spinner.style.display = 'block';
        totalSpan.textContent = '0';


        // Gọi AJAX
        fetch(`api/order-detail?action=by-table-id&id=${tableId}`)
            .then(response => response.json())
            .then(data => {
                spinner.style.display = 'none';
                container.style.display = 'block';
                const oStatus = data.order?.status;

                // Gán vào modal
                header.querySelector("#tableModalLabel").innerHTML = `${name}: ${oStatus?.label ?? "Trống"}`

                if (data.details.length === 0) {
                    modalBody.innerHTML = `<tr><td colspan="6" class="text-center text-muted">Bàn chưa có món nào</td></tr>`;
                    return;
                }


                let total = 0;
                data.details.forEach((item, index) => {
                    const product = item.product || {};
                    const itemTotal = item.quantity * item.price;
                    total += itemTotal;

                    modalBody.innerHTML += `
                        <tr>
                            <td>${index + 1}</td>
                            <td>
                                <img src="${product.image || 'images/no-image.png'}" alt="${product.name}" width="50" class="me-2 rounded">
                                ${product.name || 'N/A'}
                            </td>
                            <td>${item.quantity}</td>
                            <td>${item.price.toLocaleString('vi-VN')} ₫</td>
                            <td>${itemTotal.toLocaleString('vi-VN')} ₫</td>
                        </tr>
                    `;
                });

                // Gán link footer
                const modalFooter = document.getElementById("modalFooter");

                const labelButton = oStatus.code == "0" ? "Xác nhận" : oStatus.code == "1" ? "Hoàn tất chế biến" : "";
                let footerHtml = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>`;
                footerHtml += oStatus.code == "2" ?
                    `<button id="viewInvoiceBtn" class="btn btn-${status === "1" ? "success" : "warning"}"
                            onclick="confirmPayment('${data.order.id}',0.0)">Thanh toán</button>` :
                    `<button id="viewInvoiceBtn" class="btn btn-${status === "1" ? "success" : "warning"}" 
                            onclick="updateStatusOrder('${data.order.id}','${labelButton}','Xác nhận thay đổi?',${oStatus.code + 1})">${labelButton}</button>`;

                modalFooter.innerHTML = footerHtml;
                totalSpan.textContent = total.toLocaleString('vi-VN');
            })
            .catch(err => {
                spinner.style.display = 'none';
                modalBody.innerHTML = `<tr><td colspan="6" class="text-danger text-center">Lỗi tải dữ liệu!</td></tr>`;
                console.error(err);
            });
    });


    tableModal.addEventListener('hidden.bs.modal', () => {
        document.getElementById('orderDetailsBody').innerHTML = '';
        document.getElementById("modalFooter").innerHTML = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>`;
        document.querySelector(".modal-header").classList.remove("bg-success");
        document.querySelector(".modal-header").classList.remove("bg-warning");
    })
});

function updateStatusOrder(id, title, text, status) {
    Swal.fire({
        title: title,
        text: text,
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement("form");
            form.method = "POST";
            form.action = `/dashboard?action=update-status`;

            // input hidden chứa id
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "id";
            input.value = id;
            form.appendChild(input);

            const inputStatus = document.createElement("input");
            inputStatus.type = "hidden";
            inputStatus.name = "status";
            inputStatus.value = status;
            form.appendChild(inputStatus);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

function confirmPayment(id, discount) {
    Swal.fire({
        title: 'Chọn phương thức thanh toán',
        html: `
                  <div style="text-align:left;">
                    <div style="margin-bottom:8px;">
                      <input type="radio" id="pay_cash" name="pay_method" value="cash" checked>
                      <label for="pay_cash">Tiền mặt</label>
                    </div>
                    <div>
                      <input type="radio" id="pay_transfer" name="pay_method" value="bank">
                      <label for="pay_transfer">Chuyển khoản</label>
                    </div>
                  </div>
                `,
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Huỷ',
        preConfirm: () => {
            const checked = Swal.getPopup().querySelector('input[name="pay_method"]:checked');
            if (!checked) {
                Swal.showValidationMessage('Vui lòng chọn phương thức thanh toán');
                return false;
            }
            return checked.value; // Trả về giá trị 'cash' hoặc 'transfer'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const paymentMethod = result.value;
            const form = document.createElement("form");
            form.method = "POST";
            form.action = `/dashboard?action=completed-order`;

            let inputData = {id, discount, paymentMethod}
            for (let key in inputData) {
                const input = document.createElement("input");
                input.type = "hidden";
                input.name = key;
                input.value = inputData[key];
                form.appendChild(input);
            }

            document.body.appendChild(form);
            form.submit();
        }
    });
}