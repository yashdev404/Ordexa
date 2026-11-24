package com.ordexa.backend.service;

import com.ordexa.backend.dto.DispatchDto;
import com.ordexa.backend.entity.DiscrepancyEntity;
import com.ordexa.backend.entity.ShipmentEntity;
import com.ordexa.backend.repository.DiscrepancyRepository;
import com.ordexa.backend.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DispatchService {

    private final ShipmentRepository shipmentRepository;
    private final DiscrepancyRepository discrepancyRepository;

    public DispatchService(ShipmentRepository shipmentRepository, DiscrepancyRepository discrepancyRepository) {
        this.shipmentRepository = shipmentRepository;
        this.discrepancyRepository = discrepancyRepository;
    }

    public List<ShipmentEntity> getShipmentsByFactory(UUID factoryId) {
        return shipmentRepository.findByFactoryId(factoryId);
    }

    @Transactional
    public ShipmentEntity assignTruck(DispatchDto.TruckAssignRequest request) {
        ShipmentEntity shipment = shipmentRepository.findById(request.getShipmentId())
                .orElseThrow(() -> new IllegalArgumentException("Shipment not found"));

        // Assign truck and update status
        shipment.setTruckId(request.getTruckId());
        shipment.setStatus("ASSIGNED");
        return shipmentRepository.save(shipment);
    }

    @Transactional
    public DiscrepancyEntity resolveDiscrepancy(UUID discrepancyId, UUID approvedBy, String reason) {
        DiscrepancyEntity discrepancy = discrepancyRepository.findById(discrepancyId)
                .orElseThrow(() -> new IllegalArgumentException("Discrepancy log not found"));

        discrepancy.setResolutionStatus("RESOLVED");
        discrepancy.setApprovedBy(approvedBy);
        discrepancy.setExceptionReason(reason);
        return discrepancyRepository.save(discrepancy);
    }
}
