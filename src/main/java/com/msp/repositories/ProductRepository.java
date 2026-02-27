package com.msp.repositories;

import com.msp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByStoreId(UUID storeId, Pageable pageable);
    @Query("SELECT p FROM Product p " +
        "WHERE p.store.id = :storeId AND (" +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))"+
            "or LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) "+
            "or LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%'))" +
            ")"
    )
    Page<Product> searchByKeyword(
            @Param("storeId") UUID storeId,
            @Param("query") String keyword,
            Pageable pageable
    );
}
