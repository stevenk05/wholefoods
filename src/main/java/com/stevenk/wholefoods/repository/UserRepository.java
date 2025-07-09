package com.stevenk.wholefoods.repository;

import com.stevenk.wholefoods.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
}