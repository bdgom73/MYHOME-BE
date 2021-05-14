package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(String categoryName);
}
