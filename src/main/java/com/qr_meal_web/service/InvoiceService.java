package com.qr_meal_web.service;

import com.qr_meal_web.enums.PaymentMethod;
import com.qr_meal_web.model.Discount;
import com.qr_meal_web.model.Invoice;

import java.sql.Connection;

public interface InvoiceService {

    void createInvoice(Connection connection, int order_id, int employee_id, Discount discount, String paymentMethod, String pointOption, String phone);

    void createInvoiceWithPoint(Connection connection, int order_id, int employee_id, String paymentMethod);

    double getTodayRevenue();

    int getCountInvoiceToday();

    boolean checkDiscountExisted(int discountId);
}
