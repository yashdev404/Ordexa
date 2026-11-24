package com.ordexa.backend.controller;

import com.ordexa.backend.dto.DispatchDto;
import com.ordexa.backend.entity.DiscrepancyEntity;
import com.ordexa.backend.entity.ShipmentEntity;
import com.ordexa.backend.service.DispatchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;

    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @GetMapping("/shipments")
    public ResponseEntity<List<ShipmentEntity>> getShipments(@RequestParam UUID factoryId) {
        return ResponseEntity.ok(dispatchService.getShipmentsByFactory(factoryId));
    }

    @PostMapping("/assign-truck")
    public ResponseEntity<ShipmentEntity> assignTruck(@Valid @RequestBody DispatchDto.TruckAssignRequest request) {
        return ResponseEntity.ok(dispatchService.assignTruck(request));
    }

    @PostMapping("/resolve-discrepancy/{id}")
    public ResponseEntity<DiscrepancyEntity> resolveDiscrepancy(
            @PathVariable UUID id,
            @RequestParam UUID approvedBy,
            @RequestParam String reason) {
        return ResponseEntity.ok(dispatchService.resolveDiscrepancy(id, approvedBy, reason));
    }
}
