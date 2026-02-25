package com.msp.repositories;

import com.msp.models.ShiftReport;
import com.msp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftReportRepository extends JpaRepository<ShiftReport, UUID> {
    List<ShiftReport> findByCashierId(UUID cashierId);
    List<ShiftReport> findByBranchId(UUID branchId);
    Optional<ShiftReport> findTopByCashierAndShiftEndIsNUllOrderByShiftStartDesc(
            User cashier
    );
    Optional<ShiftReport> findByCashierAndShiftStartBetween(
            User cashier,
            LocalDateTime start,
            LocalDateTime end
    );
}
