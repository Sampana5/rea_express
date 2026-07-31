package com.rea.express.rest;

import com.rea.express.dto.SubCategoryRequest;
import com.rea.express.wrapper.ProductWrapper;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/subcategories")
public interface SubCategoryRest {

    @GetMapping
    ResponseEntity<List<SubCategoryWrapper>> getSubCategories(@RequestParam(required = false) Integer categoryId);

    @GetMapping(path = "/{id}")
    ResponseEntity<SubCategoryWrapper> getSubCategory(@PathVariable Integer id);

    @GetMapping(path = "/{id}/products")
    ResponseEntity<List<ProductWrapper>> getProducts(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<SubCategoryWrapper> createSubCategory(@Valid @RequestBody SubCategoryRequest request);

    @PutMapping(path = "/{id}")
    ResponseEntity<SubCategoryWrapper> updateSubCategory(@PathVariable Integer id, @Valid @RequestBody SubCategoryRequest request);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Map<String, String>> deleteSubCategory(@PathVariable Integer id);
}
