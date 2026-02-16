package com.msp.services;

import com.msp.payloads.dtos.CategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto) throws Exception;
    List<CategoryDto> getCategoriesByStore(UUID storeId);
    CategoryDto updateCategory(UUID id, CategoryDto categoryDto) throws Exception;
    void deleteCategory(UUID id) throws Exception;
}
