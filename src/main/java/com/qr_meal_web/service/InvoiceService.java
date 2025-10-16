package com.qr_meal_web.service;

import com.qr_meal_web.enums.PaymentMethod;
import com.qr_meal_web.model.Invoice;

public interface InvoiceService {

    boolean createInvoice(int order_id, int employee_id, double total_amount, double discount, double final_amount, String paymentMethod);
}
