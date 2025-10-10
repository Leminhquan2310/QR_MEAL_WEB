<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="container">
    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <form action="/order" method="get" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="filters">
                <input type="hidden" name="page" value="1">

                <!-- Ô tìm kiếm -->
                <div class="col-md-3">
                    <label for="code" class="form-label fw-semibold">Tìm kiếm</label>
                    <input type="text" class="form-control custom-input" id="code" name="code"
                           value="${filters.code > 0 ? filters.code : ""}"
                           placeholder="Nhập mã đơn hoặc số bàn...">
                </div>

                <!-- Trạng thái đơn hàng -->
                <div class="col-md-3">
                    <label for="status" class="form-label fw-semibold">Trạng thái</label>
                    <select id="status" name="status" class="form-select custom-input">
                        <option value="-1" ${filters.status == -1 ? "selected" : ""}>-- Tất cả --</option>
                        <c:forEach var="status" items="${statuses}">
                            <option value="${status.code}" ${filters.status == status.code ? "selected" : ""}>${status.label}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Từ ngày -->
                <div class="col-md-3">
                    <label for="createdFrom" class="form-label fw-semibold">Từ ngày</label>
                    <input type="date" class="form-control custom-input" id="createdFrom"
                           name="createdFrom" value="${filters.createdFrom}">
                </div>

                <!-- Đến ngày -->
                <div class="col-md-3">
                    <label for="createdTo" class="form-label fw-semibold">Đến ngày</label>
                    <input type="date" class="form-control custom-input" id="createdTo"
                           name="createdTo" value="${filters.createdTo}">
                </div>

                <!-- Nút thao tác -->
                <div class="col-12 d-flex justify-content-end gap-2 mt-3">
                    <button type="submit" class="btn btn-info d-flex align-items-center shadow-sm px-3">
                        <i class="fa-solid fa-filter me-1"></i> Lọc
                    </button>
                    <a href="/order" class="btn btn-secondary d-flex align-items-center shadow-sm px-3">
                        <i class="fa-solid fa-rotate-left me-1"></i> Reset
                    </a>
                </div>
            </form>
        </div>
    </div>


    <!-- Bảng danh sách nhân viên -->
    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách hóa đơn</h5>
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-primary">
                <tr>
                    <th>ID</th>
                    <th>Table ID</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th style="width: 25%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.table_id}</td>
                        <td><fmt:formatDate value="${order.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td><span class="badge bg-${order.status.badge}">${order.status.label}</span></td>
                        <td>
                            <a href="/order?action=detail&id=${order.id}" class="btn btn-sm btn-warning me-2">✏
                                Chi tiết</a>
                            <button onclick="handleDelOrder(${order.id})" class="btn btn-sm btn-danger">🗑 Xóa
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- PHÂN TRANG -->
            <c:if test="${not empty filters}">
                <c:set var="context"
                       value="&action=filters&code=${filters.code}&status=${filters.status}&createdFrom=${filters.createdFrom}&createdTo=${filters.createdTo}"/>
            </c:if>
            <nav>
                <ul class="pagination justify-content-center">
                    <!-- Nút Trang Trước -->
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage - 1}${context}">«</a>
                        </li>
                    </c:if>

                    <!-- Nếu không ở đầu danh sách, hiển thị nút "1" và dấu "..." -->
                    <c:if test="${startPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=1${context}">1</a>
                        </li>
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>

                    <!-- Các trang hiển thị trong phạm vi -->
                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}${context}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- Nếu không ở cuối danh sách, hiển thị "..." và trang cuối -->
                    <c:if test="${endPage < totalPages}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                        <li class="page-item">
                            <a class="page-link" href="?page=${totalPages}${context}">${totalPages}</a>
                        </li>
                    </c:if>

                    <!-- Nút Trang Sau -->
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage + 1}${context}">»</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>