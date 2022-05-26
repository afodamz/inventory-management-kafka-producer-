package com.mintyn.inventorymanagement.controllers;

import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import com.mintyn.inventorymanagement.dto.ProductUpdateDto;
import com.mintyn.inventorymanagement.models.Products;
import com.mintyn.inventorymanagement.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping(produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductCreateDto product) {
        log.info("Product created - {}", product);

        Products created = productService.createProduct(product);
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    @PutMapping(value = "{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody @Valid ProductUpdateDto product) {
        log.info("### Product updation request - {}", product);

        Products updated = productService.updateProduct(id, product);
        return new ResponseEntity(updated, HttpStatus.OK);
    }

    @GetMapping (produces = "application/json")
    public ResponseEntity<?> getAllProducts() {
        log.info("### Fetching the list of all products");
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }
}

