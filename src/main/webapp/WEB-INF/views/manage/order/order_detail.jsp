<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container py-4">
    <!-- Nút quay lại -->
    <div class="mb-3">
        <a href="order?page=1" class="btn btn-secondary">
            <i class="fa fa-arrow-left me-1"></i> Quay lại danh sách
        </a>
    </div>

    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">Chi tiết đơn hàng #${order.id}</h5>
        </div>

        <div class="card-body">
            <form action="/order" method="POST">
                <div class="row mb-2">
                    <input type="hidden" name="id" value="${order.id}">
                    <input type="hidden" name="action" value="update-status">
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
                    <div class="col-md-6">
                        <button type="button" class="btn btn-md btn-secondary"
                                onclick="openHistoryModal(${order.id})">
                            Lịch sử cập nhật <i class="fa-regular fa-clock"></i>
                        </button>
                    </div>
                    <div class="col-md-6">
                        <!-- Buttons -->
                        <div class="col-12 d-flex justify-content-start ">
                            <c:choose>
                                <c:when test="${order.status.code == 0}">
                                    <input type="hidden" name="status" value="${order.status.code + 1}">
                                    <button type="submit" class="btn btn-success btn-sm me-2">
                                        <i class="fa-solid fa-check"></i> Xác nhận
                                    </button>
                                    <a href="#" onclick="handleCancelledOrder('${order.id}')"
                                       class="btn btn-danger btn-sm"> <i class="fa-solid fa-trash"></i>
                                        Hủy đơn</a>
                                </c:when>
                                <c:when test="${order.status.code == 1}">
                                    <input type="hidden" name="status" value="${order.status.code + 1}">
                                    <button type="button" class="btn btn-primary btn-sm me-2"
                                            onclick="openServeModal()">
                                        <i class="fa-solid fa-utensils"></i> Phục vụ
                                    </button>
                                    <button onclick="closeOrder('${order.id}')" type="button"
                                            class="btn btn-success btn-sm me-2">
                                        <i class="fa-solid fa-check me-1"></i> Đóng đơn
                                    </button>
                                    <a href="#" onclick="handleCancelledOrder('${order.id}')"
                                       class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i>
                                        Hủy đơn</a>
                                </c:when>
                                <c:when test="${order.status.code == 2}">
                                    <button type="button" class="btn btn-primary btn-sm me-2"
                                            onclick="confirmPayment(${order.id},${totalAmount}, 0)">
                                        <i class="fa-solid fa-money-bill-1"></i> Thanh toán
                                    </button>
                                    <button type="button" onclick="openPaymentModal(${order.id})"
                                            class="btn btn-success btn-sm me-2">
                                        <i class="fa-solid fa-print"></i> In hóa đơn
                                    </button>
                                    <a href="#" onclick="handleCancelledOrder('${order.id}')"
                                       class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i>
                                        Hủy đơn</a>
                                </c:when>
                            </c:choose>
                        </div>
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
                        <td class="text-end"><fmt:formatNumber value="${detail.price}" groupingUsed="true"/> vnđ</td>
                        <td class="text-end"><fmt:formatNumber value="${detail.quantity * detail.price}"
                                                               groupingUsed="true"/> vnđ
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot class="table-light">
                <tr>
                    <th colspan="4" class="text-end">Tổng cộng:</th>
                    <th class="text-end" id="amount">
                        <fmt:formatNumber value="${totalAmount}" type="number" groupingUsed="true"/> vnđ
                    </th>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="statusHistoryModal" tabindex="-1" aria-labelledby="statusHistoryLabel" aria-hidden="true">
    <div class="modal-dialog modal-md modal-dialog-scrollable">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="statusHistoryLabel">Lịch sử cập nhật</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>
            <div class="modal-body p-2">
                <div id="historyTableContainer">
                    <!-- sẽ render bảng AJAX vào đây -->
                    <table class="table table-md">
                        <thead class="table-light">
                        <tr>
                            <th style="width:20%">Thời gian</th>
                            <th style="width: 50%">Trạng thái</th>
                            <th style="width:30%">Người thay đổi</th>
                        </tr>
                        </thead>
                        <tbody id="historyTbody">
                        <tr>
                            <td colspan="3" class="text-center">Đang tải...</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<!-- MODAL PHỤC VỤ MÓN -->
<div class="modal fade" id="serveModal" tabindex="-1" aria-labelledby="serveModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-md">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="serveModalLabel">
                    <i class="fa-solid fa-utensils me-2"></i> Phục vụ món
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>

            <div class="modal-body">
                <div class="row mb-3">
                    <div class="col-md-6">
                        <p><strong>Mã đơn:</strong> <span>${order.id}</span></p>
                        <p><strong>Bàn:</strong> <span>${order.table_id}</span></p>
                    </div>
                    <div class="col-md-6">
                        <p><strong>Thời gian tạo:</strong> ${order.created_at}</p>
                        <p><strong>Trạng thái hiện tại:</strong>
                            <span class="badge bg-${order.status.badge}">${order.status}</span>
                        </p>
                    </div>
                </div>
                <hr>
                <div>
                    <h6 class="fw-bold mb-2">Danh sách món ăn</h6>
                    <ul class="list-group list-group-flush">
                        <c:forEach var="detail" items="${orderDetails}">
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <div class="d-flex align-items-center">
                                    <img src="${detail.product.image}"
                                         alt="${detail.product.name}"
                                         class="rounded me-2"
                                         style="width:50px; height:50px; object-fit:cover;">
                                    <div>
                                        <div class="fw-semibold">${detail.product.name}</div>
                                    </div>
                                </div>
                                <div class="text-end fw-semibold">
                                    Số lượng: ${detail.quantity}
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>

            <div class="modal-footer">
                <button id="btnPrint" type="button" class="btn btn-primary btn-sm" data-id="${order.id}">
                    <i class="fa-solid fa-print me-1"></i> In đơn chế biến
                </button>
                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">
                    <i class="fa-solid fa-xmark me-1"></i> Hủy
                </button>
            </div>
        </div>
    </div>
</div>

<!-- MODAL THANH TOÁN -->
<div class="modal fade" id="paymentModal" tabindex="-1" aria-labelledby="paymentModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-md">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title" id="paymentModalLabel">
                    <i class="fa-solid fa-money-bill-wave me-2"></i> Thanh toán đơn hàng
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
            </div>

            <div id="pm-bill" class="modal-body">
                <div class="border rounded p-3 bg-light" id="billPreview" style="min-height: 320px;">
                    <h6 class="fw-semibold text-center mb-2">
                        <i class="fa-solid fa-receipt me-1"></i> Hóa đơn thanh toán
                    </h6>
                    <div class="d-flex justify-content-between">
                        <span><strong>Mã đơn:</strong></span>
                        <span id="pm-order-id">#${order.id}</span>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span><strong>Bàn:</strong></span>
                        <span id="pm-order-table">${order.table_id}</span>
                    </div>
                    <div class="d-flex justify-content-between">
                        <span><strong>Ngày tạo:</strong></span>
                        <span id="pm-created-at"> ${order.created_at}</span>
                    </div>
                    <hr class="my-2">
                    <div>
                        <strong>Chi tiết món:</strong>
                        <table class="table table-sm table-borderless align-middle small mt-1 mb-2"
                               id="pm-items">
                            <thead class="border-bottom">
                            <tr class="text-secondary">
                                <th>Tên món</th>
                                <th class="text-center" style="width:60px;">SL</th>
                                <th class="text-end" style="width:100px;">Đơn giá</th>
                                <th class="text-end" style="width:100px;">Thành tiền</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="detail" items="${orderDetails}">
                                <tr>
                                    <td>${detail.product.name}</td>
                                    <td class="text-center">${detail.quantity}</td>
                                    <td class="text-end"><fmt:formatNumber value="${detail.price}"
                                                                           groupingUsed="true"/>đ
                                    </td>
                                    <td class="text-end"><fmt:formatNumber
                                            value="${detail.quantity * detail.price}"
                                            groupingUsed="true"/>đ
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <hr class="my-2">
                    <div class="d-flex justify-content-between">
                        <strong>Tổng tiền:</strong>
                        <span id="pm-total" class="text-danger fw-bold"><fmt:formatNumber value="${totalAmount}"
                                                                                          groupingUsed="true"/>đ</span>
                    </div>
                    <!-- QR hiển thị nếu chọn -->
                    <div id="qrSection" class="text-center mt-3">
                        <img src="" alt="QR thanh toán" style="width:150px;">
                        <p class="text-muted mt-2 mb-0">Quét mã để thanh toán nhanh</p>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success btn-sm" id="btnPrintPayment">
                    <i class="fa-solid fa-print me-1"></i> In hóa đơn
                </button>
                <button type="button" class="btn btn-secondary btn-sm" data-bs-dismiss="modal">
                    <i class="fa-solid fa-xmark me-1"></i> Hủy
                </button>
            </div>
        </div>
    </div>
</div>


<%--                    <!-- BÊN PHẢI: Chọn hình thức thanh toán -->--%>
<%--                    <div class="col-md-6">--%>
<%--                        <h6 class="fw-semibold mb-2">Chọn hình thức thanh toán:</h6>--%>
<%--                        <div class="d-flex flex-column gap-2">--%>
<%--                            <div class="form-check">--%>
<%--                                <input class="form-check-input" type="radio" name="paymentMethod" id="cash" value="cash"--%>
<%--                                       checked>--%>
<%--                                <label class="form-check-label" for="cash">--%>
<%--                                    <i class="fa-solid fa-coins text-warning me-1"></i> Tiền mặt--%>
<%--                                </label>--%>
<%--                            </div>--%>

<%--                            <div class="form-check">--%>
<%--                                <input class="form-check-input" type="radio" name="paymentMethod" id="bank"--%>
<%--                                       value="bank">--%>
<%--                                <label class="form-check-label" for="bank">--%>
<%--                                    <i class="fa-solid fa-building-columns text-primary me-1"></i> Chuyển khoản--%>
<%--                                </label>--%>
<%--                            </div>--%>

<%--                            <div class="form-check">--%>
<%--                                <input class="form-check-input" type="radio" name="paymentMethod" id="qr"--%>
<%--                                       value="qr">--%>
<%--                                <label class="form-check-label" for="qr">--%>
<%--                                    <i class="fa-solid fa-qrcode text-success me-1"></i> Quét mã QR--%>
<%--                                </label>--%>
<%--                            </div>--%>
<%--                        </div>--%>

