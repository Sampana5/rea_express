package com.rea.express.serviceImpl;

import com.rea.express.POJO.Cart;
import com.rea.express.POJO.CartItem;
import com.rea.express.POJO.Product;
import com.rea.express.POJO.User;
import com.rea.express.dao.CartDao;
import com.rea.express.dao.ProductDao;
import com.rea.express.dto.CartItemRequest;
import com.rea.express.exceptions.ResourceNotFoundException;
import com.rea.express.service.CartService;
import com.rea.express.utils.CurrentUserService;
import com.rea.express.wrapper.CartWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartDao cartDao;
    private final ProductDao productDao;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public CartWrapper getMyCart() {
        User user = currentUserService.requireCurrentUser();
        return cartDao.findByUserIdWithItems(user.getId())
                .map(CartWrapper::from)
                .orElseGet(CartWrapper::empty);
    }

    @Override
    @Transactional
    public CartWrapper addItem(CartItemRequest request) {
        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Le produit est requis.");
        }
        Cart cart = getOrCreateCart();
        Product product = productDao.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable."));

        CartItem existing = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            int next = existing.getQuantity() + request.getQuantity();
            existing.setQuantity(Math.min(next, 9999));
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cart.getItems().add(item);
        }

        return CartWrapper.from(cartDao.save(cart));
    }

    @Override
    @Transactional
    public CartWrapper updateItem(Integer productId, CartItemRequest request) {
        Cart cart = requireCart();
        CartItem item = findItem(cart, productId);
        item.setQuantity(request.getQuantity());
        return CartWrapper.from(cartDao.save(cart));
    }

    @Override
    @Transactional
    public CartWrapper removeItem(Integer productId) {
        Cart cart = requireCart();
        CartItem item = findItem(cart, productId);
        cart.getItems().remove(item);
        return CartWrapper.from(cartDao.save(cart));
    }

    @Override
    @Transactional
    public CartWrapper clear() {
        User user = currentUserService.requireCurrentUser();
        Cart cart = cartDao.findByUserIdWithItems(user.getId()).orElse(null);
        if (cart == null) {
            return CartWrapper.empty();
        }
        cart.getItems().clear();
        return CartWrapper.from(cartDao.save(cart));
    }

    private Cart getOrCreateCart() {
        User user = currentUserService.requireCurrentUser();
        return cartDao.findByUserIdWithItems(user.getId()).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartDao.save(cart);
        });
    }

    private Cart requireCart() {
        User user = currentUserService.requireCurrentUser();
        return cartDao.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Panier introuvable."));
    }

    private CartItem findItem(Cart cart, Integer productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Article introuvable dans le panier."));
    }
}
