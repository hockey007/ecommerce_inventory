package com.ecommerce.inventory.service;

import com.ecommerce.common.CreateInventoryRequest;
import com.ecommerce.common.InventoryResponse;
import com.ecommerce.common.InventoryServiceGrpc;
import com.ecommerce.inventory.util.InventoryMapper;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

public class GrpcInventoryService extends InventoryServiceGrpc.InventoryServiceImplBase {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public void createInventory(CreateInventoryRequest request,
                                StreamObserver<InventoryResponse> responseStreamObserver) {
        InventoryResponse inventoryResponse = inventoryService.createInventory(
                inventoryMapper.convert(request.getInventory()));

        responseStreamObserver.onNext(inventoryResponse);
        responseStreamObserver.onCompleted();
    }

}
