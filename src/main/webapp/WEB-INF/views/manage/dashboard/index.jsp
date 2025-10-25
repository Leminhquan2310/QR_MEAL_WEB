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
            </div>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bodymovin/5.10.2/lottie.min.js"></script>
