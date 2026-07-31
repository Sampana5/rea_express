package com.rea.express.wrapper;

import com.rea.express.POJO.Product;
import com.rea.express.POJO.SubCategory;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductWrapper {

    private Integer id;
    private String name;
    private String slug;
    private String description;
    private String reference;
    private String imageUrl;
    private String technicalInfo;
    private String brand;
    private String referenceManufacturer;
    private String unitOfSale;
    private String availability;
    private Integer subCategoryId;
    private String subCategoryName;
    private Integer categoryId;
    private String categoryName;
    private List<ProductImageWrapper> images;
    private List<ProductDocumentWrapper> documents;

    /** Vue légère pour les listes (sans images/documents détaillés). */
    public static ProductWrapper summary(Product product) {
        ProductWrapper wrapper = new ProductWrapper();
        wrapper.setId(product.getId());
        wrapper.setName(product.getName());
        wrapper.setSlug(product.getSlug());
        wrapper.setDescription(product.getDescription());
        wrapper.setReference(product.getReference());
        wrapper.setImageUrl(product.getImageUrl());
        wrapper.setBrand(product.getBrand());
        wrapper.setReferenceManufacturer(product.getReferenceManufacturer());
        wrapper.setUnitOfSale(product.getUnitOfSale());
        wrapper.setAvailability(product.getAvailability());
        SubCategory subCategory = product.getSubCategory();
        if (subCategory != null) {
            wrapper.setSubCategoryId(subCategory.getId());
            wrapper.setSubCategoryName(subCategory.getName());
            if (subCategory.getCategory() != null) {
                wrapper.setCategoryId(subCategory.getCategory().getId());
                wrapper.setCategoryName(subCategory.getCategory().getName());
            }
        }
        return wrapper;
    }

    /** Vue détaillée (page produit) avec images et documents. */
    public static ProductWrapper detail(Product product) {
        ProductWrapper wrapper = summary(product);
        wrapper.setTechnicalInfo(product.getTechnicalInfo());
        if (product.getImages() != null) {
            wrapper.setImages(product.getImages().stream()
                    .map(ProductImageWrapper::fromProductImage)
                    .collect(Collectors.toList()));
        }
        if (product.getDocuments() != null) {
            wrapper.setDocuments(product.getDocuments().stream()
                    .map(ProductDocumentWrapper::fromProductDocument)
                    .collect(Collectors.toList()));
        }
        return wrapper;
    }
}
