package com.pratap.enterprise.productservice.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void prePersistShouldInitializeTimestampsAndActiveFlag() {
        Product product = Product.builder()
                .name("Enterprise Laptop")
                .description("Business laptop")
                .price(new BigDecimal("75000.00"))
                .quantity(10)
                .category("Electronics")
                .build();

        assertNull(product.getActive());
        assertNull(product.getCreatedAt());
        assertNull(product.getUpdatedAt());

        product.prePersist();

        assertTrue(product.getActive());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void prePersistShouldPreserveExistingActiveFlag() {
        Product product = Product.builder()
                .name("Enterprise Laptop")
                .description("Business laptop")
                .price(new BigDecimal("75000.00"))
                .quantity(10)
                .category("Electronics")
                .active(false)
                .build();

        product.prePersist();

        assertFalse(product.getActive());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void preUpdateShouldRefreshUpdatedTimestamp() {
        LocalDateTime previousTimestamp =
                LocalDateTime.now().minusDays(1);

        Product product = Product.builder()
                .name("Enterprise Laptop")
                .price(new BigDecimal("75000.00"))
                .quantity(10)
                .category("Electronics")
                .active(true)
                .createdAt(previousTimestamp)
                .updatedAt(previousTimestamp)
                .build();

        product.preUpdate();

        assertNotNull(product.getUpdatedAt());
        assertTrue(
                product.getUpdatedAt()
                        .isAfter(previousTimestamp)
        );
    }
}
