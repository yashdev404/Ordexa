package com.ordexa.backend.controller;

import com.ordexa.backend.dto.InventoryDto;
import com.ordexa.backend.entity.InventoryStockEntity;
import com.ordexa.backend.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/stock")
    public ResponseEntity<List<InventoryStockEntity>> getStock(@RequestParam UUID warehouseId) {
        return ResponseEntity.ok(inventoryService.getStockByWarehouse(warehouseId));
    }

    @PostMapping("/grn")
    public ResponseEntity<InventoryStockEntity> recordGrn(@Valid @RequestBody InventoryDto.GrnReceiptRequest request) {
        return ResponseEntity.ok(inventoryService.recordGrnReceipt(request));
    }

    @PostMapping("/damage")
    public ResponseEntity<InventoryStockEntity> recordDamage(@Valid @RequestBody InventoryDto.DamageReportRequest request) {
        return ResponseEntity.ok(inventoryService.recordDamagedStock(request));
    }
}
