<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container mt-4">
    <!-- Form thêm nhân viên -->
    <div class="card shadow-sm">
        <div class="card-body">
            <form method="post" action="/employee?action=create" class="row g-3">

                <!-- Tên nhân viên -->
                <div class="col-md-6">
                    <label class="form-label">Tên nhân viên</label>
                    <input type="text" name="name" class="form-control" placeholder="Nhập tên nhân viên" required>
                </div>

                <!-- Vai trò -->
                <div class="col-md-6">
                    <label class="form-label">Vai trò</label>
                    <select name="role" class="form-select" required>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.id}">${role.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Số điện thoại -->
                <div class="col-md-6">
                    <label class="form-label">Số điện thoại</label>
                    <input type="text" name="phone" class="form-control" placeholder="VD: 0987xxx">
                </div>

                <!-- Mật khẩu -->
                <div class="col-md-6">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input class="form-control" type="password" id="password" name="password" placeholder="Nhập mật khẩu">
                </div>

                <!-- Buttons -->
                <div class="col-12 d-flex justify-content-end mt-3">
                    <button type="submit" class="btn btn-success me-2">💾 Lưu</button>
                    <a href="/employee" class="btn btn-secondary">↩ Hủy</a>
                </div>
            </form>
        </div>
    </div>
</div>