package com.rea.express.serviceImpl;

import com.rea.express.POJO.Category;
import com.rea.express.POJO.Product;
import com.rea.express.POJO.SubCategory;
import com.rea.express.constents.ReaConstants;
import com.rea.express.dao.CategoryDao;
import com.rea.express.dao.ProductDao;
import com.rea.express.dao.SubCategoryDao;
import com.rea.express.dto.CategoryRequest;
import com.rea.express.dto.ProductRequest;
import com.rea.express.dto.SubCategoryRequest;
import com.rea.express.exceptions.DuplicateResourceException;
import com.rea.express.exceptions.ResourceNotFoundException;
import com.rea.express.service.CatalogService;
import com.rea.express.utils.SlugUtils;
import com.rea.express.wrapper.CategoryWrapper;
import com.rea.express.wrapper.ProductWrapper;
import com.rea.express.wrapper.SubCategoryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CategoryDao categoryDao;
    private final SubCategoryDao subCategoryDao;
    private final ProductDao productDao;

    // ---------------------------------------------------------------- Lecture

    @Override
    @Transactional(readOnly = true)
    public List<CategoryWrapper> getCategories() {
        return categoryDao.findAllByOrderByNameAsc().stream()
                .map(CategoryWrapper::fromCategory)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryWrapper getCategory(Integer id) {
        return CategoryWrapper.fromCategory(findCategory(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryWrapper> getSubCategories(Integer categoryId) {
        List<SubCategory> subCategories = (categoryId != null)
                ? subCategoryDao.findByCategoryIdOrderByNameAsc(categoryId)
                : subCategoryDao.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return subCategories.stream()
                .map(SubCategoryWrapper::fromSubCategory)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoryWrapper getSubCategory(Integer id) {
        return SubCategoryWrapper.fromSubCategory(findSubCategory(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductWrapper> getProducts(Integer subCategoryId, String search) {
        List<Product> products;
        if (subCategoryId != null && StringUtils.hasText(search)) {
            String needle = search.trim().toLowerCase();
            products = productDao.findBySubCategoryIdOrderByNameAsc(subCategoryId).stream()
                    .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(needle))
                    .collect(Collectors.toList());
        } else if (subCategoryId != null) {
            products = productDao.findBySubCategoryIdOrderByNameAsc(subCategoryId);
        } else if (StringUtils.hasText(search)) {
            products = productDao.findByNameContainingIgnoreCaseOrderByNameAsc(search.trim());
        } else {
            products = productDao.findAll(Sort.by(Sort.Direction.ASC, "name"));
        }
        return products.stream()
                .map(ProductWrapper::summary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductWrapper getProduct(Integer id) {
        return ProductWrapper.detail(findProduct(id));
    }

    // ---------------------------------------------------------------- Catégories (admin)

    @Override
    @Transactional
    public CategoryWrapper createCategory(CategoryRequest request) {
        if (categoryDao.existsBySlug(SlugUtils.slugify(request.getName()))) {
            throw new DuplicateResourceException(ReaConstants.CATEGORY_EXISTS);
        }
        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
        category.setSlug(uniqueSlug(request.getName(), slug -> categoryDao.existsBySlug(slug)));
        return CategoryWrapper.fromCategory(categoryDao.save(category));
    }

    @Override
    @Transactional
    public CategoryWrapper updateCategory(Integer id, CategoryRequest request) {
        Category category = findCategory(id);
        if (!category.getName().equalsIgnoreCase(request.getName().trim())) {
            category.setSlug(uniqueSlug(request.getName(), slug -> categoryDao.existsBySlug(slug)));
        }
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
        return CategoryWrapper.fromCategory(categoryDao.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        categoryDao.delete(findCategory(id));
    }

    // ---------------------------------------------------------------- Sous-catégories (admin)

    @Override
    @Transactional
    public SubCategoryWrapper createSubCategory(SubCategoryRequest request) {
        Category category = findCategory(request.getCategoryId());
        if (subCategoryDao.existsBySlug(SlugUtils.slugify(request.getName()))) {
            throw new DuplicateResourceException(ReaConstants.SUBCATEGORY_EXISTS);
        }
        SubCategory subCategory = new SubCategory();
        subCategory.setName(request.getName().trim());
        subCategory.setDescription(request.getDescription());
        subCategory.setCategory(category);
        subCategory.setSlug(uniqueSlug(request.getName(), slug -> subCategoryDao.existsBySlug(slug)));
        return SubCategoryWrapper.fromSubCategory(subCategoryDao.save(subCategory));
    }

    @Override
    @Transactional
    public SubCategoryWrapper updateSubCategory(Integer id, SubCategoryRequest request) {
        SubCategory subCategory = findSubCategory(id);
        Category category = findCategory(request.getCategoryId());
        if (!subCategory.getName().equalsIgnoreCase(request.getName().trim())) {
            subCategory.setSlug(uniqueSlug(request.getName(), slug -> subCategoryDao.existsBySlug(slug)));
        }
        subCategory.setName(request.getName().trim());
        subCategory.setDescription(request.getDescription());
        subCategory.setCategory(category);
        return SubCategoryWrapper.fromSubCategory(subCategoryDao.save(subCategory));
    }

    @Override
    @Transactional
    public void deleteSubCategory(Integer id) {
        subCategoryDao.delete(findSubCategory(id));
    }

    // ---------------------------------------------------------------- Produits (admin)

    @Override
    @Transactional
    public ProductWrapper createProduct(ProductRequest request) {
        SubCategory subCategory = findSubCategory(request.getSubCategoryId());
        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setTechnicalInfo(request.getTechnicalInfo());
        product.setBrand(request.getBrand());
        product.setReferenceManufacturer(request.getReferenceManufacturer());
        product.setUnitOfSale(request.getUnitOfSale());
        product.setAvailability(request.getAvailability());
        product.setSubCategory(subCategory);
        product.setSlug(uniqueSlug(request.getName(), slug -> productDao.existsBySlug(slug)));
        product = productDao.save(product);
        product.setReference(resolveReference(request.getReference(), product.getId()));
        return ProductWrapper.detail(productDao.save(product));
    }

    @Override
    @Transactional
    public ProductWrapper updateProduct(Integer id, ProductRequest request) {
        Product product = findProduct(id);
        SubCategory subCategory = findSubCategory(request.getSubCategoryId());
        if (!product.getName().equalsIgnoreCase(request.getName().trim())) {
            product.setSlug(uniqueSlug(request.getName(), slug -> productDao.existsBySlug(slug)));
        }
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setTechnicalInfo(request.getTechnicalInfo());
        product.setBrand(request.getBrand());
        product.setReferenceManufacturer(request.getReferenceManufacturer());
        product.setUnitOfSale(request.getUnitOfSale());
        product.setAvailability(request.getAvailability());
        product.setSubCategory(subCategory);
        if (StringUtils.hasText(request.getReference())) {
            product.setReference(request.getReference().trim());
        }
        return ProductWrapper.detail(productDao.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        productDao.delete(findProduct(id));
    }

    // ---------------------------------------------------------------- Helpers

    private Category findCategory(Integer id) {
        return categoryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ReaConstants.CATEGORY_NOT_FOUND));
    }

    private SubCategory findSubCategory(Integer id) {
        return subCategoryDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ReaConstants.SUBCATEGORY_NOT_FOUND));
    }

    private Product findProduct(Integer id) {
        return productDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ReaConstants.PRODUCT_NOT_FOUND));
    }

    private String uniqueSlug(String source, Predicate<String> existsCheck) {
        String base = SlugUtils.slugify(source);
        if (base.isBlank()) {
            base = "item";
        }
        String candidate = base;
        int suffix = 2;
        while (existsCheck.test(candidate)) {
            candidate = base + "-" + suffix++;
        }
        return candidate;
    }

    private String resolveReference(String requested, Integer productId) {
        if (StringUtils.hasText(requested)) {
            return requested.trim();
        }
        return String.format("REA-%05d", productId);
    }
}
