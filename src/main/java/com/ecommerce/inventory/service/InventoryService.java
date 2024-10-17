package com.ecommerce.inventory.service;

import com.ecommerce.common.GetStockResponse;
import com.ecommerce.common.InventoryResponse;
import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

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

    public GetStockResponse getStock(UUID productId, UUID variantId) {
        Inventory inventory = getStockFromRepo(productId, variantId);
        Integer availableQty = inventory.getStock();

        GetStockResponse.Builder getStockResponseBuilder = GetStockResponse.newBuilder();
        getStockResponseBuilder.setStock(availableQty);
        return getStockResponseBuilder.build();
    }

    @Transactional
    public void lockInventory(List<CartItem> cartItems) {
        for(CartItem cartItem: cartItems) {
            Inventory inventory = getStockFromRepo(cartItem.getProductId(), cartItem.getVariantId());
            Integer availableQty = inventory.getStock();


            if(cartItem.getQuantity() > availableQty) {
                throw new IllegalStateException("Stock unavailable");
            }

            inventory.setStock(inventory.getStock() - cartItem.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    private Inventory getStockFromRepo(UUID productId, UUID variantId) {
        Optional<Inventory> optionalInventory = inventoryRepository.findByProductIdAndVariantId(productId, variantId);
        if(optionalInventory.isEmpty()) {
            throw new IllegalStateException("Product not found in inventory");
        }

        return optionalInventory.get();
    }
}
