package com.msp.services;

import com.msp.payloads.dtos.InventoryDto;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    InventoryDto createInventory(InventoryDto inventoryDto) throws Exception;
    InventoryDto updateInventory(UUID id,InventoryDto inventoryDto) throws Exception;
    void deleteInventory(UUID id) throws Exception;
    InventoryDto getInventoryById(UUID id) throws Exception;
    InventoryDto getInventoryByProductIdAndBranchId(UUID productId, UUID branchId);
    List<InventoryDto> getAlInventoryByBranchId(UUID branchId);
}
