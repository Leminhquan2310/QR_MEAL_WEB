package com.qr_meal_web.controller;

import com.qr_meal_web.dao.ITableDAO;
import com.qr_meal_web.dao.TableDAOImplement;
import com.qr_meal_web.model.Table;
import com.qr_meal_web.util.QRCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TableServlet", urlPatterns = "/table")
public class TableServlet extends HttpServlet {
    private ITableDAO tableDAO = new TableDAOImplement();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageActive", "table");
        request.setAttribute("pageJs", "/resources/js/table.js");
        request.setAttribute("pageCss", "/resources/css/table.css");
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
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
        }
    }

    private void showAllTable(HttpServletRequest request, HttpServletResponse response) {
        List<Table> tables = tableDAO.selectAllTable();
        request.setAttribute("pageTitle", "Quản lý bàn");
        request.setAttribute("pageContent", "../table/list.jsp");
        request.setAttribute("tables", tables);
    }

    private void showFiltersTable(HttpServletRequest request, HttpServletResponse response) {
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");
        Map<String, Object> filters = new HashMap<>();
        filters.put("createdFrom", createdFrom);
        filters.put("createdTo", createdTo);

        List<Table> tables = tableDAO.filtersTable(createdFrom, createdTo);
        request.setAttribute("pageTitle", "Quản lý bàn");
        request.setAttribute("pageContent", "../table/list.jsp");
        request.setAttribute("tables", tables);
        request.setAttribute("filters", filters);
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
        boolean created = tableDAO.insertTable(name);

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

        boolean isSuccess = tableDAO.updateTable(id, qr_code, name);

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
        boolean canDelete = tableDAO.checkCanDelete(id);

        String message, status;
        HttpSession session = request.getSession();
        if (canDelete) {
            boolean isSuccess = tableDAO.deleteTable(id);
            if (isSuccess) {
                message = "Xóa nhân viên thành công!";
                status = "success";
            } else {
                message = "Xóa nhân viên thất bại!";
                status = "error";
            }
        } else {
            boolean isSuccess = tableDAO.setInactive(id);
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
}
