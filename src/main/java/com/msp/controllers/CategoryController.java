package com.msp.controllers;

import com.msp.payloads.dtos.CategoryDto;
import com.msp.payloads.response.ApiResponse;
import com.msp.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @RequestBody CategoryDto categoryDto
    ) throws Exception {
        return ResponseEntity.ok(categoryService.createCategory(categoryDto));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<CategoryDto>> getCategoriesByStoreId(
            @PathVariable UUID storeId
            ) throws Exception {
    return ResponseEntity.ok(
            categoryService.getCategoriesByStore(storeId)
    );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable UUID id
    ) throws Exception {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, categoryDto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(
            @PathVariable UUID id
    ) throws Exception {
        categoryService.deleteCategory(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Category Deleted Successfully!");
        return ResponseEntity.ok(apiResponse);
    }
}
