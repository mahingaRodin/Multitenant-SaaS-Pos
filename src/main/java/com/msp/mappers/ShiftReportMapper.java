package com.msp.mappers;

import com.msp.models.Order;
import com.msp.models.Product;
import com.msp.models.Refund;
import com.msp.models.ShiftReport;
import com.msp.payloads.dtos.OrderDto;
import com.msp.payloads.dtos.ProductDto;
import com.msp.payloads.dtos.RefundDto;
import com.msp.payloads.dtos.ShiftReportDto;

import java.util.List;
import java.util.stream.Collectors;

public class ShiftReportMapper {
    public static ShiftReportDto toDto(ShiftReport entity) {
        return ShiftReportDto.builder()
                .id(entity.getId())
                .shiftEnd(entity.getShiftEnd())
                .shiftStart(entity.getShiftStart())
                .totalSales(entity.getTotalSales())
                .totalOrders(entity.getTotalOrders())
//                .totalRefunds(entity.getTotalRefunds())
                .netSale(entity.getNetSale())
                .cashier(UserMapper.toDTO(entity.getCashier()))
                .cashierId(entity.getCashier().getId())
                .branchId(entity.getBranch().getId())
                .recentOrders(mapOrders(entity.getRecentOrders()))
                .topSellingProducts(mapProducts(entity.getTopSellingProducts()))
                .refunds(mapRefunds(entity.getRefunds()))
                .paymentSummaries(entity.getPaymentSummaries())
                .build();
    }

    private static List<RefundDto> mapRefunds(List<Refund> refunds) {
        if (refunds == null || refunds.isEmpty()) {return null;}
        return refunds.stream().map(RefundMapper::toDto).collect(Collectors.toList());
    }

    private static List<ProductDto> mapProducts(List<Product> topSellingProducts) {
        if (topSellingProducts == null || topSellingProducts.isEmpty()) {return null;}
        return topSellingProducts.stream().map(ProductMapper::toDto).collect(Collectors.toList());
    }

    private static List<OrderDto> mapOrders(List<Order> recentOrders) {
        if (recentOrders == null || recentOrders.isEmpty()) {return null;}
        return recentOrders.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }
}
