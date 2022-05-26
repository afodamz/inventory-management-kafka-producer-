package com.mintyn.inventorymanagement.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
@Builder
public class ProductCreateDto {

    @NotEmpty
    private String name;

    private String description;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Positive
    private Double totalInStock;
}
