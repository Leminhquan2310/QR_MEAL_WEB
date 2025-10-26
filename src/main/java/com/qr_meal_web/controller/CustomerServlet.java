package com.qr_meal_web.controller;

import com.qr_meal_web.model.Customer;
import com.qr_meal_web.service.CustomerService;
import com.qr_meal_web.service.impl.CustomerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {
    private final CustomerService customerService = new CustomerServiceImpl();
    private int page = 1;
    private int limit = 10;
    private int visiblePages = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                showCreateForm(request, response);
                break;
            case "check-phone":
                checkPhone(request, response);
                break;
            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                customerService.deleteCustomer(deleteId);
                response.sendRedirect("customer");
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Customer> searchList = customerService.searchCustomers(keyword);
                request.setAttribute("list", searchList);
                request.getRequestDispatcher("views/customer.jsp").forward(request, response);
                break;
            default:
                showListCustomer(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                handleCreateCustomer(request, response);
                break;
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                Customer customer = customerService.getCustomerById(id);
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("views/create.jsp").forward(request, response);
                break;
            case "delete":
                int deleteId = Integer.parseInt(request.getParameter("id"));
                customerService.deleteCustomer(deleteId);
                response.sendRedirect("customer");
                break;
            case "search":
                String keyword = request.getParameter("keyword");
                List<Customer> searchList = customerService.searchCustomers(keyword);
                request.setAttribute("list", searchList);
                request.getRequestDispatcher("views/customer.jsp").forward(request, response);
                break;
            default:
                showListCustomer(request, response);
                break;
        }
    }

    private void showListCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Customer> customers = customerService.getAllCustomers(limit, page);
        int totalOrders = customerService.getTotalQuantityCustomer();
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        request.setAttribute("pageTitle", "Quản lý khách hàng");
        request.setAttribute("pageContent", "../customer/list.jsp");
        request.setAttribute("pageCss", "/resources/css/customer.css");
        request.setAttribute("pageJs", "/resources/js/customer.js");
        request.setAttribute("customers", customers);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
        request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Thêm mới khách hàng");
        request.setAttribute("pageContent", "../customer/create.jsp");
        request.getRequestDispatcher(request.getContextPath() + "/WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    private void checkPhone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String phone = request.getParameter("phone");
        boolean exists = customerService.isPhoneExist(phone);

        response.setContentType("application/json");
        response.getWriter().write("{\"exists\": " + exists + "}");
    }

    // ------- post --------
    private void handleCreateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setPoints(0);

        HttpSession session = request.getSession();
        boolean isCreated = customerService.addCustomer(customer);
        if (isCreated) {
            session.setAttribute("message", "Thêm mới thành công!!");
            session.setAttribute("status", "success");
            session.removeAttribute("cart");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }

        response.sendRedirect(request.getContextPath() + "/customer");
    }
}
