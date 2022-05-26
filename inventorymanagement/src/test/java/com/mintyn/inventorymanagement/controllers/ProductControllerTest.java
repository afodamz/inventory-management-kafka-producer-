package com.mintyn.inventorymanagement.controllers;

import com.mintyn.inventorymanagement.models.Products;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import com.mintyn.inventorymanagement.dto.ProductUpdateDto;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance (TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerTest {

    @LocalServerPort
    protected int port;
    private String path;
    private String update;

    String productId;

    @BeforeAll
    public void beforeTest() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        productId = UUID.randomUUID().toString().substring(0,36);
        path = "api/products";
//        update = "api/products/"+productId;
        update = "api/products/28eb4088-c3e3-4648-a769-d5eaeea41104";
    }

    @Test
    public void testProductCreationSucceedsForValidRequest() {
        ProductCreateDto product = ProductCreateDto.builder()
                .name("iPhone")
                .description("The best selling mobile device from Apple")
                .price(700.0)
                .totalInStock(280.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
        //@formatter:on
    }

    @Test
    public void testProductCreationFailsWhenNoNameSpecified() {
        ProductCreateDto product = ProductCreateDto.builder()
                .description("The best selling mobile device from Apple")
                .price(700.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        //@formatter:on
    }

    @Test
    public void testProductCreationFailsWhenNoPriceSpecified() {
        ProductCreateDto product = ProductCreateDto.builder()
                .name("iPhone")
                .description("The best selling mobile device from Apple")
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        //@formatter:on
    }

    @Test
    public void testProductCreationFailsWhenWrongPriceSpecified() {
        ProductCreateDto product = ProductCreateDto.builder()
                .name("iPhone")
                .description("The best selling mobile device from Apple")
                .price(-700.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        //@formatter:on
    }

    @Test
    public void testProductUpdationSucceedsForValidRequest() {
        String createdProductId = this.createNewProduct();
        ProductUpdateDto updated = ProductUpdateDto.builder()
                .name("iPhone 11")
                .description("The brand new iPhone from Apple")
                .price(100.0)
                .totalInStock(280.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put(update)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo("28eb4088-c3e3-4648-a769-d5eaeea41104"))
                .body("name", equalTo(updated.getName()))
                .body("description", equalTo(updated.getDescription()))
                .body("totalInStock", is(Float.valueOf("280.0")))
                .body("price", is(Float.valueOf("100.0")));
        //@formatter:on
    }

    @Test
    public void testProductUpdationSucceedsWhenPriceIsNull() {
        String createdProductId = createNewProduct();

        ProductUpdateDto updated = ProductUpdateDto.builder()
                .name("iPhone 11")
                .description("The brand new iPhone from Apple")
                .price(null)
                .totalInStock(280.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put(update)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo("28eb4088-c3e3-4648-a769-d5eaeea41104"))
                .body("name", equalTo(updated.getName()))
                .body("description", equalTo(updated.getDescription()))
                .body("totalInStock", is(Float.valueOf("280.0")))
                .body("price", is(Float.valueOf("100.0")));
        //@formatter:on
    }

    @Test
    public void testProductUpdationFailsForInvalidPrice() {
        String createdProductId = createNewProduct();

        ProductUpdateDto updated = ProductUpdateDto.builder()
                .name("iPhone 11")
                .description("The brand new iPhone from Apple")
                .price(-1000.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put(update)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        //@formatter:on
    }

    @Test
    public void testProductUpdationFailsForNoId() {
        ProductUpdateDto updated = ProductUpdateDto.builder()
                .name("iPhone 11")
                .description("The brand new iPhone from Apple")
                .price(1000.0)
                .build();

        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put(update)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
        //@formatter:on
    }

    @Test
    public void testProductUpdationFailsForNonExistingProduct() {
        ProductUpdateDto updated = ProductUpdateDto.builder()
                .name("iPhone 11")
                .description("The brand new iPhone from Apple")
                .price(1000.0)
                .totalInStock(280.0)
                .build();
        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .body(updated)
                .when()
                .put(update)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND)
                .body("message", containsString("Product doesn't exist in the system to update"));
        //@formatter:on
    }

    @Test
    public void testFetchingProducts() {
        createNewProduct();

        //@formatter:off
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(path)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", greaterThanOrEqualTo(1));
        //@formatter:on
    }

    private String createNewProduct() {
        Products product = new Products();
        product.setId(productId);
        product.setDescription("The brand new iPhone from Apple");
        product.setName("iPhone 11");
        product.setTotalInStock(280.0);
        product.setPrice(100.0);

        //@formatter:off
        return given()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .path("id");
        //@formatter:on
    }
}
