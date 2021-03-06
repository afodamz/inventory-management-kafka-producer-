package com.mintyn.inventorymanagement.services.impl;

import com.mintyn.inventorymanagement.common.PagedResponse;
import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import com.mintyn.inventorymanagement.dto.ProductUpdateDto;
import com.mintyn.inventorymanagement.exceptions.ProductNotExistsException;
import com.mintyn.inventorymanagement.models.Products;
import com.mintyn.inventorymanagement.repositories.ProductRepository;
import com.mintyn.inventorymanagement.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    private static final String DATE = "createdAt";

    @Override
    public Products createProduct(ProductCreateDto productDto) {
        Products products = new Products();
        products.setDescription(productDto.getDescription());
        products.setName(productDto.getName());
        products.setTotalInStock(productDto.getTotalInStock());
        products.setPrice(productDto.getPrice());
        return repository.save(products);
    }

    @Override
    public Products updateProduct(String id, ProductUpdateDto productDto) {
        return repository
                .findById(id)
                .map(existingProduct -> {
                            modifyUpdatedFields(productDto, existingProduct);
                            return repository.save(existingProduct);
                        }
                ).orElseThrow(() -> new ProductNotExistsException("Product doesn't exist in the system to update"));
    }

    private void modifyUpdatedFields(ProductUpdateDto productDto, Products existingProduct) {
        if (hasLength(productDto.getName())) {
            existingProduct.setName(productDto.getName());
        }
        if (hasLength(productDto.getDescription())) {
            existingProduct.setDescription(productDto.getDescription());
        }

        if (nonNull(productDto.getTotalInStock())) {
            existingProduct.setTotalInStock(productDto.getTotalInStock());
        }

        if (nonNull(productDto.getPrice())) {
            existingProduct.setPrice(productDto.getPrice());
        }
    }

    // ToDo: Add pagination
    @Override
    public PagedResponse<Products> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, DATE);
        Page<Products> products= repository.findByPagination(pageable);

        if(products.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), products.getNumber(), products.getSize(), products.getTotalElements(),
                    products.getTotalPages(), products.isLast(), products.isFirst(), products.isEmpty());
        }

        return new PagedResponse<>(products.toList(), products.getNumber(),
                products.getSize(), products.getTotalElements(), products.getTotalPages(), products.isLast(), products.isFirst(), products.isEmpty());
    }
}
