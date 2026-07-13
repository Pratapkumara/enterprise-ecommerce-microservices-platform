package com.pratap.enterprise.inventoryservice.controller;


import com.pratap.enterprise.inventoryservice.dto.InventoryRequest;
import com.pratap.enterprise.inventoryservice.dto.InventoryResponse;
import com.pratap.enterprise.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {


    private final InventoryService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(
            @Valid
            @RequestBody InventoryRequest request) {

        return service.createInventory(request);
    }


    @GetMapping("/{productId}")
    public InventoryResponse getInventory(
            @PathVariable Long productId) {

        return service.getInventoryByProductId(productId);
    }


    @PutMapping("/{productId}")
    public InventoryResponse updateInventory(
            @PathVariable Long productId,
            @Valid
            @RequestBody InventoryRequest request) {

        return service.updateInventory(productId, request);
    }


    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(
            @PathVariable Long productId) {

        service.deleteInventory(productId);
    }


    @PostMapping("/{productId}/reserve")
    public InventoryResponse reserveStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return service.reserveStock(productId, quantity);
    }


    @PostMapping("/{productId}/release")
    public InventoryResponse releaseStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return service.releaseStock(productId, quantity);
    }

}
