package com.msp.services;

import com.msp.models.Customer;
import com.msp.payloads.dtos.CustomerDto;
import com.msp.payloads.dtos.CustomerUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto dto) throws Exception;
    CustomerDto patchCustomer(UUID id, CustomerUpdateDto dto) throws Exception;
    void deleteCustomer(UUID id) throws Exception;
    CustomerDto getCustomer(UUID id) throws Exception;
    Page<CustomerDto> getAllCustomers(int page, int size) throws Exception;
    Page<CustomerDto> searchCustomers(String keyword, int page, int size) throws Exception;

}
