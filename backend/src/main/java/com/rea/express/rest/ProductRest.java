package com.rea.express.rest;

import com.rea.express.dto.ProductRequest;
import com.rea.express.wrapper.ProductWrapper;
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

@RequestMapping(path = "/products")
public interface ProductRest {

    @GetMapping
    ResponseEntity<List<ProductWrapper>> getProducts(@RequestParam(required = false) Integer subCategoryId,
                                                     @RequestParam(required = false) String q);

    @GetMapping(path = "/{id}")
    ResponseEntity<ProductWrapper> getProduct(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<ProductWrapper> createProduct(@Valid @RequestBody ProductRequest request);

    @PutMapping(path = "/{id}")
    ResponseEntity<ProductWrapper> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductRequest request);

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Integer id);
}
