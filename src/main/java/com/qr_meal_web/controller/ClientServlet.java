package com.qr_meal_web.controller;

import com.qr_meal_web.model.*;
import com.qr_meal_web.service.CartService;
import com.qr_meal_web.service.CategoryService;
import com.qr_meal_web.service.ProductService;
import com.qr_meal_web.service.impl.CartServiceImpl;
import com.qr_meal_web.service.impl.CategoryServiceImpl;
import com.qr_meal_web.service.impl.ProductServiceImpl;
import com.qr_meal_web.util.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ClientServlet", urlPatterns = "/client")
public class ClientServlet extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

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
//        resp.sendRedirect("/client");
    }

    private void showHomePage(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

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
        List<Category> categories = categoryService.selectAllCategory();
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);

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
