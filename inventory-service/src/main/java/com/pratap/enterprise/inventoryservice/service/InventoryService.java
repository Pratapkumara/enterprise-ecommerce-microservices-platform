package com.pratap.enterprise.inventoryservice.service;

import com.pratap.enterprise.inventoryservice.dto.InventoryRequest;
import com.pratap.enterprise.inventoryservice.dto.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse getInventoryByProductId(Long productId);

    InventoryResponse updateInventory(Long productId, InventoryRequest request);

    void deleteInventory(Long productId);

    InventoryResponse reserveStock(Long productId, Integer quantity);

    InventoryResponse releaseStock(Long productId, Integer quantity);

}
