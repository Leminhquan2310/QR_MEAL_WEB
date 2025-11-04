<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <!-- Nút mở modal tạo menu -->
    <a href="/menu?action=create" class="btn btn-primary mb-3">
        + Thêm menu
    </a>

    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">🍽 Danh sách Menu</h5>

            <table class="table table-bordered table-hover text-center align-middle">
                <thead class="table-success">
                <tr>
                    <th>ID</th>
                    <th>Tên Menu</th>
                    <th>Mô tả</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th style="width: 15%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="m" items="${menus}">
                    <tr>
                        <td>${m.id}</td>
                        <td>${m.name}</td>
                        <td>${m.description}</td>
                        <td>${m.created_at}</td>
                        <td>
                            <span class="badge bg-${m.status.badge}">
                                    ${m.status.label}
                            </span>
                        </td>
                        <td>
                            <!-- Nút sửa -->
                            <form action="/menu" method="get" class="d-inline update-form">
                                <input type="hidden" name="action" value="update">
                                <input type="hidden" name="id" value="${m.id}">
                                <button class="btn btn-sm btn-warning" type="submit">
                                    <i class="fa-solid fa-pen"></i> Sửa
                                </button>
                            </form>

                            <!-- Form xóa -->
                                <button type="button" onclick="handleDelete('${m.id}')" class="btn btn-sm btn-danger btn-delete">
                                    <i class="fa-solid fa-trash"></i> Xóa
                                </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>