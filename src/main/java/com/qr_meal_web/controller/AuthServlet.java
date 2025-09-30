package com.qr_meal_web.controller;

import com.qr_meal_web.dao.EmployeeDAOImp;
import com.qr_meal_web.dao.IEmployeeDAO;
import com.qr_meal_web.model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private IEmployeeDAO empDAO = new EmployeeDAOImp();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // false = không tạo mới nếu chưa có
        if (session != null) {
            session.invalidate(); // huỷ session
        }
        // Forward đến JSP login trong WEB-INF
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        Employee resultCheckLogin = empDAO.checkLogin(phone, password);

        if (resultCheckLogin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("account", resultCheckLogin);
            if (resultCheckLogin.getRole().getId() == 4) {
                response.sendRedirect(request.getContextPath() + "employee");
            }
        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
