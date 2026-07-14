package com.pratap.enterprise.orderservice.client;

import com.pratap.enterprise.orderservice.client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service")
public interface ProductClient {


    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(
            @PathVariable Long id
    );

}
