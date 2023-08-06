package com.example.technical_challenge.db.repository;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.StockMovement;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT COUNT(o) FROM Order o WHERE o.item = :item AND o.isFulfilled = false")
    long countUnfulfilledOrdersForItem(@Param("item") Item item);

    List<Order> getAllByItemAndIsFulfilledIsFalseOrderByCreationDateAsc(Item item);

}
