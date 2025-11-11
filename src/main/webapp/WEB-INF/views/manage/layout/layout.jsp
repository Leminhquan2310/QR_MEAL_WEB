<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 9/26/2025
  Time: 2:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle != null ? pageTitle : 'iRestaurant'}"/></title>
    <c:if test="${not empty pageCss}">
        <link href="${pageCss}" rel="stylesheet">
    </c:if>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/libs/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/notyf@3/notyf.min.css">
    <link rel="stylesheet" href="/resources/css/layout.css">
</head>
<body>
<%-- Check đăng nhập--%>
<c:if test="${empty sessionScope.account}">
    <%
        response.sendRedirect("/auth");
    %>
</c:if>

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

<!-- Header -->
<jsp:include page="header.jsp"/>

<div class="d-flex">
    <!-- Sidebar -->
    <div class="sidebar">
        <a href="dashboard" class="${pageActive == 'dashboard' ? 'active' : ''}">📊 Tổng quan </a>
        <a href="employee?page=1" class="${pageActive == 'employee' ? 'active' : ''}">👤 Nhân viên</a>
        <a href="customer?page=1" class="${pageActive == 'customer' ? 'active' : ''}">🦸 Khách hàng</a>
        <a href="table?page=1" class="${pageActive == 'table' ? 'active' : ''}">🪟 Bàn</a>
        <a href="product?page=1" class="${pageActive == 'product' ? 'active' : ''}">🍜 Sản phẩm</a>
        <a href="category?page=1" class="${pageActive == 'category' ? 'active' : ''}">🥗 Loại sản phẩm</a>
        <a href="menu" class="${pageActive == 'menu' ? 'active' : ''}">📋 Menu</a>
        <a href="discount?page=1" class="${pageActive == 'discount' ? 'active' : ''}">🎁 Giảm giá</a>
        <a href="order?page=1" class="${pageActive == 'order' ? 'active' : ''}">🧾 Đơn hàng</a>
        <a href="setting" class="${pageActive == 'setting' ? 'active' : ''}">⚙️ Cấu hình</a>
    </div>

    <!-- Content -->
    <div class="content">
        <h1 class="page-title"><c:out value="${pageTitle}"/></h1>
        <jsp:include page="${pageContent}"/>
    </div>
</div>

<!-- Footer -->
<div class="footer">
    © 2025 iRestaurant - Hệ thống quản lý quán ăn
</div>


<div class="toast-container position-static" style="z-index: 11">
    <%--    <div id="liveToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="false">--%>
    <%--        <div class="toast-header bg-success text-white">--%>
    <%--            <i class="fa-solid fa-bell"></i>--%>
    <%--            <strong class="me-auto ps-2"> Thông báo</strong>--%>
    <%--            <small id="toast-time">11 mins ago</small>--%>
    <%--            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>--%>
    <%--        </div>--%>
    <%--        <div class="toast-body">--%>

    <%--        </div>--%>
    <%--    </div>--%>

    <div class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
            <i class="fa-solid fa-bell"></i>
            <strong class="me-auto"> Thông báo</strong>
            <small class="text-body-secondary">just now</small>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
            See? Just like this.
        </div>
    </div>

    <div class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
            <i class="fa-solid fa-bell"></i>
            <strong class="me-auto"> Thông báo</strong>
            <small class="text-body-secondary">just now</small>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
            See? Just like this.
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bodymovin/5.10.2/lottie.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/libs/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="/resources/js/utils/debounce.js"></script>
<script src="https://cdn.jsdelivr.net/npm/notyf@3/notyf.min.js"></script>
<script src="${pageJs}"></script>
<script src="/resources/js/layout.js"></script>
<script>
    let ws;
    const connectSocket = () => {
        ws = new WebSocket('ws://localhost:8080/ws/notify');

        ws.onopen = () => console.log("✅ Connected");
        ws.onmessage = async (e) => {
            const page = '${pageActive}';
            if (page === 'dashboard') {
                await fetch('/api/table')
                    .then(res => res.json())
                    .then(data => renderTables(data, JSON.parse(e.data).table_id));
            }

            showOrderToast(JSON.parse(e.data))
        };
        ws.onclose = () => {
            console.log("🔁 Reconnecting...");
            setTimeout(connectSocket, 2000);
        };
        ws.onerror = (err) => console.error("⚠️ Error:", err);
    }
    connectSocket();
</script>
</body>
</html>
