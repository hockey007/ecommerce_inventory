package com.ecommerce.inventory.service;

import com.ecommerce.common.CreateInventoryRequest;
import com.ecommerce.common.InventoryResponse;
import com.ecommerce.common.InventoryServiceGrpc.InventoryServiceImplBase;
import com.ecommerce.inventory.util.InventoryMapper;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class GrpcInventoryService extends InventoryServiceImplBase {

    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;

    public GrpcInventoryService(InventoryService inventoryService, InventoryMapper inventoryMapper) {
        this.inventoryService = inventoryService;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public void createInventory(CreateInventoryRequest request,
                                StreamObserver<InventoryResponse> responseStreamObserver) {
        InventoryResponse inventoryResponse = inventoryService.createInventory(
                inventoryMapper.convert(request.getInventory()));

        responseStreamObserver.onNext(inventoryResponse);
        responseStreamObserver.onCompleted();
    }

}
