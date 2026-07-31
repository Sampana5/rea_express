package com.rea.express.rest;

import com.rea.express.dto.CategoryRequest;
import com.rea.express.wrapper.CategoryWrapper;
import com.rea.express.wrapper.SubCategoryWrapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/categories")
public interface CategoryRest {

    @GetMapping
    ResponseEntity<List<CategoryWrapper>> getCategories();

    @GetMapping(path = "/{id}")
    ResponseEntity<CategoryWrapper> getCategory(@PathVariable Integer id);

    @GetMapping(path = "/{id}/subcategories")
    ResponseEntity<List<SubCategoryWrapper>> getSubCategories(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<CategoryWrapper> createCategory(@Valid @RequestBody CategoryRequest request);

    @PutMapping(path = "/{id}")
    ResponseEntity<CategoryWrapper> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequest request);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Integer id);
}
