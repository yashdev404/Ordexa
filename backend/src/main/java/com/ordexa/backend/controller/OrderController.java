package com.ordexa.backend.controller;

import com.ordexa.backend.dto.OrderDto;
import com.ordexa.backend.entity.OrderEntity;
import com.ordexa.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderEntity>> getOrders(@RequestParam UUID factoryId) {
        return ResponseEntity.ok(orderService.getOrdersByFactory(factoryId));
    }

    @PostMapping("/ingest")
    public ResponseEntity<OrderEntity> ingestOrder(@Valid @RequestBody OrderDto.OrderIngestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.ingestPurchaseOrder(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderEntity> updateStatus(@PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
