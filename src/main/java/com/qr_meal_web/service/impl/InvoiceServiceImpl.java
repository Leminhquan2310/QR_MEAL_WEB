package com.qr_meal_web.service.impl;

import com.qr_meal_web.enums.PaymentMethod;
import com.qr_meal_web.model.Invoice;
import com.qr_meal_web.repository.InvoiceRepository;
import com.qr_meal_web.repository.impl.InvoiceRepositoryImpl;
import com.qr_meal_web.service.InvoiceService;

public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository = new InvoiceRepositoryImpl();

    @Override
    public boolean createInvoice(int order_id, int employee_id, double total_amount, double discount, double final_amount, String paymentMethod){
        PaymentMethod method = PaymentMethod.fromCode(paymentMethod);
        Invoice invoice = new Invoice(order_id, employee_id, total_amount, discount, final_amount, method);
        return invoiceRepository.insert(invoice);
    }
}
