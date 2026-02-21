package com.msp.repositories;

import com.msp.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String email);
}
