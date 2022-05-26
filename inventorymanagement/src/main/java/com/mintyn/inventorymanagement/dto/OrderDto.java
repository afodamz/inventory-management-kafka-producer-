package com.mintyn.inventorymanagement.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class OrderDto {

    @NotNull
    List<OrderItemDto> items;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String name;
}
