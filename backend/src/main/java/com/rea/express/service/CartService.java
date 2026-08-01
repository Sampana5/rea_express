package com.rea.express.service;

import com.rea.express.dto.CartItemRequest;
import com.rea.express.wrapper.CartWrapper;

public interface CartService {

    CartWrapper getMyCart();

    CartWrapper addItem(CartItemRequest request);

    CartWrapper updateItem(Integer productId, CartItemRequest request);

    CartWrapper removeItem(Integer productId);

    CartWrapper clear();
}
