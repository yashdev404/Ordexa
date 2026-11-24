package com.ordexa.backend.service;

import com.ordexa.backend.dto.OrderDto;
import com.ordexa.backend.entity.OrderEntity;
import com.ordexa.backend.entity.OrderItemEntity;
import com.ordexa.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderEntity> getOrdersByFactory(UUID factoryId) {
        return orderRepository.findByFactoryId(factoryId);
    }

    @Transactional
    public OrderEntity ingestPurchaseOrder(OrderDto.OrderIngestRequest request) {
        // Uniqueness Duplicate PO Check
        if (orderRepository.existsByCompanyIdAndCustomerIdAndExternalPoNumber(
                request.getCompanyId(), request.getCustomerId(), request.getExternalPoNumber())) {
            throw new IllegalArgumentException("Duplicate PO Detected! PO " + request.getExternalPoNumber() + " already exists.");
        }

        OrderEntity order = new OrderEntity();
        order.setCompanyId(request.getCompanyId());
        order.setFactoryId(request.getFactoryId());
        order.setCustomerId(request.getCustomerId());
        order.setExternalPoNumber(request.getExternalPoNumber());
        order.setOrderDate(request.getOrderDate());
        order.setRequestedDeliveryDate(request.getRequestedDeliveryDate());
        order.setPriority(request.getPriority());
        order.setStatus("APPROVED");
        order.setSupplierName(request.getSupplierName());
        order.setShipToAddress(request.getShipToAddress());
        order.setBillToAddress(request.getBillToAddress());
        order.setDeliveryInstructions(request.getDeliveryInstructions());

        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderDto.OrderItemDto itemDto : request.getLineItems()) {
            BigDecimal totalItemAmount = itemDto.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            subtotal = subtotal.add(totalItemAmount);

            OrderItemEntity item = new OrderItemEntity();
            item.setProductId(itemDto.getProductId());
            item.setSku(itemDto.getSku());
            item.setDescription(itemDto.getDescription());
            item.setUom(itemDto.getUom());
            item.setQuantity(itemDto.getQuantity());
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setTotalAmount(totalItemAmount);

            order.addLineItem(item);
        }

        BigDecimal gst = subtotal.multiply(new BigDecimal("0.18"));
        order.setSubtotal(subtotal);
        order.setGstAmount(gst);
        order.setTotalAmount(subtotal.add(gst));

        return orderRepository.save(order);
    }

    @Transactional
    public OrderEntity updateStatus(UUID orderId, String newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID " + orderId));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
