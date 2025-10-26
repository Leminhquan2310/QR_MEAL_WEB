package com.qr_meal_web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qr_meal_web.model.Table;
import com.qr_meal_web.service.TableService;
import com.qr_meal_web.service.impl.TableServiceImpl;
import com.qr_meal_web.util.QRCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TableServlet", urlPatterns = "/table")
public class TableServlet extends HttpServlet {
    private final TableService tableService = new TableServiceImpl();
    private int page = 1;
    private int limit = 10;
    private int visiblePages = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageActive", "table");
        request.setAttribute("pageJs", "/resources/js/table.js");
        request.setAttribute("pageCss", "/resources/css/table.css");
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "table-layout":
                showTableLayout(request, response);
                break;
            case "filters":
                showFiltersTable(request, response);
                break;
            case "refreshQR":
                getRefreshQRCode(request, response);
                return;
            default:
                showAllTable(request, response);
                break;
        }
        request.getRequestDispatcher("WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                handleCreateTable(request, response);
                break;
            case "update":
                handleUpdateTable(request, response);
                break;
            case "delete":
                handleDeleteTable(request, response);
                break;
            case "update-positions":
                handleUpdatePositions(request, response);
                break;
        }
    }

    private void showTableLayout(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("pageTitle", "Quản lý sơ đồ bàn");
        request.setAttribute("pageContent", "../table/table-layout.jsp");
        request.setAttribute("pageJs", "/resources/js/table-layout.js");
        request.setAttribute("pageCss", "/resources/css/table-layout.css");
    }

    private void showAllTable(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Table> tables = tableService.selectAllTable(limit, page);
        int totalTable = tableService.getCountTotal();
        int totalPages = (int) Math.ceil((double) totalTable / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        request.setAttribute("pageTitle", "Quản lý bàn");
        request.setAttribute("pageContent", "../table/list.jsp");
        request.setAttribute("tables", tables);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
    }

    private void showFiltersTable(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");
        int totalTable = tableService.getCountTotalTableFilter(createdFrom, createdTo);
        int totalPages = (int) Math.ceil((double) totalTable / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        Map<String, Object> filters = new HashMap<>();
        filters.put("createdFrom", createdFrom);
        filters.put("createdTo", createdTo);

        List<Table> tables = tableService.filtersTable(createdFrom, createdTo, limit, page);
        request.setAttribute("pageTitle", "Quản lý bàn");
        request.setAttribute("pageContent", "../table/list.jsp");
        request.setAttribute("tables", tables);
        request.setAttribute("filters", filters);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
    }

    private void getRefreshQRCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));

        // Sinh QR code mới
        String qrBase64 = QRCode.generateBase64QRCode(id);

        String json = "{ \"qrCode\": \"" + qrBase64 + "\" }";
        response.getWriter().write(json);
    }

    //    -------------- do post -----------------
    private void handleCreateTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        boolean created = tableService.insertTable(name);

        HttpSession session = request.getSession();
        if (created) {
            session.setAttribute("message", "Thêm mới thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }
        response.sendRedirect("/table");
    }

    private void handleUpdateTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String qr_code = request.getParameter("qr_code");
        String name = request.getParameter("name");

        boolean isSuccess = tableService.updateTable(id, qr_code, name);

        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Thêm mới thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }
        response.sendRedirect("/table");
    }

    private void handleDeleteTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean canDelete = tableService.checkCanDelete(id);

        String message, status;
        HttpSession session = request.getSession();
        if (canDelete) {
            boolean isSuccess = tableService.deleteTable(id);
            if (isSuccess) {
                message = "Xóa nhân viên thành công!";
                status = "success";
            } else {
                message = "Xóa nhân viên thất bại!";
                status = "error";
            }
        } else {
            boolean isSuccess = tableService.setInactive(id);
            if (isSuccess) {
                message = "Tài khoản đã được ngưng việc sử dụng!";
                status = "success";
            } else {
                message = "Ngưng sử dụng tài khoản thất bại!";
                status = "error";
            }
        }

        session.setAttribute("message", message);
        session.setAttribute("status", status);
        response.sendRedirect(request.getContextPath() + "/table");
    }

    private void handleUpdatePositions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1️⃣ Đọc dữ liệu JSON từ body request
        BufferedReader reader = request.getReader();

        // 2️⃣ Xác định kiểu dữ liệu: List<Table>
        Type listType = new TypeToken<List<Table>>() {
        }.getType();

        // 3️⃣ Chuyển JSON -> List<Table>
        List<Table> tables = new Gson().fromJson(reader, listType);

        // 5️⃣ Gọi service update
        tableService.updateTablePositions(tables);
    }
}
