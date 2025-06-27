package com.stevenk.wholefoods.service.product;

import com.stevenk.wholefoods.exceptions.ProductNotFoundException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Product;
import com.stevenk.wholefoods.model.Category;
import com.stevenk.wholefoods.repository.CategoryRepository;
import com.stevenk.wholefoods.repository.ProductRepository;
import com.stevenk.wholefoods.requests.AddProductRequest;
import com.stevenk.wholefoods.requests.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository prodRepo;
    private final CategoryRepository catRepo;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(catRepo.findByName(request.getCategory()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory());
                    return catRepo.save(newCategory);
                });
    return prodRepo.save(createProductFromRequest(request, category));
    }

    private Product createProductFromRequest(AddProductRequest request, Category category) {
    return new Product(
            null,
            request.getName(),
            request.getBrand(),
            request.getInventory(),
            request.getPrice(),
            request.getDescription(),
            category,
            new ArrayList<>()
    );
}

    @Override
    public List<Product> getAllProducts() {
        return prodRepo.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return prodRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found."));
    }

    @Override
    public void deleteProductById(Long id) {
        prodRepo.findById(id)
                .ifPresentOrElse(prodRepo::delete,
                        ()-> {throw new ResourceNotFoundException("Product Not Found.");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long id) {
        return prodRepo.findById(id)
                .map(alr -> updateAlrProduct(alr, request))
                .map(prodRepo :: save)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found."));
    }

    private Product updateAlrProduct(Product alr, ProductUpdateRequest request) {
        alr.setName(request.getName());
        alr.setBrand(request.getBrand());
        alr.setPrice(request.getPrice());
        alr.setInventory(request.getInventory());
        alr.setDescription(request.getDescription());

        Category category = catRepo.findByName(request.getCategory().getName());
        alr.setCategory(category);

        return alr;
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return prodRepo.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return prodRepo.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return prodRepo.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return prodRepo.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return prodRepo.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return prodRepo.countByBrandAndName(brand, name);
    }
}