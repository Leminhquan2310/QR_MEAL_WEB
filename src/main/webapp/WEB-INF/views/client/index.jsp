<%@ page import="com.qr_meal_web.service.CartService" %>
<%@ page import="com.qr_meal_web.service.impl.CartServiceImpl" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cửa hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/libs/bootstrap/css/bootstrap.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="resources/css/client.css">
</head>
<body>

<!-- Thông báo -->
<c:if test="${not empty message}">
    <script>
        window.addEventListener("DOMContentLoaded", function () {
            const message = "${sessionScope.message}";
            const status = "${sessionScope.status}"; // success | error | warning

            Swal.fire({
                title: status === "success" ? "Thành công!" : "Thất bại!",
                text: message,
                icon: status
            });
        });
    </script>

    <c:remove var="message" scope="session"/>
    <c:remove var="status" scope="session"/>
</c:if>

<div class="container py-5">
    <h2 class="text-center mb-4 fw-bold">Sản phẩm nổi bật</h2>
    <!-- CATEGORY -->
    <div class="category-bar">
        <a href="${pageContext.request.contextPath}/client"
           class="category-btn ${empty param.category ? 'active' : ''}">Tất cả</a>
        <c:forEach var="c" items="${categories}">
            <a href="${pageContext.request.contextPath}/client?category=${c.id}"
               class="category-btn ${param.category == c.id ? 'active' : ''}">${c.icon} ${c.name}</a>
        </c:forEach>
    </div>

    <!-- PRODUCTS -->
    <div class="row row-cols-2 row-cols-md-3 row-cols-lg-4 g-3">
        <c:forEach var="p" items="${products}">
            <c:choose>
                <c:when test="${p.status.code == 1 || p.status.code == 2}">
                    <div class="col">
                        <div class="card product-card p-2 h-100
                         ${p.status.code == 2 ? 'out-of-stock' : ''}">
                            <div class="position-relative">
                                <img src="${p.image}" class="card-img-top product-img" alt="${p.name}"/>

                                <!-- OVERLAY HIỂN THỊ KHI HẾT HÀNG -->
                                <c:if test="${p.status.code == 2}">
                                    <div class="product-overlay">Hết hàng</div>
                                </c:if>
                            </div>

                            <div class="card-body text-center">
                                <h6 class="card-title fw-bold">${p.name}</h6>
                                <p class="text-danger fw-semibold mb-2">
                                    <fmt:formatNumber value="${p.price}"/> ₫
                                </p>

                                <!-- HIỂN THỊ NÚT THEO TRẠNG THÁI -->
                                <button type="submit" data-id="${p.id}" data-name="${p.name}" data-price="${p.price}"
                                        class="btn btn-${p.status.code != 1 ? "secondary" : "add"} w-100 btn-add-cart" ${p.status.code != 1 ? "disabled" : ""}>
                                    Thêm vào giỏ
                                </button>
                            </div>
                        </div>
                    </div>
                </c:when>
            </c:choose>
        </c:forEach>
    </div>
</div>

<%
    CartService cart = (CartService) request.getSession().getAttribute("cart");
    if (cart == null) {
        cart = new CartServiceImpl();
        session.setAttribute("cart", cart);
    }
%>

<!-- FLOATING CART (PC) -->
<a href="#" class="cart-floating" id="cart-icon" data-bs-toggle="modal" data-bs-target="#cartModal">
    <i class="bi bi-cart3 fs-4"></i>
    <span class="cart-count" id="cart-count">${cart.totalQuantity}</span>
</a>

<!-- MOBILE CART BAR -->
<div class="cart-bar" id="cart-icon-mobile" data-bs-toggle="modal" data-bs-target="#cartModal">
    <div class="cart-info">
        <i class="bi bi-cart3 fs-4"></i>
        SL: <span class="cart-count">${cart.totalQuantity}</span>
        | Tổng: <span><fmt:formatNumber value="${cart.totalAmount}"/> ₫</span>
    </div>
    <a href="#" class="btn-view">Gọi món</a>
</div>

<!-- MODAL -->
<div class="modal fade cart-modal" id="cartModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title"><i class="bi bi-cart3 me-2"></i> Giỏ hàng</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body" id="modal-cart">
                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="pills-cart-tab" data-bs-toggle="pill"
                                data-bs-target="#pills-cart" type="button" role="tab" aria-controls="pills-cart"
                                aria-selected="true">Giỏ hàng
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="pills-ordered-tab" data-bs-toggle="pill"
                                data-bs-target="#pills-ordered" type="button" role="tab" aria-controls="pills-ordered"
                                aria-selected="false">Món đã gọi
                        </button>
                    </li>
                </ul>


                <div class="tab-content" id="pills-tabContent">
                    <%-- Giỏ hàng--%>
                    <div class="tab-pane fade show active" id="pills-cart" role="tabpanel"
                         aria-labelledby="pills-cart-tab">
                        <div id="cart-container" class="${empty cart.items ? 'd-none' : ''}">
                            <c:forEach var="item" items="${cart.items}">
                                <div class="cart-item position-relative d-flex align-items-center justify-content-between p-2 border-bottom">
                                    <!-- Nút xóa tinh tế -->
                                    <button type="button" class="btn-remove" data-id="${item.product.id}"
                                            title="Xóa sản phẩm">
                                        <i class="bi bi-x-circle-fill"></i>
                                    </button>

                                    <!-- Ảnh sản phẩm -->
                                    <img src="${item.product.image}" alt="${item.product.name}" class="rounded"
                                         style="width:60px; height:60px; object-fit:cover;">

                                    <!-- Tên + đơn giá -->
                                    <div class="flex-grow-1 ms-3">
                                        <div class="fw-semibold">${item.product.name}</div>
                                        <div class="text-muted small">
                                            Đơn giá: <fmt:formatNumber value="${item.product.price}"/> ₫
                                        </div>
                                    </div>

                                    <!-- Thành tiền + số lượng -->
                                    <div class="text-danger fw-bold ms-3">
                                        <div class="d-flex justify-content-end">
                                            <fmt:formatNumber value="${item.product.price * item.quantity}"/> ₫
                                        </div>

                                        <!-- Input số lượng -->
                                        <div class="input-group input-group-sm mt-1 quantity-group"
                                             style="max-width:120px;">
                                            <button class="btn btn-outline-secondary btn-decrease"
                                                    data-id="${item.product.id}"
                                                    type="button">-
                                            </button>
                                            <input type="text" class="form-control text-center quantity-input"
                                                   value="${item.quantity}" data-id="${item.product.id}" readonly>
                                            <button class="btn btn-outline-secondary btn-increase"
                                                    data-id="${item.product.id}"
                                                    type="button">+
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>

                            <div class="text-end mt-3">
                                <h6 class="fw-bold">
                                    Tổng cộng:
                                    <span class="text-danger"><fmt:formatNumber
                                            value="${cart.totalAmount}"/> ₫</span>
                                </h6>
                            </div>
                        </div>

                        <!-- Khi giỏ trống -->
                        <p class="text-center text-muted my-4 ${not empty cart.items ? 'd-none' : ''}" id="cart-empty">
                            Giỏ hàng của bạn đang trống.
                        </p>
                    </div>


                    <!-- DANH SÁCH MÓN ĐÃ GỌI -->
                    <div class="tab-pane fade" id="pills-ordered" role="tabpanel" aria-labelledby="pills-ordered-tab">
                        <div id="ordered-list-container" class="${empty orderDetails ? 'd-none' : ''}">
                            <c:forEach var="item" items="${orderDetails}">
                                <div class="ordered-item border-bottom d-flex align-items-center justify-content-between p-2">
                                    <!-- Ảnh món -->
                                    <img src="${item.product.image}" alt="${item.product.name}" class="rounded"
                                         style="width:60px; height:60px; object-fit:cover;">

                                    <!-- Tên + giá -->
                                    <div class="flex-grow-1 ms-3">
                                        <div class="fw-semibold">${item.product.name}</div>
                                        <div class="text-muted small">
                                            Đơn giá: <fmt:formatNumber value="${item.product.price}"/> ₫
                                        </div>
                                    </div>

                                    <!-- Số lượng + trạng thái -->
                                    <div class="text-end">
                                        <div>Số lượng: <strong>${item.quantity}</strong></div>
                                    </div>

                                    <!-- Thành tiền -->
                                    <div class=" fw-bold text-danger ms-3">
                                        <fmt:formatNumber value="${item.product.price * item.quantity}"/> ₫
                                    </div>
                                </div>
                            </c:forEach>

                            <!-- Tổng cộng -->
                            <div class="text-end mt-3">
                                <h6 class="fw-bold">
                                    Tổng cộng:
                                    <span class="text-danger">
                                        <fmt:formatNumber value="${totalAmount}"/> ₫
                                    </span>
                                </h6>
                            </div>
                        </div>

                        <!-- Khi không có món nào đã gọi -->
                        <p class="text-center text-muted my-4 ${not empty orderDetails ? 'd-none' : ''}"
                           id="ordered-empty">
                            Bạn chưa gọi món nào.
                        </p>

                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                <form action="${pageContext.request.contextPath}/order" method="POST">
                    <input type="hidden" name="action" value="create">
                    <button type="submit" class="btn btn-success disabled" id="btn-checkout">Gọi món</button>
                </form>
            </div>
        </div>
    </div>
</div>



<script src="${pageContext.request.contextPath}/resources/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="resources/js/client.js"></script>
</body>
</html>
