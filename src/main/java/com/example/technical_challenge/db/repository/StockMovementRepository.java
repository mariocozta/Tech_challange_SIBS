package com.example.technical_challenge.db.repository;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement,Integer> {


    @Query("SELECT sm FROM StockMovement sm WHERE sm.item.id = :itemId AND sm.currentQuantity > 0 ORDER BY sm.creationDate ASC")
    List<StockMovement> findByItemWhereCurrentQuantityBiggerThanZero(Integer itemId);

    List<StockMovement> findByItemAndCurrentQuantityGreaterThanOrderByCreationDateAsc(Item item, int currentQuantity);
}
