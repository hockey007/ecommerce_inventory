package com.ecommerce.inventory.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id", nullable = false)
    @JsonProperty("product_id")
    private UUID productId;

    @Column(name = "variant_id", nullable = false)
    @JsonProperty("variant_id")
    private UUID variantId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("created_at")
    private Date createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty("updated_at")
    private Date updatedAt;

}
