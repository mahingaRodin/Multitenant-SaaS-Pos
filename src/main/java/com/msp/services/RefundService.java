package com.msp.services;

import com.msp.payloads.dtos.RefundDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RefundService {
    RefundDto createRefund(RefundDto refundDto) throws Exception;
    Page<RefundDto> getAllRefunds(int page, int size) throws Exception;
    Page<RefundDto> getRefundByCashier(UUID cashierId,int page,int size) throws Exception;
    Page<RefundDto> getRefundByShiftReport(UUID shiftReportId,int page,int size) throws Exception;
    Page<RefundDto> getRefundByCashierAndDateRange(UUID cashierId,
                                                   LocalDateTime startDate,
                                                   LocalDateTime endDate,
                                                   int page,
                                                   int size
                                                   ) throws Exception;

    Page<RefundDto> getRefundByBranch(UUID branchId,int page,int size) throws Exception;
    RefundDto getRefundById(UUID refundId) throws Exception;
    void deleteRefund(UUID refundId) throws Exception;
}
