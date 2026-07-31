package com.rea.express.dao;

import com.rea.express.POJO.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCategoryDao extends JpaRepository<SubCategory, Integer> {

    List<SubCategory> findByCategoryIdOrderByNameAsc(Integer categoryId);

    Optional<SubCategory> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
