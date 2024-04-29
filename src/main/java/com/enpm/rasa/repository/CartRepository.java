package com.enpm.rasa.repository;

import com.enpm.rasa.model.User;
import com.enpm.rasa.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query(value = "SELECT SUM(amount) from Cart where user_id_user_id=:user", nativeQuery = true)
    Long findTotalAmount(int user);

    List<Cart> findByUserId(User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE from Cart where user_id_user_id=:user", nativeQuery = true)
    void deleteByUser(int user);
}
