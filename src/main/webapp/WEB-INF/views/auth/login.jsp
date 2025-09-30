<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - QR Meal</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login.css">
</head>
<body>
<div class="login-container">
    <div class="login-box">
        <!-- Logo QR Meal -->
        <div class="logo">
            <img src="${pageContext.request.contextPath}/resources/img/logo.png" alt="QR Meal Logo">
        </div>

        <!-- Form đăng nhập -->
        <form action="${pageContext.request.contextPath}/auth" method="post">
            <div class="input-group">
                <label for="phone">Số điện thoại</label>
                <input type="text" id="phone" name="phone" required>
            </div>
            <div class="input-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn">Đăng nhập</button>
        </form>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
    </div>
</div>
</body>
</html>
