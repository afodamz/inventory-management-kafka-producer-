package com.mintyn.inventorymanagement.services;

import com.mintyn.inventorymanagement.dto.OrderDto;
import com.mintyn.inventorymanagement.dto.OrderItemDto;
import com.mintyn.inventorymanagement.models.Order;
import com.mintyn.inventorymanagement.models.OrderItem;
import com.mintyn.inventorymanagement.models.Products;
import com.mintyn.inventorymanagement.repositories.OrderItemRepository;
import com.mintyn.inventorymanagement.repositories.OrderRepository;
import com.mintyn.inventorymanagement.repositories.ProductRepository;
import com.mintyn.inventorymanagement.services.impl.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith (SpringExtension.class)
public class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Order> orderArgumentCaptor;

    @Captor
    private ArgumentCaptor<OrderItem> orderItemArgumentCaptor;

    @Test
    public void testOrderCreationSucceeds() {
        // Given
        String productId = UUID.randomUUID().toString().substring(0,36);
        Products product = Products.builder()
                .id(productId)
                .name("Nokia 3310")
                .description("The strongest mobile phone")
                .price(120000.98)
                .build();
        OrderItemDto orderItemDto = OrderItemDto.builder()
                .productId(productId)
                .quantity(2)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .items(Collections.singletonList(orderItemDto))
                .name("damilola")
                .build();
        Order toBeCreated = Order.builder()
                .name("damilola")
                .build();
        OrderItem orderItem = OrderItem.builder()
                .order(toBeCreated)
                .product(product)
                .unitPrice(120000.98)
                .quantity(2)
                .build();
        Order created = Order.builder()
                .id(productId)
                .name("damilola")
                .items(Collections.singleton(orderItem))
                .build();
        when(orderRepository.save(any())).thenReturn(created);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any())).thenReturn(orderItem);

        // When
        orderService.placeOrder(orderDto);

        // Then
        verify(productRepository, times(1)).findById(any());
        verify(orderItemRepository, times(1)).save(orderItemArgumentCaptor.capture());
        Assertions.assertThat(orderItemArgumentCaptor.getValue().getQuantity()).isEqualTo(2);
        Assertions.assertThat(orderItemArgumentCaptor.getValue().getUnitPrice()).isEqualTo(120000.98);
        Assertions.assertThat(orderItemArgumentCaptor.getValue().getProduct()).isEqualTo(product);
        verify(orderRepository, times(2)).save(orderArgumentCaptor.capture());
        List<Order> capturedOrders = orderArgumentCaptor.getAllValues();
        Assertions.assertThat(capturedOrders.get(0).getName()).isEqualTo("damilola");
        Assertions.assertThat(capturedOrders.get(0).getItems()).isNotEmpty();
        Assertions.assertThat(capturedOrders.get(1).getItems()).hasSize(1);
    }

    @TestConfiguration
    static class OrderServiceTestContextConfiguration {

        @MockBean
        private OrderRepository orderRepository;

        @MockBean
        private OrderItemRepository orderItemRepository;

        @MockBean
        private ProductRepository productRepository;

        @Bean
        public OrderService orderService() {
            return new OrderServiceImpl(orderRepository, orderItemRepository, productRepository);
        }
    }
}
