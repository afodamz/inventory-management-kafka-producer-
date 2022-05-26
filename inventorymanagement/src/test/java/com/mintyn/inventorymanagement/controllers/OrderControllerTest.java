package com.mintyn.inventorymanagement.controllers;

import com.mintyn.inventorymanagement.dto.OrderDto;
import com.mintyn.inventorymanagement.dto.OrderItemDto;
import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static io.restassured.RestAssured.given;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerTest {

    @LocalServerPort
    protected int port;
    private String path;

    private String createdProductId;
    private ProductCreateDto created;

    @BeforeAll
    public void beforeTest() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        path = "api/orders";
        created = ProductCreateDto.builder()
                .name("Nokia 2810")
                .description("Oldest phone")
                .price(70.58)
                .totalInStock(50.0)
                .build();

        //@formatter:off
        createdProductId = given()
                .contentType(ContentType.JSON)
                .body(created)
                .when()
                .post("api/products")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("id");
        //@formatter:on
    }

    @Test
    public void testPlaceOrderSucceedsForValidRequest() {
        OrderItemDto item = OrderItemDto.builder()
                .productId(createdProductId)
                .quantity(2)
                .build();
        OrderDto order = OrderDto.builder()
                .name("Damilola")
                .phoneNumber("0809837364")
                .items(Collections.singletonList(item))
                .build();

        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("$", hasKey("id"))
                .body("id", notNullValue())
                .body("totalPrice", is(Float.valueOf("140001.16")));
        //@formatter:on
    }

}
