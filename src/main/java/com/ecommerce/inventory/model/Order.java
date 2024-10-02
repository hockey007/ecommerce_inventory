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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private UUID paymentId;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private Double total;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonProperty("created_at")
    private Date createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    @JsonProperty("updated_at")
    private Date updatedAt;

}
