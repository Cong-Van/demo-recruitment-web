package com.funix.prj_321x.asm02.DAO;

import com.funix.prj_321x.asm02.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM category ORDER BY number_choose DESC LIMIT 4", nativeQuery = true)
    List<Category> findTopCategories();
}
