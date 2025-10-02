<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="container">
    <!-- Tiêu đề -->
    <div class="col-md-2 d-grid mb-2">
        <a href="/employee?action=create" class="btn btn-primary">➕ Thêm nhân viên</a>
    </div>
    <!-- Bảng danh sách nhân viên -->
    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách nhân viên</h5>
            <table class="table table-bordered align-middle text-center" >
                <thead class="table-primary">
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Vai trò</th>
                    <th>Số điện thoại</th>
                    <th>Ngày tạo</th>
                    <th  style="width: 15%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="emp" items="${employees}">
                    <tr>
                        <td>${emp.id}</td>
                        <td>${emp.name}</td>
                        <td>
                            <span class="badge" style="background-color: ${emp.role.color}">
                                    ${emp.role.name}
                            </span>
                        </td>
                        <td>${emp.phone}</td>
                        <td><fmt:formatDate value="${emp.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td>
                            <a href="/employee?action=update&id=${emp.id}" class="btn btn-sm btn-warning me-2">✏ Sửa</a>
                            <button onclick="handleDelEmployee(${emp.id})" class="btn btn-sm btn-danger">🗑 Xóa</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>