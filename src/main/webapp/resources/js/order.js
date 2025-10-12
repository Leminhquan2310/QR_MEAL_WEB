const handleDelOrder = (id) => {
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

document.getElementById("status").addEventListener("click", function () {

})