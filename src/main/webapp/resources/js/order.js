const serveModal = new bootstrap.Modal(document.getElementById('serveModal'));
const inlineForm = document.getElementById("inlineCreateCustomer");
const customerPhoneMain = document.getElementById("customerPhone");
const customerPointFeedback = document.getElementById("pointFeedback");
const discountForm = document.getElementById("discount-form");
const spinner = document.getElementById("spinnerPhone");
const btnConfirmPayment = document.getElementById("btnConfirmPayment");
const vnPhoneRegex = /^(?:\+84|0)(?:3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])\d{7}$/;
const optionEarn = document.getElementById("optionEarn");
const redeemContainer = document.getElementById("redeemContainer");


function handleDelOrder(id) {
    Swal.fire({
        title: "Chắc chắn muốn xóa?",
        text: "Xoó đơn hàng trong cơ sở dữ liệu!",
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
            form.action = `/order?action=delete`;

            // input hidden chứa id
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "id";
            input.value = id;
            form.appendChild(input);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

function openHistoryModal(orderId) {
    const modalEl = document.getElementById('statusHistoryModal');
    const historyTbody = document.getElementById('historyTbody');
    // show loading
    historyTbody.innerHTML = '<tr><td colspan="3" class="text-center">Đang tải...</td></tr>';

    fetch('/order/status-logs?orderId=' + encodeURIComponent(orderId), {
        method: 'GET',
        headers: {'Accept': 'application/json'}
    })
        .then(resp => {
            if (!resp.ok) throw new Error('Lỗi tải lịch sử: ' + resp.status);
            return resp.json();
        })
        .then(data => {
            if (!Array.isArray(data) || data.length === 0) {
                historyTbody.innerHTML = '<tr><td colspan="3" class="text-center">Chưa có cập nhật nào</td></tr>';
                return;
            }
            // build rows
            let html = '';
            data.forEach(item => {
                const ts = item.changedAt ? item.changedAt : item.changed_at; // tùy gson/json field
                html += `<tr>
                                    <td style="font-size:0.85rem; white-space:nowrap">${escapeHtml(formatDate(ts))}</td>
                                    <td style="font-size:0.85rem">
                                        <span class="badge bg-${item.old_status.badge}">${item.old_status.label}</span>
                                            <i class="fa-solid fa-arrow-right"></i>
                                        <span class="badge bg-${item.new_status.badge}">${item.new_status.label}</span>
                                    </td>
                                    <td style="font-size:0.85rem">${escapeHtml(item.changed_by.name)}</td>
                                 </tr>`;
            });
            historyTbody.innerHTML = html;
        })
        .catch(err => {
            historyTbody.innerHTML = '<tr><td colspan="3" class="text-danger">Lỗi tải lịch sử</td></tr>';
            console.error(err);
        });

    // show modal (Bootstrap 5)
    const bsModal = new bootstrap.Modal(modalEl);
    bsModal.show();
}

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
            form.action = `/order?action=update-status`;

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

function formatDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    // format: yyyy-mm-dd hh:mm
    const pad = n => n.toString().padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

function escapeHtml(unsafe) {
    if (unsafe == null) return '';
    return unsafe.toString().replace(/[&<>"'`=\/]/g, function (s) {
        return ({
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#39;',
            '/': '&#x2F;',
            '`': '&#x60;',
            '=': '&#x3D;'
        })[s];
    });
}

function handleCancelledOrder(id) {
    Swal.fire({
        title: "Chắc chắn muốn hủy đơn?",
        text: "Hãy đảm bảo hủy sẽ không ảnh hưởng hoạt động thực tế!",
        icon: "warning",
        input: "text",
        inputAttributes: {
            autocapitalize: "off"
        },
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy",
        inputPlaceholder: "Nhập lý do xóa...",
        showLoaderOnConfirm: true,
    }).then((result) => {
        if (result.isConfirmed) {
            const note = result.value;
            const form = document.createElement("form");
            form.method = "POST";
            form.action = `/order?action=update-status`;

            // input hidden chứa id
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "id";
            input.value = id;
            form.appendChild(input);

            const inputStatus = document.createElement("input");
            inputStatus.type = "hidden";
            inputStatus.name = "status";
            inputStatus.value = 4;
            form.appendChild(inputStatus);

            const inputNote = document.createElement("input");
            inputNote.type = "hidden";
            inputNote.name = "note";
            inputNote.value = note;
            form.appendChild(inputNote);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

function openServeModal() {
    serveModal.show();
}

// Đóng đơn (chuyển sang trạng thái thanh toán)
function closeOrder(id) {
    updateStatusOrder(id, "Hoàn thành chế biến?", "Chuyển sang trạng thái phục vụ - chờ thanh toán!", 2);
}

async function openConfirmPaymentModal(id) {
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

const resetInlineForm = () => {
    inlineForm.style.display = "none";
    inlineForm.innerHTML = "";
    btnConfirmPayment.classList.remove("disabled");
    document.getElementById('qrSection').style.display = "none";
}


const cancelFormAddCustomer = () => {
    document.getElementById("inlineCreateCustomer").style.display = "none";
    document.getElementById("inlineCreateCustomer").innerHTML = "";
    btnConfirmPayment.classList.remove("disabled");
}

const showInvalidPhone = () => {
    customerPointFeedback.textContent = "❌ Số điện thoại không hợp lệ (VD: 0912345678)";
    customerPhoneMain.classList.add("is-invalid");
    spinner.style.display = "none";
    discountForm.style.display = "none";
}

const onInputCustomerPhone = debounce(e => {
    spinner.style.display = "block";
    checkPhoneDiscount(e.target.value.trim());
}, 300);

// --- Kiểm tra số điện thoại để hiển thị form tích điểm / giảm giá ---
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

const resetPhoneFeedback = () => {
    spinner.style.display = "none";
    customerPhoneMain.classList.remove("is-invalid");
    customerPointFeedback.textContent = "";
    discountForm.style.display = "none";
}

const showNotExistPhone = () => {
    customerPointFeedback.textContent = "⚠️ Số điện thoại không tồn tại!";
    customerPhoneMain.classList.add("is-invalid");
    discountForm.style.display = "none";
}

const onChangePaymentMethodRadio = (e) => {
    const qrSection = document.getElementById('qrSection');
    if (e.target.value === 'bank') {
        qrSection.style.display = 'block';
    } else {
        qrSection.style.display = 'none';
    }
}

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