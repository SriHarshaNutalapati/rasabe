package com.enpm.rasa.repository;

import com.enpm.rasa.model.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardDetails, Integer> {

    @Query(value = "SELECT card_id, name, payment_id, payment_method, user_id_user_id FROM card_details WHERE user_id_user_id =:userId", nativeQuery = true)
    List<CardDetails> findByUser(int userId);

    //WHERE user_id_user_id =:userId
}
