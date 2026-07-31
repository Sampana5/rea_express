package com.rea.express.wrapper;

import com.rea.express.POJO.SubCategory;
import lombok.Data;

@Data
public class SubCategoryWrapper {

    private Integer id;
    private String name;
    private String slug;
    private String description;
    private Integer categoryId;
    private String categoryName;
    private Integer productCount;

    public static SubCategoryWrapper fromSubCategory(SubCategory subCategory) {
        SubCategoryWrapper wrapper = new SubCategoryWrapper();
        wrapper.setId(subCategory.getId());
        wrapper.setName(subCategory.getName());
        wrapper.setSlug(subCategory.getSlug());
        wrapper.setDescription(subCategory.getDescription());
        if (subCategory.getCategory() != null) {
            wrapper.setCategoryId(subCategory.getCategory().getId());
            wrapper.setCategoryName(subCategory.getCategory().getName());
        }
        wrapper.setProductCount(subCategory.getProducts() != null ? subCategory.getProducts().size() : 0);
        return wrapper;
    }
}
