package com.msp.configs;

import com.msp.enums.EStoreStatus;
import com.msp.enums.EUserRole;
import com.msp.models.Branch;
import com.msp.models.Store;
import com.msp.models.User;
import com.msp.repositories.BranchRepository;
import com.msp.repositories.StoreRepository;
import com.msp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("=== Starting Data Initialization ===");

        try {
            User adminUser = createOrGetAdminUser();
            log.info("Admin user: {} (ID: {})", adminUser.getEmail(), adminUser.getId());

            Store defaultStore = createOrGetDefaultStore(adminUser);
            log.info("Default store: {} (ID: {})", defaultStore.getBrand(), defaultStore.getId());

            Branch defaultBranch = createOrGetDefaultBranch(defaultStore);
            log.info("Default branch: {} (ID: {})", defaultBranch.getName(), defaultBranch.getId());

            updateAdminWithStoreAndBranch(adminUser, defaultStore, defaultBranch);

            log.info("=== Data Initialization Complete! ===");
            logCacheInfo();

        } catch (Exception e) {
            log.error("Error during data initialization", e);
            throw e;
        }
    }

    private User createOrGetAdminUser() {
        String adminEmail = "mahingarodin@gmail.com";
        User existingUser = userRepository.findByEmail(adminEmail);

        if (existingUser != null) {
            log.info("Admin user already exists with ID: {}", existingUser.getId());
            return existingUser;
        }

        log.info("Creating new admin user...");
        User admin = User.builder()
                .id(UUID.randomUUID())
                .email(adminEmail)
                .password(passwordEncoder.encode("admin!123"))
                .firstName("Mahinga")
                .lastName("Rodin")
                .role(EUserRole.ROLE_SUPER_ADMIN)
                .phone("+250794415318")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();

        User savedAdmin = userRepository.save(admin);
        log.info("Admin user created with ID: {}", savedAdmin.getId());
        return savedAdmin;
    }

    private Store createOrGetDefaultStore(User adminUser) {
        List<Store> existingStores = storeRepository.findAll();

        if (!existingStores.isEmpty()) {
            Store existingStore = existingStores.get(0);
            log.info("Store already exists with ID: {}", existingStore.getId());

            // Check if store needs to be updated with admin
            if (existingStore.getStoreAdmin() == null) {
                log.info("Updating existing store with admin user...");
                existingStore.setStoreAdmin(adminUser);
                existingStore.setStatus(EStoreStatus.ACTIVE);
                Store updatedStore = storeRepository.save(existingStore);
                log.info("Store updated with admin ID: {}", updatedStore.getStoreAdmin().getId());
                return updatedStore;
            }

            return existingStore;
        }

        log.info("Creating default store...");
        Store store = new Store();
        store.setBrand("SaaS POS Default");
        store.setStoreType("General Retail");
        store.setDescription("Automatic initialization store");
        store.setStoreAdmin(adminUser);
        store.setStatus(EStoreStatus.ACTIVE);

        // Let the @PrePersist handle createdAt and status
        Store savedStore = storeRepository.save(store);
        log.info("Default store created with ID: {} and Admin ID: {}",
                savedStore.getId(),
                savedStore.getStoreAdmin() != null ? savedStore.getStoreAdmin().getId() : "NULL");

        return savedStore;
    }

    private Branch createOrGetDefaultBranch(Store store) {
        List<Branch> existingBranches = branchRepository.findAll();

        if (!existingBranches.isEmpty()) {
            Branch existingBranch = existingBranches.get(0);
            log.info("Branch already exists with ID: {}", existingBranch.getId());

            // Check if branch needs to be updated with store
            if (existingBranch.getStore() == null) {
                log.info("Updating existing branch with store...");
                existingBranch.setStore(store);
                Branch updatedBranch = branchRepository.save(existingBranch);
                log.info("Branch updated with store ID: {}", updatedBranch.getStore().getId());
                return updatedBranch;
            }

            return existingBranch;
        }

        log.info("Creating default branch...");
        Branch branch = Branch.builder()
                .name("Main Branch")
                .address("Default Address")
                .phone("+250794415312")
                .email("umurungiolga12@gmail.com")
                .store(store)
                .workingDays(List.of("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"))
                .openTime(LocalTime.of(8, 0))
                .closeTime(LocalTime.of(20, 0))
                .build();

        Branch savedBranch = branchRepository.save(branch);
        log.info("Default branch created with ID: {}", savedBranch.getId());
        return savedBranch;
    }

    private void updateAdminWithStoreAndBranch(User admin, Store store, Branch branch) {
        log.info("Checking admin's store and branch associations...");
        boolean needsUpdate = false;

        if (admin.getStore() == null) {
            log.info("Setting store for admin...");
            admin.setStore(store);
            needsUpdate = true;
        } else {
            log.info("Admin already has store: {}", admin.getStore().getId());
        }

        if (admin.getBranch() == null) {
            log.info("Setting branch for admin...");
            admin.setBranch(branch);
            needsUpdate = true;
        } else {
            log.info("Admin already has branch: {}", admin.getBranch().getId());
        }

        if (needsUpdate) {
            admin.setUpdatedAt(LocalDateTime.now());
            User updatedAdmin = userRepository.save(admin);
            log.info("Admin updated with store ID: {} and branch ID: {}",
                    updatedAdmin.getStore().getId(),
                    updatedAdmin.getBranch().getId());
        } else {
            log.info("Admin already properly configured with store and branch");
        }
    }

    private void logCacheInfo() {
        log.info("=== CACHE INFORMATION ===");
        log.info("Available cache names: users, products, orders, branches, categories, inventory, refunds, shifts");
        log.info("Cache TTL: 1 hour");
        log.info("To view cache stats: GET /api/admin/cache/stats");
        log.info("To clear cache: DELETE /api/admin/cache/clear/all");
        log.info("To check specific cache: GET /api/admin/cache/check/{cacheName}/{key}");
        log.info("=== END CACHE INFO ===");
    }
}