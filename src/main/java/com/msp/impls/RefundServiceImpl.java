package com.msp.impls;

import com.msp.mappers.RefundMapper;
import com.msp.models.*;
import com.msp.payloads.dtos.RefundDto;
import com.msp.repositories.OrderRepository;
import com.msp.repositories.RefundRepository;
import com.msp.services.RefundService;
import com.msp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;

    @Override
    public RefundDto createRefund(RefundDto refundDto) throws Exception {
        User cashier = userService.getCurrentUser();
        Order order = orderRepository.findById(refundDto.getOrderId()).orElseThrow(
                () -> new Exception("Order Not Found!")
        );
        Branch branch = order.getBranch();
        Refund createdRefund = Refund.builder()
                .order(order)
                .cashier(cashier)
                .branch(branch)
                .reason(refundDto.getReason())
                .amount(refundDto.getAmount())
                .createdAt(refundDto.getCreatedAt())
                .build();
        Refund savedRefund = refundRepository.save(createdRefund);
        return RefundMapper.toDto(savedRefund);
    }

    @Override
    public Page<RefundDto> getAllRefunds(int page,int size) throws Exception {
        Pageable pageable = PageRequest.of(page,size);
        return refundRepository.findAll(pageable)
                .map(RefundMapper::toDto);
    }

    @Override
    public Page<RefundDto> getRefundByCashier(UUID cashierId,int page,int size) throws Exception {
        Pageable pageable = PageRequest.of(page,size);
        return refundRepository.findByCashierId(cashierId,pageable)
                .map(RefundMapper::toDto);
    }

    @Override
    public Page<RefundDto> getRefundByShiftReport(UUID shiftReportId,int page,int size) throws Exception {
        Pageable pageable = PageRequest.of(page,size);
        return refundRepository.findByShiftReportId(shiftReportId,pageable)
                .map(RefundMapper::toDto);
    }

    @Override
    public Page<RefundDto> getRefundByCashierAndDateRange(UUID cashierId, LocalDateTime startDate, LocalDateTime endDate, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page,size);
        return refundRepository.findByCashierIdAndCreatedAtBetween(
                cashierId, startDate, endDate,pageable
        ).map(RefundMapper::toDto);
    }

    @Override
    public Page<RefundDto> getRefundByBranch(UUID branchId,int page,int size) throws Exception {
        Pageable pageable = PageRequest.of(page,size);
        return refundRepository.findByBranchId(branchId,pageable)
                .map(RefundMapper::toDto);
    }

    @Override
    public RefundDto getRefundById(UUID refundId) throws Exception {
        return refundRepository.findById(refundId
        ).map(RefundMapper::toDto).orElseThrow(
                () -> new Exception("Refund Not Found!")
        );
    }

    @Override
    public void deleteRefund(UUID refundId) throws Exception {
        this.getRefundById(refundId);
        refundRepository.deleteById(refundId);
    }
}
