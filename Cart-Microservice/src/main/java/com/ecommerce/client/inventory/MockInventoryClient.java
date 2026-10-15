package com.ecommerce.client.inventory;

import org.springframework.stereotype.Component;

@Component
public class MockInventoryClient implements InventoryClient {

    @Override
    public void decreaseStock(Long productId, Integer quantity) {
        System.out.println("Stock decreased.");
    }

    @Override
    public void increaseStock(Long productId, Integer quantity) {
        System.out.println("Stock increased.");
    }
}