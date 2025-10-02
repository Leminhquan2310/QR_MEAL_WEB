const handleDelCategory = (id) => {
    Swal.fire({
        title: "Are you sure?",
        text: "Deleting removes the use of this field!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, do it!"
    }).then((result) => {
        if (result.isConfirmed) {
            const form = document.createElement("form");
            form.method = "post";
            form.action = `/category?action=delete`;

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