package com.ecommerce.service.impl;

import com.ecommerce.dto.request.CategoryRequest;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.model.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.security.UserContext;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.factory.CategoryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryFactory categoryFactory;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = categoryFactory.createCategory(request);

        Long userId = UserContext.getCurrentUserId();

        category.setCreatedBy(userId);
        category.setUpdatedBy(userId);

        Category savedCategory = categoryRepository.save(category);

        log.info(
                "Category created successfully with category id: {}",
                savedCategory.getCategoryId()
        );

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(
            Long categoryId,
            CategoryRequest request) {

        Category category =
                categoryFactory.getCategoryById(categoryId);

        categoryFactory.validateCategoryNameForUpdate(
                categoryId,
                request.getName()
        );

        categoryMapper.updateFromRequest(
                request,
                category
        );

        if (request.getParentCategoryId() != null) {

            Category parentCategory =
                    categoryFactory.getCategoryById(
                            request.getParentCategoryId()
                    );

            if (parentCategory.getCategoryId()
                    .equals(category.getCategoryId())) {

                throw new BadRequestException(
                        "Category cannot be its own parent."
                );
            }

            category.setParentCategory(parentCategory);

        } else {

            category.setParentCategory(null);
        }

        category.setUpdatedBy(
                UserContext.getCurrentUserId()
        );

        Category savedCategory =
                categoryRepository.save(category);

        log.info(
                "Category {} updated successfully",
                categoryId
        );

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        Category category =
                categoryFactory.getCategoryById(categoryId);

        boolean hasChildCategories =
                categoryRepository
                        .existsByParentCategoryCategoryId(
                                categoryId
                        );

        if (hasChildCategories) {

            throw new BadRequestException(
                    "Cannot delete category because child categories " +
                            "are referring to this parent category."
            );
        }

        category.setActive(false);

        category.setUpdatedBy(
                UserContext.getCurrentUserId()
        );

        categoryRepository.save(category);

        log.info(
                "Category deactivated successfully with id: {}",
                categoryId
        );
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {

        Category category =
                categoryFactory.getCategoryById(categoryId);

        CategoryResponse response =
                categoryMapper.toResponse(category);

        log.info(
                "Successfully fetched category id: {}",
                categoryId
        );

        return response;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        List<Category> categories =
                categoryRepository.findAllByActiveTrue();

        List<CategoryResponse> responses =
                categories.stream()
                        .map(categoryMapper::toResponse)
                        .toList();

        log.info(
                "Successfully fetched {} active categories",
                responses.size()
        );

        return responses;
    }
}