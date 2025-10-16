package com.qr_meal_web.repository.impl;

import com.qr_meal_web.model.Invoice;
import com.qr_meal_web.repository.InvoiceRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final Connection connection;
    private static final String INSERT_INVOICE = "INSERT INTO invoice (order_id, employee_id, total_amount, discount, final_amount, payment_method) VALUES (?, ?, ?, ?, ?, ?)";

    public InvoiceRepositoryImpl() {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public boolean insert(Invoice invoice) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_INVOICE)) {
            ps.setInt(1, invoice.getOrder_d());
            ps.setInt(2, invoice.getEmployee_id());
            ps.setDouble(3, invoice.getTotal_amount());
            ps.setDouble(4, invoice.getDiscount());
            ps.setDouble(5, invoice.getFinal_amount());
            ps.setString(6, invoice.getPayment_method().getCode());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
