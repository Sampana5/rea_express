package com.rea.express.wrapper;

import com.rea.express.POJO.ProductDocument;
import lombok.Data;

@Data
public class ProductDocumentWrapper {

    private Integer id;
    private String name;
    private String fileUrl;
    private String type;

    public static ProductDocumentWrapper fromProductDocument(ProductDocument document) {
        ProductDocumentWrapper wrapper = new ProductDocumentWrapper();
        wrapper.setId(document.getId());
        wrapper.setName(document.getName());
        wrapper.setFileUrl(document.getFileUrl());
        wrapper.setType(document.getType());
        return wrapper;
    }
}
