const inlineForm = document.getElementById("inlineCreateCustomer");
const customerPhoneMain = document.getElementById("customerPhone");
const customerPointFeedback = document.getElementById("pointFeedback");
const discountForm = document.getElementById("discount-form");
const spinner = document.getElementById("spinnerPhone");
const btnConfirmPayment = document.getElementById("btnConfirmPayment");
const vnPhoneRegex = /^(?:\+84|0)(?:3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])\d{7}$/;
const redeemContainer = document.getElementById("redeemContainer");
const optionEarn = document.getElementById("optionEarn");
const floorPlan = document.querySelector('.floor-plan');

const toggleFormAddCustomer = (e) => {
    e.preventDefault();
    const isOpen = inlineForm.style.display === "block";

    if (isOpen) {
        resetInlineForm();
        return;
    }

    btnConfirmPayment.classList.add("disabled");
    inlineForm.style.display = "block";
    inlineForm.innerHTML = `
            <h6 class="fw-bold mb-2">🦸 Thêm khách hàng mới</h6>
            <div class="mb-2">
                <label class="form-label">Tên khách hàng</label>
                <input type="text" id="newCustomerName" class="form-control" placeholder="Họ và tên..." required>
            </div>
            <div class="mb-3">
                <label class="form-label">Số điện thoại</label>
                <input type="text" id="newCustomerPhone" class="form-control" placeholder="VD: 0912345678" required>
                <small id="createFeedback" class="text-danger small"></small>
            </div>
            <div class="d-flex justify-content-end gap-2">
                <button type="button" onclick="cancelFormAddCustomer()" class="btn btn-secondary btn-sm">Huỷ</button>
                <button type="button" id="saveCreateCustomer" class="btn btn-primary btn-sm">Lưu</button>
            </div>`;

    // --- Gắn sự kiện cho các input ---
    const nameInput = inlineForm.querySelector("#newCustomerName");
    const phoneInput = inlineForm.querySelector("#newCustomerPhone");
    const feedback = inlineForm.querySelector("#createFeedback");
    const saveBtn = inlineForm.querySelector("#saveCreateCustomer");

    // Kiểm tra số điện thoại khi nhập
    phoneInput.addEventListener("input", debounce(async e => {
        const phone = e.target.value.trim();
        feedback.textContent = "";

        if (!vnPhoneRegex.test(phone)) {
            feedback.textContent = "❌ Số điện thoại không hợp lệ (VD: 0912345678)";
            phoneInput.classList.add("is-invalid");
            return;
        }

        try {
            const res = await fetch(`customer?action=check-phone&phone=${encodeURIComponent(phone)}`);
            const data = await res.json();

            if (data.exists) {
                feedback.textContent = "⚠️ Số điện thoại đã tồn tại!";
                phoneInput.classList.add("is-invalid");
            } else {
                feedback.textContent = "";
                phoneInput.classList.remove("is-invalid");
            }
        } catch {
            console.error("Lỗi khi kiểm tra SĐT");
        }
    }, 500));

    // Lưu khách hàng
    saveBtn.addEventListener("click", async () => {
        const name = nameInput.value.trim();
        const phone = phoneInput.value.trim();

        if (!name || !phone) {
            feedback.textContent = "⚠️ Vui lòng nhập đầy đủ thông tin!";
            return;
        }

        if (!vnPhoneRegex.test(phone)) {
            feedback.textContent = "❌ Số điện thoại không hợp lệ!";
            return;
        }

        try {
            const res = await fetch("dashboard?action=create-customer", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({name, phone})
            });

            const data = await res.json();
            if (data.success) {
                Swal.fire("🎉 Thành công!", "Thêm khách hàng thành công!", "success");
                customerPhoneMain.value = phone;
                resetInlineForm();
            } else {
                feedback.textContent = data.message || "⚠️ Số điện thoại đã tồn tại!";
            }
        } catch {
            Swal.fire("Lỗi!", "Không thể kết nối tới server!", "error");
        }
    });
}

const checkPhoneDiscount = debounce(async phone => {
    if (!phone) {
        resetPhoneFeedback();
        return;
    }

    if (!vnPhoneRegex.test(phone)) {
        showInvalidPhone();
        return;
    }

    try {
        const res = await fetch(`customer?action=check-phone&phone=${encodeURIComponent(phone)}`);
        const data = await res.json();

        spinner.style.display = "none";
        if (data.exists) {
            discountForm.style.display = "block";
            customerPhoneMain.classList.remove("is-invalid");
            customerPointFeedback.textContent = "";
            document.getElementById("optionEarn").checked = true;
            document.getElementById("optionEarn").dispatchEvent(new Event("change"));
        } else {
            showNotExistPhone();
        }
    } catch {
        console.error("Lỗi khi kiểm tra SĐT");
    }
}, 700);

const onInputCustomerPhone = debounce(e => {
    spinner.style.display = "block";
    checkPhoneDiscount(e.target.value.trim());
}, 300)

const onChangeEarnOption = () => {
    redeemContainer.style.display = "none";
    const totalAmount = parseInt(document.getElementById("pm-total").dataset.totalAmount);
    const totalFinal = document.getElementById("pm-final");
    document.getElementById("pm-discount").innerText = "0₫";
    totalFinal.innerText = totalAmount.toLocaleString('vi-VN') + "₫";
    totalFinal.dataset.totalFinal = totalAmount;
    resetQrSection();
}

const onChangeRedeemOption = async () => {
    try {
        const res = await fetch(`/dashboard?action=get-discounts-by-phone&phone=${encodeURIComponent(customerPhoneMain.value)}`);
        const data = await res.json();
        if (data.length > 0) {
            redeemContainer.innerHTML = `
                    <label for="redeemSelect" class="form-label">Chọn mức giảm giá</label>
                    <select id="redeemSelect" onchange="changeSelectDiscount()" name="redeemSelect" class="form-select">
                        ${data.map(item => `<option value="${item.id}" data-value="${item.discount_value}" data-type="${item.discount_type}">
                                                                (${item.points_required} điểm) - ${item.description}
                                                              </option>`).join("")}
                    </select>`;
            changeSelectDiscount();
        } else {
            optionEarn.checked = true;
            redeemContainer.innerHTML = `<div id="discountFeedback" class="form-text text-muted mt-1">Chưa đủ điểm để giảm giá!</div>`;
        }

        redeemContainer.style.display = "block";
    } catch (err) {
        console.error("Lỗi khi lấy giảm giá:", err);
    }
}

const resetInlineForm = () => {
    inlineForm.style.display = "none";
    inlineForm.innerHTML = "";
    btnConfirmPayment.classList.remove("disabled");
    document.getElementById('qrSection').style.display = "none";
}

const resetPhoneFeedback = () => {
    spinner.style.display = "none";
    customerPhoneMain.classList.remove("is-invalid");
    customerPointFeedback.textContent = "";
    discountForm.style.display = "none";
}

const showInvalidPhone = () => {
    customerPointFeedback.textContent = "❌ Số điện thoại không hợp lệ (VD: 0912345678)";
    customerPhoneMain.classList.add("is-invalid");
    spinner.style.display = "none";
    discountForm.style.display = "none";
}

const showNotExistPhone = () => {
    customerPointFeedback.textContent = "⚠️ Số điện thoại không tồn tại!";
    customerPhoneMain.classList.add("is-invalid");
    discountForm.style.display = "none";
}

const updateStatusOrder = (id, title, text, status) => {
    Swal.fire({
        title, text, icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy"
    }).then(result => {
        if (result.isConfirmed) {
            const form = Object.entries({id, status}).reduce((f, [k, v]) => {
                const i = document.createElement("input");
                Object.assign(i, {type: "hidden", name: k, value: v});
                f.appendChild(i);
                return f;
            }, Object.assign(document.createElement("form"), {
                method: "POST",
                action: "/dashboard?action=update-status"
            }));
            document.body.appendChild(form);
            form.submit();
        }
    });
}

const openConfirmPaymentModal = async (id) => {
    const modalEl = document.getElementById("confirmPaymentModal");
    const totalAmountSpan = document.getElementById("pm-total");
    document.getElementById("orderId").value = id;
    document.getElementById("confirmPaymentForm").reset();
    document.getElementById("pointFeedback").textContent = "";
    // set data hóa đơn
    try {
        const res = await fetch(`api/order-detail?action=by-order-id&id=${id}`);
        const data = await res.json();
        document.getElementById("pm-order-id").textContent = "#" + id;
        document.getElementById("pm-order-table").textContent = "#" + data.order.table_id;
        document.getElementById("pm-created-at").textContent = data.order.created_at;
        let oDetailHtml = "";
        let totalAmount = 0;
        data.details.forEach((item, i) => {
            totalAmount += item.quantity * item.price
            oDetailHtml += `<tr>
                                        <td>${item.product.name}</td>
                                        <td class="text-center">${item.quantity}</td>
                                        <td class="text-end">${item.price.toLocaleString('vi-VN')}đ</td>
                                        <td class="text-end">${(item.quantity * item.price).toLocaleString('vi-VN')}đ</td>
                                    </tr>`
        });
        document.getElementById("dataOrderDetail").innerHTML = oDetailHtml;
        totalAmountSpan.innerText = totalAmount.toLocaleString('vi-VN') + "đ";
        totalAmountSpan.dataset.totalAmount = totalAmount;
        document.getElementById("pm-final").innerText = totalAmount.toLocaleString('vi-VN') + "đ";
    } catch (err) {
        console.error(err);
        // spinner.style.display = "none";
        // modalBody.innerHTML = `<tr><td colspan="6" class="text-danger text-center">Lỗi tải dữ liệu!</td></tr>`;
    }
    new bootstrap.Modal(modalEl).show();
}

const onChangePaymentMethodRadio = (e) => {
    const qrSection = document.getElementById('qrSection');
    if (e.target.value === 'bank') {
        qrSection.style.display = 'block';
    } else {
        qrSection.style.display = 'none';
    }
}

// Sự kiện click nút tạo QR
const createQRCodeClick = () => {
    const totalFinal = document.getElementById("pm-final").dataset.totalFinal;
    const id = document.getElementById("orderId").value;
    refreshQrCodePayment(id, totalFinal);
}

// fetch lấy thông tin "QR code" thanh toán
const refreshQrCodePayment = (orderId, amount) => {
    const qrImage = document.getElementById('qrCodeImage');
    const qrLoading = document.getElementById('qrLoading');
    const btnGenerate = document.getElementById('btnGenerateQR');

    // Ẩn nút, hiện loading
    btnGenerate.classList.add('d-none');
    qrLoading.classList.remove('d-none');

    let qrUrl = '';
    fetch(`/order?action=get-payment-info`)
        .then(res => res.json())
        .then(data => {
            let bank = data.bankAccount;
            qrUrl = `https://img.vietqr.io/image/${bank.bank_code}-${bank.account_number}-qr_only.png?amount=${amount}&addInfo=ORDER ${orderId}&accountName=${bank.account_name}`
            qrImage.src = qrUrl;
        }).catch(res => {
        console.log(res)
    })

    // Giả lập load QR sau 1.5 giây
    setTimeout(() => {
        qrLoading.classList.add('d-none');
        qrImage.classList.remove('d-none');
    }, 1000);
}

const cancelFormAddCustomer = () => {
    document.getElementById("inlineCreateCustomer").style.display = "none";
    document.getElementById("inlineCreateCustomer").innerHTML = "";
    document.getElementById("btnConfirmPayment").classList.remove("disabled");
}

const resetQrSection = () => {
    const qrSection = document.getElementById('qr_content');
    qrSection.innerHTML = `<div id="qrLoading" class="d-flex align-items-center justify-content-center h-100 d-none">
                                                    <div class="spinner-border text-primary" role="status">
                                                        <span class="visually-hidden">Loading...</span>
                                                    </div>
                                              </div>
                                              <img id="qrCodeImage" src="" alt="QR Code" class="img-fluid d-none"
                                                     style="width: 150px;">
                                               <span id="btnGenerateQR" class="text-primary small fw-semibold" role="button"
                                                      style="cursor: pointer;" onclick="createQRCodeClick()">
                                                    <i class="fa fa-qrcode me-1"></i> Tạo QR Code
                                                </span>`
}

const changeSelectDiscount = () => {
    const totalAmount = parseInt(document.getElementById("pm-total").dataset.totalAmount);
    const totalFinal = document.getElementById("pm-final");
    const selectedOption = redeemSelect.selectedOptions[0];
    const discountType = selectedOption.dataset.type;
    let discountValue = parseInt(selectedOption.dataset.value);
    let finalAmount = Math.max(totalAmount - discountValue, 0);
    if (discountType === "PERCENT") {
        finalAmount = totalAmount - totalAmount / 100 * discountValue;
        discountValue = totalAmount - finalAmount
    }

    // Cập nhật hiển thị
    document.getElementById("pm-discount").innerText = discountValue.toLocaleString('vi-VN') + "₫";
    totalFinal.innerText = finalAmount.toLocaleString('vi-VN') + "₫";
    totalFinal.dataset.totalFinal = finalAmount;
    resetQrSection();
}

// in hóa đơn
const printBill = () => {
    const printContent = document.getElementById("billPreview").innerHTML;
    // Mở cửa sổ in
    const printWindow = window.open('', '', 'width=600,height=800');
    printWindow.document.write(`<html lang="en">
                                                            <head>
                                                            <title>Document</title>
                                                            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                                                            <style>
                                                            @media print {
                                                                body {
                                                                    width: 58mm;
                                                                    font-size: 10px;
                                                                    line-height: 1.4;
                                                                }
                                                                .no-print {
                                                                    display: none !important;
                                                                }
                                                                img {
                                                                    max-width: 100%;
                                                                    display: block;
                                                                    margin: 0 auto;
                                                                }
                                                            }
                                                            </style>
                                                            </head>
                                                                <body>
                                                                       <div class="col-md-6 border rounded p-3 bg-light" id="billPreview" style="min-height: 320px;">
                                                                            ${printContent}
                                                                       </div>
                                                                </body>
                                                       </html>`);
    printWindow.document.close();

    // Đảm bảo nội dung render xong
    setTimeout(() => {
        const imgs = printWindow.document.images;
        if (imgs.length === 0) {
            printWindow.print();
            return;
        }

        let loaded = 0;
        for (const img of imgs) {
            img.onload = img.onerror = () => {
                loaded++;
                if (loaded === imgs.length) {
                    printWindow.focus();
                    printWindow.print();
                }
            };
        }
    }, 300);
}

document.addEventListener("DOMContentLoaded", () => {
    const paymentModal = document.getElementById("confirmPaymentModal");
    const tableModal = document.getElementById("tableModal");

    const lottieAnim = lottie.loadAnimation({
        container: document.getElementById("lottieLoading"),
        renderer: "svg",
        loop: true,
        autoplay: true,
        path: "resources/animations/loading.json"
    });

    const params = new URLSearchParams(window.location.search);
    const highlightId = params.get("highlight");
    if (highlightId) {
        highlightTable(highlightId); // Gọi hàm hiệu ứng bạn đã viết
    }

    paymentModal.addEventListener("hidden.bs.modal", () => {
        resetInlineForm();
        discountForm.style.display = "none";
        redeemContainer.style.display = "none";
    });

    tableModal.addEventListener("show.bs.modal", async event => {
        const btn = event.relatedTarget;
        const {id: tableId, name, status} = btn.dataset;

        const header = tableModal.querySelector(".modal-header");
        const modalBody = document.getElementById("orderDetailsBody");
        const spinner = document.getElementById("loadingSpinner");
        const container = document.getElementById("orderDetailsContainer");
        const totalSpan = document.getElementById("orderTotal");
        const modalFooter = document.getElementById("modalFooter");

        header.classList.add(`bg-${status === "1" ? "success" : "warning"}`);
        modalBody.innerHTML = "";
        totalSpan.textContent = "0";
        spinner.style.display = "block";
        container.style.display = "none";

        try {
            const res = await fetch(`api/order-detail?action=by-table-id&id=${tableId}`);
            const data = await res.json();

            spinner.style.display = "none";
            container.style.display = "block";

            const oStatus = data.order?.status;
            header.querySelector("#tableModalLabel").innerHTML = `${name}: ${oStatus?.label ?? "Trống"}`;

            if (!data.details.length) {
                modalBody.innerHTML = `<tr><td colspan="6" class="text-center text-muted">Bàn chưa có món nào</td></tr>`;
                return;
            }

            let total = 0;
            data.details.forEach((item, i) => {
                const p = item.product || {};
                const itemTotal = item.quantity * item.price;
                total += itemTotal;

                modalBody.innerHTML += `
                    <tr>
                        <td>${i + 1}</td>
                        <td><img src="${p.image || 'images/no-image.png'}" width="50" class="me-2 rounded">${p.name || 'N/A'}</td>
                        <td>${item.quantity}</td>
                        <td>${item.price.toLocaleString('vi-VN')} ₫</td>
                        <td>${itemTotal.toLocaleString('vi-VN')} ₫</td>
                    </tr>`;
            });

            totalSpan.textContent = total.toLocaleString("vi-VN");

            const nextLabel = oStatus.code == "0" ? "Xác nhận"
                : oStatus.code == "1" ? "Hoàn tất chế biến" : "";

            modalFooter.innerHTML = `
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                ${
                oStatus.code == "2"
                    ? `<button class="btn btn-${status === "1" ? "success" : "warning"}"
                            onclick="openConfirmPaymentModal('${data.order.id}')">
                            Thanh toán
                       </button>`
                    : `<button class="btn btn-${status === "1" ? "success" : "warning"}"
                            onclick="updateStatusOrder('${data.order.id}', '${nextLabel}', 'Xác nhận thay đổi?', ${oStatus.code + 1})">
                            ${nextLabel}
                       </button>`
            }`;
        } catch (err) {
            console.error(err);
            spinner.style.display = "none";
            modalBody.innerHTML = `<tr><td colspan="6" class="text-danger text-center">Lỗi tải dữ liệu!</td></tr>`;
        }
    });

    tableModal.addEventListener("hidden.bs.modal", () => {
        document.getElementById("orderDetailsBody").innerHTML = "";
        document.getElementById("modalFooter").innerHTML =
            `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>`;
        tableModal.querySelector(".modal-header").classList.remove("bg-success", "bg-warning");
    });

});

// Hàm render lại sơ đồ bàn
const renderTables = (tables, table_highlight) => {
    floorPlan.innerHTML = ''; // Xóa sơ đồ cũ

    Object.values(tables).forEach(table => {
        const div = document.createElement('div');
        div.className = `
        table-item position-absolute fw-bold d-flex align-items-center
        justify-content-center text-white flex-column bg-${table.status.badge}
      `;
        div.style = `
        left:${table.pos_x * 10}px;
        top:${table.pos_y * 10}px;
        width:${table.width * 10}px;
        height:${table.height * 10}px;
        line-height:${table.height}px;
        border-radius:10px;
        cursor:pointer;
        border:2px solid #fff;
        transition: all 0.4s ease;
      `;
        div.title = `Bàn: ${table.name}\nTrạng thái: ${table.status.label}`;
        div.dataset.id = table.id;
        div.dataset.name = table.name;
        div.dataset.status = table.status.code;
        div.dataset.bsToggle = "modal";
        div.dataset.bsTarget = "#tableModal";

        div.innerHTML = `
        <h4>${table.name}</h4>
        <p>${table.status.label}</p>
      `;

        floorPlan.appendChild(div);
    });

    highlightTable(table_highlight);
}

// ✅ Hàm highlight bàn vừa thay đổi
const highlightTable = (tableId) => {
    const el = document.querySelector(`[data-id="${tableId}"]`);
    if (!el) return;

    el.classList.add('table-updated');
    setTimeout(() => {
        el.classList.remove('table-updated')
        // xóa param khỏi url
        const newUrl = window.location.origin + window.location.pathname;
        window.history.replaceState({}, document.title, newUrl);
    }, 4000); // 4 giây


};