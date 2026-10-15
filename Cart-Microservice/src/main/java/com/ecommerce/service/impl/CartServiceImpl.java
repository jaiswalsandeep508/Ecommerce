package com.ecommerce.service.impl;

import com.ecommerce.dto.request.UpdateCartStatusRequest;
import com.ecommerce.dto.response.CartResponse;
import com.ecommerce.dto.response.PageResponse;
import com.ecommerce.exception.AccessDeniedException;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.model.Cart;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.security.UserContext;
import com.ecommerce.service.CartService;
import com.ecommerce.service.factory.CartFactory;
import com.ecommerce.util.PageResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartFactory cartFactory;
    private final CartMapper cartMapper;

    @Override
    public CartResponse createCart() {
        Cart cart = cartFactory.createCart();
        cart = cartRepository.save(cart);
        log.info(
                "Cart created successfully with ID: {}",
                cart.getCartId()
        );
        return cartMapper.toResponse(cart);
    }

    @Override
    public PageResponse<CartResponse> getAllCarts(
            Integer page,
            Integer size,
            String sortBy,
            String direction) {
        Pageable pageable = createPageable(
                page,
                size
        );
        Long currentUserId = UserContext.getCurrentUserId();
        List<Cart> carts = StreamSupport.stream(
                        cartRepository.findAll().spliterator(),
                        false
                )
                .filter(cart ->
                        cart.getUserId().equals(currentUserId)
                )
                .toList();
        int start = Math.min(
                (int) pageable.getOffset(),
                carts.size()
        );
        int end = Math.min(
                start + pageable.getPageSize(),
                carts.size()
        );
        List<CartResponse> responses = carts
                .subList(start, end)
                .stream()
                .map(cartMapper::toResponse)
                .toList();
        Page<CartResponse> cartPage = new PageImpl<>(
                responses,
                pageable,
                carts.size()
        );
        log.info(
                "Successfully fetched {} carts.",
                cartPage.getNumberOfElements()
        );
        return PageResponseUtil.from(cartPage);
    }

    @Override
    public CartResponse getCartById(Long cartId) {

        Cart cart = cartFactory.getCartById(cartId);

        validateCartOwnership(cart);

        log.info(
                "Cart fetched successfully with ID: {}",
                cartId
        );

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse getCartByUserId(Long userId) {

        Long currentUserId = UserContext.getCurrentUserId();

        if (!currentUserId.equals(userId)) {

            throw new AccessDeniedException(
                    "You are not authorized to access this cart"
            );
        }

        Cart cart = cartFactory.getCartByUserId(userId);

        log.info(
                "Cart fetched successfully for user ID: {}",
                userId
        );

        return cartMapper.toResponse(cart);
    }

    @Override
    public CartResponse updateCartStatus(
            Long cartId,
            UpdateCartStatusRequest request) {

        Cart cart = cartFactory.getCartById(cartId);

        validateCartOwnership(cart);

        cart.setCartStatus(request.getStatus());

        cart = cartRepository.save(cart);

        log.info(
                "Cart status updated successfully for cart ID: {}",
                cartId
        );

        return cartMapper.toResponse(cart);
    }

    @Override
    public void deleteCart(Long cartId) {

        Cart cart = cartFactory.getCartById(cartId);

        validateCartOwnership(cart);

        cartRepository.delete(cart);

        log.info(
                "Cart deleted successfully with ID: {}",
                cartId
        );
    }

    private void validateCartOwnership(Cart cart) {

        Long currentUserId = UserContext.getCurrentUserId();

        if (!cart.getUserId().equals(currentUserId)) {

            log.warn(
                    "Unauthorized cart access. Cart ID: {}, Owner ID: {}, Current User ID: {}",
                    cart.getCartId(),
                    cart.getUserId(),
                    currentUserId
            );

            throw new AccessDeniedException(
                    "You are not authorized to access this cart"
            );
        }
    }

    private Pageable createPageable(
            Integer page,
            Integer size) {

        int pageNumber = page == null || page < 0
                ? 0
                : page;

        int pageSize = size == null || size <= 0
                ? 10
                : Math.min(size, 100);

        return PageRequest.of(
                pageNumber,
                pageSize
        );
    }
}