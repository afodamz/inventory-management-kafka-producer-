package com.mintyn.inventorymanagement.repositories;

import com.mintyn.inventorymanagement.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("product repository")
public interface ProductRepository extends JpaRepository<Products, String> {

}

