package com.example.technical_challenge.service.stockMovement;

import com.example.technical_challenge.db.model.Item;
import com.example.technical_challenge.db.model.Order;
import com.example.technical_challenge.db.model.StockMovement;
import com.example.technical_challenge.dto.StockMovementDto;
import com.example.technical_challenge.dto.UserDto;

import java.util.List;

public interface IStockMovementService {

    StockMovementDto getStockMovement (Integer stockMovementId);

    StockMovementDto createStockMovement (StockMovementDto stockMovementDto);

    StockMovementDto updateStockMovement(StockMovementDto stockMovementDto);

    void deleteStockMovement(Integer stockMovementId);

    List<StockMovementDto> getAllStockMovements();

    void tryToFulfilOrdersForGivenItem(Item item);

    List<StockMovement> getAllStockMovementsNeededToFulfilOrder (Order order);

    Integer getCurrentStockOfItem(Item item);

}
