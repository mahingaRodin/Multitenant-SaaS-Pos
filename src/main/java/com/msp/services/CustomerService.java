package com.msp.services;

import com.msp.models.Customer;
import com.msp.payloads.dtos.CustomerDto;
import com.msp.payloads.dtos.CustomerUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto dto) throws Exception;
    CustomerDto patchCustomer(UUID id, CustomerUpdateDto dto) throws Exception;
    void deleteCustomer(UUID id) throws Exception;
    CustomerDto getCustomer(UUID id) throws Exception;
    List<CustomerDto> getAllCustomers() throws Exception;
    List<CustomerDto> searchCustomers(String keyword) throws Exception;

}
