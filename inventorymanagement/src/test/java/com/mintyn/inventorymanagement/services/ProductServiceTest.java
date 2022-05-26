package com.mintyn.inventorymanagement.services;

import com.mintyn.inventorymanagement.dto.ProductCreateDto;
import com.mintyn.inventorymanagement.dto.ProductUpdateDto;
import com.mintyn.inventorymanagement.exceptions.ProductNotExistsException;
import com.mintyn.inventorymanagement.models.Products;
import com.mintyn.inventorymanagement.repositories.ProductRepository;
import com.mintyn.inventorymanagement.services.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Captor
    private ArgumentCaptor<Products> productArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> productIdArgumentCaptor;

    @Test
    public void testProductCreationSucceeds() {
        // Given
        Products product = Products.builder()
                .id(UUID.randomUUID().toString().substring(0,36))
                .name("Batarang")
                .description("Small but deadliest weapon of Batman")
                .price(8000.5)
                .build();
        when(productRepository.save(product)).thenReturn(product);

        // When
        productService
                .createProduct(ProductCreateDto.builder()
                        .name("Batarang")
                        .description("Small but deadliest weapon of Batman")
                        .price(8000.5)
                        .build());

        // Then
        verify(productRepository, times(1)).save(productArgumentCaptor.capture());
        assertThat(productArgumentCaptor.getValue(), hasProperty("name", equalTo("Batarang")));
        assertThat(productArgumentCaptor.getValue(), hasProperty("description", equalTo("Small but deadliest weapon of Batman")));
        assertThat(productArgumentCaptor.getValue(), hasProperty("price", equalTo(8000.5)));
    }

    @Test
    public void testProductUpdationSucceeds() {
        // Given
        String productId = UUID.randomUUID().toString().substring(0,36);
        Products product = Products.builder()
                .id(productId.toString())
                .name("Kryptonite Grenade Launcher")
                .description("The Grenade Launcher in Batman's style")
                .price(120000.98)
                .build();
        Products updated = Products.builder()
                .id(productId.toString())
                .name("Grenade Launcher")
                .description("The Grenade Launcher in Batman's style")
                .price(130000.0)
                .build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(updated);

        // When
        Products returned = productService
                .updateProduct(productId, ProductUpdateDto.builder()
                        .name("Grenade Launcher")
                        .price(130000.0)
                        .build());

        // Then
        verify(productRepository, times(1)).findById(productIdArgumentCaptor.capture());
        assertThat(productIdArgumentCaptor.getValue(), equalTo(productId));
        verify(productRepository, times(1)).save(productArgumentCaptor.capture());
        assertThat(productArgumentCaptor.getValue(), hasProperty("name", equalTo("Grenade Launcher")));
        assertThat(productArgumentCaptor.getValue(), hasProperty("description", equalTo("The Grenade Launcher in Batman's style")));
        assertThat(productArgumentCaptor.getValue(), hasProperty("price", equalTo(130000.0)));
        assertThat(returned.getPrice(), equalTo(130000.0));
        assertThat(returned.getName(), equalTo("Grenade Launcher"));
    }

    @Test
    public void testProductUpdationThrowsExceptionForNonExistingProduct() {
        // Given
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(
                ProductNotExistsException.class,
                () -> productService
                        .updateProduct(UUID.randomUUID().toString().substring(0,36), ProductUpdateDto.builder()
                                .name("Grenade Launcher")
                                .price(130000.0)
                                .build()));
        Assertions.assertThat(exception.getMessage()).isEqualTo("Product doesn't exist in the system to update");
    }

    @TestConfiguration
    static class ProductServiceTestContextConfiguration {

        @MockBean
        private ProductRepository productRepository;

        @Bean
        public ProductService productService() {
            return new ProductServiceImpl(productRepository);
        }
    }
}
