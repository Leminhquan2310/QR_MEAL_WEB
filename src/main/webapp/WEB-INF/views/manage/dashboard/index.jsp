<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@page language="java" pageEncoding="UTF-8" %>
<!-- DASHBOARD CONTENT START -->
<div class="mt-5">
    <!-- ======= Thống kê nhanh ======= -->
    <div class="row text-center mb-4">
        <div class="col-md-3 mb-3">
            <div class="card bg-success text-white shadow-sm">
                <div class="card-body">
                    <h5 class="card-title"><i class="fa fa-chair me-2"></i>Bàn trống</h5>
                    <h3>${freeTables}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card bg-warning text-white shadow-sm">
                <div class="card-body">
                    <h5 class="card-title"><i class="fa fa-utensils me-2"></i>Đang phục vụ</h5>
                    <h3>${servingTables}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card bg-danger text-white shadow-sm">
                <div class="card-body">
                    <h5 class="card-title"><i class="fa fa-bookmark me-2"></i>Đơn hôm nay</h5>
                    <h3>${countInvoiceToday}</h3>
                </div>
            </div>
        </div>
        <div class="col-md-3 mb-3">
            <div class="card bg-info text-white shadow-sm">
                <div class="card-body">
                    <h5 class="card-title"><i class="fa fa-coins me-2"></i>Doanh thu hôm nay</h5>
                    <h3><fmt:formatNumber value="${todayRevenue}" type="number" groupingUsed="true"/>đ</h3>
                </div>
            </div>
        </div>
    </div>

    <!-- ======= Sơ đồ bàn ======= -->
    <div class="card-header">
        <h4 class="mb-3"><i class="fa fa-map me-2"></i>Sơ đồ bàn</h4>
    </div>

    <div class="card-body">
        <div class="floor-plan position-relative border rounded bg-light"
             style="width: 100%; height: 632px; overflow: auto;">
            <c:forEach var="table" items="${tables}">
                <div class="table-item position-absolute fw-bold d-flex align-items-center justify-content-center text-white flex-column bg-${table.status.badge}"
                     style="
                             left:${table.pos_x * 10}px;
                             top:${table.pos_y * 10}px;
                             width:${table.width * 10}px;
                             height:${table.height * 10}px;
                             line-height:${table.height}px;
                             border-radius:10px;
                             cursor: pointer;
                             border: 2px solid #fff;
                             "
                     title="Bàn: ${table.name}&#10;Trạng thái: ${table.status.label}"
                     data-bs-toggle="modal"
                     data-bs-target="#tableModal"
                     data-id="${table.id}"
                     data-name="${table.name}"
                     data-status="${table.status.code}">
                    <h4>${table.name}</h4>
                    <p>${table.status.label}</p>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Modal thông tin bàn -->
<div class="modal fade" id="tableModal" tabindex="-1" aria-labelledby="tableModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header text-white">
                <h5 class="modal-title" id="tableModalLabel">Chi tiết bàn</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>

            <div class="modal-body">
                <div id="loadingSpinner" class="text-center py-2" style="display:none;">
                    <div id="lottieLoading" style="width: 200px; height: 200px; margin: 0 auto;"></div>
                </div>

                <div id="orderDetailsContainer" style="display:none;">
                    <table class="table table-bordered align-middle">
                        <thead class="table-light">
                        <tr>
                            <th>#</th>
                            <th>Món ăn</th>
                            <th>Số lượng</th>
                            <th>Giá</th>
                            <th>Tổng</th>
                        </tr>
                        </thead>
                        <tbody id="orderDetailsBody">
                        <!-- Dữ liệu AJAX sẽ chèn vào đây -->
                        </tbody>
                    </table>

                    <div class="text-end mt-3">
                        <h5><strong>Tổng cộng:</strong> <span id="orderTotal">0</span> ₫</h5>
                    </div>
                </div>
            </div>

            <div id="modalFooter" class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal xác nhận thanh toán -->
<div class="modal fade" id="confirmPaymentModal" tabindex="-1" aria-labelledby="confirmPaymentLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-warning text-white">
                <h5 class="modal-title" id="confirmPaymentLabel">Xác nhận thanh toán</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Đóng"></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <form class="col-md-6" id="confirmPaymentForm" method="POST"
                          action="/dashboard?action=complete-order">
                        <!-- Nhập số điện thoại khách hàng -->
                        <div class="mb-3 position-relative">
                            <label for="customerPhone" class="mb-2 fw-bold">
                                Số điện thoại khách hàng
                                <a onclick="toggleFormAddCustomer(event)" id="toggleCreateCustomer" class="text-decoration-none ms-2" href="#">Tạo mới</a>
                            </label>
                            <input type="text" id="customerPhone" name="phone" class="form-control"
                                   placeholder="Dùng để tích điểm hoặc giảm giá nếu có."
                                   style="background-image: none !important;" oninput="onInputCustomerPhone(event)">
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
                                       value="earn" onchange="onChangeEarnOption()"
                                       checked>
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
                <button id="printBtn" class="btn btn-primary" onclick="printBill()">🖨️ In hóa đơn</button>
                <button type="submit" class="btn btn-warning" id="btnConfirmPayment" form="confirmPaymentForm">
                    Xác nhận
                </button>
            </div>
        </div>
    </div>
</div>
