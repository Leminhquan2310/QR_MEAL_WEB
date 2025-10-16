package com.qr_meal_web.controller;

import com.qr_meal_web.enums.BankAccountStatus;
import com.qr_meal_web.model.BankAccount;
import com.qr_meal_web.service.BankAccountService;
import com.qr_meal_web.service.impl.BankAccountServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "SettingServlet", urlPatterns = "/setting")
public class SettingServlet extends HttpServlet {
    private final BankAccountService bankAccountService = new BankAccountServiceImpl();
    private final List<BankAccountStatus> statuses = Arrays.asList(BankAccountStatus.values());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageActive", "setting");
        showMainSetting(req, resp);
        req.getRequestDispatcher("/WEB-INF/views/manage/layout/layout.jsp").forward(req, resp);
    }

    private void showMainSetting(HttpServletRequest req, HttpServletResponse resp) {
        BankAccount bankAccount = bankAccountService.getBankAccount();
        req.setAttribute("pageTitle", "Cấu hình");
        req.setAttribute("pageContent", "../setting/main.jsp");
        req.setAttribute("bankAccount", bankAccount);
        req.setAttribute("statuses", statuses);
    }
}
