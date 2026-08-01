package com.rea.express.wrapper;

import com.rea.express.POJO.Cart;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartWrapper {

    private Integer id;
    private List<CartItemWrapper> items = Collections.emptyList();
    private int itemCount;
    private int totalQuantity;

    public static CartWrapper from(Cart cart) {
        CartWrapper wrapper = new CartWrapper();
        wrapper.setId(cart.getId());
        List<CartItemWrapper> items = cart.getItems() == null
                ? Collections.emptyList()
                : cart.getItems().stream().map(CartItemWrapper::from).collect(Collectors.toList());
        wrapper.setItems(items);
        wrapper.setItemCount(items.size());
        wrapper.setTotalQuantity(items.stream().mapToInt(CartItemWrapper::getQuantity).sum());
        return wrapper;
    }

    public static CartWrapper empty() {
        CartWrapper wrapper = new CartWrapper();
        wrapper.setItems(Collections.emptyList());
        wrapper.setItemCount(0);
        wrapper.setTotalQuantity(0);
        return wrapper;
    }
}
