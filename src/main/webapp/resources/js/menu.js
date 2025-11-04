
const handleDelete = (id) => {
    Swal.fire({
        title: "DELETE",
        text: "Bạn có chắc muốn xóa menu này?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Xác nhận!",
        cancelButtonText: "Hủy"
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement("form");
            form.method = "post";
            form.action = `/menu?action=delete`;

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