package com.mintyn.inventorymanagement.services;

import com.mintyn.inventorymanagement.dto.OrderDto;
import com.mintyn.inventorymanagement.models.Order;

import java.util.Date;
import java.util.List;

public interface OrderService {

    Order placeOrder(OrderDto order);

    List<Order> getOrdersByPeriod(Date start, Date end);

    List<Order> findAll();

}
