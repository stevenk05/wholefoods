package com.stevenk.wholefoods.service.product;

import com.stevenk.wholefoods.dto.ProductDTO;
import com.stevenk.wholefoods.model.Product;
import com.stevenk.wholefoods.requests.AddProductRequest;
import com.stevenk.wholefoods.requests.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long id);
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand (String category, String brand);
    List<Product> getProductsByName (String name);
    List<Product> getProductsByBrandAndName (String brand, String name);
    Long countProductsByBrandAndName (String brand, String name);

    List<ProductDTO> getConvertedProducts(List<Product> products);

    ProductDTO convertToDTO(Product product);
}
