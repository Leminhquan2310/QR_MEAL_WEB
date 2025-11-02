package com.qr_meal_web.repository;

import com.qr_meal_web.model.Invoice;

import java.sql.Connection;

public interface InvoiceRepository {
    boolean insert(Connection connection, Invoice invoice);

    double getTodayRevenue();

    int getTotalInvoiceToday();

    boolean checkDiscountExisted(int discountId);
}
