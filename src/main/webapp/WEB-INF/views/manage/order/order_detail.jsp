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
                                            onclick="openConfirmPaymentModal('${order.id}')">
                                        <i class="fa-solid fa-money-bill-1"></i> Thanh toán
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
                    <th>Thành tiền</th>
                    <th>Đơn giá</th>
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


<!-- Modal xác nhận thanh toán -->
<div class="modal fade" id="confirmPaymentModal" tabindex="-1" aria-labelledby="confirmPaymentLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="confirmPaymentLabel">Xác nhận thanh toán</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <form class="col-md-6" id="confirmPaymentForm" method="POST"
                          action="/order?action=complete-order">
                        <!-- Nhập số điện thoại khách hàng -->
                        <div class="mb-3 position-relative">
                            <label for="customerPhone" class="mb-2 fw-bold">
                                Số điện thoại khách hàng
                                <a onclick="toggleFormAddCustomer(event)" class="text-decoration-none ms-2" href="#">Tạo
                                    mới</a>
                            </label>
                            <input type="text" oninput="onInputCustomerPhone(event)" id="customerPhone" name="phone"
                                   class="form-control"
                                   placeholder="Dùng để tích điểm hoặc giảm giá nếu có."
                                   style="background-image: none !important;">
                            <div id="spinnerPhone" class="position-absolute end-0 translate-middle-y me-3"
                                 style="display:none; top: 50px">
                                <div class="spinner-border spinner-border-sm text-primary" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                            </div>
                            <div id="pointFeedback" class="text-danger small mt-1"></div>
                        </div>

                        <!-- Form thêm khách hàng ẩn -->
                        <div id="inlineCreateCustomer" class="p-3 border rounded bg-light shadow-sm"
                             style="display:none;">

                        </div>

                        <!-- Chọn phương thức thanh toán -->
                        <div class="mb-3">
                            <label class="mb-2 fw-bold">Phương thức thanh toán</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_cash"
                                       value="cash" onchange="onChangePaymentMethodRadio(event)" checked>
                                <label class="form-check-label" for="pay_cash">Tiền mặt</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="paymentMethod" id="pay_transfer"
                                       value="bank" onchange="onChangePaymentMethodRadio(event)">
                                <label class="form-check-label" for="pay_transfer">Chuyển khoản</label>
                            </div>
                        </div>

                        <!-- Lựa chọn tích điểm hoặc đổi điểm -->
                        <div class="mb-3" id="discount-form" style="display: none">
                            <label class="mb-2 fw-bold">Chọn hình thức điểm thưởng</label>

                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="pointOption" id="optionEarn"
                                       value="earn" onchange="onChangeEarnOption()" checked>
                                <label class="form-check-label" for="optionEarn">
                                    Tích điểm (cộng điểm sau thanh toán)
                                </label>
                            </div>

                            <div class="form-check mt-2">
                                <input class="form-check-input" type="radio" name="pointOption" id="optionRedeem"
                                       value="redeem" onchange="onChangeRedeemOption()">
                                <label class="form-check-label" for="optionRedeem">
                                    Đổi điểm để giảm giá
                                </label>
                            </div>

                            <!-- Chỉ hiện khi chọn “Đổi điểm” -->
                            <div id="redeemContainer" class="mt-3" style="display:none;">
                                <%-- Hiển thị các dữ liệu giảm giá --%>
                            </div>
                        </div>

                        <!-- Hidden input -->
                        <input type="hidden" id="orderId" name="id">
                    </form>

                    <%-- Hóa đơn thanh toán--%>
                    <div class="col-md-6 border rounded p-3 bg-light" id="billPreview" style="min-height: 320px;">
                        <h6 class="fw-semibold text-center mb-2">
                            <i class="fa-solid fa-receipt me-1"></i> Hóa đơn thanh toán
                        </h6>
                        <div class="d-flex justify-content-between">
                            <span><strong>Mã đơn:</strong></span>
                            <span id="pm-order-id"></span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span><strong>Bàn:</strong></span>
                            <span id="pm-order-table"></span>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span><strong>Ngày tạo:</strong></span>
                            <span id="pm-created-at"></span>
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
                                <tbody id="dataOrderDetail">
                                </tbody>
                            </table>
                        </div>
                        <hr class="my-2">
                        <div>
                            <div class="d-flex justify-content-between">
                                <strong>Tổng tiền:</strong>
                                <span id="pm-total" class="fw-bold text-secondary">0 ₫</span>
                            </div>

                            <div class="d-flex justify-content-between">
                                <span>Giảm giá:</span>
                                <span id="pm-discount" class="text-success">0 ₫</span>
                            </div>

                            <div class="d-flex justify-content-between mt-2 border-top pt-2">
                                <strong>Tổng thanh toán:</strong>
                                <strong id="pm-final" class="fw-bold text-danger">0 ₫</strong>
                            </div>
                        </div>

                        <!-- QR hiển thị nếu chọn -->
                        <div id="qrSection" class="mt-3 text-center" style="display: none;">
                            <div class="d-inline-block p-3 border border-3 border-muted rounded-3 bg-white shadow-sm"
                                 id="qr_content" style="min-width: 160px; min-height: 50px; position: relative;">

                                <!-- Spinner hiển thị khi đang load -->
                                <div id="qrLoading"
                                     class="d-flex align-items-center justify-content-center h-100 d-none">
                                    <div class="spinner-border text-primary" role="status">
                                        <span class="visually-hidden">Loading...</span>
                                    </div>
                                </div>

                                <!-- Ảnh QR -->
                                <img id="qrCodeImage" src="" alt="QR Code" class="img-fluid d-none"
                                     style="width: 150px;">

                                <!-- Dòng tạo QR Code -->
                                <span id="btnGenerateQR" onclick="createQRCodeClick()"
                                      class="text-primary small fw-semibold" role="button"
                                      style="cursor: pointer;">
                                    <i class="fa fa-qrcode me-1"></i> Tạo QR Code
                                </span>
                            </div>
                            <p class="text-muted mt-2 mb-0 small">Quét mã để thanh toán nhanh</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Huỷ</button>
                <button id="printBtn" class="btn btn-warning" onclick="printBill()">🖨️ In hóa đơn</button>
                <button type="submit" class="btn btn-primary" id="btnConfirmPayment" form="confirmPaymentForm">
                    Xác nhận
                </button>
            </div>
        </div>
    </div>
</div>
