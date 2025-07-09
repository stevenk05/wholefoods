package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.exceptions.AlreadyExistsException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Category;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService catService;

    @GetMapping("/allCATs")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = catService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found " + categories.size() + " categories", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error: ", e.getMessage()));
        }
    }

    @PostMapping("/addCAT")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category name) {
        try {
            Category category = catService.addCategory(name);
            return ResponseEntity.ok(new ApiResponse("Added " + category.getName(), category));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/getCATbyID")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category cat = catService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found " + cat.getName(), cat));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{name}/getCATbyName")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category cat = catService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found " + cat.getName(), cat));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @DeleteMapping("/{id}/delCAT")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            catService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Found.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/updateCAT")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = catService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Update success!", updatedCategory));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
