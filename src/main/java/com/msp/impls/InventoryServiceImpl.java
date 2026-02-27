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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "inventory")
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    @Override
    @Caching(
            put = {
                    @CachePut(key = "#result.id")
            },
            evict = {
                    @CacheEvict(value = "inventory-by-branch", allEntries = true),
                    @CacheEvict(value = "inventory-by-product-branch", allEntries = true)
            }
    )
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
    @Caching(
            put = {
                    @CachePut(key = "#id")
            },
            evict = {
                    @CacheEvict(value = "inventory-by-branch", allEntries = true),
                    @CacheEvict(value = "inventory-by-product-branch", allEntries = true)
            }
    )
    public InventoryDto updateInventory(UUID id, InventoryDto inventoryDto) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory Not Found!")
        );
        inventory.setQuantity(inventoryDto.getQuantity());
        Inventory updatedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.toDto(updatedInventory);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(key = "#id"),
                    @CacheEvict(value = "inventory-by-branch", allEntries = true),
                    @CacheEvict(value = "inventory-by-product-branch", allEntries = true)
            }
    )
    public void deleteInventory(UUID id) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory Not Found!")
        );
        inventoryRepository.delete(inventory);
    }

    @Override
    @Cacheable(key = "#id")
    public InventoryDto getInventoryById(UUID id) throws Exception {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new Exception("Inventory Not Found!")
        );
        return InventoryMapper.toDto(inventory);
    }

    @Override
    @Cacheable(value = "inventory-by-product-branch", key = "#productId + '-' + #branchId")
    public InventoryDto getInventoryByProductIdAndBranchId(UUID productId, UUID branchId) {
        Inventory inventory = inventoryRepository.findByProductIdAndBranchId(productId, branchId);
        return InventoryMapper.toDto(inventory);
    }

    @Override
    @Cacheable(value = "inventory-by-branch", key = "#branchId + '-' + #page + '-' + #size")
    public Page<InventoryDto> getAllInventoryByBranchId(UUID branchId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventories = inventoryRepository.findByBranchId(branchId, pageable);
        return inventories.map(InventoryMapper::toDto);
    }
}