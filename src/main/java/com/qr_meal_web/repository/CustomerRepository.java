package com.qr_meal_web.repository;

import com.qr_meal_web.model.Customer;

import java.util.List;

public interface CustomerRepository {
    boolean insert(Customer customer);

    boolean update(Customer customer);

    boolean delete(int id);

    Customer findById(int id);

    Customer findByPhone(String phone);

    List<Customer> findAll(int limit, int offset);

    List<Customer> searchByName(String name);

    int getCountAllCustomer();
}
