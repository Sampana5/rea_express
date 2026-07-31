package com.rea.express.restImpl;

import com.rea.express.dto.CategoryRequest;
import com.rea.express.rest.CategoryRest;
import com.rea.express.service.CatalogService;
import com.rea.express.utils.ReaUtils;
import com.rea.express.wrapper.CategoryWrapper;
import com.rea.express.wrapper.SubCategoryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CategoryRestImpl implements CategoryRest {

    private final CatalogService catalogService;

    @Override
    public ResponseEntity<List<CategoryWrapper>> getCategories() {
        return ResponseEntity.ok(catalogService.getCategories());
    }

    @Override
    public ResponseEntity<CategoryWrapper> getCategory(Integer id) {
        return ResponseEntity.ok(catalogService.getCategory(id));
    }

    @Override
    public ResponseEntity<List<SubCategoryWrapper>> getSubCategories(Integer id) {
        catalogService.getCategory(id);
        return ResponseEntity.ok(catalogService.getSubCategories(id));
    }

    @Override
    public ResponseEntity<CategoryWrapper> createCategory(CategoryRequest request) {
        return new ResponseEntity<>(catalogService.createCategory(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoryWrapper> updateCategory(Integer id, CategoryRequest request) {
        return ResponseEntity.ok(catalogService.updateCategory(id, request));
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteCategory(Integer id) {
        catalogService.deleteCategory(id);
        return ReaUtils.getResponseEntity("Catégorie supprimée", HttpStatus.OK);
    }
}
