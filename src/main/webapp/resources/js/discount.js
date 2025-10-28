document.addEventListener("DOMContentLoaded", function() {
    const deleteButtons = document.querySelectorAll(".btn-delete");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function() {
            const form = this.closest("form");

            Swal.fire({
                title: 'Xóa giảm giá?',
                text: "Xóa sẽ dừng sử dụng loại giảm giá này!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            });
        });
    });
});

function resetModal() {
    document.getElementById('modalTitle').textContent = 'Thêm giảm giá';
    document.getElementById('formAction').value = 'create';
    document.getElementById('discountId').value = '';
    document.getElementById('discountPoints').value = '';
    document.getElementById('discountDescription').value = '';
}

function editDiscount(id, points_required, description, discount_value, discount_type, staus) {
    const modal = new bootstrap.Modal(document.getElementById('discountModal'));
    document.getElementById('modalTitle').textContent = 'Cập nhật giảm giá';
    document.getElementById('formAction').value = 'update';
    document.getElementById('discountId').value = id;
    document.getElementById('discountPoints').value = points_required;
    document.getElementById('discountDescription').value = description;
    document.getElementById('discountType').value = discount_type;
    document.getElementById('discountValue').value = discount_value;
    document.getElementById('discountStatus').value = staus;
    document.getElementById('discountStatus').checked = staus === 1;
    modal.show();
}

document.getElementById('discountType').addEventListener('change', function() {
    const hint = document.getElementById('discountHint');
    if (this.value === 'PERCENT') {
        hint.textContent = "Ví dụ: 10 nghĩa là giảm 10%.";
    } else if (this.value === 'FIXED') {
        hint.textContent = "Ví dụ: 50000 nghĩa là giảm 50.000 VNĐ.";
    } else {
        hint.textContent = "Nhập giá trị theo loại giảm giá đã chọn.";
    }
});

document.getElementById('discountStatus').addEventListener('change', function () {
    const toggle = document.getElementById('discountStatus');
    const label = document.getElementById('statusLabel');

    toggle.addEventListener('change', () => {
        label.style.opacity = 0;
        setTimeout(() => {
            label.textContent = toggle.checked ? 'Hoạt động' : 'Không hoạt động';
            label.style.opacity = 1;
        }, 150);
    });
    this.value = this.checked ? 1 : 0;
});