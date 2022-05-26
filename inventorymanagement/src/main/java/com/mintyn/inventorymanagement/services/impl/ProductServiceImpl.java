package com.mintyn.inventorymanagement.services.impl;

import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import com.mintyn.inventorymanagement.dto.ProductUpdateDto;
import com.mintyn.inventorymanagement.exceptions.ProductNotExistsException;
import com.mintyn.inventorymanagement.models.Products;
import com.mintyn.inventorymanagement.repositories.ProductRepository;
import com.mintyn.inventorymanagement.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasLength;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

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
    public List<Products> getAllProducts() {
        return repository.findAll();
    }
}
