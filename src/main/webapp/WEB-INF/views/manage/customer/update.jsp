<%--
  Created by IntelliJ IDEA.
  User: DELL
  Date: 10/26/2025
  Time: 8:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="customer" method="post" class="w-50">
    <input type="hidden" name="id" value="${customer.id}">
    <div class="mb-3">
        <label class="form-label">Tên khách hàng</label>
        <input type="text" name="name" class="form-control" value="${customer.name}" required>
    </div>
    <div class="mb-3">
        <label class="form-label">Số điện thoại</label>
        <input type="text" name="phone" class="form-control" value="${customer.phone}" required>
    </div>
    <div class="mb-3">
        <label class="form-label">Email</label>
        <input type="email" name="email" class="form-control" value="${customer.email}">
    </div>

    <button type="submit" class="btn btn-success">Lưu</button>
    <a href="customer" class="btn btn-secondary">Hủy</a>
</form>