<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#createCustomerModal">
        + Thêm khách hàng
    </button>

    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách khách hàng</h5>
            <table class="table table-bordered table-hover align-middle text-center">
                <thead class="table-primary">
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Điện thoại</th>
                    <th>Điểm</th>
                    <th>Ngày tạo</th>
                    <th style="width: 15%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="c" items="${customers}">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.name}</td>
                        <td>${c.phone}</td>
                        <td>${c.points}</td>
                        <td>${c.created_at}</td>
                        <td>
                            <a class="btn btn-sm btn-warning" onclick="showModalUpdateCustomer(${c.id})">✏ Sửa</a>
                                <%--   <a href="customer?action=delete&id=${c.id}" class="btn btn-sm btn-danger"--%>
                                <%--    onclick="return confirm('Xóa khách hàng này?')">🗑 Xóa</a>--%>
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

<!-- Modal Thêm Khách Hàng -->
<div class="modal fade" id="createCustomerModal" tabindex="-1" aria-labelledby="createCustomerModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <!-- Header -->
            <div class="modal-header bg-gradient bg-primary text-white">
                <h5 class="modal-title fw-bold d-flex align-items-center gap-2" id="addTableModalLabel">
                    🦸 Thêm khách hàng mới
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>

            <!-- Body -->
            <div class="modal-body">
                <form action="customer?action=create" method="post" id="addCustomerForm">
                    <div class="mb-3">
                        <label class="form-label" for="name">Tên khách hàng</label>
                        <input id="nameInput" type="text" name="name" class="form-control" required
                               placeholder="Họ và tên...">
                    </div>

                    <div class="mb-2">
                        <label class="form-label" for="phone">Số điện thoại</label>
                        <input id="phoneInput" type="text" name="phone" class="form-control" required
                               placeholder="VD: 0912345678">
                        <small id="phoneFeedback" class="text-danger mt-1"></small>
                    </div>
                </form>
            </div>

            <!-- Footer -->
            <div class="modal-footer d-flex justify-content-end">
                <button type="button" class="btn btn-light border shadow-sm rounded-3" data-bs-dismiss="modal">
                    <i class="fa-solid fa-xmark me-2"></i> Hủy
                </button>
                <button id="addCustomerBtn" type="submit" form="addCustomerForm"
                        class="btn btn-primary shadow-sm rounded-3 disabled">
                    <i class="fa-solid fa-check me-2"></i> Lưu
                </button>
            </div>
        </div>
    </div>
</div>


<!-- Modal Sửa Khách Hàng -->
<div class="modal fade" id="updateCustomerModal" tabindex="-1" aria-labelledby="updateCustomerModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <!-- Header -->
            <div class="modal-header bg-gradient bg-primary text-white">
                <h5 class="modal-title fw-bold d-flex align-items-center gap-2" id="addTableModalLabel">
                    🦸 Sửa thông tin khách hàng
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>

            <!-- Body -->
            <div class="modal-body">
                <%--   spinner--%>
                <div id="loadingSpinner" class="text-center py-2" style="display:none;">
                    <div id="lottieLoading" style="width: 200px; height: 200px; margin: 0 auto;"></div>
                </div>

                <form action="customer?action=update" method="post" id="updateCustomerForm">
                    <input type="hidden" id="updateId" name="id">
                    <div class="mb-3">
                        <label class="form-label" for="name">Tên khách hàng</label>
                        <input id="updateName" type="text" name="name" class="form-control" required
                               placeholder="Họ và tên...">
                    </div>

                    <div class="mb-2">
                        <label class="form-label" for="phone">Số điện thoại</label>
                        <input id="updatePhone" type="text" name="phone" class="form-control" required
                               placeholder="VD: 0912345678">
                        <small id="phoneFeedbackUpdate" class="text-danger mt-1"></small>
                    </div>
                </form>
            </div>

            <!-- Footer -->
            <div class="modal-footer d-flex justify-content-end">
                <button type="button" class="btn btn-light border shadow-sm rounded-3" data-bs-dismiss="modal">
                    <i class="fa-solid fa-xmark me-2"></i> Hủy
                </button>
                <button id="updateCustomerBtn" type="submit" form="updateCustomerForm"
                        class="btn btn-primary shadow-sm rounded-3">
                    <i class="fa-solid fa-check me-2"></i> Lưu
                </button>
            </div>
        </div>
    </div>
</div>
