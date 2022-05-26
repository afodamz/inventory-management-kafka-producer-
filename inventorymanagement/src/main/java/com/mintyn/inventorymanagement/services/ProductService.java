package com.mintyn.inventorymanagement.services;

import com.mintyn.inventorymanagement.common.PagedResponse;
import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import com.mintyn.inventorymanagement.dto.ProductUpdateDto;
import com.mintyn.inventorymanagement.models.Products;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Products createProduct(ProductCreateDto productDto);

    Products updateProduct(String id, ProductUpdateDto productDto);

    PagedResponse<Products> getAllProducts(int page, int size);
}