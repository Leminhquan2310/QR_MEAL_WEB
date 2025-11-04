<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- head: below existing links -->
<link rel="stylesheet"
      href="https://cdn.jsdelivr.net/gh/habibmhamadi/multi-select-tag@4.0.1/dist/css/multi-select-tag.min.css">
<div class="container mt-3">
    <div class="card shadow-sm p-4">
        <form action="/menu" method="post" id="menuForm" class="row">
            <input type="hidden" name="action" value="create">

            <!-- Tên Menu -->
            <div class="mb-3 col-md-4">
                <label class="form-label fw-bold">Tên Menu</label>
                <input type="text" name="name" class="form-control" required>
            </div>

            <!-- Danh sách Product  -->
            <div class="mb-3 col-md-8">
                <label class="form-label fw-bold">Chọn Sản phẩm</label>
                <select name="products" id="products" multiple>
                    <c:forEach var="p" items="${products}">
                        <option value="${p.id}"> ${p.name} </option>
                    </c:forEach>
                </select>
            </div>

            <!-- Mô tả -->
            <div class="mb-3 col-md-12">
                <label class="form-label fw-bold">Mô tả</label>
                <textarea name="description" class="form-control" rows="2"></textarea>
            </div>

            <!-- Submit -->
            <div class="col-md">
                <button type="submit" class="btn btn-primary">Lưu Menu</button>
                <a href="/menu?action=menu" class="btn btn-secondary">Huỷ</a>
            </div>
        </form>
    </div>
</div>

<!-- End of <body> -->
<script src="https://cdn.jsdelivr.net/gh/habibmhamadi/multi-select-tag@4.0.1/dist/js/multi-select-tag.min.js"></script>