package com.rea.express.wrapper;

import com.rea.express.POJO.Category;
import lombok.Data;

@Data
public class CategoryWrapper {

    private Integer id;
    private String name;
    private String slug;
    private String description;
    private Integer subCategoryCount;

    public static CategoryWrapper fromCategory(Category category) {
        CategoryWrapper wrapper = new CategoryWrapper();
        wrapper.setId(category.getId());
        wrapper.setName(category.getName());
        wrapper.setSlug(category.getSlug());
        wrapper.setDescription(category.getDescription());
        wrapper.setSubCategoryCount(category.getSubCategories() != null ? category.getSubCategories().size() : 0);
        return wrapper;
    }
}
