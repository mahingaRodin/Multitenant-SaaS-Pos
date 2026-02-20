package com.msp.repositories;

import com.msp.models.Store;
import com.msp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
    List<User> findByStore(Store store);
    List<User> findByBranchId(UUID branchId);
}
