package com.ekart.inventoryservice.repository;

import com.ekart.inventoryservice.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    public Optional<Inventory> findBySkuCode(String skuCode);
}
