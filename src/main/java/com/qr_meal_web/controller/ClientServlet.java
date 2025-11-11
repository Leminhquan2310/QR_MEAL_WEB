package com.qr_meal_web.controller;

import com.google.gson.Gson;
import com.qr_meal_web.model.*;
import com.qr_meal_web.service.*;
import com.qr_meal_web.service.impl.*;
import com.qr_meal_web.util.Helper;
import com.qr_meal_web.websocket.NotificationSocket;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ClientServlet", urlPatterns = "/client")
public class ClientServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();
    private final OrderDetailService orderDetailService = new OrderDetailServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showHomePage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "add_to_cart":
                addToCart(req, resp);
                return;
            default:
                break;
        }
    }

    private void showHomePage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        int tableIdParam = Helper.parseIntegerSafe(request.getParameter("table-id"), -1);

        // lưu table-id lần đầu
        if (tableIdParam > 0) {
            session.setAttribute("tableId", tableIdParam);
        } else if (session.getAttribute("tableId") == null) {
            response.sendRedirect(request.getContextPath() + "/error");
            return;
        }

        int category = Helper.parseIntegerSafe(request.getParameter("category"), -1);
        List<Product> products = productService.selectProductForClient(category);
        List<Category> categories = categoryService.selectListCategory();
        List<OrderDetail> orderDetails = orderDetailService.selectOrderDetailByTableId((Integer) session.getAttribute("tableId"));
        double totalAmount = orderDetails.stream()
                .mapToDouble(d -> d.getPrice() * d.getQuantity())
                .sum();
        Map<Integer, List<MenuProduct>> menuProductMap = productService.selectMenuProductForClient();
        request.setAttribute("menuProductMap", menuProductMap);
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("orderDetails", orderDetails);
        request.setAttribute("totalAmount", totalAmount);
        request.getRequestDispatcher("/WEB-INF/views/client/index.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        CartService cart = (CartService) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartServiceImpl();
            session.setAttribute("cart", cart);
        }

        int id = Integer.parseInt(request.getParameter("id"));
        Product p = productService.selectById(id);
        if (p != null) {
            cart.addItem(p, 1);
        }
    }
}
