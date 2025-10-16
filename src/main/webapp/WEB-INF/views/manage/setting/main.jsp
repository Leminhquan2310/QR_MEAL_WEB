<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<div class="container mt-4" style="max-width: 500px;">
    <h5 class="mb-3 fw-bold text-center">⚙️ Cấu hình tài khoản ngân hàng</h5>

    <form action="/admin/bank-account" method="post" enctype="multipart/form-data"
          class="card shadow-sm p-3 rounded-3 border-0">
        <!-- Mã ngân hàng -->
        <div class="mb-2">
            <label for="bankName" class="form-label fw-medium small">Mã ngân hàng</label>
            <input type="text" class="form-control form-control-sm" id="bankName" name="bank_name"
                   placeholder="VD: Vietcombank" value="${bankAccount.bank_code}">
        </div>

        <!-- Tên ngân hàng -->
        <div class="mb-2">
            <label for="bankName" class="form-label fw-medium small">Tên ngân hàng</label>
            <input type="text" class="form-control form-control-sm" id="bankName" name="bank_name"
                   placeholder="VD: Vietcombank" value="${bankAccount.bank_name}">
        </div>

        <!-- Tên chủ tài khoản -->
        <div class="mb-2">
            <label for="accountName" class="form-label fw-medium small">Tên chủ tài khoản</label>
            <input type="text" class="form-control form-control-sm" id="accountName" name="account_name"
                   placeholder="VD: CÔNG TY TNHH ABC" value="${bankAccount.account_name}">
        </div>

        <!-- Số tài khoản -->
        <div class="mb-2">
            <label for="accountNumber" class="form-label fw-medium small">Số tài khoản</label>
            <input type="text" class="form-control form-control-sm" id="accountNumber" name="account_number"
                   placeholder="VD: 0123456789" value="${bankAccount.account_number}">
        </div>

        <!-- Trạng thái -->
        <div class="mb-3">
            <label for="status" class="form-label fw-medium small">Trạng thái</label>
            <select id="status" name="status" class="form-select form-select-sm">
                <c:forEach var="status" items="${statuses}">
                <option value="${status.code}" ${bankAccount.status.code == status.code ? 'selected' : ''}>${status.label}</option>
                </c:forEach>
            </select>
        </div>

        <!-- Nút lưu -->
        <div class="text-center">
            <button type="submit" class="btn btn-primary btn-sm px-3">
                <i class="bi bi-save"></i> Lưu thay đổi
            </button>
        </div>
    </form>
</div>
