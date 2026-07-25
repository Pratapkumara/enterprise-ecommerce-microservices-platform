package com.pratap.enterprise.productservice.service;

import com.pratap.enterprise.productservice.dto.ProductRequest;
import com.pratap.enterprise.productservice.dto.ProductResponse;
import com.pratap.enterprise.productservice.entity.Product;
import com.pratap.enterprise.productservice.exception.ProductNotFoundException;
import com.pratap.enterprise.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest request;

    @BeforeEach
    void setUp() {

        request = new ProductRequest();
        request.setName("Enterprise Laptop");
        request.setDescription("Business laptop");
        request.setPrice(new BigDecimal("75000.00"));
        request.setQuantity(10);
        request.setCategory("Electronics");
        request.setActive(true);

        product = Product.builder()
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
    void createProductShouldReturnCreatedProduct() {

        when(repository.save(any(Product.class))).thenReturn(product);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Enterprise Laptop", response.getName());
        assertEquals("Business laptop", response.getDescription());
        assertEquals(new BigDecimal("75000.00"), response.getPrice());
        assertEquals(10, response.getQuantity());
        assertEquals("Electronics", response.getCategory());
        assertTrue(response.getActive());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());

        verify(repository).save(any(Product.class));
    }

    @Test
    void getProductByIdShouldReturnProductWhenFound() {

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Enterprise Laptop", response.getName());

        verify(repository).findById(1L);
    }

    @Test
    void getProductByIdShouldThrowExceptionWhenNotFound() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(99L)
        );

        assertEquals("Product not found with id: 99", exception.getMessage());

        verify(repository).findById(99L);
    }

    @Test
    void getAllProductsShouldReturnProductList() {

        Product secondProduct = Product.builder()
                .id(2L)
                .name("Enterprise Phone")
                .description("Business smartphone")
                .price(new BigDecimal("45000.00"))
                .quantity(20)
                .category("Electronics")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(repository.findAll()).thenReturn(List.of(product, secondProduct));

        List<ProductResponse> responses = productService.getAllProducts();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Enterprise Laptop", responses.get(0).getName());
        assertEquals("Enterprise Phone", responses.get(1).getName());

        verify(repository).findAll();
    }

    @Test
    void getAllProductsShouldReturnEmptyList() {

        when(repository.findAll()).thenReturn(List.of());

        List<ProductResponse> responses = productService.getAllProducts();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void updateProductShouldReturnUpdatedProduct() {

        ProductRequest updateRequest = new ProductRequest();
        updateRequest.setName("Updated Laptop");
        updateRequest.setDescription("Updated description");
        updateRequest.setPrice(new BigDecimal("80000.00"));
        updateRequest.setQuantity(15);
        updateRequest.setCategory("Premium Electronics");
        updateRequest.setActive(false);

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);

        ProductResponse response =
                productService.updateProduct(1L, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Laptop", response.getName());
        assertEquals("Updated description", response.getDescription());
        assertEquals(new BigDecimal("80000.00"), response.getPrice());
        assertEquals(15, response.getQuantity());
        assertEquals("Premium Electronics", response.getCategory());
        assertFalse(response.getActive());

        verify(repository).findById(1L);
        verify(repository).save(product);
    }

    @Test
    void updateProductShouldThrowExceptionWhenNotFound() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(99L, request)
        );

        assertEquals("Product not found with id: 99", exception.getMessage());

        verify(repository).findById(99L);
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void deleteProductShouldDeleteProductWhenFound() {

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(repository).findById(1L);
        verify(repository).delete(product);
    }

    @Test
    void deleteProductShouldThrowExceptionWhenNotFound() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(99L)
        );

        assertEquals("Product not found with id: 99", exception.getMessage());

        verify(repository).findById(99L);
        verify(repository, never()).delete(any(Product.class));
    }
}
