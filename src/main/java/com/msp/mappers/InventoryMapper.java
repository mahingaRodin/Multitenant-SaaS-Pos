package com.msp.mappers;

import com.msp.models.Branch;
import com.msp.models.Inventory;
import com.msp.models.Product;
import com.msp.payloads.dtos.InventoryDto;

public class InventoryMapper {
    public static InventoryDto toDto(Inventory inventory) {
        return InventoryDto.builder()
                .id(inventory.getId())
                .branchId(inventory.getBranch().getId())
                .productId(inventory.getProduct().getId())
                .product(ProductMapper.toDto(inventory.getProduct()))
                .quantity(inventory.getQuantity())
                .build();
    }

    public static Inventory toEntity(InventoryDto inventoryDto, Branch branch, Product product) {
        return Inventory.builder()
                .branch(branch)
                .product(product)
                .quantity(inventoryDto.getQuantity())
                .build();
    }
}
