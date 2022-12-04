package com.ekart.inventoryservice.service;

import com.ekart.inventoryservice.dto.InventoryResponse;
import com.ekart.inventoryservice.entity.Inventory;
import com.ekart.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly=true)
    public List<InventoryResponse> isInStock(List<String> skuCodes){
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(this::mapToDto)
                .toList();
    }

    private InventoryResponse mapToDto(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity()>0)
                .build();
    }
}
