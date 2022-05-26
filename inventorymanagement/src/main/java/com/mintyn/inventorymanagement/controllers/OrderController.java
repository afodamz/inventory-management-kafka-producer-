package com.mintyn.inventorymanagement.controllers;

import com.mintyn.inventorymanagement.dto.OrderDto;
import com.mintyn.inventorymanagement.models.Order;
import com.mintyn.inventorymanagement.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Resource
    private KafkaTemplate<String, Order> kafkaObjectTemplate;
    private static final String TOPIC = "orderProducts";

    @PostMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> placeOrder(@RequestBody @Valid OrderDto order) {
        log.info("### Order- {}", order);
        Order created = orderService.placeOrder(order);

        this.kafkaObjectTemplate.send(TOPIC, created);

        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @GetMapping (produces = "application/json")
    public ResponseEntity<?> getOrdersByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        log.info("### Fetching all orders between {} and {}", start, end);
        return new ResponseEntity<>(orderService.getOrdersByPeriod(start, end), HttpStatus.OK);
    }
}
