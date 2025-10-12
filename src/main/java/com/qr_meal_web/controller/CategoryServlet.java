package com.qr_meal_web.controller;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.repository.impl.CategoryRepositoryImpl;
import com.qr_meal_web.repository.CategoryRepository;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.service.CategoryService;
import com.qr_meal_web.service.impl.CategoryServiceImpl;
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

@WebServlet(name = "CategoryServlet", urlPatterns = "/category")
public class CategoryServlet extends HttpServlet {
    CategoryService categoryService = new CategoryServiceImpl();
    private int page = 1;
    private int limit = 10;
    private int visiblePages = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        request.setAttribute("pageActive", "category");
        if (action == null) action = "";
        switch (action) {
            case "create":
                showCreateCategory(request, response);
                break;
            case "update":
                showUpdateCategory(request, response);
                break;
            case "filters":
                showFilterCategory(request, response);
                break;
            default:
                showAllCategory(request, response);
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
                createCategory(request, response);
                break;
            case "update":
                updateCategory(request, response);
                break;
            case "delete":
                deleteCategory(request, response);
                break;
        }
        response.sendRedirect(request.getContextPath() + "/category");
    }


    private void showAllCategory(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Category> categories = categoryService.selectAllCategory(limit, page);
        int totalOrders = categoryService.getCountTotal();
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        request.setAttribute("pageTitle", "Quản lý loại sản phẩm");
        request.setAttribute("pageContent", "../category/list.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");
        request.setAttribute("pageJs", "/resources/js/category.js");
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
    }

    private void showCreateCategory(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("pageTitle", "Thêm loại sản phẩm");
        request.setAttribute("pageContent", "../category/create.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");
    }

    private void showUpdateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category category = categoryService.selectCategory(id);
        request.setAttribute("pageTitle", "Sửa loại sản phẩm");
        request.setAttribute("pageContent", "../category/update.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");

        if (category != null) {
            request.setAttribute("category", category);
        } else {
            request.setAttribute("message", "Có lỗi sảy ra!");
            request.setAttribute("status", "error");
        }
    }


    private void showFilterCategory(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        int status = Integer.parseInt(request.getParameter("status"));
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");
        Map<String, Object> filters = new HashMap<>();
        filters.put("keyword", keyword);
        filters.put("status", status);
        filters.put("createdFrom", createdFrom);
        filters.put("createdTo", createdTo);
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int totalCategory = categoryService.getCountTotalCategoryFilter(keyword, status, createdFrom, createdTo);
        int totalPages = (int) Math.ceil((double) totalCategory / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        List<Category> categories = categoryService.filterCategory(keyword, status, createdFrom, createdTo, limit, page);
        request.setAttribute("pageTitle", "Quản lý loại sản phẩm");
        request.setAttribute("pageContent", "../category/list.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");
        request.setAttribute("pageJs", "/resources/js/category.js");
        request.setAttribute("categories", categories);
        request.setAttribute("filters", filters);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
    }

    // ----------------- doPost category ----------------
    private void createCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String desc = request.getParameter("description");
        String icon = request.getParameter("icon");
        boolean isSuccess = categoryService.insertCategory(name, desc, icon);

        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Thêm mới thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }
    }

    private void updateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String desc = request.getParameter("description");
        String icon = request.getParameter("icon");
        boolean isSuccess = categoryService.updateCategory(id, name, desc, icon);

        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Sửa loại sản phẩm thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Sửa loại sản phẩm thất bại!");
            session.setAttribute("status", "error");
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isDeleted = categoryService.deleteCategory(id);

        HttpSession session = request.getSession();
        if (isDeleted) {
            session.setAttribute("message", "Xóa thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Xóa không thành công!");
            session.setAttribute("status", "error");
        }

    }

}
