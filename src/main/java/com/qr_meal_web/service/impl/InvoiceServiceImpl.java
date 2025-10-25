package com.qr_meal_web.service.impl;

import com.qr_meal_web.enums.PaymentMethod;
import com.qr_meal_web.model.Invoice;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.repository.InvoiceRepository;
import com.qr_meal_web.repository.OrderDetailRepository;
import com.qr_meal_web.repository.impl.InvoiceRepositoryImpl;
import com.qr_meal_web.repository.impl.OrderDetailRepositoryImpl;
import com.qr_meal_web.service.InvoiceService;

import java.sql.Connection;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository = new InvoiceRepositoryImpl();

    @Override
    public void createInvoice(Connection connection, int order_id, int employee_id, double discount, String paymentMethod) {
        OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();
        List<OrderDetail> orderDetails = orderDetailRepository.selectOrderDetailByOrderId(order_id);
        double total_amount = orderDetails.stream()
                .filter(orderDetail -> orderDetail.getOrderId() == order_id)
                .mapToDouble(OrderDetail::getPrice)
                .sum();
        PaymentMethod method = PaymentMethod.fromCode(paymentMethod);
        Invoice invoice = new Invoice(order_id, employee_id, total_amount, discount, total_amount - discount, method);
        invoiceRepository.insert(connection, invoice);
    }

    @Override
    public double getTodayRevenue() {
        return invoiceRepository.getTodayRevenue();
    }

    @Override
    public int getCountInvoiceToday() {
        return invoiceRepository.getTotalInvoiceToday();
    }
}
