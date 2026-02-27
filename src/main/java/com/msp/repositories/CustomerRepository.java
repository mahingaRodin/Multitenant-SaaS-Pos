package com.msp.repositories;

import com.msp.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Page<Customer> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String email, Pageable pageable);
}
