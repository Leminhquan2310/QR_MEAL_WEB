package com.qr_meal_web.controller;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Table;
import com.qr_meal_web.service.InvoiceService;
import com.qr_meal_web.service.OrderService;
import com.qr_meal_web.service.TableService;
import com.qr_meal_web.service.impl.InvoiceServiceImpl;
import com.qr_meal_web.service.impl.OrderServiceImpl;
import com.qr_meal_web.service.impl.TableServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
public class DashboardServlet extends HttpServlet {
    private final TableService tableService = new TableServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final InvoiceService invoiceService = new InvoiceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            default -> showDashboardIndex(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "update-status":
                handleChangeOrderStatus(req, resp);
                break;
            case "complete-order":
                handleCompleteOrder(req, resp);
                break;
        }
    }

    private void showDashboardIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Table> tables = tableService.getListTable();

        int freeTables = (int) tables.stream().filter(table -> table.getStatus().getCode() == 1).count();
        int servingTables = (int) tables.stream().filter(table -> table.getStatus().getCode() == 2).count();
        double todayRevenue = invoiceService.getTodayRevenue();
        int countInvoiceToday = invoiceService.getCountInvoiceToday();
        request.setAttribute("pageTitle", "Tổng quan");
        request.setAttribute("pageActive", "dashboard");
        request.setAttribute("pageContent", "../dashboard/index.jsp");
        request.setAttribute("tables", tables);
        request.setAttribute("freeTables", freeTables);
        request.setAttribute("servingTables", servingTables);
        request.setAttribute("todayRevenue", todayRevenue);
        request.setAttribute("countInvoiceToday", countInvoiceToday);
        request.setAttribute("pageCss", "/resources/css/dashboard.css");
        request.setAttribute("pageJs", "/resources/js/dashboard.js");
        request.getRequestDispatcher("/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

//    post

    private void handleChangeOrderStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        String note = request.getParameter("note");
        HttpSession session = request.getSession();
        Employee account = (Employee) session.getAttribute("account");
        boolean isUpdated = orderService.changeOrderStatus(id, status, account, note);
        if (isUpdated) {
            session.setAttribute("message", "Cập nhật thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại - vui lòng thử lại!");
            session.setAttribute("status", "error");
        }
        showDashboardIndex(request, response);
    }

    private void handleCompleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        double discount = Double.parseDouble(request.getParameter("discount"));
        String paymentMethod = request.getParameter("paymentMethod");
        HttpSession session = request.getSession();
        Employee employee = (Employee) session.getAttribute("account");
        boolean isSuccess = orderService.completeOrder(id, employee, discount, paymentMethod);
        if (isSuccess) {
            session.setAttribute("message", "Cập nhật thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại - vui lòng thử lại!");
            session.setAttribute("status", "error");
        }
        showDashboardIndex(request, response);
    }
}
