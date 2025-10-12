<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Form thêm loại sản phẩm -->
<div class="card shadow-sm mb-4">
    <div class="card-body">
        <form class="row g-3 " action="/category?action=update" method="post">
            <input type="hidden" name="id" value="${category.id}">
            <div class="mb-3 col-lg-6">
                <label class="form-label">Tên loại</label>
                <input type="text" name="name" value="${category.name}" class="form-control" required>
            </div>
            <!-- Icon chọn bằng Emoji Picker -->
            <div class="mb-3 col-lg-6">
                <label class="form-label">Chọn Icon</label>
                <div class="input-group">
                    <input type="text" name="icon" id="iconInput" readonly value="${category.icon}" class="form-control"
                           placeholder="Chọn biểu tượng..." required>
                    <button type="button" class="btn-icon-picker" data-bs-toggle="modal"
                            data-bs-target="#emojiModal">
                        <span class="me-1">😀</span>Chọn Icon
                    </button>
                </div>
            </div>

            <!-- Mô tả -->
            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea name="description" class="form-control">${category.description}</textarea>
            </div>
            <div class="col-md-12 d-flex justify-content-end">
                <button type="submit" class="btn btn-success me-2">💾 Lưu</button>
                <a href="/category?page=1" class="btn btn-secondary">↩ Hủy</a>
            </div>
        </form>
    </div>
</div>


<!-- Modal chứa Emoji Picker -->
<div class="modal fade" id="emojiModal" tabindex="-1" aria-labelledby="emojiModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered custom-emoji-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="emojiModalLabel">Chọn biểu tượng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body p-2">
                <!-- Emoji Picker -->
                <emoji-picker id="emojiPicker"></emoji-picker>
            </div>
        </div>
    </div>
</div>


<script type="module">
    import 'https://cdn.jsdelivr.net/npm/emoji-picker-element/index.js'

    const picker = document.querySelector('#emojiPicker');
    const input = document.querySelector('#iconInput');
    const modal = document.querySelector('#emojiModal');

    // Khi chọn emoji → ghi vào input và tự đóng modal
    picker.addEventListener('emoji-click', event => {
        input.value = event.detail.unicode;
        const bootstrapModal = bootstrap.Modal.getInstance(modal);
        bootstrapModal.hide();
    });
</script>