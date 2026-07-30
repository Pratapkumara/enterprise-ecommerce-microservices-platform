package com.pratap.enterprise.orderservice.mapper;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest {

    @Test
    void mapperConstructorShouldBePrivate()
            throws Exception {

        Constructor<OrderMapper> constructor =
                OrderMapper.class.getDeclaredConstructor();

        assertTrue(
                Modifier.isPrivate(
                        constructor.getModifiers()
                )
        );

        constructor.setAccessible(true);

        assertNotNull(constructor.newInstance());
    }
}
