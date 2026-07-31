package com.rea.express.service;

import com.rea.express.dto.CategoryRequest;
import com.rea.express.dto.ProductRequest;
import com.rea.express.dto.SubCategoryRequest;
import com.rea.express.wrapper.CategoryWrapper;
import com.rea.express.wrapper.ProductWrapper;
import com.rea.express.wrapper.SubCategoryWrapper;

import java.util.List;

public interface CatalogService {

    // ---- Lecture (public) ----
    List<CategoryWrapper> getCategories();

    CategoryWrapper getCategory(Integer id);

    List<SubCategoryWrapper> getSubCategories(Integer categoryId);

    SubCategoryWrapper getSubCategory(Integer id);

    List<ProductWrapper> getProducts(Integer subCategoryId, String search);

    ProductWrapper getProduct(Integer id);

    // ---- Écriture (admin) ----
    CategoryWrapper createCategory(CategoryRequest request);

    CategoryWrapper updateCategory(Integer id, CategoryRequest request);

    void deleteCategory(Integer id);

    SubCategoryWrapper createSubCategory(SubCategoryRequest request);

    SubCategoryWrapper updateSubCategory(Integer id, SubCategoryRequest request);

    void deleteSubCategory(Integer id);

    ProductWrapper createProduct(ProductRequest request);

    ProductWrapper updateProduct(Integer id, ProductRequest request);

    void deleteProduct(Integer id);
}
