package com.msp.repositories;

import com.msp.models.Refund;
import com.msp.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {
    // Paginated version — for other functions that need pagination
    Page<Refund> findByCashierIdAndCreatedAtBetween(
            UUID cashierId,
            LocalDateTime from,
            LocalDateTime to,
            Pageable pageable
    );

    // Non-paginated version — for shift report
    List<Refund> findByCashierIdAndCreatedAtBetween(
            UUID cashierId,
            LocalDateTime from,
            LocalDateTime to
    );

    Page<Refund> findByCashierId(UUID cashierId, Pageable pageable);
    Page<Refund> findByShiftReportId(UUID shiftReportId,Pageable pageable);
    Page<Refund> findByBranchId(UUID branchId,Pageable pageable);
}
