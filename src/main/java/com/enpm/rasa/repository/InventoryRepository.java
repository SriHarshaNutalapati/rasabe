package com.enpm.rasa.repository;

import com.enpm.rasa.model.Inventory;
import com.enpm.rasa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Inventory findByProduct(Product product);
}
