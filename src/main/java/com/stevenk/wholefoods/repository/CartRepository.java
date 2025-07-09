package com.stevenk.wholefoods.repository;


import com.stevenk.wholefoods.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    Cart findByUserId(Long userId);

    boolean existsByUserId(Long id);
}