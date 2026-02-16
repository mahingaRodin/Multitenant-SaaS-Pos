package com.msp.impls;

import com.msp.enums.EStoreStatus;
import com.msp.exceptions.UserException;
import com.msp.mappers.StoreMapper;
import com.msp.models.Store;
import com.msp.models.StoreContact;
import com.msp.models.User;
import com.msp.payloads.dtos.StoreDto;
import com.msp.repositories.StoreRepository;
import com.msp.services.StoreService;
import com.msp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepo;
    private final UserService userService;


    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {
        Store store = StoreMapper.toEntity(storeDto, user);
        return StoreMapper.toDto(storeRepo.save(store));
    }

    @Override
    public StoreDto getStoreById(UUID id) throws Exception {
        Store store = storeRepo.findById(id).orElseThrow(
                () -> new Exception("Store Not Found!")
        );
        return StoreMapper.toDto(store);
    }

    @Override
    public List<StoreDto> getAllStores() {
        List<Store> dtos = storeRepo.findAll();
        return dtos.stream().map(StoreMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Store getStoreByAdmin() {
        User admin = userService.getCurrentUser();
        return storeRepo.findByStoreAdminId(admin.getId());
    }

    @Override
    public StoreDto updateStore(UUID id, StoreDto storeDto) throws Exception {
        User currentUser = userService.getCurrentUser();
        Store existing = storeRepo.findByStoreAdminId(currentUser.getId());
        if(existing== null) {
            throw new Exception("Store Not Found!");
        }
        existing.setBrand(storeDto.getBrand());
        existing.setDescription(storeDto.getDescription());

        if(storeDto.getStoreType()!= null) {
            existing.setStoreType(storeDto.getStoreType());
        }
        if(storeDto.getContact()!= null) {
            StoreContact contact = StoreContact.builder()
                    .address(storeDto.getContact().getAddress())
                    .phone(storeDto.getContact().getPhone())
                    .email(storeDto.getContact().getEmail())
                    .build();
            existing.setContact(contact);
        }
        Store updatedScore = storeRepo.save(existing);
        return StoreMapper.toDto(updatedScore);
    }

    @Override
    public void deleteStore(UUID id) {
        Store store = getStoreByAdmin();
        storeRepo.delete(store);
    }

    @Override
    public StoreDto getStoreByEmployee() {
        User currentUser = userService.getCurrentUser();
        if(currentUser==null) {
            throw new UserException("You don't have permission to access this store!");
        }
        return StoreMapper.toDto(currentUser.getStore());
    }

    @Override
    public StoreDto moderateStore(UUID id, EStoreStatus status) throws Exception {
        Store store = storeRepo.findById(id).orElseThrow(
                ()-> new Exception("Store Not Found!")
        );
        store.setStatus(status);
        Store updatedScore = storeRepo.save(store);
        return StoreMapper.toDto(updatedScore);
    }
}
