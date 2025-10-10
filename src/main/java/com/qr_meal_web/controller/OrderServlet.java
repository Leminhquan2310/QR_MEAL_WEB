package com.qr_meal_web.controller;

import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.service.OrderService;
import com.qr_meal_web.service.impl.OrderServiceImpl;
import com.qr_meal_web.util.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    private int page = 1;
    private int limit = 10;
    private int visiblePages = 5;
    private final OrderService orderService = new OrderServiceImpl();
    private List<OrderStatus> statuses = Arrays.asList(OrderStatus.values());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageActive", "order");
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "detail":
                showOrderDetail(request, response);
                break;
            case "filters":
                showFiltersOrder(request, response);
                break;
            default:
                showAllOrder(request, response);
                break;
        }

        request.getRequestDispatcher("/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                handleCreateOrder(request, response);
                break;
            case "update":
                handleUpdateOrder(request, response);
                break;
            case "delete":
                handleDeleteOrder(request, response);
                break;
            default:
                break;
        }
    }


    private void showAllOrder(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Order> orders = orderService.selectAllOrder(limit, page);
        int totalOrders = orderService.getTotalOrders();
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }

        // Gửi sang JSP
        request.setAttribute("pageTitle", "Quản lý đơn hàng");
        request.setAttribute("pageContent", "../order/list.jsp");
        request.setAttribute("pageCss", "/resources/css/order.css");
        request.setAttribute("pageJs", "/resources/js/order.js");
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("statuses", statuses);
    }

    private void showFiltersOrder(HttpServletRequest request, HttpServletResponse response) {
        int code = Helper.parseIntegerSafe(request.getParameter("code"), 0);
        int status = Integer.parseInt(request.getParameter("status"));
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");
        Map<String, Object> filters = new HashMap<>();
        filters.put("code", code);
        filters.put("status", status);
        filters.put("createdFrom", createdFrom);
        filters.put("createdTo", createdTo);
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Order> orders = orderService.filterOrder(code, status, createdFrom, createdTo, limit, page);
        int totalOrders = orderService.getTotalOrdersFilter(code, status, createdFrom, createdTo);
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        request.setAttribute("pageTitle", "Quản lý đơn hàng");
        request.setAttribute("pageContent", "../order/list.jsp");
        request.setAttribute("pageCss", "/resources/css/order.css");
        request.setAttribute("pageJs", "/resources/js/order.js");
        request.setAttribute("orders", orders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.setAttribute("filters", filters);
        request.setAttribute("statuses", statuses);
    }

    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response) {
        int id = Helper.parseIntegerSafe(request.getParameter("id"), -1);
        Order order = orderService.selectOrderById(id);
        List<OrderDetail> orderDetails = orderService.selectOrderDetailByOrderId(order.getId());
        List<OrderStatus> statuses = Arrays.asList(OrderStatus.values());
        double totalAmount = orderDetails.stream()
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();
        request.setAttribute("pageTitle", "Chi tiết đơn hàng");
        request.setAttribute("pageContent", "../order/order_detail.jsp");
        request.setAttribute("pageCss", "/resources/css/order.css");
        request.setAttribute("pageJs", "/resources/js/order.js");
        request.setAttribute("order", order);
        request.setAttribute("orderDetails", orderDetails);
        request.setAttribute("totalAmount", totalAmount);
        request.setAttribute("statuses", statuses);
    }


    //  --------------- do post --------------------

    private void handleCreateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        Object tableObj = session.getAttribute("tableId");
        CartService cart = (CartService) session.getAttribute("cart");
        if (tableObj == null || cart == null) {
            session.setAttribute("message", "Không tìm thấy dữ liệu bàn!");
            session.setAttribute("status", "error");
            return; // thoát sớm
        }

        int table_id = (int) tableObj;

        boolean isCreated = orderService.insertOrder(table_id, cart);
        if (isCreated) {
            session.setAttribute("message", "Gọi món thành công!");
            session.setAttribute("status", "success");
            session.removeAttribute("cart");
        } else {
            session.setAttribute("message", "Gọi món thất bại - vui lòng thử lại!");
            session.setAttribute("status", "error");
        }

        response.sendRedirect(request.getContextPath() + "/client");
    }

    private void handleDeleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isDeleted = orderService.deleteOrder(id);
        HttpSession session = request.getSession();
        if (isDeleted) {
            session.setAttribute("message", "Xóa đơn hàng thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Xóa đơn hàng thất bại - vui lòng thử lại!");
            session.setAttribute("status", "error");
        }

        response.sendRedirect(request.getContextPath() + "/order");
    }

    private void handleUpdateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        boolean isUpdated = orderService.updateOrder(id, status);
        HttpSession session = request.getSession();
        if (isUpdated) {
            session.setAttribute("message", "Cập nhật thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại - vui lòng thử lại!");
            session.setAttribute("status", "error");
        }

        response.sendRedirect(request.getContextPath() + "/order");
    }
}
