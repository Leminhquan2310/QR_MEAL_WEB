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

            <!-- PHÂN TRANG -->
            <c:if test="${not empty filters}">
                <c:set var="context"
                       value="&action=filters&name=${filters.name}&role=${filters.role}&createdFrom=${filters.createdFrom}&createdTo=${filters.createdTo}"/>
            </c:if>
            <nav>
                <ul class="pagination justify-content-center">
                    <%--     Nút Trang Trước -->--%>
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage - 1}${context}">«</a>
                        </li>
                    </c:if>

                    <%--     Nếu không ở đầu danh sách -->--%>
                    <c:if test="${startPage > 1}">
                        <li class="page-item"><a class="page-link" href="?page=1${context}">1</a></li>
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>

                    <%--     Các trang trong phạm vi -->--%>
                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}${context}">${i}</a>
                        </li>
                    </c:forEach>

                    <%--     Nếu không ở cuối danh sách --%>
                    <c:if test="${endPage < totalPages}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                        <li class="page-item">
                            <a class="page-link" href="?page=${totalPages}${context}">${totalPages}</a>
                        </li>
                    </c:if>

                    <%--     Nút Trang Sau --%>
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


<!-- Modal thêm/sửa -->
<div class="modal fade" id="discountModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form method="post" action="/discount">
                <div class="modal-header bg-gradient bg-primary text-white">
                    <h5 class="modal-title" id="modalTitle">
                        <i class="fa-solid fa-tags text-danger me-2"></i>Thêm giảm giá
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">
                    <input type="hidden" name="id" id="discountId">
                    <input type="hidden" name="action" id="formAction" value="create">

                    <!-- Điểm yêu cầu -->
                    <div class="mb-3">
                        <label class="form-label"> Điểm yêu cầu </label>
                        <input type="number" name="points_required" id="discountPoints" class="form-control" required
                               min="0">
                    </div>

                    <!-- Loại giảm giá -->
                    <div class="mb-3">
                        <label class="form-label"> Loại đơn vị giảm giá </label>
                        <select name="discount_type" id="discountType" class="form-select" required>
                            <c:forEach var="t" items="${types}">
                                <option value="${t.value}">Giảm theo ${t.label}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Giá trị giảm -->
                    <div class="mb-3">
                        <label class="form-label"> Giá trị giảm </label>
                        <input type="number" name="discount_value" id="discountValue" class="form-control" required
                               min="0" step="0.01" placeholder="VD: 10 hoặc 50000">
                        <div class="form-text" id="discountHint">Nhập giá trị theo loại giảm giá đã chọn.</div>
                    </div>

                    <!-- Mô tả -->
                    <div class="mb-3">
                        <label class="form-label"> Mô tả </label>
                        <textarea name="description" id="discountDescription" class="form-control" rows="3"
                                  required></textarea>
                    </div>

                    <div class="mb-3 status-toggle">
                        <label class="form-label mb-1">Trạng thái</label>
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" id="discountStatus" name="status" value="1" checked>
                            <label class="form-check-label" for="discountStatus" id="statusLabel">Hoạt động</label>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Huỷ</button>
                    <button type="submit" class="btn btn-primary">Lưu</button>
                </div>
            </form>
        </div>
    </div>
</div>