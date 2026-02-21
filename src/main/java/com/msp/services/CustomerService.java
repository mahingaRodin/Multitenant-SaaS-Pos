package com.msp.services;

import com.msp.models.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    Customer createCustomer(Customer customer) throws Exception;
    Customer updateCustomer(UUID id, Customer customer) throws Exception;
    void deleteCustomer(UUID id) throws Exception;
    Customer getCustomer(UUID id) throws Exception;
    List<Customer> getAllCustomers() throws Exception;
    List<Customer> searchCustomers(String keyword) throws Exception;

}
