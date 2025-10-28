document.addEventListener('DOMContentLoaded', () => {
    const customerModalAdd = document.getElementById('createCustomerModal');
    const customerModalUpdate = document.getElementById('updateCustomerModal');

    // khởi tạo loading animation
    const lottieAnim = lottie.loadAnimation({
        container: document.getElementById('lottieLoading'), // DOM element
        renderer: 'svg',
        loop: true,
        autoplay: true,
        path: 'resources/animations/loading.json' // đường dẫn tới file JSON loading
    });

    customerModalAdd.addEventListener('hidden.bs.modal', () => {
        document.getElementById('nameInput').value = '';
        document.getElementById("phoneInput").value = '';
        document.getElementById('phoneFeedback').textContent = '';
        document.getElementById('phoneInput').classList.remove('is-invalid');
    })

    customerModalUpdate.addEventListener('hidden.bs.modal', () => {
        document.getElementById('phoneFeedbackUpdate').textContent = '';
        document.getElementById('updatePhone').classList.remove('is-invalid');
        document.getElementById("updateCustomerBtn").classList.remove("disabled")

    })
});

document.getElementById('phoneInput').addEventListener('input', function () {
    const phone = this.value.trim();
    const feedback = document.getElementById('phoneFeedback');

    // Regex cho số điện thoại Việt Nam (bắt đầu bằng 0 hoặc +84, theo sau là 9-10 chữ số)
    const vnPhoneRegex = /^(?:\+84|0)(?:3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])\d{7}$/;

    // Kiểm tra định dạng
    if (!vnPhoneRegex.test(phone)) {
        feedback.textContent = '❌ Số điện thoại không hợp lệ (VD: 0912345678 hoặc +84912345678)';
        phoneInput.classList.add('is-invalid');
        document.getElementById("addCustomerBtn").classList.add("disabled")
        return;
    }

    fetch(`customer?action=check-phone&phone=${encodeURIComponent(phone)}`)
        .then(response => response.json())
        .then(data => {
            if (data.exists) {
                feedback.textContent = '⚠️ Số điện thoại đã tồn tại!';
                this.classList.add('is-invalid');
                document.getElementById("addCustomerBtn").classList.add("disabled")
            } else {
                feedback.textContent = '';
                this.classList.remove('is-invalid');
                document.getElementById("addCustomerBtn").classList.remove("disabled")
            }
        })
        .catch(error => {
            console.error('Lỗi khi kiểm tra số điện thoại:', error);
        });
});

let phoneUpdate = "";
document.getElementById('updatePhone').addEventListener('input', function () {
    const phone = this.value.trim();
    const feedback = document.getElementById('phoneFeedbackUpdate');

    // Regex cho số điện thoại Việt Nam (bắt đầu bằng 0 hoặc +84, theo sau là 9-10 chữ số)
    const vnPhoneRegex = /^(?:\+84|0)(?:3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])\d{7}$/;

    // Kiểm tra định dạng
    if (!vnPhoneRegex.test(phone)) {
        feedback.textContent = '❌ Số điện thoại không hợp lệ (VD: 0912345678 hoặc +84912345678)';
        updatePhone.classList.add('is-invalid');
        document.getElementById("updateCustomerBtn").classList.add("disabled")
        return;
    }

    fetch(`customer?action=check-phone&phone=${encodeURIComponent(phone)}`)
        .then(response => response.json())
        .then(data => {
            if (data.exists && phoneUpdate !== phone) {
                feedback.textContent = '⚠️ Số điện thoại đã tồn tại!';
                this.classList.add('is-invalid');
                document.getElementById("updateCustomerBtn").classList.add("disabled")
            } else {
                feedback.textContent = '';
                this.classList.remove('is-invalid');
                document.getElementById("updateCustomerBtn").classList.remove("disabled")
            }
        })
        .catch(error => {
            console.error('Lỗi khi kiểm tra số điện thoại:', error);
        });
});

function showModalUpdateCustomer(id) {
    const modalElement = document.getElementById('updateCustomerModal');
    const modal = new bootstrap.Modal(modalElement);

    const spinner = document.getElementById('loadingSpinner');
    const containerForm = document.getElementById('updateCustomerForm');
    spinner.style.display = 'block';
    containerForm.style.display = 'none';
    // Mở modal
    modal.show();

    fetch(`customer?action=get-customer&id=${id}`)
        .then(response => response.json())
        .then(data => {
            spinner.style.display = 'none';
            containerForm.style.display = 'block';
            // Gán dữ liệu vào form modal
            document.getElementById('updateId').value = data.id;
            document.getElementById('updateName').value = data.name;
            document.getElementById('updatePhone').value = data.phone;
            phoneUpdate = data.phone;
        })
        .catch(error => {
            spinner.style.display = 'none';
            Swal.fire({
                title: "Error",
                text: 'Lỗi khi lấy dữ liệu khách hàng: ' + error,
                icon: "warning"
            });
        });
}