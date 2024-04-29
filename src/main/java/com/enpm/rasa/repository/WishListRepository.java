package com.enpm.rasa.repository;

import com.enpm.rasa.model.User;
import com.enpm.rasa.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListRepository extends JpaRepository<Wishlist, Integer> {

    List<Wishlist> findByUserId(User user);
}
