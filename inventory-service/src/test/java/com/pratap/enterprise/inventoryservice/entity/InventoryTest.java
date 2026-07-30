package com.pratap.enterprise.inventoryservice.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void prePersistShouldInitializeTimestampsAndReservedQuantity() {
        Inventory inventory = Inventory.builder()
                .productId(101L)
                .quantity(100)
                .build();

        inventory.prePersist();

        assertEquals(0, inventory.getReservedQuantity());
        assertNotNull(inventory.getCreatedAt());
        assertNotNull(inventory.getUpdatedAt());
    }

    @Test
    void prePersistShouldPreserveExistingReservedQuantity() {
        Inventory inventory = Inventory.builder()
                .productId(101L)
                .quantity(100)
                .reservedQuantity(20)
                .build();

        inventory.prePersist();

        assertEquals(20, inventory.getReservedQuantity());
        assertNotNull(inventory.getCreatedAt());
        assertNotNull(inventory.getUpdatedAt());
    }

    @Test
    void preUpdateShouldRefreshUpdatedTimestamp() {
        Inventory inventory = Inventory.builder()
                .productId(101L)
                .quantity(100)
                .reservedQuantity(0)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        LocalDateTime previousTimestamp =
                inventory.getUpdatedAt();

        inventory.preUpdate();

        assertTrue(
                inventory.getUpdatedAt()
                        .isAfter(previousTimestamp)
        );
    }
}
