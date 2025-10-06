<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="container">
    <div class="col-md-2 d-grid mb-2">
        <a href="/employee?action=create" class="btn btn-primary">➕ Thêm nhân viên</a>
    </div>
    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <form action="/employee" method="get" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="filters">
                <div class="col-md-3">
                    <label for="name" class="form-label fw-semibold">Tên nhân viên</label>
                    <input type="text" class="form-control custom-input" value="${filters.name}" id="name" name="name"
                           placeholder="Nhập tên...">
                </div>

                <div class="col-md-3">
                    <label for="role" class="form-label fw-semibold">Vai trò</label>
                    <select id="role" name="role" class="form-select custom-input">
                        <option value="-1">-- Tất cả --</option>
                        <c:forEach var="role" items="${roles}">
                            <option value="${role.id}" ${filters.role == role.id ? "selected" : ""}>${role.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col-md-3">
                    <label for="createdFrom" class="form-label fw-semibold">Từ ngày</label>
                    <input type="date" class="form-control custom-input" value="${filters.createdFrom}" id="createdFrom"
                           name="createdFrom">
                </div>

                <div class="col-md-3">
                    <label for="createdTo" class="form-label fw-semibold">Đến ngày</label>
                    <input type="date" class="form-control custom-input" value="${filters.createdTo}" id="createdTo"
                           name="createdTo">
                </div>

                <div class="col-md-12 d-flex justify-content-end gap-2 mt-3">
                    <button type="submit" class="btn btn-info d-flex align-items-center shadow-sm">
                        <i class="fa-solid fa-filter"></i> Lọc
                    </button>
                    <a href="/employee" class="btn btn-secondary d-flex align-items-center px-3 shadow-sm">
                        <i class="fa-solid fa-trash"></i> Reset
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Bảng danh sách nhân viên -->
    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách nhân viên</h5>
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-primary">
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Vai trò</th>
                    <th>Số điện thoại</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th style="width: 15%;">Hành động</th>
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
                        <td><span class="badge bg-${emp.status.badge}">${emp.status.label}</span></td>
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