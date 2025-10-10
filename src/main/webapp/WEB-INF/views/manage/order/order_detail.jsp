<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container py-4">
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Chi tiết đơn hàng #${order.id}</h5>
        </div>
        <div class="card-body">
            <form action="/order" method="POST">
                <div class="row mb-2">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${order.id}">
                    <div class="col-md-6">
                        <p><strong>Mã đơn hàng:</strong> ${order.id}</p>
                        <p><strong>Bàn số:</strong> ${order.table_id}</p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Thời gian tạo:</strong> ${order.created_at}</p>
                        <p><strong>Trạng thái:</strong>
                            <span class="badge bg-${order.status.badge}"> ${order.status} </span>
                        </p>
                    </div>
                    <!-- 🔽 Thêm phần lọc theo trạng thái đơn hàng -->
                    <div class="col-md-3">
                        <label for="status" class="form-label fw-semibold">Trạng thái đơn hàng</label>
                        <select id="status" name="status" class="form-select custom-input">
                            <c:forEach var="status" items="${statuses}">
                                <option value="${status.code}" ${order.status.code == status.code ? 'selected' : ''}>${status.label}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <!-- Buttons -->
                    <div class="col-12 d-flex justify-content-start mt-3">
                        <button type="submit" class="btn btn-success me-2">💾 Lưu</button>
                        <a href="/order" class="btn btn-secondary">↩ Hủy</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Danh sách sản phẩm trong đơn -->
    <div class="card shadow-sm">
        <div class="card-header bg-secondary text-white">
            <h6 class="mb-0">Danh sách sản phẩm</h6>
        </div>
        <div class="card-body">
            <table class="table align-middle table-bordered table-hover">
                <thead class="table-light">
                <tr class="text-center">
                    <th>Ảnh</th>
                    <th>Tên sản phẩm</th>
                    <th>Số lượng</th>
                    <th>Đơn giá</th>
                    <th>Thành tiền</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="detail" items="${orderDetails}">
                    <tr>
                        <td class="text-center">
                            <img src="${detail.product.image}"
                                 alt="${detail.product.name}"
                                 class="rounded"
                                 style="width:60px; height:60px; object-fit:cover;">
                        </td>
                        <td>${detail.product.name}</td>
                        <td class="text-center">${detail.quantity}</td>
                        <td class="text-end">${detail.price}</td>
                        <td class="text-end">${detail.quantity * detail.price}</td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot class="table-light">
                <tr>
                    <th colspan="4" class="text-end">Tổng cộng:</th>
                    <th class="text-end">
                        <fmt:formatNumber value="${totalAmount}" type="number" groupingUsed="true"/> vnđ
                    </th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>

    <!-- Nút quay lại -->
    <div class="mt-3">
        <a href="order" class="btn btn-outline-secondary">
            <i class="fa fa-arrow-left me-1"></i> Quay lại danh sách
        </a>
    </div>
</div>
