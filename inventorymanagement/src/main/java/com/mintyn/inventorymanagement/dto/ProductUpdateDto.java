package com.mintyn.inventorymanagement.dto;

import com.mintyn.inventorymanagement.validations.NullablePrice;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@Builder
public class ProductUpdateDto {

    private String name;

    private String description;
    @NullablePrice
    private Double price;
    @NullablePrice
    private Double totalInStock;
}
