package com.mintyn.inventorymanagement.services.impl;

import com.mintyn.inventorymanagement.dto.OrderDto;
import com.mintyn.inventorymanagement.exceptions.NoItemsInOrderException;
import com.mintyn.inventorymanagement.exceptions.ProductNotExistsException;
import com.mintyn.inventorymanagement.models.Order;
import com.mintyn.inventorymanagement.models.OrderItem;
import com.mintyn.inventorymanagement.models.Products;
import com.mintyn.inventorymanagement.repositories.OrderItemRepository;
import com.mintyn.inventorymanagement.repositories.OrderRepository;
import com.mintyn.inventorymanagement.repositories.ProductRepository;
import com.mintyn.inventorymanagement.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public Order placeOrder(OrderDto order) {
        checkIfItemsAreEmpty(order);

        Order created = new Order();
        created.setPhoneNumber(order.getPhoneNumber());
        created.setName(order.getName());
        orderRepository.save(created);
        log.info("### New order persisted");

        Set<OrderItem> items = new HashSet<>();


        order.getItems().forEach(item -> {
            Products products = productRepository.findById(item.getProductId()).orElseThrow(() -> new ProductNotExistsException("One or more of the Products in the request doesn't exist"));
            if (!(products.getTotalInStock() < item.getQuantity())){
                throw new ProductNotExistsException("Not enough quantity");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(created);
            products.setTotalInStock(products.getTotalInStock() - item.getQuantity());
            productRepository.save(products);
            orderItem.setProduct(products);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(products.getPrice());

            items.add(orderItem);
        });

        log.info("### All order items are persisted");
        created.setItems(items);

        return orderRepository.save(created);
    }

    private void checkIfItemsAreEmpty(OrderDto order) {
        if (isNull(order) || order.getItems().isEmpty()) {
            throw new NoItemsInOrderException("Order creation request doesn't contain any Products");
        }
    }

    @Override
    public List<Order> getOrdersByPeriod(Date start, Date end) {
        log.info("### Fetching orders from the database");
        return orderRepository.findByCreatedAtBetween(start, end);
    }

    // ToDo: Add pagination
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
