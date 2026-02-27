package com.msp.impls;

import com.msp.enums.EUserRole;
import com.msp.mappers.CategoryMapper;
import com.msp.models.Category;
import com.msp.models.Store;
import com.msp.models.User;
import com.msp.payloads.dtos.CategoryDto;
import com.msp.repositories.CategoryRepository;
import com.msp.repositories.StoreRepository;
import com.msp.services.CategoryService;
import com.msp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository catRepo;
    private final UserService userService;
    private final StoreRepository storeRepo;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) throws Exception {
        User user = userService.getCurrentUser();
        Store store = storeRepo.findById(categoryDto.getStoreId()).orElseThrow(
                () -> new Exception("Store Not Found")
        );
        Category category = Category.builder()
                .store(store)
                .name(categoryDto.getName())
                .build();
        checkAuthority(user, category.getStore());
        Category savedCategory = catRepo.save(category);
        return CategoryMapper.toDto(savedCategory);
    }

    @Override
    public Page<CategoryDto> getCategoriesByStore(UUID storeId,int page,int size) {
        Pageable pageable = PageRequest.of(page,size);
        return catRepo.findByStoreId(storeId,pageable)
                .map(
                        CategoryMapper::toDto
                );
    }

    @Override
    public CategoryDto updateCategory(UUID id, CategoryDto categoryDto) throws Exception {
        Category category = catRepo.findById(id).orElseThrow(
                ()-> new Exception("Category Not Found")
        );
        User user = userService.getCurrentUser();
        category.setName(categoryDto.getName());
        checkAuthority(user, category.getStore());
        return CategoryMapper.toDto(catRepo.save(category));
    }

    @Override
    public void deleteCategory(UUID id) throws Exception {
    Category category = catRepo.findById(id).orElseThrow(
            () -> new Exception("Category Doesn't Exist!")
    );
    User user = userService.getCurrentUser();
    checkAuthority(user, category.getStore());
    catRepo.delete(category);
    }

    private void checkAuthority(User user, Store store) throws Exception {
        boolean isAdmin = user.getRole().equals(EUserRole.ROLE_STORE_ADMIN);
        boolean isManager = user.getRole().equals(EUserRole.ROLE_STORE_MANAGER);
        boolean isSameStore = user.equals(store.getStoreAdmin());
        if((!isAdmin && !isManager) && !isSameStore) {
            throw new Exception("You do not have permission to manage this category");
        }
    }
}
