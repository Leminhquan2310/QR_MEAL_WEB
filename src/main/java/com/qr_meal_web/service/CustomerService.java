package com.qr_meal_web.service;

import com.qr_meal_web.model.Customer;

import java.util.List;

public interface CustomerService {

    boolean addCustomer(Customer customer);

    boolean updateCustomer(Customer customer);

    boolean deleteCustomer(int id);

    Customer getCustomerById(int id);

    List<Customer> getAllCustomers(int limit, int page);

    List<Customer> searchCustomers(String keyword);

    boolean isPhoneExist(String phone);

    boolean addPoints(String phone, int points);

    boolean redeemPoints(String phone, int points);

    int getTotalQuantityCustomer();
}
