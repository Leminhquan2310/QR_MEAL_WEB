<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
    /* ép radio trong SweetAlert2 hiển thị dọc */
    .swal2-radio {
        display: flex !important;
        flex-direction: column !important; /* sắp xếp dọc */
        align-items: flex-start !important; /* căn trái */
        padding-left: 20px;
    }

    .swal2-radio label {
        margin: 6px 0;
    }
</style>
<c:if test="${sessionScope.showBoxDelete}">
    <script>
        const showBoxDelete =
        ${sessionScope.showBoxDelete == true}
        // Gán boolean từ server sang JS (nếu null thì coi như false)
        const canDelete = ${sessionScope.canDelete == true};
    </script>
    <!-- Xóa flag sau khi dùng để F5 không show lại -->
    <c:remove var="showBoxDelete" scope="session"/>
</c:if>
<div class="container">
    <%--  Button thêm sản phẩm  --%>
    <div class="col-md-2 d-grid mb-2">
        <a href="/product?action=create" class="btn btn-primary">➕ Thêm sản phẩm</a>
    </div>

    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <form method="get" action="/product" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="filters">
                <div class="col-md-2">
                    <label class="form-label">Từ khóa</label>
                    <input type="text" name="keyword" class="form-control" value="${filters.keyword}"
                           placeholder="Nhập tên / mô tả">
                </div>

                <!-- Price range -->
                <div class="col-md-2">
                    <label class="form-label">Giá từ</label>
                    <input type="number" name="minPrice" class="form-control"
                           value="${filters.minPrice > 0 ? filters.minPrice : ""}" placeholder="Nhập giá từ">
                </div>

                <div class="col-md-2">
                    <label class="form-label">đến</label>
                    <input type="number" name="maxPrice" class="form-control"
                           value="${filters.maxPrice > 0 ? filters.maxPrice : ""}" placeholder="Nhập giá đến">
                </div>

                <!-- Category -->
                <div class="col-md-3">
                    <label class="form-label">Loại</label>
                    <select name="category" class="form-select">
                        <option value="-1">-- Tất cả --</option>
                        <c:forEach var="cate" items="${categories}">
                            <option value="${cate.id}" ${filters.category == cate.id ? "selected" : ""}>${cate.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Status -->
                <div class="col-md-3">
                    <label class="form-label">Trạng thái</label>
                    <select name="status" class="form-select">
                        <option value="-1"}>-- Tất cả --</option>
                        <c:forEach var="status" items="${statuses}">
                            <option value="${status.code}" ${filters.status == status.code ? "selected" : ""}>${status.description}</option>
                        </c:forEach>
                    </select>
                </div>


                <div class="col-md-12 d-flex justify-content-end gap-2 mt-3">
                    <button type="submit" class="btn btn-info d-flex align-items-center shadow-sm">
                        <i class="fa-solid fa-filter"></i> Lọc
                    </button>
                    <a href="/product" class="btn btn-secondary d-flex align-items-center px-3 shadow-sm">
                        <i class="fa-solid fa-trash"></i> Reset
                    </a>
                </div>
            </form>
        </div>
    </div>


    <!-- Bảng danh sách nhân viên -->
    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách sản phẩm</h5>
            <table class="table table-bordered align-middle text-center" id="categoryTable">
                <thead class="table-primary">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Ảnh</th>
                    <th scope="col">Tên</th>
                    <th scope="col">Mô tả</th>
                    <th scope="col">Giá</th>
                    <th scope="col">Loại sản phẩm</th>
                    <th scope="col">Thời gian</th>
                    <th scope="col">Trạng thái</th>
                    <th scope="col" style="width: 15%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="pro">
                    <tr>
                        <td>${pro.id}</td>
                        <td>
                            <img src="data:image/gif;base64,${pro.image}" alt="${pro.name}" class="img-thumbnail"
                                 style="width: 70px; height: 70px; object-fit: contain;">
                        </td>
                        <td>${pro.name}</td>
                        <td>${pro.description}</td>
                        <td><fmt:formatNumber value="${pro.price}" type="number" groupingUsed="true"/> vnđ</td>
                        <td>${pro.category.name}</td>
                        <td>${pro.cooking_time} phút</td>
                        <td><span class="badge bg-${pro.status.badge}">${pro.status.description}</span></td>
                        <td>
                            <a href="/product?action=update&id=${pro.id}" class="btn btn-sm btn-warning me-2">✏ Sửa</a>
                            <a onclick="showDeleteAlert(${pro.id})" class="btn btn-sm btn-danger">🗑 Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>