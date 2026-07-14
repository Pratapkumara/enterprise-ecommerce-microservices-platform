package com.pratap.enterprise.orderservice.service;

import com.pratap.enterprise.orderservice.client.ProductClient;
import com.pratap.enterprise.orderservice.client.dto.ProductResponse;
import com.pratap.enterprise.orderservice.dto.OrderRequest;
import com.pratap.enterprise.orderservice.dto.OrderResponse;
import com.pratap.enterprise.orderservice.entity.Order;
import com.pratap.enterprise.orderservice.entity.OrderItem;
import com.pratap.enterprise.orderservice.exception.OrderNotFoundException;
import com.pratap.enterprise.orderservice.mapper.OrderMapper;
import com.pratap.enterprise.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    private final ProductClient productClient;



    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {


        Order order = Order.builder()
                .userId(request.getUserId())
                .build();



        List<OrderItem> items = request.getItems()
                .stream()
                .map(itemRequest -> {


                    ProductResponse product = productClient.getProductById(
                            itemRequest.getProductId()
                    );


                    OrderItem item = OrderItem.builder()
                            .productId(product.getId())
                            .quantity(itemRequest.getQuantity())
                            .price(product.getPrice())
                            .order(order)
                            .build();


                    return item;
                })
                .toList();



        order.setItems(items);



        BigDecimal totalAmount = items.stream()
                .map(item ->
                        item.getPrice()
                                .multiply(
                                        BigDecimal.valueOf(item.getQuantity())
                                )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );



        order.setTotalAmount(totalAmount);



        Order savedOrder = orderRepository.save(order);



        return OrderMapper.toResponse(savedOrder);
    }




    @Override
    public OrderResponse getOrderById(Long id) {


        Order order = orderRepository.findById(id)
                .orElseThrow(
                        () -> new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );


        return OrderMapper.toResponse(order);
    }




    @Override
    public List<OrderResponse> getAllOrders() {


        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

}
