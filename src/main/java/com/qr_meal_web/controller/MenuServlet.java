package com.qr_meal_web.controller;

import com.google.gson.GsonBuilder;
import com.qr_meal_web.enums.DiscountStatus;
import com.qr_meal_web.enums.DiscountType;
import com.qr_meal_web.model.*;
import com.qr_meal_web.service.MenuService;
import com.qr_meal_web.service.impl.MenuServiceImpl;
import com.qr_meal_web.service.impl.ProductServiceImpl;
import com.qr_meal_web.util.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "MenuServlet", urlPatterns = "/menu")
public class MenuServlet extends HttpServlet {
    private final MenuService menuService = new MenuServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageActive", "menu");
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                showCreate(req, resp);
                break;
            case "update":
                showUpdate(req, resp);
                break;
            case "get-products":
                getListJsonProduct(req, resp);
                break;
            default:
                showListMenu(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                handleCreate(req, resp);
                break;
            case "update":
                handleUpdate(req, resp);
                break;
            case "delete":
                handleDelete(req, resp);
                break;
            default:
                break;
        }
    }

    private void showListMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Menu> menus = menuService.findAll();
        request.setAttribute("pageTitle", "Quản lý thực đơn");
        request.setAttribute("pageContent", "../menu/list.jsp");
        request.setAttribute("pageCss", "/resources/css/menu.css");
        request.setAttribute("pageJs", "/resources/js/menu.js");
        request.setAttribute("menus", menus);
        request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    private void showCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = new ProductServiceImpl().selectListProduct();
        request.setAttribute("pageTitle", "Thêm thực đơn");
        request.setAttribute("pageContent", "../menu/create.jsp");
        request.setAttribute("pageCss", "/resources/css/menu.css");
        request.setAttribute("pageJs", "/resources/js/add-update-menu.js");
        request.setAttribute("products", products);
        request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    private void showUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        List<Product> products = new ProductServiceImpl().selectListProduct();
        List<MenuProduct> menuProducts =  menuService.findByMenuId(id);
        Set<Integer> selectedProductIds = menuProducts.stream()
                .map(MenuProduct::getProduct_id)
                .collect(Collectors.toSet());
        request.setAttribute("pageTitle", "Cập nhật thực đơn");
        request.setAttribute("pageContent", "../menu/update.jsp");
        request.setAttribute("pageCss", "/resources/css/menu.css");
        request.setAttribute("pageJs", "/resources/js/add-update-menu.js");
        request.setAttribute("products", products);
        request.setAttribute("menu", menuProducts.get(0).getMenu());
        request.setAttribute("selectedProductIds", selectedProductIds);
        request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    private void getListJsonProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        List<Product> products = new ProductServiceImpl().selectListProduct();
        String productsJson = new GsonBuilder().create().toJson(products);
        response.getWriter().print(productsJson);
    }

    // post
    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] productIds = request.getParameterValues("products");

        List<Integer> products = null;
        if (productIds != null) {
            products = new ArrayList<>();
            for (String id : productIds) {
                products.add(Integer.parseInt(id));
            }
        }

        Menu menu = new Menu();
        menu.setName(name);
        menu.setDescription(description);

        // Gọi service xử lý
        HttpSession session = request.getSession();
        boolean isCreated = menuService.create(menu, products);
        if (isCreated) {
            session.setAttribute("message", "Thêm mới thành công!!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }
        response.sendRedirect("/menu");
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id =  Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] productIds = request.getParameterValues("products");

        List<Integer> products = null;
        if (productIds != null) {
            products = new ArrayList<>();
            for (String idPro : productIds) {
                products.add(Integer.parseInt(idPro));
            }
        }

        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setDescription(description);

        // Gọi service xử lý
        HttpSession session = request.getSession();
        boolean isUpdated = menuService.update(menu, products);
        if (isUpdated) {
            session.setAttribute("message", "Cập nhật thành công!!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại!");
            session.setAttribute("status", "error");
        }
        response.sendRedirect("/menu");
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        boolean isDeleted = menuService.delete(id);
        if (isDeleted) {
            session.setAttribute("message", "Xóa menu thành công!!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Xóa menu thất bại!");
            session.setAttribute("status", "error");
        }
        response.sendRedirect("/menu");
    }
}
