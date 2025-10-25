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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css"
          integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
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
        <a href="dashboard" class="${pageActive == 'dashboard' ? 'active' : ''}">📊 Dashboard</a>
        <a href="employee?page=1" class="${pageActive == 'employee' ? 'active' : ''}">👤 Quản lý nhân viên</a>
        <a href="table?page=1" class="${pageActive == 'table' ? 'active' : ''}">🪟 Quản lý bàn</a>
        <a href="product?page=1" class="${pageActive == 'product' ? 'active' : ''}">🍜 Sản phẩm</a>
        <a href="category?page=1" class="${pageActive == 'category' ? 'active' : ''}">🥗 Phân loại sản phẩm</a>
        <a href="menu.jsp?page=1" class="${pageActive == 'menu' ? 'active' : ''}">📋 Menu</a>
        <a href="order?page=1" class="${pageActive == 'order' ? 'active' : ''}">🧾 Đơn hàng</a>
        <a href="/setting" class="${pageActive == 'setting' ? 'active' : ''}">⚙️ Cấu hình</a>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageJs}"></script>
</body>
</html>
