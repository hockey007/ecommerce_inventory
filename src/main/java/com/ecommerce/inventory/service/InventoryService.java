package com.ecommerce.inventory.service;

import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import inventory.InventoryProto;
import inventory.InventoryProto.LockInventoryRequest;
import inventory.InventoryProto.LockInventoryResponse;
import inventory.InventoryProto.GetStockRequest;
import inventory.InventoryProto.GetStockResponse;
import inventory.InventoryProto.InventoryRequest;
import inventory.InventoryProto.CreateInventoryRequest;
import inventory.InventoryProto.InventoryResponse;
import inventory.InventoryServiceGrpc;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService extends InventoryServiceGrpc.InventoryServiceImplBase {

    @Autowired
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public void lockInventory(
            LockInventoryRequest request,
            StreamObserver<LockInventoryResponse> responseStreamObserver
    ) {
        LockInventoryResponse.Builder inventoryResponse = LockInventoryResponse.newBuilder();

        try {
            for (InventoryProto.Product product : request.getProductsList()) {
                UUID productId = fromString(product.getProductId());
                UUID variantId = fromString(product.getVariantId());
                Integer quantity = product.getQuantity();

                Inventory inventory = inventoryRepository.findByProductIdAndVariantId(productId, variantId)
                        .orElseThrow(() -> new IllegalStateException("Product not found"));

                if (inventory.getStock() < quantity) {
                    throw new IllegalStateException("Insufficient stock");
                }

                inventory.setStock(inventory.getStock() - quantity);
                inventoryRepository.save(inventory);
            }

            inventoryResponse.setLock(true);
        } catch (Exception e) {
            inventoryResponse.setError(true);
            inventoryResponse.setLock(false);
            inventoryResponse.setMessage(e.getMessage());
        } finally {
            responseStreamObserver.onNext(inventoryResponse.build());
            responseStreamObserver.onCompleted();
        }
    }

    @Override
    public void createInventory(CreateInventoryRequest request,
                                StreamObserver<InventoryResponse> responseStreamObserver) {
        InventoryRequest inventoryRequest = request.getInventory();

        Inventory inventory = new Inventory();

        inventory.setProductId(fromString(inventoryRequest.getProductId()));
        inventory.setVariantId(fromString(inventoryRequest.getVariantId()));
        inventory.setLocation(inventoryRequest.getLocation());
        inventory.setSku(inventoryRequest.getSku());
        inventory.setStock(inventoryRequest.getStock());

        inventoryRepository.save(inventory);

        InventoryResponse response = InventoryResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Inventory created successfully")
                .setInventoryId(inventory.getId().toString())
                .build();

        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    public void getStock(
            GetStockRequest getStockRequest,
            StreamObserver<GetStockResponse> responseStreamObserver
    ) {
        Optional<Inventory> optionalInventory = inventoryRepository.findByProductIdAndVariantId(
                fromString(getStockRequest.getProductId()),
                fromString(getStockRequest.getVariantId()));

         if(optionalInventory.isEmpty()) {
              throw new IllegalStateException("Inventory not found for the product");
         }

        Inventory inventory = optionalInventory.get();
        GetStockResponse stockResponse = GetStockResponse.newBuilder()
                .setStock(inventory.getStock())
                .build();

        responseStreamObserver.onNext(stockResponse);
        responseStreamObserver.onCompleted();
    }

    public Inventory getStock(UUID productId, UUID variantId) {
        Optional<Inventory> optionalInventory = inventoryRepository.findByProductIdAndVariantId(productId, variantId);
        return optionalInventory.orElse(null);
    }

    private UUID fromString(String uuid) {
        return UUID.fromString(uuid);
    }

}
