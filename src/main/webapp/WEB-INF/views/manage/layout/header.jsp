<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;" language="java" pageEncoding="UTF-8" %>
<!-- Header -->
<nav class="navbar navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand">🍽️ iRestaurant</a>
        <div class="d-flex align-items-center">
            <div class="account-info me-3 text-end">
                <div class="role">Xin chào, <span class=""><c:out value="${sessionScope.account.role.name}" /></span></div>
                <div class="username">👤 <c:out value="${sessionScope.account.name}" /></div>
            </div>
            <a href="/auth" class="btn btn-light btn-sm">Đăng xuất</a>
        </div>
    </div>
</nav>