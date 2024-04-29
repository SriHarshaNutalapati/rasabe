package com.enpm.rasa.repository;

import com.enpm.rasa.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

//    @Query(value = "SELECT * from _order where order_status='PENDING'", nativeQuery = true)
@Query(value = "SELECT o.order_id, u.user_id, u.first_name, u.last_name, o.order_date, o.total_amount, o.payment_method, o.order_status " +
        "FROM _order o " +
        "JOIN _user u ON o.user_id_user_id = u.user_id " +
        "WHERE o.order_status = 'PENDING'", nativeQuery = true)
    List<Order> findAllPending();

    @Query(value = "SELECT o FROM Order o WHERE YEAR(o.order_date) == :year", nativeQuery = true)
    List<Order> findByYear(int year);

    @Query(value = "SELECT o FROM Order o WHERE o.order_date >= CURRENT_DATE - 30", nativeQuery = true)
    List<Order> findOrdersInLast30Days();

    @Query(value = "SELECT o FROM Order o WHERE o.order_date >= CURRENT_DATE - 60", nativeQuery = true)
    List<Order> findOrdersInLast8Weeks();

    @Query(value = "SELECT o FROM Order o WHERE YEAR(o.order_date) >= YEAR(CURRENT_DATE) - 5", nativeQuery = true)
    List<Order> findOrdersInLast5Years();

    @Query(value = "SELECT o FROM Order o WHERE o.order_date >= CURRENT_DATE - 180", nativeQuery = true)
    List<Order> findOrdersInLast6Months();

}
