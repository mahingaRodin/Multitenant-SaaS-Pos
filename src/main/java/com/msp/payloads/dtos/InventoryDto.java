package com.msp.payloads.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class InventoryDto {
    private UUID id;
    private BranchDto branch;
    private UUID branchId;
    private ProductDto product;
    private UUID productId;
    private Integer quantity;
    private LocalTime lastUpdate;
}
