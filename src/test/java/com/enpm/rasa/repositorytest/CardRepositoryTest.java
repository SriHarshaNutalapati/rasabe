package com.enpm.rasa.repositorytest;

import com.enpm.rasa.model.CardDetails;
import com.enpm.rasa.model.PaymentMethod;
import com.enpm.rasa.model.User;
import com.enpm.rasa.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

//    @Test
//    public void testFindByUser_WhenUserHasCards_ReturnsCardDetailsList() {
//        // Arrange
//        User userObj = new User();
//        userObj.setUserId(1);
//        CardDetails card1 = CardDetails.builder()
//                .name("Card1")
//                .paymentId("123456789")
//                .paymentMethod(PaymentMethod.PAYPAL)
//                .userId(userObj)
//                .build();
//
//        CardDetails card2 = CardDetails.builder()
//                .name("Card2")
//                .paymentId("987654321")
//                .paymentMethod(PaymentMethod.CREDITCARD)
//                .userId(userObj)
//                .build();
//
//        entityManager.persist(card1);
//        entityManager.persist(card2);
//        entityManager.flush();
//
//        // Act
//        List<CardDetails> cards = cardRepository.findByUser(userObj.getUserId());
//
//        // Assert
//        assertEquals(2, cards.size());
//    }

    @Test
    public void testFindByUser_WhenUserHasNoCards_ReturnsEmptyList() {
        // Arrange
        int userId = 1;

        // Act
        List<CardDetails> cards = cardRepository.findByUser(userId);

        // Assert
        assertEquals(0, cards.size());
    }
}

