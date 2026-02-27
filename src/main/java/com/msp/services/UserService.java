package com.msp.services;

import com.msp.models.User;
import com.msp.payloads.dtos.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User getCurrentUserFromToken(String token);
    User getCurrentUser();
    User getUserByEmail(String email);
    User getUserById(UUID id);
    Page<UserDto> getAllUsers(int page, int size, String sortBy);
}
