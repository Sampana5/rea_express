package com.rea.express.wrapper;

import com.rea.express.POJO.ProductImage;
import lombok.Data;

@Data
public class ProductImageWrapper {

    private Integer id;
    private String url;
    private String type;

    public static ProductImageWrapper fromProductImage(ProductImage image) {
        ProductImageWrapper wrapper = new ProductImageWrapper();
        wrapper.setId(image.getId());
        wrapper.setUrl(image.getUrl());
        wrapper.setType(image.getType());
        return wrapper;
    }
}
