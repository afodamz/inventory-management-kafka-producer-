package com.mintyn.inventorymanagement.repositories;

import com.mintyn.inventorymanagement.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByCreatedAtBetween(Date start, Date end);
}
