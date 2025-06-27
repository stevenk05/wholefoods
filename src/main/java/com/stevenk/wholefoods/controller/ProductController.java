package com.stevenk.wholefoods.controller;

import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Category;
import com.stevenk.wholefoods.model.Product;
import com.stevenk.wholefoods.requests.AddProductRequest;
import com.stevenk.wholefoods.requests.ProductUpdateRequest;
import com.stevenk.wholefoods.response.ApiResponse;
import com.stevenk.wholefoods.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductService prodService;

    @GetMapping("/all")
    ResponseEntity<ApiResponse> getAllProducts(){
    try {
        List<Product> products = prodService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("Success", products));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .body(new ApiResponse("Error retrieving products: " + e.getMessage(), null));
    }
}

    @GetMapping("/{id}/getPRODbyID")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product prod = prodService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Found.", prod));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/addPROD")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product prod = prodService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add product success", prod));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}/updatePROD")
    public  ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
        try {
            Product prod = prodService.updateProduct(request, productId);
            return ResponseEntity.ok(new ApiResponse("Update product success!", prod));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}/deletePROD")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            prodService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product success!", productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/getProdByBrand+Name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = prodService.getProductsByBrandAndName(brand, name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found with brand,name == " + brand + "," + name, products));
            }
            return ResponseEntity.ok(new ApiResponse("Successful.", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{brand}/{cat}/getBrandCat")
    public ResponseEntity<ApiResponse> getProductByBrandAndCategory(@PathVariable String brand, @PathVariable Category cat) {
        try {
            List<Product> products = prodService.getProductsByCategoryAndBrand(cat.getName(), brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found with brand,cat == " + brand + "," + cat.getName(), products));
            }
            return ResponseEntity.ok(new ApiResponse("Successful.", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{name}/getProdByName")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = prodService.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found with name == " + name, products));
            }
            return ResponseEntity.ok(new ApiResponse("Successful.", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/findProductsByBrand")
    public ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = prodService.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
        return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{catName}/findProductsByCategory")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String catName) {
        try {
            List<Product> products = prodService.getProductsByCategory(catName);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found ", null));
            }
            return ResponseEntity.ok(new ApiResponse("success", products));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = prodService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}