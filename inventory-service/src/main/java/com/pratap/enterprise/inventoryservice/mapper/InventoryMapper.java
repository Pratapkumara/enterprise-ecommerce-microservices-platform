package com.pratap.enterprise.inventoryservice.mapper;

import com.pratap.enterprise.inventoryservice.dto.InventoryRequest;
import com.pratap.enterprise.inventoryservice.dto.InventoryResponse;
import com.pratap.enterprise.inventoryservice.entity.Inventory;

public class InventoryMapper {


    private InventoryMapper() {
    }


    public static Inventory toEntity(InventoryRequest request) {

        return Inventory.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .reservedQuantity(0)
                .build();
    }


    public static InventoryResponse toResponse(Inventory inventory) {

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableQuantity(
                        inventory.getQuantity()
                        - inventory.getReservedQuantity()
                )
                .createdAt(inventory.getCreatedAt())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
