<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#discountModal" onclick="resetModal()">
        + Thêm giảm giá
    </button>
    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">🎁 Danh sách giảm giá</h5>
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-primary">
                <tr>
                    <th>ID</th>
                    <th>Điểm</th>
                    <th>Mô tả</th>
                    <th>Giá trị</th>
                    <th>Đơn vị</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th style="width: 15%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="d" items="${discounts}">
                    <tr>
                        <td>${d.id}</td>
                        <td>${d.points_required}</td>
                        <td>${d.description}</td>
                        <td>${d.discount_value}</td>
                        <td>${d.discount_type.label}</td>
                        <td>${d.created_at}</td>
                        <td><span class="badge bg-${d.status.badge}">${d.status.label}</span></td>
                        <td>
                            <button class="btn btn-sm btn-warning"
                                    onclick="editDiscount(${d.id}, '${d.points_required}', '${d.description}', '${d.discount_value}', '${d.discount_type.value}', ${d.status.code})">
                                <i class="fa-solid fa-pen"></i> Sửa
                            </button>

                            <form action="/discount" method="post" class="d-inline delete-form">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${d.id}">
                                <button type="button" class="btn btn-sm btn-danger btn-delete">
                                    <i class="fa-solid fa-trash"></i> Xóa
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
