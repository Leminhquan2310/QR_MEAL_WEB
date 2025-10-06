// Hàm mở modal QR Code
function openQrModal(tableId, tableName, qrBase64) {
    document.getElementById("qrCodeImage").src = qrBase64;
    document.getElementById("tableNameUpdate").value = tableName;

    // Lưu tableId để tiện thao tác
    document.getElementById("btnSaveTable").setAttribute("data-id", tableId);
    document.getElementById("btnRefreshQR").setAttribute("data-id", tableId);
    document.getElementById("btnPrintQR").setAttribute("data-id", tableId);
    document.getElementById("btnDownloadQR").setAttribute("data-id", tableId);

    let qrModal = new bootstrap.Modal(document.getElementById('qrCodeModal'));
    qrModal.show();
}

document.getElementById("btnSaveTable").addEventListener("click", function () {
    let id = this.getAttribute("data-id");
    let form = document.createElement("form");
    form.action = `/table?action=update`;
    form.method = "post";

    let input_id = document.createElement("input");
    input_id.type = "hidden";
    input_id.name = "id";
    input_id.value = id;

    let qr_code = document.getElementById("qrCodeImage").src
    let input_qrcode = document.createElement("input");
    input_qrcode.type = "hidden";
    input_qrcode.name = "qr_code";
    input_qrcode.value = qr_code;

    let name = document.getElementById("tableNameUpdate").value
    let input_name = document.createElement("input");
    input_name.type = "hidden";
    input_name.name = "name";
    input_name.value = name;

    form.append(input_id);
    form.append(input_qrcode);
    form.append(input_name)
    document.body.appendChild(form);
    form.submit();
})

// Nút Làm mới QR
document.getElementById("btnRefreshQR").addEventListener("click", function () {
    let id = this.getAttribute("data-id");
    let qrImg = document.getElementById("qrCodeImage");
    let spinner = document.getElementById("qrLoading");

    // Hiện spinner, ẩn ảnh
    spinner.classList.remove("d-none");
    qrImg.classList.add("d-none");
    fetch("/table?action=refreshQR&id=" + id)
        .then(res => res.json())
        .then(data => {
            qrImg.onload = function () {
                // Khi ảnh đã load xong => ẩn spinner
                setTimeout(() => {
                    spinner.classList.add("d-none");
                    qrImg.classList.remove("d-none");
                    qrImg.classList.add("fade-in");
                }, 1000)
            };
            qrImg.src = data.qrCode;
        })
        .catch(err => console.error(err.message));
});

document.getElementById("btnDownloadQR").addEventListener("click", function () {
    let id = this.getAttribute("data-id");
    const qrImg = document.getElementById("qrCodeImage");
    const link = document.createElement("a");
    link.href = qrImg.src;
    link.download = `qrcode_table_${id}.png`; // tên file tải về
    link.click();
});

document.getElementById("btnPrintQR").addEventListener("click", function () {
    let id = document.getElementById("btnSaveTable").getAttribute("data-id");
    const content = document.getElementById("qr_content").lastElementChild.outerHTML;
    console.log(content);
    const printWindow = window.open('', '', 'width=600,height=600');
    printWindow.document.write(`
        <html>
            <head>
                <title>In QR</title>
                <style>
                    body { text-align: center; font-family: Arial; margin: 30px; }
                    img { max-width: 100%; height: auto; }
                </style>
            </head>
            <body>
                 <h3>Bàn ${id}</h3>   
                 ${content}
            </body>
        </html>
    `);
    printWindow.document.close();
    printWindow.print();
})

const showDeleteAlert = (id) => {
    Swal.fire({
        title: "Chắc chắn muốn xóa?",
        text: "Xóa sẽ thay đổi trạng thái hoặc xóa hẳn khi không thuộc đơn hàng nào?",
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
            form.action = `/table?action=delete`;

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