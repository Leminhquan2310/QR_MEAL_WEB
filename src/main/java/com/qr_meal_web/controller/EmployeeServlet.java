package com.qr_meal_web.controller;

import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.Role;
import com.qr_meal_web.service.EmployeeService;
import com.qr_meal_web.service.impl.EmployeeServiceImpl;
import com.qr_meal_web.service.impl.RoleServiceImpl;
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

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final List<Role> roles = new RoleServiceImpl().selectAllRole();
    private int page = 1;
    private int limit = 10;
    private int visiblePages = 5;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        request.setAttribute("pageActive", "employee");
        if (action == null) action = "";
        switch (action) {
            case "create":
                showFormCreateEmp(request, response);
                break;
            case "update":
                showFormUpdateEmp(request, response);
                break;
            case "filters":
                showFilterEmployee(request, response);
                break;
            default:
                showListEmp(request, response);
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
                createEmp(request, response);
                break;
            case "update":
                updateEmp(request, response);
                break;
            case "delete":
                deleteEmp(request, response);
                break;
        }
    }

    private void showListEmp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        List<Employee> employees = employeeService.selectAllEmp(limit, page);
        int totalOrders = employeeService.getTotalEmployees();
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        request.setAttribute("pageTitle", "Quản lý nhân viên");
        request.setAttribute("pageContent", "../employee/list.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("employees", employees);
        request.setAttribute("roles", roles);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
    }

    private void showFormCreateEmp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageTitle", "Thêm nhân viên");
        request.setAttribute("pageContent", "../employee/create.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("roles", roles);
    }

    private void showFormUpdateEmp(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Employee employee = employeeService.selectById(id);
        request.setAttribute("pageTitle", "Sửa thông tin nhân viên");
        request.setAttribute("pageContent", "../employee/update.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("roles", roles);
        request.setAttribute("employee", employee);
    }

    private void showFilterEmployee(HttpServletRequest request, HttpServletResponse response) {
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        String name = request.getParameter("name");
        int role = Integer.parseInt(request.getParameter("role"));
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");
        int totalOrders = employeeService.getTotalEmployeeFilter(name, role, createdFrom, createdTo);
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        int startPage = Math.max(1, page - visiblePages / 2);
        int endPage = Math.min(totalPages, startPage + visiblePages - 1);

        // Cập nhật lại nếu gần cuối danh sách
        if (endPage - startPage < visiblePages - 1) {
            startPage = Math.max(1, endPage - visiblePages + 1);
        }
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("role", role);
        filters.put("createdFrom", createdFrom);
        filters.put("createdTo", createdTo);

        List<Employee> employees = employeeService.filtersEmployee(name, role, createdFrom, createdTo, limit, page);
        request.setAttribute("pageTitle", "Quản lý nhân viên");
        request.setAttribute("pageContent", "../employee/list.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("employees", employees);
        request.setAttribute("roles", roles);
        request.setAttribute("filters", filters);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("startPage", startPage);
        request.setAttribute("endPage", endPage);
    }

    //    post .....
    private void createEmp(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        int role_id = Integer.parseInt(request.getParameter("role"));
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        boolean created = employeeService.insertEmp(name, role_id, phone, password);

        HttpSession session = request.getSession();
        if (created) {
            session.setAttribute("message", "Thêm mới thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }

        // Redirect -> lần gọi tiếp theo sẽ là GET
        try {
            response.sendRedirect(request.getContextPath() + "/employee?action=create");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEmp(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int role = Integer.parseInt(request.getParameter("role"));
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        boolean isSuccess = employeeService.updateEmp(id, name, role, phone, password);

        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Cập nhật thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Cập nhật thất bại!");
            session.setAttribute("status", "error");
        }

        // Redirect -> lần gọi tiếp theo sẽ là GET
        try {
            response.sendRedirect(request.getContextPath() + "/employee?action=update&id=" + id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteEmp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean canDelete = employeeService.checkCanDelete(id);

        String message, status;
        HttpSession session = request.getSession();
        if (canDelete) {
            boolean isSuccess = employeeService.deleteEmp(id);
            if (isSuccess) {
                message = "Xóa nhân viên thành công!";
                status = "success";
            } else {
                message = "Xóa nhân viên thất bại!";
                status = "error";
            }
        } else {
            boolean isSuccess = employeeService.setInactiveEmployee(id);
            if (isSuccess) {
                message = "Tài khoản đã được ngưng việc sử dụng!";
                status = "success";
            } else {
                message = "Ngưng sử dụng tài khoản thất bại!";
                status = "error";
            }
        }

        session.setAttribute("message", message);
        session.setAttribute("status", status);
        response.sendRedirect(request.getContextPath() + "/employee");
    }

}

