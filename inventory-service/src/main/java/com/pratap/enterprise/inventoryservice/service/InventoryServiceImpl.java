package com.pratap.enterprise.inventoryservice.service;

import com.pratap.enterprise.inventoryservice.dto.InventoryRequest;
import com.pratap.enterprise.inventoryservice.dto.InventoryResponse;
import com.pratap.enterprise.inventoryservice.entity.Inventory;
import com.pratap.enterprise.inventoryservice.exception.InventoryNotFoundException;
import com.pratap.enterprise.inventoryservice.mapper.InventoryMapper;
import com.pratap.enterprise.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {


    private final InventoryRepository repository;


    @Override
    public InventoryResponse createInventory(InventoryRequest request) {

        Inventory inventory = InventoryMapper.toEntity(request);

        Inventory saved = repository.save(inventory);

        return InventoryMapper.toResponse(saved);
    }


    @Override
    public List<InventoryResponse> getAllInventory() {

        return repository.findAll()
                .stream()
                .map(InventoryMapper::toResponse)
                .toList();
    }


    @Override
    public InventoryResponse getInventoryByProductId(Long productId) {

        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product id: " + productId
                        ));

        return InventoryMapper.toResponse(inventory);
    }


    @Override
    public InventoryResponse updateInventory(
            Long productId,
            InventoryRequest request) {


        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product id: " + productId
                        ));


        inventory.setQuantity(request.getQuantity());


        Inventory updated = repository.save(inventory);


        return InventoryMapper.toResponse(updated);
    }


    @Override
    public void deleteInventory(Long productId) {

        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product id: " + productId
                        ));


        repository.delete(inventory);
    }


    @Override
    public InventoryResponse reserveStock(
            Long productId,
            Integer quantity) {


        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product id: " + productId
                        ));


        int available =
                inventory.getQuantity()
                - inventory.getReservedQuantity();


        if (available < quantity) {

            throw new RuntimeException(
                    "Insufficient stock available"
            );
        }


        inventory.setReservedQuantity(
                inventory.getReservedQuantity() + quantity
        );


        Inventory updated = repository.save(inventory);


        return InventoryMapper.toResponse(updated);
    }


    @Override
    public InventoryResponse releaseStock(
            Long productId,
            Integer quantity) {


        Inventory inventory = repository.findByProductId(productId)
                .orElseThrow(() ->
                        new InventoryNotFoundException(
                                "Inventory not found for product id: " + productId
                        ));


        int reserved =
                inventory.getReservedQuantity();


        if (reserved < quantity) {

            throw new RuntimeException(
                    "Release quantity exceeds reserved stock"
            );
        }


        inventory.setReservedQuantity(
                reserved - quantity
        );


        Inventory updated = repository.save(inventory);


        return InventoryMapper.toResponse(updated);
    }

}
