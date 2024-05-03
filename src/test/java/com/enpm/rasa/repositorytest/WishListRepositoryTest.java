package com.enpm.rasa.repositorytest;

import com.enpm.rasa.model.User;
import com.enpm.rasa.model.Wishlist;
import com.enpm.rasa.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByUserId() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("abc123");
        entityManager.persist(user);
        entityManager.flush();

        Wishlist wishlist1 = new Wishlist();
        wishlist1.setUserId(user);
        entityManager.persist(wishlist1);
        entityManager.flush();

        Wishlist wishlist2 = new Wishlist();
        wishlist2.setUserId(user);
        entityManager.persist(wishlist2);
        entityManager.flush();

        // Act
        List<Wishlist> wishlists = wishListRepository.findByUserId(user);

        // Assert
        assertEquals(2, wishlists.size());
        assertTrue(wishlists.contains(wishlist1));
        assertTrue(wishlists.contains(wishlist2));
    }
}