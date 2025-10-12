<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html; ISO-8859-1" pageEncoding="UTF-8" language="java" %>

<div class="container mt-4">
    <!-- Nút mở modal -->
    <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addTableModal">
        <i class="fa-solid fa-plus"></i> Thêm bàn
    </button>

    <div class="card shadow-sm mb-3">
        <div class="card-body p-3"> <!-- giảm padding -->
            <form method="get" action="/table" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="filters">
                <input type="hidden" name="page" value="1">

                <div class="col-md-3">
                    <label for="createdFrom" class="form-label fw-semibold">Từ ngày</label>
                    <input type="date" class="form-control custom-input"
                           value="${filters.createdFrom}" id="createdFrom"
                           name="createdFrom">
                </div>

                <div class="col-md-3">
                    <label for="createdTo" class="form-label fw-semibold">Đến ngày</label>
                    <input type="date" class="form-control custom-input"
                           value="${filters.createdTo}" id="createdTo"
                           name="createdTo">
                </div>

                <!-- Nút lọc & reset -->
                <div class="col-auto d-flex gap-2">
                    <button type="submit" class="btn btn-info d-flex align-items-center shadow-sm">
                        <i class="fa-solid fa-filter me-2"></i> Lọc
                    </button>
                    <a href="/table?page=1" class="btn btn-secondary d-flex align-items-center shadow-sm">
                        <i class="fa-solid fa-trash me-2"></i> Reset
                    </a>
                </div>
            </form>
        </div>
    </div>

    <!-- Danh sách bàn -->
    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách bàn</h5>
            <table class="table table-bordered table-hover text-center" id="tableList">
                <thead class="table-primary">
                <tr>
                    <th scope="col">Mã</th>
                    <th scope="col">Tên</th>
                    <th scope="col">Ngày tạo</th>
                    <th scope="col">Ngày cập nhật</th>
                    <th scope="col">Trạng thái</th>
                    <th scope="col" style="width: 20%">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <!-- Ví dụ dữ liệu -->
                <c:forEach var="table" items="${tables}">
                    <tr>
                        <td>${table.id}</td>
                        <td>${table.name}</td>
                        <td><fmt:formatDate value="${table.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td><fmt:formatDate value="${table.updated_at}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td>
                            <span class="badge bg-${table.status.badge}">${table.status.label}</span>
                        </td>
                        <td>
                            <a onclick="openQrModal('${table.id}', '${table.name}', '${table.qr_code_image}')"
                               class="btn btn-sm btn-warning me-2">✏ Tùy chọn</a>
                            <a onclick="showDeleteAlert(${table.id})" class="btn btn-sm btn-danger">🗑 Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <!-- PHÂN TRANG -->
            <c:if test="${not empty filters}">
                <c:set var="context"
                       value="&action=filters&createdFrom=${filters.createdFrom}&createdTo=${filters.createdTo}"/>
            </c:if>
            <nav>
                <ul class="pagination justify-content-center">
                    <!-- Nút Trang Trước -->
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?page=${currentPage - 1}${context}">«</a>
                        </li>
                    </c:if>

                    <!-- Nếu không ở đầu danh sách -->
                    <c:if test="${startPage > 1}">
                        <li class="page-item"><a class="page-link" href="?page=1${context}">1</a></li>
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                    </c:if>

                    <!--     Các trang trong phạm vi -->
                    <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}${context}">${i}</a>
                        </li>
                    </c:forEach>

                    <!--     Nếu không ở cuối danh sách -->
                    <c:if test="${endPage < totalPages}">
                        <li class="page-item disabled"><span class="page-link">...</span></li>
                        <li class="page-item">
                            <a class="page-link" href="?page=${totalPages}${context}">${totalPages}</a>
                        </li>
                    </c:if>

                    <!--     Nút Trang Sau -->
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

<!-- Modal Thêm Bàn -->
<div class="modal fade" id="addTableModal" tabindex="-1" aria-labelledby="addTableModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0 rounded-3">

            <!-- Header -->
            <div class="modal-header bg-gradient bg-primary text-white">
                <h5 class="modal-title fw-bold d-flex align-items-center gap-2" id="addTableModalLabel">
                    <i class="fa-solid fa-table"></i> Thêm bàn mới
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>

            <!-- Body -->
            <div class="modal-body">
                <form method="post" action="/table?action=create" id="addTableForm">
                    <div class="mb-3">
                        <label for="tableName" class="form-label fw-semibold">Tên bàn</label>
                        <input type="text" class="form-control rounded-3 shadow-sm" id="tableName" name="name"
                               placeholder="Nhập tên bàn (ví dụ: Bàn 1)" required>
                    </div>
                </form>
            </div>

            <!-- Footer -->
            <div class="modal-footer d-flex justify-content-end">
                <button type="button" class="btn btn-light border shadow-sm rounded-3" data-bs-dismiss="modal">
                    <i class="fa-solid fa-xmark me-2"></i> Hủy
                </button>
                <button type="submit" form="addTableForm" class="btn btn-success shadow-sm rounded-3">
                    <i class="fa-solid fa-check me-2"></i> Lưu
                </button>
            </div>

        </div>
    </div>
</div>


<!-- Modal QR Code -->
<div class="modal fade" id="qrCodeModal" tabindex="-1" aria-labelledby="qrCodeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0 rounded-3">

            <!-- Header -->
            <div class="modal-header bg-secondary text-white">
                <h5 class="modal-title fw-bold d-flex align-items-center gap-2" id="qrCodeModalLabel">
                    <i class="fa-solid fa-qrcode"></i> QR Code Bàn
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>

            <!-- Body -->
            <div class="modal-body ">
                <div class="mb-3 text-center">
                    <label for="tableNameUpdate" class="form-label fw-semibold small d-block">
                        Tên bàn
                    </label>
                    <input id="tableNameUpdate" type="text" name="name"
                           class="form-control text-center mx-auto"
                           style="max-width: 250px;"
                           placeholder="Nhập tên bàn (ví dụ: Bàn 1)" required>
                </div>

                <!-- QR Code Box -->
                <div class="d-flex justify-content-center mb-3 mt-3">
                    <div class="p-3 border border-3 border-muted rounded-3 bg-white shadow-sm" id="qr_content">
                        <!-- Spinner hiển thị khi đang load -->
                        <div id="qrLoading" class="d-flex align-items-center justify-content-center h-100 d-none">
                            <div class="spinner-border text-primary" role="status">
                                <span class="visually-hidden">Loading...</span>
                            </div>
                        </div>
                        <img id="qrCodeImage" src="" alt="QR Code"
                             class="img-fluid" style="width: 220px;">
                    </div>
                </div>

                <div class="d-flex justify-content-center mt-3">
                    <button type="button" class="btn btn-warning btn-sm d-flex align-items-center gap-2"
                            id="btnRefreshQR">
                        <i class="fa-solid fa-rotate"></i> Làm mới QR
                    </button>
                </div>
            </div>

            <!-- Footer -->
            <div class="modal-footer text-center w-100">
                <div class="d-flex justify-content-center gap-2 w-100">
                    <button type="button" class="btn btn-primary btn-sm d-flex align-items-center gap-2"
                            id="btnSaveTable">
                        <i class="fa-solid fa-floppy-disk"></i></i> Lưu
                    </button>
                    <button type="button" class="btn btn-success btn-sm d-flex align-items-center gap-2"
                            id="btnPrintQR">
                        <i class="fa-solid fa-print"></i> In QR
                    </button>
                    <button type="button" class="btn btn-info btn-sm d-flex align-items-center gap-2 text-white"
                            id="btnDownloadQR">
                        <i class="fa-solid fa-download"></i> Tải về
                    </button>
                    <button type="button" class="btn btn-secondary btn-sm d-flex align-items-center gap-2"
                            data-bs-dismiss="modal">
                        <i class="fa-solid fa-xmark"></i> Hủy
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
