package com.ecommerce.config;

import com.ecommerce.model.Role;
import com.ecommerce.model.enums.RoleType;
import com.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {

        seedRole(
                RoleType.ADMIN,
                "System administrator"
        );

        seedRole(
                RoleType.CUSTOMER,
                "E-Commerce customer"
        );

        seedRole(
                RoleType.SELLER,
                "E-Commerce seller"
        );
    }

    private void seedRole(RoleType roleType, String description) {

        if (roleRepository.existsByRoleType(roleType)) {
            log.info("Role already exists: {}", roleType);
            return;
        }

        Role role = Role.builder()
                .roleType(roleType)
                .description(description)
                .build();

        Role savedRole = roleRepository.save(role);

        log.info(
                "Default role created successfully. roleId: {}, roleType: {}",
                savedRole.getRoleId(),
                savedRole.getRoleType()
        );
    }
}