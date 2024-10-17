package com.ecommerce.inventory.config;

import event.InventoryFailedEvent;
import event.OrderCreatedEvent;
import event.OrderPlacedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> kafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    @Bean
    public ProducerFactory<String, OrderPlacedEvent> orderPlacedEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties());
    }

    @Bean
    public KafkaTemplate<String, OrderPlacedEvent> orderPlacedEventKafkaTemplate() {
        return new KafkaTemplate<>(orderPlacedEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, InventoryFailedEvent> inventoryFailedEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties());
    }

    @Bean
    public KafkaTemplate<String, InventoryFailedEvent> inventoryFailedEventKafkaTemplate() {
        return new KafkaTemplate<>(inventoryFailedEventProducerFactory());
    }

    private Map<String, Object> kafkaProperties() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        config.put(ConsumerConfig.GROUP_ID_CONFIG, "inventory-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return config;
    }

}
