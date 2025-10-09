package com.qr_meal_web.controller;

import com.qr_meal_web.model.Order;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.service.OrderService;
import com.qr_meal_web.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageActive", "order");
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            default:
                showAllOrder(request, response);
                break;
        }
        request.getRequestDispatcher("/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    private void showAllOrder(HttpServletRequest request, HttpServletResponse response) {
        List<Order> orders = orderService.selectAllOrder();
        request.setAttribute("pageTitle", "Quản lý đơn hàng");
        request.setAttribute("pageContent", "../order/list.jsp");
        request.setAttribute("pageCss", "/resources/css/order.css");
        request.setAttribute("pageJs", "/resources/js/order.js");
        request.setAttribute("orders", orders);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                handleCreateOrder(request, response);
                break;
            default:
                break;
        }
    }

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
}
