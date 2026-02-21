package com.msp.controllers;

import com.msp.payloads.dtos.CustomerDto;
import com.msp.payloads.dtos.CustomerUpdateDto;
import com.msp.payloads.response.ApiResponse;
import com.msp.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(
            @Valid @RequestBody CustomerDto customerDto
    ) throws Exception {
        return ResponseEntity.ok(customerService.createCustomer(customerDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerDto> patchCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody CustomerUpdateDto dto
    ) throws Exception {
        return ResponseEntity.ok(customerService.patchCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCustomer(
            @PathVariable UUID id
    ) throws Exception {
        customerService.deleteCustomer(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Customer Deleted Successfully!");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(
            @PathVariable UUID id
    ) throws Exception {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() throws Exception {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> search(
            @RequestParam String q
    ) throws Exception {
        return ResponseEntity.ok(customerService.searchCustomers(q));
    }
}