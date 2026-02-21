package com.msp.impls;

import com.msp.models.Customer;
import com.msp.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Override
    public Customer createCustomer(Customer customer) throws Exception {
        return null;
    }

    @Override
    public Customer updateCustomer(UUID id, Customer customer) throws Exception {
        return null;
    }

    @Override
    public void deleteCustomer(UUID id) throws Exception {

    }

    @Override
    public Customer getCustomer(UUID id) throws Exception {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws Exception {
        return List.of();
    }

    @Override
    public List<Customer> searchCustomers(String keyword) throws Exception {
        return List.of();
    }
}
