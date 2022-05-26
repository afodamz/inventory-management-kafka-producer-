package com.mintyn.inventorymanagement.repositories;

import com.mintyn.inventorymanagement.models.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("product repository")
public interface ProductRepository extends JpaRepository<Products, String> {
    @Query("select p from Products p where p.isDeleted=false")
    public Page<Products> findByPagination(Pageable pageable);

}

