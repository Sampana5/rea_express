package com.rea.express.restImpl;

import com.rea.express.dto.SubCategoryRequest;
import com.rea.express.rest.SubCategoryRest;
import com.rea.express.service.CatalogService;
import com.rea.express.utils.ReaUtils;
import com.rea.express.wrapper.ProductWrapper;
import com.rea.express.wrapper.SubCategoryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SubCategoryRestImpl implements SubCategoryRest {

    private final CatalogService catalogService;

    @Override
    public ResponseEntity<List<SubCategoryWrapper>> getSubCategories(Integer categoryId) {
        return ResponseEntity.ok(catalogService.getSubCategories(categoryId));
    }

    @Override
    public ResponseEntity<SubCategoryWrapper> getSubCategory(Integer id) {
        return ResponseEntity.ok(catalogService.getSubCategory(id));
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProducts(Integer id) {
        catalogService.getSubCategory(id);
        return ResponseEntity.ok(catalogService.getProducts(id, null));
    }

    @Override
    public ResponseEntity<SubCategoryWrapper> createSubCategory(SubCategoryRequest request) {
        return new ResponseEntity<>(catalogService.createSubCategory(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SubCategoryWrapper> updateSubCategory(Integer id, SubCategoryRequest request) {
        return ResponseEntity.ok(catalogService.updateSubCategory(id, request));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteSubCategory(Integer id) {
        catalogService.deleteSubCategory(id);
        return ReaUtils.getResponseEntity("Sous-catégorie supprimée", HttpStatus.OK);
    }
}
