<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3>Thêm nhân viên</h3>
<form method="post" action="/employee?action=create" class="mt-3">
    <div class="mb-3">
        <label class="form-label">Tên nhân viên</label>
        <input type="text" name="name" class="form-control" required>
    </div>

    <div class="mb-3">
        <label class="form-label">Vai trò</label>
        <select name="role" class="form-select">
            <c:forEach var="role" items="${roles}">
                <option value="${role.id}">${role.name}</option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label class="form-label">Số điện thoại</label>
        <input type="text" name="phone" class="form-control">
    </div>

    <div class="mb-3">
        <label class="form-label" for="password">Mật khẩu:</label>
        <input class="form-control" type="text" id="password" name="password">
    </div>

    <button type="submit" class="btn btn-success">Lưu</button>
    <a href="/employee" class="btn btn-secondary">Hủy</a>
</form>
