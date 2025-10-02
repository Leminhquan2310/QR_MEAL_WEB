package com.qr_meal_web.controller;

import com.qr_meal_web.dao.CategoryDAOImplement;
import com.qr_meal_web.dao.ICategoryDAO;
import com.qr_meal_web.model.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = "/category")
public class CategoryServlet extends HttpServlet {
    ICategoryDAO categoryDAO = new CategoryDAOImplement();

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
                filterCategory(request, response);
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
        List<Category> categories = categoryDAO.selectAllCategory();
        request.setAttribute("pageTitle", "Quản lý loại sản phẩm");
        request.setAttribute("pageContent", "../category/list.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");
        request.setAttribute("pageJs", "/resources/js/category.js");
        request.setAttribute("categories", categories);
    }

    private void showCreateCategory(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("pageTitle", "Thêm loại sản phẩm");
        request.setAttribute("pageContent", "../category/create.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");
    }

    private void showUpdateCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Category category = categoryDAO.selectCategory(id);
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


    private void filterCategory(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        int status = Integer.parseInt(request.getParameter("status"));
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");
        request.setAttribute("name", name);
        request.setAttribute("status", status);
        request.setAttribute("createdFrom", createdFrom);
        request.setAttribute("createdTo", createdTo);
        List<Category> categories = categoryDAO.filterCategory(name, status, createdFrom, createdTo);
        request.setAttribute("pageTitle", "Quản lý loại sản phẩm");
        request.setAttribute("pageContent", "../category/list.jsp");
        request.setAttribute("pageCss", "/resources/css/category.css");
        request.setAttribute("pageJs", "/resources/js/category.js");
        request.setAttribute("categories", categories);
    }

    // ----------------- doPost category ----------------
    private void createCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String desc = request.getParameter("description");
        String icon = request.getParameter("icon");
        boolean isSuccess = categoryDAO.insertCategory(name, desc, icon);

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
        boolean isSuccess = categoryDAO.updateCategory(id, name, desc, icon);

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
        boolean isDeleted = categoryDAO.deleteCategory(id);

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
