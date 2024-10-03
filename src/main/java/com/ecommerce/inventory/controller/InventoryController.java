//package com.ecommerce.inventory.controller;
//
////import com.ecommerce.inventory.service.InventoryService;
//import com.ecommerce.inventory.model.Inventory;
//import com.ecommerce.inventory.service.InventoryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/inventory")
//public class InventoryController {
//
//    @Autowired
//    private InventoryService inventoryService;
//
//    @GetMapping("/get/{productId}/{variantId}")
//    public ResponseEntity<Inventory> getInventory(@RequestParam UUID productId, @RequestParam UUID variantId) {
//        Inventory inventory = inventoryService.getStock(productId, variantId);
//        return new ResponseEntity<Inventory>(inventory, HttpStatus.ACCEPTED);
//    }
//
//}
