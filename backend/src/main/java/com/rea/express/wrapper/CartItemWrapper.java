package com.rea.express.wrapper;

import com.rea.express.POJO.CartItem;
import com.rea.express.POJO.Product;
import lombok.Data;

@Data
public class CartItemWrapper {

    private Integer productId;
    private String productName;
    private String productReference;
    private String productImageUrl;
    private String availability;
    private Integer quantity;

    public static CartItemWrapper from(CartItem item) {
        CartItemWrapper wrapper = new CartItemWrapper();
        Product product = item.getProduct();
        wrapper.setProductId(product.getId());
        wrapper.setProductName(product.getName());
        wrapper.setProductReference(product.getReference());
        wrapper.setProductImageUrl(product.getImageUrl());
        wrapper.setAvailability(product.getAvailability());
        wrapper.setQuantity(item.getQuantity());
        return wrapper;
    }
}
