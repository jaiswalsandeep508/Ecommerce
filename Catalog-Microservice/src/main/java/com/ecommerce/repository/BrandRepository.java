package com.ecommerce.repository;

import com.ecommerce.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Brand> findByNameIgnoreCase(String name);

    List<Brand> findAllByActiveTrue();
}