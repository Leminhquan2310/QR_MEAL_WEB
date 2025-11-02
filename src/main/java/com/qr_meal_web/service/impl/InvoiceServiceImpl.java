package com.qr_meal_web.service.impl;

import com.qr_meal_web.enums.PaymentMethod;
import com.qr_meal_web.model.Discount;
import com.qr_meal_web.model.Invoice;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.repository.CustomerRepository;
import com.qr_meal_web.repository.InvoiceRepository;
import com.qr_meal_web.repository.OrderDetailRepository;
import com.qr_meal_web.repository.impl.CustomerRepositoryImpl;
import com.qr_meal_web.repository.impl.InvoiceRepositoryImpl;
import com.qr_meal_web.repository.impl.OrderDetailRepositoryImpl;
import com.qr_meal_web.service.CustomerService;
import com.qr_meal_web.service.InvoiceService;

import java.sql.Connection;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository = new InvoiceRepositoryImpl();
    private final CustomerService customerService = new CustomerServiceImpl();

    @Override
    public void createInvoice(Connection connection, int order_id, int employee_id, Discount discount, String paymentMethod, String pointOption, String phone) {
        Invoice invoice = new Invoice();
        OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();
        List<OrderDetail> orderDetails = orderDetailRepository.selectOrderDetailByOrderId(order_id);
        double total_amount = orderDetails.stream()
                .filter(orderDetail -> orderDetail.getOrderId() == order_id)
                .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                .sum();
        PaymentMethod method = PaymentMethod.fromCode(paymentMethod);

        invoice.setOrder_id(order_id);
        invoice.setEmployee_id(employee_id);
        invoice.setPayment_method(method);
        invoice.setTotal_amount(total_amount);
        invoice.setDiscount(0.0);
        invoice.setFinal_amount(total_amount);
        if (pointOption.isEmpty() || phone.isEmpty()) {
            invoiceRepository.insert(connection, invoice);
            return;
        } else if (pointOption.equals("earn")) {
            // cộng điểm vào customer
            customerService.addPoints(phone, 10);
        } else {
            //tạo invoice, thêm giảm giá trừ điểm customer
            customerService.redeemPoints(phone, discount.getPoints_required());
            double discountValue = discount.calculateDiscountAmount(total_amount);
            invoice.setDiscount(discountValue);
            invoice.setFinal_amount(discount.calculateFinalAmount(total_amount));
            invoice.setDiscount_id(discount.getId());
            invoice.setPoints_used(discount.getPoints_required());
        }
        invoiceRepository.insert(connection, invoice);
    }

    @Override
    public void createInvoiceWithPoint(Connection connection, int order_id, int employee_id, String paymentMethod) {

    }

    @Override
    public double getTodayRevenue() {
        return invoiceRepository.getTodayRevenue();
    }

    @Override
    public int getCountInvoiceToday() {
        return invoiceRepository.getTotalInvoiceToday();
    }

    @Override
    public boolean checkDiscountExisted(int discountId) {
        return invoiceRepository.checkDiscountExisted(discountId);
    }
}
