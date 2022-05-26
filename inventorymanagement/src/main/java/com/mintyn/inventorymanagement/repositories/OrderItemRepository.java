package com.mintyn.inventorymanagement.repositories;

import com.mintyn.inventorymanagement.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
}
