package com.mintyn.inventorymanagement.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
@Builder
public class OrderItemDto {

    @NotNull
    private String productId;

    @NotNull
    private Integer quantity;
}

