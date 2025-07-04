package com.stevenk.wholefoods.repository;


import com.stevenk.wholefoods.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    void deleteAllByCartId(Long id);
}