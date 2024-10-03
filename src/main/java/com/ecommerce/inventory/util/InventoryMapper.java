package com.ecommerce.inventory.util;

import com.ecommerce.common.InventoryRequest;
import com.ecommerce.inventory.model.Inventory;
import org.springframework.stereotype.Component;

import static java.util.UUID.fromString;

@Component
public class InventoryMapper {

    public Inventory convert(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setProductId(fromString(inventoryRequest.getProductId()));
        inventory.setVariantId(fromString(inventoryRequest.getVariantId()));
        inventory.setLocation(inventoryRequest.getLocation());
        inventory.setSku(inventoryRequest.getSku());
        inventory.setStock(inventoryRequest.getStock());

        return inventory;
    }

}
