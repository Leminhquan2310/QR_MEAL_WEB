package com.qr_meal_web.controller;

import com.qr_meal_web.dao.EmployeeDAOImp;
import com.qr_meal_web.dao.IEmployeeDAO;
import com.qr_meal_web.dao.IRoleDAO;
import com.qr_meal_web.dao.RoleDAOImplement;
import com.qr_meal_web.model.Employee;
import com.qr_meal_web.model.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    IEmployeeDAO employeeDAO = new EmployeeDAOImp();

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
    }

    private void showFormCreateEmp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IRoleDAO roleDAO = new RoleDAOImplement();
        List<Role> roles = roleDAO.selectAllRole();
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

    private void deleteEmp(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isSuccess = employeeDAO.deleteEmp(id);

        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Xóa thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Xóa thất bại!");
            session.setAttribute("status", "error");
        }

        try {
            response.sendRedirect(request.getContextPath() + "/employee");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

