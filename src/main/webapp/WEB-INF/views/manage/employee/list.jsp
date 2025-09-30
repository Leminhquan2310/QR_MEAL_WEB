<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="d-flex justify-content-between mb-3">
    <h3>Danh sách nhân viên</h3>
    <a href="/employee?action=create" class="btn btn-primary">Thêm nhân viên</a>
</div>

<table class="table table-striped table-bordered align-middle">
    <thead class="table-dark">
    <tr>
        <th>ID</th>
        <th>Tên</th>
        <th>Vai trò</th>
        <th>Số điện thoại</th>
        <th>Ngày tạo</th>
        <th>Hành động</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="emp" items="${employees}">
        <tr>
            <td>${emp.id}</td>
            <td>${emp.name}</td>
            <td><span class="badge" style="background-color: ${emp.role.color}">${emp.role.name}</span></td>
            <td>${emp.phone}</td>
            <td><fmt:formatDate value="${emp.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
            <td>
                <a href="/employee?action=update&id=${emp.id}" class="btn btn-warning btn-sm">Sửa</a>
                <!-- Button trigger modal -->
                <button id="btnDel" onclick="handleDelEmployee(${emp.id})" value="${emp.id}" type="button" class="btn btn-danger btn-sm">
                    Xóa
                </button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%-- Modal delete employee --%>
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Xóa nhân viên</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <button type="button" class="btn btn-danger">Xóa</button>
            </div>
        </div>
    </div>
</div>