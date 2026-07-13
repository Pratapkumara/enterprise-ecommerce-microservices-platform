package com.pratap.enterprise.productservice.service;

import com.pratap.enterprise.productservice.dto.ProductRequest;
import com.pratap.enterprise.productservice.dto.ProductResponse;
import com.pratap.enterprise.productservice.entity.Product;
import com.pratap.enterprise.productservice.exception.ProductNotFoundException;
import com.pratap.enterprise.productservice.mapper.ProductMapper;
import com.pratap.enterprise.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = ProductMapper.toEntity(request);

        Product savedProduct = repository.save(product);

        return ProductMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        return ProductMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return repository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setActive(request.getActive());

        Product updatedProduct = repository.save(product);

        return ProductMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        repository.delete(product);
    }
}
