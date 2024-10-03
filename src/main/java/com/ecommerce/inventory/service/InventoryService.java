package com.ecommerce.inventory.service;

import com.ecommerce.common.InventoryResponse;
import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryResponse createInventory(Inventory inventory) {
        InventoryResponse.Builder responseBuilder;
        try {
            inventoryRepository.save(inventory);
            responseBuilder = InventoryResponse.newBuilder()
                    .setMessage("Inventory created successfully")
                    .setInventoryId(inventory.getId().toString());
        } catch (Exception e) {
            responseBuilder = InventoryResponse.newBuilder()
                    .setError(true)
                    .setMessage("Failed to create inventory");
        }

        return responseBuilder.build();
    }

    public Inventory getStock(UUID productId, UUID variantId) {
        Optional<Inventory> optionalInventory = inventoryRepository.findByProductIdAndVariantId(productId, variantId);
        return optionalInventory.orElse(null);
    }

}
