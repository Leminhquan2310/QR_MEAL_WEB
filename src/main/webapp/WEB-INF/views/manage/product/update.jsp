<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="card shadow-sm">
    <div class="card-body">
        <form action="/product?action=update" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${product.id}">
            <div class="row g-3">
                <!-- Tên sản phẩm -->
                <div class="col-md-6">
                    <label for="name" class="form-label fw-semibold">Tên sản phẩm</label>
                    <input type="text" class="form-control" id="name" name="name" value="${product.name}"
                           placeholder="Nhập tên sản phẩm" required>
                </div>

                <!-- Giá -->
                <div class="col-md-6">
                    <label for="price" class="form-label fw-semibold">Giá (VNĐ)</label>
                    <input type="number" class="form-control" id="price" name="price" value="${product.price}"
                           placeholder="Ví dụ: 250000" min="0" required>
                </div>

                <!-- Mô tả -->
                <div class="col-12">
                    <label for="description" class="form-label fw-semibold">Mô tả</label>
                    <textarea class="form-control" id="description" name="description" rows="3"
                              placeholder="Mô tả chi tiết sản phẩm...">${product.description}</textarea>
                </div>

                <!-- Trạng thái -->
                <div class="col-md-6">
                    <label for="status" class="form-label fw-semibold">Trạng thái</label>
                    <select class="form-select" id="status" name="status" required>
                        <c:forEach items="${statuses}" var="status">
                            <option ${status.code == product.status.code ? "selected" : ""}
                                    value="${status.code}">${status.description}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Danh mục -->
                <div class="col-md-6">
                    <label for="category" class="form-label fw-semibold">Danh mục</label>
                    <select class="form-select" id="category" name="category_id" required>
                        <c:forEach var="cate" items="${categories}">
                            <option ${cate.id == product.category.id ? "selected" : ""} value="${cate.id}">${cate.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <!-- Ảnh -->
                <div class="col-md-6">
                    <label for="image" class="form-label fw-semibold">Ảnh sản phẩm</label>

                    <!-- Input file -->
                    <input type="file" class="form-control" id="image" name="image" accept="image/*"
                           onchange="previewFile(this)">

                    <!-- Khung preview -->
                    <div class="d-flex justify-content-center align-items-center border rounded p-2 mt-2"
                         style="width: 100%; max-width: 200px; height: 200px; overflow: hidden;">
                        <img id="previewImage"
                             src="data:image/gif;base64,${product.image}"
                             alt="Image Preview"
                             class="img-fluid"
                             style="object-fit: contain; max-width: 100%; max-height: 100%;"
                             onerror="this.onerror=null;this.src='/resources/img/food_placeholder.png'">
                    </div>
                </div>

                <!-- Thời gian chế biến -->
                <div class="col-md-6">
                    <label for="cooking_time" class="form-label fw-semibold">Thời gian chế biến
                        (phút)</label>
                    <input type="number" class="form-control" id="cooking_time" name="cooking_time" min="0"
                          value="${product.cooking_time}"  placeholder="Ví dụ: 15">
                </div>
            </div>

            <!-- Buttons -->
            <div class="col-12 d-flex justify-content-end mt-3">
                <button type="submit" class="btn btn-success me-2">💾 Lưu</button>
                <a href="/product" class="btn btn-secondary">↩ Hủy</a>
            </div>
        </form>
    </div>
</div>

