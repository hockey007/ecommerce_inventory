package com.ecommerce.inventory.server;

import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.GrpcInventoryService;
import com.ecommerce.inventory.service.InventoryService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GrpcServer {

     @Value("${server.grpc.port}")
    private Integer grpcPort = 9090;

    @Autowired
    private InventoryRepository inventoryRepository;

    private Server server;

    @PostConstruct
    public void start() throws IOException {
        server = ServerBuilder.forPort(grpcPort)
                .addService(new GrpcInventoryService())
                .build()
                .start();

        System.out.println("gRPC Server started, listening on port:" + grpcPort);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server...");
            GrpcServer.this.stop();
        }));
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}