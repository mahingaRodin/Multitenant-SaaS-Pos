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
    public void run(String... args) throws Exception {
        log.info("Checking the Initial Data...");

        // Create admin user first (without store/branch)
        User adminUser = createAdminUser();

        // Now create store with admin ID
        Store defaultStore = ensureDefaultStore(adminUser);

        // Create branch with store
        Branch defaultBranch = ensureDefaultBranch(defaultStore);

        // Update admin with store and branch
        updateAdminWithStoreAndBranch(adminUser, defaultStore, defaultBranch);

        logCacheInfo();
        log.info("Data initialization Complete!");
    }

    private User createAdminUser() {
        String adminEmail = "mahingarodin@gmail.com";
        User existingUser = userRepository.findByEmail(adminEmail);

        if (existingUser == null) {
            log.info("Creating admin user...");
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
        return existingUser;
    }

    private Store ensureDefaultStore(User adminUser) {
        if (storeRepository.count() == 0) {
            log.info("Creating default store...");
            Store store = new Store();
            store.setBrand("SaaS POS Default");
            store.setStoreType("General Retail");
            store.setDescription("Automatic initialization store");
            store.setStoreAdmin(adminUser);
            store.setStatus(EStoreStatus.ACTIVE); // Set status as active
            store.setCreatedAt(LocalDateTime.now());
            store.setUpdatedAt(LocalDateTime.now());
            return storeRepository.save(store);
        }
        return storeRepository.findAll().get(0);
    }

    private Branch ensureDefaultBranch(Store store) {
        if (branchRepository.count() == 0) {
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
        return branchRepository.findAll().get(0);
    }

    private void updateAdminWithStoreAndBranch(User admin, Store store, Branch branch) {
        log.info("Updating admin with store and branch...");
        boolean changed = false;

        if (admin.getStore() == null) {
            admin.setStore(store);
            changed = true;
        }

        if (admin.getBranch() == null) {
            admin.setBranch(branch);
            changed = true;
        }

        if (changed) {
            userRepository.save(admin);
            log.info("Admin updated with store and branch!");
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