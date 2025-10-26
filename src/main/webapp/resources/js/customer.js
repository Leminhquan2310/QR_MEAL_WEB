document.addEventListener('DOMContentLoaded', () => {
    const customerModal = document.getElementById('createCustomerModal');


    customerModal.addEventListener('hidden.bs.modal', () => {
        document.getElementById('nameInput').value = '';
        document.getElementById("phoneInput").value = '';
        document.getElementById('phoneFeedback').textContent = '';
        document.getElementById('phoneInput').classList.remove('is-invalid');
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

