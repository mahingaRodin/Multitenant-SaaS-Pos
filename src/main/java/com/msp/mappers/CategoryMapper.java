package com.msp.mappers;

import com.msp.models.Category;
import com.msp.payloads.dtos.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .storeId(category.getStore()!=null ? category.getStore().getId():null)
                .build();
    }
}
