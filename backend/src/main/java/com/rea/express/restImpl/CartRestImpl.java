package com.rea.express.restImpl;

import com.rea.express.dto.CartItemRequest;
import com.rea.express.rest.CartRest;
import com.rea.express.service.CartService;
import com.rea.express.wrapper.CartWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartRestImpl implements CartRest {

    private final CartService cartService;

    @Override
    public ResponseEntity<CartWrapper> getMyCart() {
        return ResponseEntity.ok(cartService.getMyCart());
    }

    @Override
    public ResponseEntity<CartWrapper> addItem(CartItemRequest request) {
        return new ResponseEntity<>(cartService.addItem(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CartWrapper> updateItem(Integer productId, CartItemRequest request) {
        request.setProductId(productId);
        return ResponseEntity.ok(cartService.updateItem(productId, request));
    }

    @Override
    public ResponseEntity<CartWrapper> removeItem(Integer productId) {
        return ResponseEntity.ok(cartService.removeItem(productId));
    }

    @Override
    public ResponseEntity<CartWrapper> clear() {
        return ResponseEntity.ok(cartService.clear());
    }
}
