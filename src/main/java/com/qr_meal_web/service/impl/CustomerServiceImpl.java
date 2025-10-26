package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Customer;
import com.qr_meal_web.repository.CustomerRepository;
import com.qr_meal_web.repository.impl.CustomerRepositoryImpl;
import com.qr_meal_web.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository = new CustomerRepositoryImpl();

    @Override
    public boolean addCustomer(Customer customer) {
        // kiểm tra số điện thoại đã tồn tại chưa
        Customer existed = customerRepository.findByPhone(customer.getPhone());
        if (existed != null) {
            System.err.println("Phone already exists: " + customer.getPhone());
            return false;
        }
        return customerRepository.insert(customer);
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return customerRepository.update(customer);
    }

    @Override
    public boolean deleteCustomer(int id) {
        return customerRepository.delete(id);
    }

    @Override
    public Customer getCustomerById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> getAllCustomers(int limit, int page) {
        int offset = (page - 1) * limit;
        return customerRepository.findAll(limit, offset);
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllCustomers(1, 1);
        }
        return customerRepository.searchByName(keyword);
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return customerRepository.findByPhone(phone) != null;
    }

    @Override
    public boolean addPoints(int customerId, int points) {
        Customer customer = getCustomerById(customerId);
        if (customer == null) return false;
        int newPoints = customer.getPoints() + points;
        customer.setPoints(newPoints);
        return updateCustomer(customer);
    }

    @Override
    public int getTotalQuantityCustomer() {
        return customerRepository.getCountAllCustomer();
    }
}
