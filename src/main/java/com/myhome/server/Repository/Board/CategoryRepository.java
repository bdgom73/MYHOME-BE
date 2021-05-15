package com.myhome.server.Repository.Board;

import com.myhome.server.Entity.Board.Category;
import com.myhome.server.Entity.Board.CategoryList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByName(CategoryList name);
}
