package com.pratap.enterprise.orderservice.mapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderMapperTest {

    @Test
    void mapperClassShouldBeInstantiable() {
        OrderMapper mapper = new OrderMapper();

        assertNotNull(mapper);
    }
}
