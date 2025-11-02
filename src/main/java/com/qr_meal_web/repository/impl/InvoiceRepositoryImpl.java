package com.qr_meal_web.repository.impl;

import com.qr_meal_web.model.Invoice;
import com.qr_meal_web.repository.InvoiceRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private static final String INSERT_INVOICE = "INSERT INTO invoice (order_id, employee_id, total_amount, discount, final_amount, payment_method, discount_id, points_used) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_TODAY_REVENUE = "SELECT sum(final_amount) AS today_revenue FROM invoice WHERE DATE(paid_at) = CURRENT_DATE";
    private static final String SELECT_COUNT_TODAY_INVOICE = "SELECT count(*) AS count_invoice FROM invoice WHERE DATE(paid_at) = CURRENT_DATE";
    private static final String SELECT_CHECK_DISCOUNT= "SELECT count(*) AS count_invoice FROM invoice WHERE discount_id = ?";

    @Override
    public boolean insert(Connection connection, Invoice invoice) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_INVOICE)) {
            ps.setInt(1, invoice.getOrder_id());
            ps.setInt(2, invoice.getEmployee_id());
            ps.setDouble(3, invoice.getTotal_amount());
            ps.setDouble(4, invoice.getDiscount());
            ps.setDouble(5, invoice.getFinal_amount());
            ps.setString(6, invoice.getPayment_method().getCode());
                ps.setInt(7, invoice.getDiscount_id());
            if (invoice.getDiscount_id() <= 0){
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            ps.setInt(8, invoice.getPoints_used());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public double getTodayRevenue() {
        double today_revenue = 0.0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_TODAY_REVENUE)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) today_revenue = rs.getDouble("today_revenue");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return today_revenue;
    }

    @Override
    public int getTotalInvoiceToday() {
        int count_invoice_today = 0;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_COUNT_TODAY_INVOICE)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) count_invoice_today = rs.getInt("count_invoice");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count_invoice_today;
    }

    @Override
    public boolean checkDiscountExisted(int discountId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_CHECK_DISCOUNT)) {
            statement.setInt(1, discountId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getInt("count_invoice") > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
