package com.ecommerce.inventory.service;

import event.InventoryFailedEvent;
import event.OrderCreatedEvent;
import event.OrderPlacedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaInventoryService {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private KafkaTemplate<String, InventoryFailedEvent> inventoryFailedKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, OrderPlacedEvent> orderPlacedEventKafkaTemplate;

    @KafkaListener(topics = "order-created", groupId = "inventory-group", containerFactory = "kafkaListenerFactory")
    public void handleOrderCreated(@Payload OrderCreatedEvent orderCreatedEvent) {
        System.out.println("Order Creation Event Consumed");
        try {
            inventoryService.lockInventory(orderCreatedEvent.getCartItems());
            sendOrderPlacedEvent(orderCreatedEvent);
        } catch(Exception e) {
            sendInventoryFailedEvent(orderCreatedEvent, e.getMessage());
        }
    }

    private void sendInventoryFailedEvent(OrderCreatedEvent orderCreatedEvent, String reason) {
        InventoryFailedEvent event = new InventoryFailedEvent();
        event.setOrderId(orderCreatedEvent.getOrderId());
        event.setUserId(orderCreatedEvent.getUserId());
        event.setReason(reason);

        System.out.println("Inventory Failed Event Published");
        inventoryFailedKafkaTemplate.send("inventory-failed", event);
    }

    private void sendOrderPlacedEvent(OrderCreatedEvent orderCreatedEvent) {
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        orderPlacedEvent.setOrderId(orderCreatedEvent.getOrderId());
        orderPlacedEvent.setUserId(orderCreatedEvent.getUserId());

        System.out.println("Order Placed Event Published");
        orderPlacedEventKafkaTemplate.send("order-placed", orderPlacedEvent);
    }

}
