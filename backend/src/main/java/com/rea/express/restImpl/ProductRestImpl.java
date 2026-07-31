package com.rea.express.restImpl;

import com.rea.express.dto.ProductRequest;
import com.rea.express.rest.ProductRest;
import com.rea.express.service.CatalogService;
import com.rea.express.utils.ReaUtils;
import com.rea.express.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductRestImpl implements ProductRest {

    private final CatalogService catalogService;

    @Override
    public ResponseEntity<List<ProductWrapper>> getProducts(Integer subCategoryId, String q) {
        return ResponseEntity.ok(catalogService.getProducts(subCategoryId, q));
    }

    @Override
    public ResponseEntity<ProductWrapper> getProduct(Integer id) {
        return ResponseEntity.ok(catalogService.getProduct(id));
    }

    @Override
    public ResponseEntity<ProductWrapper> createProduct(ProductRequest request) {
        return new ResponseEntity<>(catalogService.createProduct(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductWrapper> updateProduct(Integer id, ProductRequest request) {
        return ResponseEntity.ok(catalogService.updateProduct(id, request));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteProduct(Integer id) {
        catalogService.deleteProduct(id);
        return ReaUtils.getResponseEntity("Produit supprimé", HttpStatus.OK);
    }
}
