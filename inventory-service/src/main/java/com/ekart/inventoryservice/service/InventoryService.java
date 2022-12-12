package com.ekart.inventoryservice.service;

import com.ekart.inventoryservice.dto.InventoryResponse;
import com.ekart.inventoryservice.entity.Inventory;
import com.ekart.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly=true)
    public List<InventoryResponse> isInStock(List<String> skuCodes){
//        log.info("Wait Started");
//        try{Thread.sleep(10000);}catch (Exception e){}
//        log.info("Wait Ended");
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
