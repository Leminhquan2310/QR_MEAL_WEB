package com.qr_meal_web.controller;

import com.qr_meal_web.dao.EmployeeDAOImplement;
import com.qr_meal_web.dao.IEmployeeDAO;
import com.qr_meal_web.dao.IRoleDAO;
import com.qr_meal_web.dao.RoleDAOImplement;
import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.model.Role;
import com.qr_meal_web.util.Helper;
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
    IEmployeeDAO employeeDAO = new EmployeeDAOImplement();
    private List<Role> roles = new RoleDAOImplement().selectAllRole();


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
        List<Employee> employees = employeeDAO.selectAllEmp();
        request.setAttribute("pageTitle", "Quản lý nhân viên");
        request.setAttribute("pageContent", "../employee/list.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("employees", employees);
        request.setAttribute("roles", roles);
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
        Employee employee = employeeDAO.selectById(id);
        IRoleDAO roleDAO = new RoleDAOImplement();
        List<Role> roles = roleDAO.selectAllRole();
        request.setAttribute("pageTitle", "Sửa thông tin nhân viên");
        request.setAttribute("pageContent", "../employee/update.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("roles", roles);
        request.setAttribute("employee", employee);
    }

    private void showFilterEmployee(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        int role = Integer.parseInt(request.getParameter("role"));
        String createdFrom = request.getParameter("createdFrom");
        String createdTo = request.getParameter("createdTo");

        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("role", role);
        filters.put("createdFrom", createdFrom);
        filters.put("createdTo", createdTo);

        List<Employee> employees = employeeDAO.filtersEmployee(name, role, createdFrom, createdTo);
        request.setAttribute("pageTitle", "Quản lý nhân viên");
        request.setAttribute("pageContent", "../employee/list.jsp");
        request.setAttribute("pageCss", "/resources/css/employee.css");
        request.setAttribute("pageJs", "/resources/js/employee.js");
        request.setAttribute("employees", employees);
        request.setAttribute("roles", roles);
        request.setAttribute("filters", filters);
    }

    //    post .....
    private void createEmp(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        int role_id = Integer.parseInt(request.getParameter("role"));
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        boolean created = employeeDAO.insertEmp(name, role_id, phone, password);

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
        boolean isSuccess = employeeDAO.updateEmp(id, name, role, phone, password);

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
        boolean canDelete = employeeDAO.checkCanDelete(id);

        String message, status;
        HttpSession session = request.getSession();
        if (canDelete) {
            boolean isSuccess = employeeDAO.deleteEmp(id);
            if (isSuccess) {
                message = "Xóa nhân viên thành công!";
                status = "success";
            } else {
                message = "Xóa nhân viên thất bại!";
                status = "error";
            }
        } else {
            boolean isSuccess = employeeDAO.setInactiveEmployee(id);
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

