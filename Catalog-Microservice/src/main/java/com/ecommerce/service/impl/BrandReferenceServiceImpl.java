package com.ecommerce.service.impl;

import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.BrandReferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandReferenceServiceImpl implements BrandReferenceService {

    private final ProductRepository productRepository;

    @Override
    public boolean isBrandInUse(Long brandId) {

        log.info("Checking whether brand with ID: {} is in use.", brandId);

        boolean inUse = productRepository.existsByBrandBrandId(brandId);

        log.info("Brand with ID: {} is in use: {}", brandId, inUse);

        return inUse;
    }
}