package com.pratap.enterprise.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratap.enterprise.productservice.dto.ProductRequest;
import com.pratap.enterprise.productservice.dto.ProductResponse;
import com.pratap.enterprise.productservice.exception.ProductNotFoundException;
import com.pratap.enterprise.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductRequest request;
    private ProductResponse response;

    @BeforeEach
    void setUp() {

        request = new ProductRequest();
        request.setName("Enterprise Laptop");
        request.setDescription("Business laptop");
        request.setPrice(new BigDecimal("75000.00"));
        request.setQuantity(10);
        request.setCategory("Electronics");
        request.setActive(true);

        response = ProductResponse.builder()
                .id(1L)
                .name("Enterprise Laptop")
                .description("Business laptop")
                .price(new BigDecimal("75000.00"))
                .quantity(10)
                .category("Electronics")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createProductShouldReturn201() throws Exception {

        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Enterprise Laptop"))
                .andExpect(jsonPath("$.price").value(75000.00))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.active").value(true));

        verify(productService).createProduct(any(ProductRequest.class));
    }

    @Test
    void createProductShouldReturn400ForInvalidRequest() throws Exception {

        ProductRequest invalidRequest = new ProductRequest();
        invalidRequest.setName("");
        invalidRequest.setPrice(BigDecimal.ZERO);
        invalidRequest.setQuantity(-1);
        invalidRequest.setCategory("");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.name")
                        .value("Product name is required"))
                .andExpect(jsonPath("$.errors.price")
                        .value("Price must be greater than 0"))
                .andExpect(jsonPath("$.errors.quantity")
                        .value("Quantity cannot be negative"))
                .andExpect(jsonPath("$.errors.category")
                        .value("Category is required"));

        verifyNoInteractions(productService);
    }

    @Test
    void getProductByIdShouldReturn200() throws Exception {

        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Enterprise Laptop"));

        verify(productService).getProductById(1L);
    }

    @Test
    void getProductByIdShouldReturn404WhenNotFound() throws Exception {

        when(productService.getProductById(99L))
                .thenThrow(new ProductNotFoundException(
                        "Product not found with id: 99"));

        mockMvc.perform(get("/api/products/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Product not found with id: 99"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(productService).getProductById(99L);
    }

    @Test
    void getAllProductsShouldReturn200() throws Exception {

        when(productService.getAllProducts()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("Enterprise Laptop"));

        verify(productService).getAllProducts();
    }

    @Test
    void updateProductShouldReturn200() throws Exception {

        ProductResponse updatedResponse = ProductResponse.builder()
                .id(1L)
                .name("Updated Laptop")
                .description("Updated description")
                .price(new BigDecimal("80000.00"))
                .quantity(15)
                .category("Premium Electronics")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        request.setName("Updated Laptop");
        request.setDescription("Updated description");
        request.setPrice(new BigDecimal("80000.00"));
        request.setQuantity(15);
        request.setCategory("Premium Electronics");

        when(productService.updateProduct(
                eq(1L), any(ProductRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(80000.00))
                .andExpect(jsonPath("$.quantity").value(15));

        verify(productService).updateProduct(
                eq(1L), any(ProductRequest.class));
    }

    @Test
    void updateProductShouldReturn404WhenNotFound() throws Exception {

        when(productService.updateProduct(
                eq(99L), any(ProductRequest.class)))
                .thenThrow(new ProductNotFoundException(
                        "Product not found with id: 99"));

        mockMvc.perform(put("/api/products/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Product not found with id: 99"));

        verify(productService).updateProduct(
                eq(99L), any(ProductRequest.class));
    }

    @Test
    void deleteProductShouldReturn204() throws Exception {

        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(productService).deleteProduct(1L);
    }

    @Test
    void deleteProductShouldReturn404WhenNotFound() throws Exception {

        doThrow(new ProductNotFoundException(
                "Product not found with id: 99"))
                .when(productService).deleteProduct(99L);

        mockMvc.perform(delete("/api/products/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Product not found with id: 99"));

        verify(productService).deleteProduct(99L);
    }

    @Test
    void unexpectedExceptionShouldReturn500() throws Exception {

        when(productService.getAllProducts())
                .thenThrow(new RuntimeException("Unexpected server error"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error")
                        .value("Unexpected server error"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(productService).getAllProducts();
    }
}
