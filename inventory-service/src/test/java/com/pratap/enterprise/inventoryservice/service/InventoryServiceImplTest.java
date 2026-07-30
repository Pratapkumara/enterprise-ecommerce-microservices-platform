package com.pratap.enterprise.inventoryservice.service;

import com.pratap.enterprise.inventoryservice.dto.InventoryRequest;
import com.pratap.enterprise.inventoryservice.dto.InventoryResponse;
import com.pratap.enterprise.inventoryservice.entity.Inventory;
import com.pratap.enterprise.inventoryservice.exception.InventoryNotFoundException;
import com.pratap.enterprise.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryServiceImpl service;

    private Inventory inventory;
    private InventoryRequest request;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        inventory = Inventory.builder()
                .id(1L)
                .productId(101L)
                .quantity(100)
                .reservedQuantity(20)
                .createdAt(now)
                .updatedAt(now)
                .build();

        request = new InventoryRequest();
        request.setProductId(101L);
        request.setQuantity(100);
    }

    @Test
    void createInventoryShouldSaveAndReturnInventory() {
        when(repository.save(any(Inventory.class)))
                .thenAnswer(invocation -> {
                    Inventory saved = invocation.getArgument(0);
                    saved.setId(1L);
                    saved.setCreatedAt(LocalDateTime.now());
                    saved.setUpdatedAt(LocalDateTime.now());
                    return saved;
                });

        InventoryResponse response = service.createInventory(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(101L, response.getProductId());
        assertEquals(100, response.getQuantity());
        assertEquals(0, response.getReservedQuantity());
        assertEquals(100, response.getAvailableQuantity());
        verify(repository).save(any(Inventory.class));
    }

    @Test
    void getAllInventoryShouldReturnMappedList() {
        when(repository.findAll()).thenReturn(List.of(inventory));

        List<InventoryResponse> responses = service.getAllInventory();

        assertEquals(1, responses.size());
        assertEquals(101L, responses.get(0).getProductId());
        assertEquals(80, responses.get(0).getAvailableQuantity());
        verify(repository).findAll();
    }

    @Test
    void getInventoryShouldReturnInventoryWhenFound() {
        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));

        InventoryResponse response =
                service.getInventoryByProductId(101L);

        assertEquals(101L, response.getProductId());
        assertEquals(100, response.getQuantity());
        assertEquals(20, response.getReservedQuantity());
        assertEquals(80, response.getAvailableQuantity());
    }

    @Test
    void getInventoryShouldThrowWhenNotFound() {
        when(repository.findByProductId(999L))
                .thenReturn(Optional.empty());

        InventoryNotFoundException exception =
                assertThrows(
                        InventoryNotFoundException.class,
                        () -> service.getInventoryByProductId(999L)
                );

        assertEquals(
                "Inventory not found for product id: 999",
                exception.getMessage()
        );
    }

    @Test
    void updateInventoryShouldUpdateQuantity() {
        InventoryRequest updateRequest = new InventoryRequest();
        updateRequest.setProductId(101L);
        updateRequest.setQuantity(150);

        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryResponse response =
                service.updateInventory(101L, updateRequest);

        assertEquals(150, response.getQuantity());
        assertEquals(130, response.getAvailableQuantity());
        verify(repository).save(inventory);
    }

    @Test
    void updateInventoryShouldThrowWhenNotFound() {
        when(repository.findByProductId(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                InventoryNotFoundException.class,
                () -> service.updateInventory(999L, request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void deleteInventoryShouldDeleteWhenFound() {
        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));

        service.deleteInventory(101L);

        verify(repository).delete(inventory);
    }

    @Test
    void deleteInventoryShouldThrowWhenNotFound() {
        when(repository.findByProductId(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                InventoryNotFoundException.class,
                () -> service.deleteInventory(999L)
        );

        verify(repository, never()).delete(any());
    }

    @Test
    void reserveStockShouldIncreaseReservedQuantity() {
        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryResponse response =
                service.reserveStock(101L, 30);

        assertEquals(50, response.getReservedQuantity());
        assertEquals(50, response.getAvailableQuantity());
        verify(repository).save(inventory);
    }

    @Test
    void reserveStockShouldRejectInsufficientStock() {
        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.reserveStock(101L, 81)
        );

        assertEquals(
                "Insufficient stock available",
                exception.getMessage()
        );
        verify(repository, never()).save(any());
    }

    @Test
    void reserveStockShouldThrowWhenInventoryNotFound() {
        when(repository.findByProductId(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                InventoryNotFoundException.class,
                () -> service.reserveStock(999L, 10)
        );
    }

    @Test
    void releaseStockShouldDecreaseReservedQuantity() {
        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));
        when(repository.save(inventory)).thenReturn(inventory);

        InventoryResponse response =
                service.releaseStock(101L, 10);

        assertEquals(10, response.getReservedQuantity());
        assertEquals(90, response.getAvailableQuantity());
        verify(repository).save(inventory);
    }

    @Test
    void releaseStockShouldRejectQuantityAboveReservedStock() {
        when(repository.findByProductId(101L))
                .thenReturn(Optional.of(inventory));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.releaseStock(101L, 21)
        );

        assertEquals(
                "Release quantity exceeds reserved stock",
                exception.getMessage()
        );
        verify(repository, never()).save(any());
    }

    @Test
    void releaseStockShouldThrowWhenInventoryNotFound() {
        when(repository.findByProductId(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                InventoryNotFoundException.class,
                () -> service.releaseStock(999L, 10)
        );
    }
}
