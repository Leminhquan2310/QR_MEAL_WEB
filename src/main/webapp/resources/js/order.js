const serveModal = new bootstrap.Modal(document.getElementById('serveModal'));
const paymentModal = new bootstrap.Modal(document.getElementById('paymentModal'));

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

// in phiếu chế biến
async function fetchCookingTicket(orderId) {
    try {
        const response = await fetch(`/order?action=getCookingTicket&id=${orderId}`);
        if (!response.ok) throw new Error("Không thể tải dữ liệu phiếu chế biến!");
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Lỗi khi tải dữ liệu phiếu chế biến!");
        return null;
    }
}

document.getElementById('btnPrint').addEventListener('click', async (event) => {
    const orderId = event.currentTarget.dataset.id;
    const data = await fetchCookingTicket(orderId);
    if (!data) return;

    const {order, items} = data;

    // Dựng danh sách món ăn cho phiếu bếp
    let itemsHtml = '';
    items.forEach((item, idx) => {
        itemsHtml += `
            <tr>
                <td style="text-align:center;">${idx + 1}</td>
                <td>${item.product.name}</td>
                <td style="text-align:center; font-weight:bold;">${item.quantity}</td>
            </tr>`;
    });

    // Mở cửa sổ in
    const printWindow = window.open('', '', 'width=600,height=800');
    printWindow.document.write(`
        <html>
          <head>
            <title>Phiếu chế biến</title>
            <style>
              * { font-family: 'Courier New', monospace; }
              body { margin: 10px 15px; font-size: 14px; }
              h2 {
                text-align: center;
                text-transform: uppercase;
                margin: 4px 0;
                font-size: 18px;
                letter-spacing: 1px;
              }
              .header, .footer {
                text-align: center;
                font-size: 13px;
              }
              .order-info {
                border: 1px dashed #999;
                padding: 6px;
                margin: 8px 0;
                font-size: 13px;
                line-height: 1.5;
              }
              table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 10px;
              }
              th, td {
                border-bottom: 1px dashed #ccc;
                padding: 6px 4px;
              }
              th {
                background: #f3f3f3;
                text-align: center;
                text-transform: uppercase;
                font-size: 13px;
              }
              td {
                vertical-align: top;
              }
              tr:last-child td {
                border-bottom: 2px solid #000;
              }
              .highlight {
                background: #fff8dc;
              }
              @media print {
                @page { size: 80mm auto; margin: 5mm; }
                body { margin: 0; }
              }
            </style>
          </head>
          <body>
            <h2>PHIẾU CHẾ BIẾN</h2>
            <div class="header">--- Nhà hàng ABC ---</div>

            <div class="order-info">
              <div><b>Mã đơn:</b> #${order.id}</div>
              <div><b>Bàn:</b> ${order.table_id}</div>
              <div><b>Giờ tạo:</b> ${order.created_at}</div>
            </div>

            <table>
              <thead>
                <tr>
                  <th style="width:10%">#</th>
                  <th style="text-align:left;">Tên món</th>
                  <th style="width:20%">SL</th>
                </tr>
              </thead>
              <tbody>
                ${itemsHtml}
              </tbody>
            </table>

            <div class="footer">
              <hr>
              <div>In lúc: ${new Date().toLocaleTimeString('vi-VN')}</div>
              <div>Người in: ${order.staff_name || '---'}</div>
            </div>
          </body>
        </html>
    `);

    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
});

// Đóng đơn (chuyển sang trạng thái thanh toán)
function closeOrder(id) {
    updateStatusOrder(id, "Hoàn thành chế biến?", "Chuyển sang trạng thái phục vụ - chờ thanh toán!", 2);
}

function confirmPayment(id, totalAmount, discount) {
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
            form.action = `/order?action=completed-order`;

            let inputData = {id, totalAmount, discount, paymentMethod}
            for (let key in inputData) {
                const input = document.createElement("input");
                input.type = "hidden";
                input.name = key;
                input.value = inputData[key];
                form.appendChild(input);
            }

            //
            // const inputAmount = document.createElement("input");
            // inputAmount.type = "hidden";
            // inputAmount.name = "totalAmount";
            // inputAmount.value = totalAmount;
            // form.appendChild(inputAmount);
            //
            // const inputDiscount = document.createElement("input");
            // inputDiscount.type = "hidden";
            // inputDiscount.name = "discount";
            // inputDiscount.value = discount;
            // form.appendChild(inputDiscount);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

// Mở modal và load thông tin đơn
function openPaymentModal(orderId) {
    paymentModal.show();
    getInfoQRPayment(orderId);
}

// fetch lấy thông tin "QR code" thanh toán
function getInfoQRPayment(orderId) {
    let imgElement = document.getElementById('qrSection').querySelector("img");
    let priceText = document.getElementById('pm-total').innerHTML;
    let totalAmount = parseInt(priceText.replace(/[^\d]/g, ''), 10);
    let order_code = document.getElementById('pm-order-id').innerHTML;
    if (!imgElement.getAttribute('src')) {
        fetch(`/order?action=get-payment-info`)
            .then(res => res.json())
            .then(data => {
                let bank = data.bankAccount;
                let src = `https://img.vietqr.io/image/${bank.bank_code}-${bank.account_number}-qr_only.png?amount=${totalAmount}&addInfo=ORDER ${orderId}&accountName=${bank.account_name}`
                imgElement.src = src;
            }).catch(res => {
            console.log(res)
        })
    }
}

//In hóa đơn
document.getElementById('btnPrintPayment').addEventListener('click', async (e) => {
    const printContent = document.getElementById("pm-bill").innerHTML;
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
                                                                    font-size: 12px;
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
                                                                       ${printContent}
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
});