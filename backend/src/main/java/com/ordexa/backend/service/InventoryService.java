package com.ordexa.backend.service;

import com.ordexa.backend.dto.InventoryDto;
import com.ordexa.backend.entity.InventoryLedgerEntity;
import com.ordexa.backend.entity.InventoryStockEntity;
import com.ordexa.backend.repository.InventoryLedgerRepository;
import com.ordexa.backend.repository.InventoryStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryStockRepository stockRepository;
    private final InventoryLedgerRepository ledgerRepository;

    public InventoryService(InventoryStockRepository stockRepository, InventoryLedgerRepository ledgerRepository) {
        this.stockRepository = stockRepository;
        this.ledgerRepository = ledgerRepository;
    }

    public List<InventoryStockEntity> getStockByWarehouse(UUID warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId);
    }

    @Transactional
    public InventoryStockEntity recordGrnReceipt(InventoryDto.GrnReceiptRequest request) {
        // Pessimistic Lock for Stock Update
        InventoryStockEntity stock = stockRepository.findByWarehouseIdAndSkuForUpdate(request.getWarehouseId(), request.getSku())
                .orElseGet(() -> {
                    InventoryStockEntity newStock = new InventoryStockEntity();
                    newStock.setWarehouseId(request.getWarehouseId());
                    newStock.setProductId(request.getProductId());
                    newStock.setSku(request.getSku());
                    newStock.setPhysicalQty(0);
                    newStock.setReservedQty(0);
                    return newStock;
                });

        int newPhysical = stock.getPhysicalQty() + request.getQuantity();
        stock.setPhysicalQty(newPhysical);
        InventoryStockEntity savedStock = stockRepository.save(stock);

        // Record Immutable Ledger Entry
        InventoryLedgerEntity ledger = new InventoryLedgerEntity();
        ledger.setInventoryStockId(savedStock.getId());
        ledger.setTransactionType("RECEIPT");
        ledger.setReferenceType("GRN");
        ledger.setReferenceId(request.getGrnReference());
        ledger.setQtyIn(request.getQuantity());
        ledger.setQtyOut(0);
        ledger.setBalanceAfter(newPhysical);
        ledger.setPerformedBy(request.getPerformedBy());
        ledger.setApprovedBy(request.getApprovedBy());
        ledger.setNotes("Receipt via GRN " + request.getGrnReference());

        ledgerRepository.save(ledger);
        return savedStock;
    }

    @Transactional
    public InventoryStockEntity recordDamagedStock(InventoryDto.DamageReportRequest request) {
        // Pessimistic Lock
        InventoryStockEntity stock = stockRepository.findByWarehouseIdAndSkuForUpdate(request.getWarehouseId(), request.getSku())
                .orElseThrow(() -> new IllegalArgumentException("Stock not found for SKU " + request.getSku()));

        if (stock.getPhysicalQty() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient physical stock to report damage!");
        }

        int newPhysical = stock.getPhysicalQty() - request.getQuantity();
        stock.setPhysicalQty(newPhysical);
        InventoryStockEntity savedStock = stockRepository.save(stock);

        InventoryLedgerEntity ledger = new InventoryLedgerEntity();
        ledger.setInventoryStockId(savedStock.getId());
        ledger.setTransactionType("DAMAGE");
        ledger.setReferenceType("DAMAGE_REPORT");
        ledger.setReferenceId(request.getDamageReference());
        ledger.setQtyIn(0);
        ledger.setQtyOut(request.getQuantity());
        ledger.setBalanceAfter(newPhysical);
        ledger.setPerformedBy(request.getPerformedBy());
        ledger.setApprovedBy(request.getApprovedBy());
        ledger.setNotes(request.getNotes() != null ? request.getNotes() : "Damage reported");

        ledgerRepository.save(ledger);
        return savedStock;
    }
}
