package com.msp.impls;

import com.msp.mappers.InventoryMapper;
import com.msp.models.Branch;
import com.msp.models.Inventory;
import com.msp.models.Product;
import com.msp.payloads.dtos.InventoryDto;
import com.msp.repositories.BranchRepository;
import com.msp.repositories.InventoryRepository;
import com.msp.repositories.ProductRepository;
import com.msp.services.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl  implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) throws Exception {
        Branch branch = branchRepository.findById(inventoryDto.getBranchId()).orElseThrow(
                () -> new Exception("Branch Doesn't Exist")
        );
        Product product = productRepository.findById(inventoryDto.getProductId()).orElseThrow(
                () -> new Exception("Product Doesn't Exist")
        );
        Inventory inventory = InventoryMapper.toEntity(inventoryDto, branch, product);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toDto(savedInventory);
    }

    @Override
    public InventoryDto updateInventory(UUID id,InventoryDto inventoryDto) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () ->new Exception("Inventory Not Found!")
        );
        inventory.setQuantity(inventoryDto.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toDto(updatedInventory);
    }

    @Override
    public void deleteInventory(UUID id) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory Not Found!")
        );
        inventoryRepository.delete(inventory);
    }

    @Override
    public InventoryDto getInventoryById(UUID id) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                ()-> new Exception("Inventory Not Found!")
        );
        return InventoryMapper.toDto(inventory);
    }

    @Override
    public InventoryDto getInventoryByProductIdAndBranchId(UUID productId, UUID branchId) {
        Inventory inventory = inventoryRepository.findByProductIdAndBranchId(productId, branchId);
        return InventoryMapper.toDto(inventory);
    }

    @Override
    public List<InventoryDto> getAlInventoryByBranchId(UUID branchId) {
        List<Inventory> inventories = inventoryRepository.findByBranchId(branchId);
        return inventories.stream().map(
                InventoryMapper::toDto
        ).collect(Collectors.toList());
    }
}
