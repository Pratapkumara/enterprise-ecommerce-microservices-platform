package com.pratap.enterprise.inventoryservice.controller;

import com.pratap.enterprise.inventoryservice.dto.InventoryRequest;
import com.pratap.enterprise.inventoryservice.dto.InventoryResponse;
import com.pratap.enterprise.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService service;

    @InjectMocks
    private InventoryController controller;

    private InventoryRequest request;
    private InventoryResponse response;

    @BeforeEach
    void setUp() {
        request = new InventoryRequest();
        request.setProductId(101L);
        request.setQuantity(100);

        response = InventoryResponse.builder()
                .id(1L)
                .productId(101L)
                .quantity(100)
                .reservedQuantity(20)
                .availableQuantity(80)
                .build();
    }

    @Test
    void createInventoryShouldReturnCreatedInventory() {
        when(service.createInventory(request)).thenReturn(response);

        InventoryResponse result =
                controller.createInventory(request);

        assertSame(response, result);
        verify(service).createInventory(request);
    }

    @Test
    void getAllInventoryShouldReturnInventoryList() {
        when(service.getAllInventory())
                .thenReturn(List.of(response));

        List<InventoryResponse> result =
                controller.getAllInventory();

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
        verify(service).getAllInventory();
    }

    @Test
    void getInventoryShouldReturnRequestedProduct() {
        when(service.getInventoryByProductId(101L))
                .thenReturn(response);

        InventoryResponse result =
                controller.getInventory(101L);

        assertSame(response, result);
        verify(service).getInventoryByProductId(101L);
    }

    @Test
    void updateInventoryShouldReturnUpdatedInventory() {
        when(service.updateInventory(101L, request))
                .thenReturn(response);

        InventoryResponse result =
                controller.updateInventory(101L, request);

        assertSame(response, result);
        verify(service).updateInventory(101L, request);
    }

    @Test
    void deleteInventoryShouldCallService() {
        doNothing().when(service).deleteInventory(101L);

        controller.deleteInventory(101L);

        verify(service).deleteInventory(101L);
    }

    @Test
    void reserveStockShouldReturnUpdatedInventory() {
        when(service.reserveStock(101L, 10))
                .thenReturn(response);

        InventoryResponse result =
                controller.reserveStock(101L, 10);

        assertSame(response, result);
        verify(service).reserveStock(101L, 10);
    }

    @Test
    void releaseStockShouldReturnUpdatedInventory() {
        when(service.releaseStock(101L, 10))
                .thenReturn(response);

        InventoryResponse result =
                controller.releaseStock(101L, 10);

        assertSame(response, result);
        verify(service).releaseStock(101L, 10);
    }
}
