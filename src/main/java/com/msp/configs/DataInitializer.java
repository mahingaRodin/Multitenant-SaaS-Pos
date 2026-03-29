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
            // Create or get admin user
            User adminUser = createOrGetAdminUser();
            log.info("Admin user: {} (ID: {})", adminUser.getEmail(), adminUser.getId());

            // Create or get default store
            Store defaultStore = createOrGetDefaultStore(adminUser);
            log.info("Default store: {} (ID: {})", defaultStore.getBrand(), defaultStore.getId());

            // Create or get default branch
            Branch defaultBranch = createOrGetDefaultBranch(defaultStore);
            log.info("Default branch: {} (ID: {})", defaultBranch.getName(), defaultBranch.getId());

            // Update admin with store and branch
            updateAdminWithStoreAndBranch(adminUser, defaultStore, defaultBranch);

            log.info("=== Data Initialization Complete! ===");
            logCacheInfo();

        } catch (Exception e) {
            log.error("Error during data initialization", e);
            throw e;
        }
    }

    @Transactional
    public User createOrGetAdminUser() {
        String adminEmail = "mahingarodin@gmail.com";
        User existingUser = userRepository.findByEmail(adminEmail);

        if (existingUser != null) {
            log.info("Admin user already exists with ID: {}", existingUser.getId());
            // Refresh to avoid version conflicts
            return userRepository.findById(existingUser.getId()).orElse(existingUser);
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

        return userRepository.save(admin);
    }

    @Transactional
    public Store createOrGetDefaultStore(User adminUser) {
        List<Store> existingStores = storeRepository.findAll();

        if (!existingStores.isEmpty()) {
            Store existingStore = existingStores.get(0);
            log.info("Store already exists with ID: {}", existingStore.getId());

            // Check if store needs to be updated with admin
            if (existingStore.getStoreAdmin() == null) {
                log.info("Updating existing store with admin user...");
                existingStore.setStoreAdmin(adminUser);
                existingStore.setStatus(EStoreStatus.ACTIVE);
                return storeRepository.save(existingStore);
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

        return storeRepository.save(store);
    }

    @Transactional
    public Branch createOrGetDefaultBranch(Store store) {
        List<Branch> existingBranches = branchRepository.findAll();

        if (!existingBranches.isEmpty()) {
            Branch existingBranch = existingBranches.get(0);
            log.info("Branch already exists with ID: {}", existingBranch.getId());

            // Check if branch needs to be updated with store
            if (existingBranch.getStore() == null) {
                log.info("Updating existing branch with store...");
                existingBranch.setStore(store);
                return branchRepository.save(existingBranch);
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

        return branchRepository.save(branch);
    }

    @Transactional
    public void updateAdminWithStoreAndBranch(User admin, Store store, Branch branch) {
        log.info("Updating admin with store and branch...");

        try {
            // Get a fresh copy of the admin to avoid version conflicts
            User freshAdmin = userRepository.findById(admin.getId())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + admin.getId()));

            boolean needsUpdate = false;

            if (freshAdmin.getStore() == null || !freshAdmin.getStore().getId().equals(store.getId())) {
                log.info("Setting store for admin...");
                freshAdmin.setStore(store);
                needsUpdate = true;
            }

            if (freshAdmin.getBranch() == null || !freshAdmin.getBranch().getId().equals(branch.getId())) {
                log.info("Setting branch for admin...");
                freshAdmin.setBranch(branch);
                needsUpdate = true;
            }

            if (needsUpdate) {
                freshAdmin.setUpdatedAt(LocalDateTime.now());
                User updatedAdmin = userRepository.save(freshAdmin);
                log.info("Admin updated successfully with store ID: {} and branch ID: {}",
                        updatedAdmin.getStore().getId(),
                        updatedAdmin.getBranch().getId());
            } else {
                log.info("Admin already has correct store and branch");
            }

        } catch (Exception e) {
            log.error("Error updating admin: {}", e.getMessage());
            throw e;
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