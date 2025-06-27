package com.stevenk.wholefoods.service.category;

import com.stevenk.wholefoods.exceptions.AlreadyExistsException;
import com.stevenk.wholefoods.exceptions.ResourceNotFoundException;
import com.stevenk.wholefoods.model.Category;
import com.stevenk.wholefoods.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository catRepo;

    @Override
    public Category getCategoryById(Long id) {
        return catRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Category Not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return catRepo.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return catRepo.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !catRepo.existsByName(c.getName()))
                .map(catRepo::save)
                .orElseThrow(()->new AlreadyExistsException(category.getName() + " already exists."));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id)).map(previousCat -> {
            previousCat.setName(category.getName());
            return catRepo.save(previousCat);
        }).orElseThrow(()->new ResourceNotFoundException("Category Not Found"));

    }

    @Override
    public void deleteCategory(Long id) {
        catRepo.findById(id)
                .ifPresentOrElse(catRepo::delete, () -> {
                    throw new ResourceNotFoundException("Category Not Found");
                });
    }
}
