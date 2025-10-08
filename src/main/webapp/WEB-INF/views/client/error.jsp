<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lỗi hệ thống</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="/resources/css/error.css">
</head>
<body>
<div class="error-box">
    <i class="bi bi-exclamation-triangle-fill error-icon"></i>
    <h4 class="fw-bold text-danger mb-3">Đã xảy ra lỗi</h4>

    <%
        String msg = request.getParameter("msg");
        String message;
        if ("missing_table".equals(msg)) {
            message = "Không tìm thấy thông tin bàn. Vui lòng quét lại mã QR để tiếp tục.";
        } else if ("unauthorized".equals(msg)) {
            message = "Bạn không có quyền truy cập trang này.";
        } else {
            message = "Có lỗi xảy ra trong quá trình xử lý. Vui lòng thử lại sau.";
        }
    %>

    <p class="text-secondary mb-4"><%= message %>
    </p>
</div>
</body>
</html>
