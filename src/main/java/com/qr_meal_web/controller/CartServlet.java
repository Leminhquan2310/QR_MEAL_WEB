package com.qr_meal_web.controller;

import com.qr_meal_web.model.CartItem;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.service.ProductService;
import com.qr_meal_web.service.impl.CartServiceImpl;
import com.qr_meal_web.service.impl.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private ProductService productService = new ProductServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "update":
                updateFromCart(request, response);
                break;
            default:
                break;
        }
    }

    private void updateFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int delta = Integer.parseInt(request.getParameter("delta"));
        HttpSession session = request.getSession();
        CartService cart = (CartService) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartServiceImpl();
            session.setAttribute("cart", cart);
        }
        cart.updateQuantity(id, delta);

        // Trả về toàn bộ cart
        response.setContentType("application/json");
        response.getWriter().write(cartToJson(cart));
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        CartService cart = (CartService) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartServiceImpl();
            session.setAttribute("cart", cart);
        }
        cart.removeItem(id);

        // Trả về toàn bộ cart
        response.setContentType("application/json");
        response.getWriter().write(cartToJson(cart));
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        CartService cart = (CartService) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartServiceImpl();
            session.setAttribute("cart", cart);
        }
        Product p = productService.selectById(id);
        if (p != null) {
            cart.addItem(p, 1);
        }

        // Trả về toàn bộ cart
        response.setContentType("application/json");
        response.getWriter().write(cartToJson(cart));
    }

    private String cartToJson(CartService cart) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"totalAmount\":").append(cart.getTotalAmount())
                .append(",\"totalQuantity\":").append(cart.getTotalQuantity()).append(",\"items\":[");
        for (CartItem item : cart.getItems()) {
            sb.append("{\"id\":").append(item.getProduct().getId())
                    .append(",\"name\":\"").append(item.getProduct().getName()).append("\"")
                    .append(",\"image\":\"").append(item.getProduct().getImage()).append("\"")
                    .append(",\"price\":").append(item.getProduct().getPrice())
                    .append(",\"quantity\":").append(item.getQuantity()).append("},");
        }
        if (!cart.getItems().isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }
}
