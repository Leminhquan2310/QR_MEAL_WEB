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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <c:if test="${not empty pageCss}">
        <link href="${pageCss}" rel="stylesheet">
    </c:if>
    <style>
        body {
            min-height: 100vh;
            background: #f0f9ff;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #333;
        }

        .navbar {
            background: linear-gradient(90deg, #4facfe, #00f2fe);
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
            padding: 0.6rem 1rem;
        }

        .navbar-brand {
            font-weight: 700;
            font-size: 1.4rem;
            color: #fff;
            letter-spacing: 0.5px;
        }

        .account-info {
            line-height: 1.2;
            color: #fff;
        }

        .account-info .role {
            font-size: 0.95rem;
            font-weight: 600;
        }

        .account-info .role span {
            color: #ffeb3b; /* nổi bật vai trò */
        }

        .account-info .username {
            font-size: 0.8rem;
            color: rgba(255, 255, 255, 0.85);
            font-style: italic;
        }

        .btn-sm {
            border-radius: 20px;
            padding: 4px 14px;
            font-size: 0.85rem;
            transition: all 0.3s ease;
        }

        .btn-sm:hover {
            background-color: #f1f1f1;
            box-shadow: 0 0 8px rgba(255, 255, 255, 0.5);
        }


        .sidebar {
            width: 240px;
            background: #e0f7fa;
            min-height: 100vh;
            padding-top: 15px;
            border-right: 1px solid #cfd8dc;
        }

        .sidebar a {
            color: #006064;
            text-decoration: none;
            display: block;
            padding: 12px 18px;
            margin: 5px 10px;
            border-radius: 8px;
            transition: background 0.3s, transform 0.2s;
        }

        .sidebar a:hover {
            background: #b2ebf2;
            transform: translateX(4px);
        }

        .sidebar .active {
            background: #4dd0e1;
            color: white;
            font-weight: 600;
        }

        .content {
            flex: 1;
            padding: 25px;
            background: #f8fbfc;
        }

        .footer {
            text-align: center;
            padding: 12px;
            background: #e0f7fa;
            border-top: 1px solid #cfd8dc;
            font-size: 0.9rem;
            color: #555;
        }

        .btn {
            border: none !important;
            border-radius: 8px !important;
            font-weight: 600;
            letter-spacing: 0.5px;
            transition: all 0.3s ease-in-out;
            position: relative;
            overflow: hidden;
        }

        /* Hiệu ứng glow khi hover */
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 0 15px rgba(0, 255, 255, 0.5);
        }


        /* Primary */
        .btn-primary {
            background: linear-gradient(135deg, #00f5ff, #0077ff);
            color: #fff;
        }

        .btn-primary:hover {
            background: linear-gradient(135deg, #3399ff, #0066ff);
            box-shadow: 0 0 20px rgba(0, 123, 255, 0.7);
        }

        /* Success */
        .btn-success {
            background: linear-gradient(135deg, #28a745, #1e7e34);
            color: #fff;
        }

        .btn-success:hover {
            background: linear-gradient(135deg, #4cd964, #1e7e34);
            box-shadow: 0 0 20px rgba(40, 167, 69, 0.7);
        }

        /* Danger */
        .btn-danger {
            background: linear-gradient(135deg, #dc3545, #a71d2a);
            color: #fff;
        }

        .btn-danger:hover {
            background: linear-gradient(135deg, #ff5f6d, #a71d2a);
            box-shadow: 0 0 20px rgba(220, 53, 69, 0.7);
        }

        /* Warning */
        .btn-warning {
            background: linear-gradient(135deg, #ffc107, #e0a800);
            color: #212529;
        }

        .btn-warning:hover {
            background: linear-gradient(135deg, #ffe066, #e0a800);
            box-shadow: 0 0 20px rgba(255, 193, 7, 0.7);
        }

        /* Info */
        .btn-info {
            background: linear-gradient(135deg, #17a2b8, #117a8b);
            color: #fff;
        }

        .btn-info:hover {
            background: linear-gradient(135deg, #5bc0de, #117a8b);
            box-shadow: 0 0 20px rgba(23, 162, 184, 0.7);
        }

        /* Label tinh tế hơn */
        .form-label {
            font-weight: 500; /* hơi đậm hơn bình thường */
            color: #2c3e50; /* xám xanh thay vì đen */
            letter-spacing: 0.3px; /* giãn chữ nhẹ */
            font-size: 0.95rem; /* nhỏ gọn vừa phải */
            transition: color 0.3s ease;
        }

        .form-label:hover {
            color: #1e90ff; /* đổi màu xanh nhạt khi hover */
            cursor: pointer;
        }
    </style>
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
        <a href="dashboard.jsp" class="${pageActive == 'dashboard' ? 'active' : ''}">📊 Dashboard</a>
        <a href="employee" class="${pageActive == 'employee' ? 'active' : ''}">👤 Quản lý nhân viên</a>
        <a href="products.jsp" class="${pageActive == 'products' ? 'active' : ''}">🍜 Sản phẩm</a>
        <a href="menu.jsp" class="${pageActive == 'menu' ? 'active' : ''}">📋 Menu</a>
        <a href="orders.jsp" class="${pageActive == 'orders' ? 'active' : ''}">🧾 Đơn hàng</a>
        <a href="settings.jsp" class="${pageActive == 'settings' ? 'active' : ''}">⚙️ Cấu hình</a>
    </div>

    <!-- Content -->
    <div class="content">
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
