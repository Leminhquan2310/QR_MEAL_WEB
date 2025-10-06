<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
    <%-- Button thêm loại sản phẩm --%>
    <div class="col-md-2 d-grid mb-2">
        <a href="/category?action=create" class="btn btn-primary">➕ Thêm mới</a>
    </div>
    <div class="card shadow-sm mb-3">
        <div class="card-body">
            <form action="/category" method="get" class="row g-3 align-items-end">
                <input type="hidden" name="action" value="filters">
                <div class="col-md-3">
                    <label for="keyword" class="form-label fw-semibold">Tên loại</label>
                    <input type="text" class="form-control custom-input" value="${keyword}" id="keyword" name="keyword"
                           placeholder="Nhập tên hoặc mô tả...">
                </div>

                <div class="col-md-3">
                    <label for="status" class="form-label fw-semibold">Trạng thái</label>
                    <select id="status" name="status" class="form-select custom-input">
                        <option value="2" }>-- Tất cả --</option>
                        <option value="1" ${status == 1 ? "selected" : ""}>Hoạt động</option>
                        <option value="0" ${status == 0 ? "selected" : ""}>Ngừng hoạt động</option>
                    </select>
                </div>

                <div class="col-md-3">
                    <label for="createdFrom" class="form-label fw-semibold">Từ ngày</label>
                    <input type="date" class="form-control custom-input" value="${createdFrom}" id="createdFrom"
                           name="createdFrom">
                </div>

                <div class="col-md-3">
                    <label for="createdTo" class="form-label fw-semibold">Đến ngày</label>
                    <input type="date" class="form-control custom-input" value="${createdTo}" id="createdTo"
                           name="createdTo">
                </div>

                <div class="col-md-12 d-flex justify-content-end gap-2 mt-3">
                    <button type="submit" class="btn btn-info d-flex align-items-center shadow-sm">
                        <i class="fa-solid fa-filter"></i> Lọc
                    </button>
                    <a href="/category" class="btn btn-secondary d-flex align-items-center px-3 shadow-sm">
                        <i class="fa-solid fa-trash"></i> Reset
                    </a>
                </div>
            </form>
        </div>
    </div>


    <!-- Bảng danh sách loại sản phẩm -->
    <div class="card shadow-sm">
        <div class="card-body">
            <h5 class="card-title mb-3">📋 Danh sách loại sản phẩm</h5>
            <table class="table table-bordered table-hover align-middle text-center" id="categoryTable">
                <thead class="table-primary">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Icon</th>
                    <th scope="col">Tên loại</th>
                    <th scope="col">Mô tả</th>
                    <th scope="col">Ngày tạo</th>
                    <th scope="col">Trạng thái</th>
                    <th scope="col" style="width: 15%;">Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${categories}" var="cate">
                    <tr>
                        <td>${cate.id}</td>
                        <td style="font-size: 22px;">${cate.icon}</td>
                        <td>${cate.name}</td>
                        <td>${cate.description}</td>
                        <td><fmt:formatDate value="${cate.created_at}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td>
                            <span class="badge bg-${cate.status.badge}">${cate.status.label}</span>
                        </td>
                        <td>
                            <a href="/category?action=update&id=${cate.id}" class="btn btn-sm btn-warning me-2">✏
                                Sửa</a>
                            <button onclick="handleDelCategory(${cate.id})" class="btn btn-sm btn-danger">🗑 Xóa</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

