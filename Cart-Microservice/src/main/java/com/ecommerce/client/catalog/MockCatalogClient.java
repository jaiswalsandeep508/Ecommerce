package com.ecommerce.client.catalog;

import com.ecommerce.client.product.ProductResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockCatalogClient implements CatalogClient {

    @Override
    public ProductResponse getProductById(Long productId) {

        return ProductResponse.builder()
                .productId(productId)
                .name("Dummy Product")
                .sku("SKU-" + productId)
                .price(BigDecimal.valueOf(1000))
                .discount(BigDecimal.valueOf(100))
                .specialPrice(BigDecimal.valueOf(900))
                .build();
    }
}