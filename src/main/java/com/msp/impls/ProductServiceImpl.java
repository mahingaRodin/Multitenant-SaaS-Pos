package com.msp.impls;

import com.msp.mappers.ProductMapper;
import com.msp.models.Category;
import com.msp.models.Product;
import com.msp.models.Store;
import com.msp.models.User;
import com.msp.payloads.dtos.ProductDto;
import com.msp.repositories.CategoryRepository;
import com.msp.repositories.ProductRepository;
import com.msp.repositories.StoreRepository;
import com.msp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final StoreRepository storeRepo;
    private final CategoryRepository catRepo;

    @Override
    public ProductDto createProduct(ProductDto productDto, User user) throws Exception {
        Store store = storeRepo.findById(
                productDto.getStoreId()
        ).orElseThrow(
                () -> new Exception("Store Not Found")
        );
        Category category = catRepo.findById(productDto.getCategoryId()).orElseThrow(
                ()-> new Exception("Category Not Found")
        );
        Product product = ProductMapper.toEntity(productDto, store, category);
        Product savedProduct = productRepo.save(product);
        return ProductMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(UUID id, ProductDto productDto, User user) throws Exception {
        Product product = productRepo.findById(id).orElseThrow(
                () -> new Exception("Product Not Found!")
        );
        if(productDto.getCategoryId() != null) {
            Category category = catRepo.findById(productDto.getCategoryId()).orElseThrow(
                    () -> new Exception("Category Not Found!")
            );
            product.setCategory(category);
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setSku(productDto.getSku());
        product.setImage(productDto.getImage());
        product.setMrp(productDto.getMrp());
        product.setSellingPrice(productDto.getSellingPrice());
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepo.save(product);
        return ProductMapper.toDto(savedProduct);
    }

    @Override
    public void deleteProduct(UUID id, User user) throws Exception {
    Product product = productRepo.findById(id).orElseThrow(
            ()-> new Exception("Product Not Found")
    );
    productRepo.delete(product);
    }

    @Override
    public Page<ProductDto> getProductsByStoreId(UUID storeId,int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return productRepo.findByStoreId(storeId,pageable).map(ProductMapper::toDto);
    }

    @Override
    public Page<ProductDto> searchByKeyword(UUID storeId, String keyword, int page ,int size) {
        Pageable pageable = PageRequest.of(page,size);
        return productRepo.searchByKeyword(storeId, keyword,pageable).map(ProductMapper::toDto);
    }
}
