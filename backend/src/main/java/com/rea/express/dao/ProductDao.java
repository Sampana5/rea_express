package com.rea.express.dao;

import com.rea.express.POJO.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<Product> findBySubCategoryIdOrderByNameAsc(Integer subCategoryId);

    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);

    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
