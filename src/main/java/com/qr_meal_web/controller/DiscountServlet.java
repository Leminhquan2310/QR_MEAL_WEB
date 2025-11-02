package com.qr_meal_web.controller;


import com.qr_meal_web.enums.DiscountStatus;
import com.qr_meal_web.enums.DiscountType;
import com.qr_meal_web.model.Customer;
import com.qr_meal_web.model.Discount;
import com.qr_meal_web.service.DiscountService;
import com.qr_meal_web.service.impl.DiscountServiceImpl;
import com.qr_meal_web.util.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DiscountServlet", urlPatterns = "/discount")
public class DiscountServlet extends HttpServlet {
    private final DiscountService discountService = new DiscountServiceImpl();
    private int page = 1;
    private int limit = 10;
    private int visiblePages = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageActive", "discount");
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            default:
                showListDiscount(req, resp);
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
        resp.sendRedirect(req.getContextPath() + "/discount");
    }

    private void showListDiscount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Discount> discounts = discountService.getAll(limit, page);
        int totalRecords = discountService.getTotalQuantityDiscount();
        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        request.setAttribute("pageTitle", "Quản lý giảm giá");
        request.setAttribute("pageContent", "../discount/list.jsp");
        request.setAttribute("pageCss", "/resources/css/discount.css");
        request.setAttribute("pageJs", "/resources/js/discount.js");
        request.setAttribute("discounts", discounts);
        request.setAttribute("types", DiscountType.values());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    // post
    private void handleCreate(HttpServletRequest request, HttpServletResponse response) {String name = request.getParameter("name");
        // Lấy dữ liệu từ form
        int pointsRequired = Integer.parseInt(request.getParameter("points_required"));
        String description = request.getParameter("description");
        String discountTypeStr = request.getParameter("discount_type");
        double discountValue = Helper.parseDoubleSafe(request.getParameter("discount_value"), 0.0);
        int statusInt = Helper.parseIntegerSafe(request.getParameter("status"), 0);

        DiscountType discountType = DiscountType.fromString(discountTypeStr);
        DiscountStatus status = DiscountStatus.fromCode(statusInt);

        // Tạo đối tượng Discount
        Discount discount = new Discount();
        discount.setPoints_required(pointsRequired);
        discount.setDescription(description);
        discount.setDiscount_type(discountType);
        discount.setDiscount_value(discountValue);
        discount.setStatus(status);

        // Gọi service xử lý
        HttpSession session = request.getSession();
        boolean isCreated = discountService.create(discount);
        if (isCreated) {
            session.setAttribute("message", "Thêm mới thành công!!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) {String name = request.getParameter("name");
        // Lấy dữ liệu từ form
        int id = Integer.parseInt(request.getParameter("id"));
        int pointsRequired = Integer.parseInt(request.getParameter("points_required"));
        String description = request.getParameter("description");
        String discountTypeStr = request.getParameter("discount_type");
        double discountValue = Helper.parseDoubleSafe(request.getParameter("discount_value"), 0.0);
        int statusInt = Helper.parseIntegerSafe(request.getParameter("status"), 0);

        DiscountType discountType = DiscountType.fromString(discountTypeStr);
        DiscountStatus status = DiscountStatus.fromCode(statusInt);

        // Tạo đối tượng Discount
        Discount discount = new Discount();
        discount.setId(id);
        discount.setPoints_required(pointsRequired);
        discount.setDescription(description);
        discount.setDiscount_type(discountType);
        discount.setDiscount_value(discountValue);
        discount.setStatus(status);

        // Gọi service xử lý
        HttpSession session = request.getSession();
        boolean isUpdated = discountService.update(discount);
        if (isUpdated) {
            session.setAttribute("message", "Cập nhật thành công!!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại!");
            session.setAttribute("status", "error");
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) {String name = request.getParameter("name");
        // Lấy dữ liệu từ form
        int id = Integer.parseInt(request.getParameter("id"));
        // Gọi service xử lý
        HttpSession session = request.getSession();
        boolean isDeleted = discountService.delete(id);
        if (isDeleted) {
            session.setAttribute("message", "Cập nhật thành công!!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại!");
            session.setAttribute("status", "error");
        }
    }
}