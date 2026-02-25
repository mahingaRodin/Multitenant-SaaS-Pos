package com.msp.mappers;

import com.msp.models.Refund;
import com.msp.payloads.dtos.RefundDto;

public class RefundMapper {

    public static RefundDto toDto(Refund refund) {
        if (refund == null) return null;

        RefundDto.RefundDtoBuilder builder = RefundDto.builder()
                .id(refund.getId())
                .orderId(refund.getOrder() != null ? refund.getOrder().getId() : null)
                .reason(refund.getReason())
                .amount(refund.getAmount())
                .createdAt(refund.getCreatedAt());

        // cashier
        if (refund.getCashier() != null) {
            builder.cashierName(refund.getCashier().getFirstName());
        }

        // branch
        if (refund.getBranch() != null) {
            builder.branchId(refund.getBranch().getId());
        }

        // shiftReport
        if (refund.getShiftReport() != null) {
            builder.shiftReportId(refund.getShiftReport().getId());
        }

        // paymentType
        if (refund.getPaymentType() != null) {
            builder.paymentType(refund.getPaymentType());
        }

        return builder.build();
    }
}