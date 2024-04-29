package com.enpm.rasa.repository;

import com.enpm.rasa.model.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<ProductTag, Integer> {

    ProductTag findByTagName(String name);
}
