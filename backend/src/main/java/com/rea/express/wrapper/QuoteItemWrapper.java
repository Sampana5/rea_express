package com.rea.express.wrapper;

import com.rea.express.POJO.QuoteItem;
import lombok.Data;

@Data
public class QuoteItemWrapper {

    private Integer productId;
    private String productName;
    private String productReference;
    private String productImageUrl;
    private Integer quantity;

    public static QuoteItemWrapper from(QuoteItem item) {
        QuoteItemWrapper wrapper = new QuoteItemWrapper();
        wrapper.setProductId(item.getProductId());
        wrapper.setProductName(item.getProductName());
        wrapper.setProductReference(item.getProductReference());
        wrapper.setProductImageUrl(item.getProductImageUrl());
        wrapper.setQuantity(item.getQuantity());
        return wrapper;
    }
}
