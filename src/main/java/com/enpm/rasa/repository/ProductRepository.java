package com.enpm.rasa.repository;

import com.enpm.rasa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByProductId(int productId);

    @Query(value = "SELECT * FROM Product WHERE name ILIKE %:name%", nativeQuery = true)
    List<Product> findByProductName(String name);

    @Query(value = "SELECT * FROM Product WHERE tag_tag_id=:tag_id", nativeQuery = true)
    List<Product> findByTagId(int tag_id);
}
