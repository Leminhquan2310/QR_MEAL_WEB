<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container mt-4">
    <!-- Form update -->
    <div class="card shadow-sm">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/employee?action=update"
                  method="post"
                  class="row g-3 needs-validation"
                  novalidate>

                <!-- ID hidden -->
                <input type="hidden" name="id" value="${employee.id}"/>

                <!-- Tên nhân viên -->
                <div class="col-md-6">
                    <label for="name" class="form-label">Tên nhân viên</label>
                    <input type="text" class="form-control" id="name" name="name"
                           value="${employee.name}" required>
                    <div class="invalid-feedback">Vui lòng nhập tên nhân viên.</div>
                </div>

                <!-- Role -->
                <div class="col-md-6">
                    <label for="role" class="form-label">Vai trò</label>
                    <select class="form-select" id="role" name="role" required>
                        <c:forEach var="r" items="${roles}">
                            <option value="${r.id}" ${employee.role.id == r.id ? 'selected' : ''}>
                                    ${r.name}
                            </option>
                        </c:forEach>
                    </select>
                    <div class="invalid-feedback">Vui lòng chọn vai trò.</div>
                </div>

                <!-- Số điện thoại -->
                <div class="col-md-6">
                    <label for="phone" class="form-label">Số điện thoại</label>
                    <input type="text" class="form-control" id="phone" name="phone"
                           value="${employee.phone}" required>
                    <div class="invalid-feedback">Vui lòng nhập số điện thoại.</div>
                </div>

                <!-- Password -->
                <div class="col-md-6">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input class="form-control" type="password" id="password" name="password"
                           placeholder="Nhập mật khẩu mới (nếu muốn đổi)">
                </div>

                <!-- Buttons -->
                <div class="col-12 d-flex justify-content-end mt-3">
                    <button type="submit" class="btn btn-primary me-2">💾 Cập nhật</button>
                    <a href="${pageContext.request.contextPath}/employee?page=1" class="btn btn-secondary">↩ Hủy</a>
                </div>

            </form>
        </div>
    </div>
</div>

<script>
    // Bootstrap validation
    (() => {
        'use strict'
        const forms = document.querySelectorAll('.needs-validation')
        Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()
                }
                form.classList.add('was-validated')
            }, false)
        })
    })()
</script>
