package com.msp.payloads.dtos;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CustomerUpdateDto {
    private String firstName;
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String phone;
}
