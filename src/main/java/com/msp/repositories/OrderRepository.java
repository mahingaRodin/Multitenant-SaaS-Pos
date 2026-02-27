package com.msp.repositories;

import com.msp.models.Order;
import com.msp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByCustomerId(UUID customerId,Pageable pageable);
    Page<Order> findByBranchId(UUID branchId, Pageable pageable);
    Page<Order> findByCashier_Id(UUID cashierId,Pageable pageable);
    Page<Order> findByBranchIdAndCreatedAtBetween(UUID branchId, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<Order> findByCashierAndCreatedAtBetween(User cashier, LocalDateTime from, LocalDateTime to);
    Page<Order> findTop5ByBranchIdOrderByCreatedAtDesc(UUID branchId,Pageable pageable);
}
