package com.msp.impls;

import com.msp.mappers.CustomerMapper;
import com.msp.models.Customer;
import com.msp.payloads.dtos.CustomerDto;
import com.msp.payloads.dtos.CustomerUpdateDto;
import com.msp.repositories.CustomerRepository;
import com.msp.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto dto) throws Exception {
        Customer customer = CustomerMapper.toEntity(dto);
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return CustomerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public CustomerDto patchCustomer(UUID id, CustomerUpdateDto dto) throws Exception {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new Exception("Customer Not Found!"));

        if (dto.getFirstName() != null) existing.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) existing.setLastName(dto.getLastName());
        if (dto.getEmail() != null) existing.setEmail(dto.getEmail());
        if (dto.getPhone() != null) existing.setPhone(dto.getPhone());

        existing.setUpdatedAt(java.time.LocalDateTime.now());

        Customer saved = customerRepository.save(existing);
        return com.msp.mappers.CustomerMapper.toDto(saved);
    }

    @Override
    public void deleteCustomer(UUID id) throws Exception {
        Customer customerToDelete = customerRepository.findById(id).orElseThrow(
                () -> new Exception("Customer Not Found!")
        );
        customerRepository.delete(customerToDelete);
    }

    @Override
    public CustomerDto getCustomer(UUID id) throws Exception {
    Customer customer = customerRepository.findById(id).orElseThrow(
            () -> new Exception("Customer Not Found!")
    );
    return CustomerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() throws Exception {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDto> searchCustomers(String keyword) throws Exception {
        return customerRepository.findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                keyword, keyword
        )
                .stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }
}
